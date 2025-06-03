package Factory;



import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class DeleteUtil {
    public static <T> void deleteFromTable(
            Component parent,
            TablePanel tablePanel,
            List<T> items,
            Function<T, String> toLabel,
            Consumer<T> remover,
            Function<T, Integer> getId
    ) {
        if (items == null || items.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Brak elementów do usunięcia.");
            return;
        }

        JTable table = tablePanel.getTable();
        DefaultTableModel model = tablePanel.getTableModel();

        DeleteHelper.deleteSelected(
                parent,
                table,
                items,
                remover,
                deleted -> {
                    for (int row = model.getRowCount() - 1; row >= 0; row--) {
                        int id = (Integer) model.getValueAt(row, 0);
                        if (deleted.stream().anyMatch(e -> getId.apply(e) == id)) {
                            model.removeRow(row);
                        }
                    }
                },
                toLabel
        );
    }
}
