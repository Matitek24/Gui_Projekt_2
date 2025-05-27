package Dialog;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

public class DeleteDialog extends JDialog {
    private final JList<String> list;
    private boolean confirmed = false;

    private DeleteDialog(Component parent, List<String> objects, String message, String title) {
        setModal(true);
        setTitle(title);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.add(new JLabel(message), BorderLayout.NORTH);

        list = new JList<>(objects.toArray(new String[0]));
        list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        list.setVisibleRowCount(Math.min(objects.size(), 10)); // maks 10 linii naraz
        JScrollPane scrollPane = new JScrollPane(list);
        panel.add(scrollPane, BorderLayout.CENTER);

        add(panel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton deleteButton = new JButton("Usuń");
        JButton cancelButton = new JButton("Anuluj");

        deleteButton.setBackground(new Color(220, 20, 60));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setOpaque(true);
        deleteButton.setBorderPainted(false);

        buttonPanel.add(deleteButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);

        deleteButton.addActionListener(e -> {
            List<String> selected = list.getSelectedValuesList();

            if (selected.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nie wybrano żadnych elementów do usunięcia.", "Uwaga", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int option = JOptionPane.showConfirmDialog(
                    this,
                    "Czy na pewno chcesz usunąć " + selected.size() + " element(y)?",
                    "Potwierdzenie usunięcia",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (option == JOptionPane.YES_OPTION) {
                confirmed = true;
                dispose();
            }
        });

        cancelButton.addActionListener(e -> dispose());

        pack();
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public List<String> getSelectedObjects() {
        return list.getSelectedValuesList();
    }

    public static List<String> showDialog(Component parent, List<String> objects, String message, String title) {
        if (objects == null || objects.isEmpty()) {
            return new ArrayList<>();
        }

        DeleteDialog dialog = new DeleteDialog(parent, objects, message, title);
        dialog.setVisible(true);

        return dialog.isConfirmed() ? dialog.getSelectedObjects() : new ArrayList<>();
    }
}
