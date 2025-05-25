package Dialog;

import javax.swing.*;
import java.awt.*;

public class InputDialog extends JDialog {
    private String input;
    private final JTextField textField = new JTextField(20);
    private boolean confirmed = false;

    public InputDialog(Component parent, String message, String title) {
        setModal(true);
        setTitle(title);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridLayout(2, 1));
        panel.add(new JLabel(message));
        panel.add(textField);
        add(panel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Anuluj");
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);

        okButton.addActionListener(e -> {
            input = textField.getText();
            confirmed = true;
            dispose();
        });

        cancelButton.addActionListener(e -> {
            dispose();
        });

        pack();
        setLocationRelativeTo(parent);
    }

    public String getInput() {
        return confirmed ? input : null;
    }

    public static String showDialog(Component parent, String message, String title) {
        InputDialog dialog = new InputDialog(parent, message, title);
        dialog.setVisible(true);
        return dialog.getInput();
    }
}
