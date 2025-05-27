package Factory;

import Dialog.DeleteDialog;

import java.awt.*;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DeleteHelper {
    public static <T> void deleteMultiple(
            Component parent,
            List<T> items,
            Function<T, String> toLabel,
            Consumer<T> remover,
            Consumer<List<T>> onRemoved
    ) {
        // Zamieniamy każdy obiekt na etykietę
        List<String> labels = items.stream()
                .map(toLabel)
                .collect(Collectors.toList());
        // Wyświetlamy dialog
        List<String> selectedLabels = DeleteDialog.showDialog(
                parent,
                labels,
                "Wybierz elementy do usunięcia:",
                "Usuń elementy"
        );
        if (selectedLabels.isEmpty()) {
            return;
        }
        List<T> toDelete = items.stream()
                .filter(item -> selectedLabels.contains(toLabel.apply(item)))
                .collect(Collectors.toList());

        toDelete.forEach(remover);
        onRemoved.accept(toDelete);
    }
}
