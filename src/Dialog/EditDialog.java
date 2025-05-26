package Dialog;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class EditDialog extends JDialog {
    private String selectedObject;
    private String newObject;
    private final JComboBox<String> comboBox;
    private final JTextField textField = new JTextField(20);
    private boolean confirmed = false;

    private EditDialog(Component parent, List<String> objects, String selectMessage, String nameMessage, String title) {
        setModal(true);
        setTitle(title);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridLayout(4,1,5,5));


        panel.add(new JLabel(selectMessage));
        comboBox = new JComboBox<>(objects.toArray(new String[0]));
        if (!objects.isEmpty()) {
            comboBox.setSelectedIndex(0);
        }
        panel.add(comboBox);

        panel.add(new JLabel(nameMessage));
        panel.add(textField);
        add(panel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancel");

        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);

        okButton.addActionListener(e -> {
            selectedObject = comboBox.getSelectedItem().toString();
            newObject = textField.getText().trim();

            if(newObject.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nazwa nie moze byc pusta");
                return;
            }
            confirmed = true;
            dispose();
        });

        cancelButton.addActionListener(e -> dispose());

        pack();
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    public String getSelectedObject() {
        return selectedObject;
    }
    public String getNewObject() {
        return newObject;
    }
    public boolean isConfirmed() {
        return confirmed;
    }

    public static String[] showDialog(Component parent, List<String> objects, String selectMessage, String nameMessage, String title) {
        if (objects == null || objects.isEmpty()) {
            return null;
        }

        EditDialog dialog = new EditDialog(parent, objects, selectMessage, nameMessage, title);
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            return new String[]{dialog.getSelectedObject(), dialog.getNewObject()};
        }
        return null;
    }


}
