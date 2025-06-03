package Factory;

import Dialog.DeleteDialog;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DeleteHelper {
    public static <T> void deleteSelected(
            Component parent,
            JTable table,
            List<T> items,
            Consumer<T> remover,
            Consumer<List<T>> onRemoved,
            Function<T, String> toLabel // tylko do komunikatu
    ) {
        int[] selectedRows = table.getSelectedRows();
        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(parent, "Nie zaznaczono żadnych elementów do usunięcia.");
            return;
        }

        List<T> toDelete = new ArrayList<>();
        for (int row : selectedRows) {
            int modelIndex = table.convertRowIndexToModel(row); // jeśli sortujesz
            if (modelIndex >= 0 && modelIndex < items.size()) {
                toDelete.add(items.get(modelIndex));
            }
        }

        int confirmed = JOptionPane.showConfirmDialog(
                parent,
                "Czy na pewno chcesz usunąć " + toDelete.size() + " element(y)?\n" +
                        toDelete.stream().map(toLabel).collect(Collectors.joining(", ")),
                "Potwierdzenie usunięcia",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirmed == JOptionPane.YES_OPTION) {
            toDelete.forEach(remover);
            onRemoved.accept(toDelete);
        }
    }
}
