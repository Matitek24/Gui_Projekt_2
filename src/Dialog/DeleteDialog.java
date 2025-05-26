package Dialog;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class DeleteDialog  extends JDialog {
    private String selectedObject;
    private final JComboBox<String> comboBox;
    private boolean confirmed = false;

    private DeleteDialog(Component parent, List<String> objects, String message, String title) {
        setModal(true);
        setTitle(title);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridLayout(2,1,5,5));
        panel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        panel.add(new JLabel(message));
        comboBox = new JComboBox<>(objects.toArray(new String[0]));
        if(!objects.isEmpty()){
            comboBox.setSelectedIndex(0);
        }
        panel.add(comboBox);

        add(panel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton deleteButton = new JButton("Usun");
        JButton cancelButton = new JButton("Anuluj");

        deleteButton.setBackground(new Color(220,20,60));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setOpaque(true);
        deleteButton.setBorderPainted(false);

        buttonPanel.add(deleteButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);

        deleteButton.addActionListener(e -> {
            selectedObject = comboBox.getSelectedItem().toString();

            int option = JOptionPane.showConfirmDialog(
                    this,
                    "Czy na pewno chcesz usunąć: " + selectedObject + "?",
                    "Potwierdzenie usunięcia",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if(option == JOptionPane.YES_OPTION){
                confirmed = true;
                dispose();
            }
        });
        cancelButton.addActionListener(e -> dispose());

        pack();
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

    }

    public String getSelectedObject() {
        return selectedObject;
    }
    public boolean isConfirmed() {
        return confirmed;
    }

    public static String showDialog(Component parent, List<String> objects, String message, String title) {
        if (objects == null || objects.isEmpty()) {
            return null;
        }

        DeleteDialog dialog = new DeleteDialog(parent, objects, message, title);
        dialog.setVisible(true);

        return dialog.getSelectedObject();
    }
}


