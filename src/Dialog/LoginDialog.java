package Dialog;

import Model.Uzytkownik;
import Services.AuthService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class LoginDialog extends JDialog {

    private final JTextField loginField = new JTextField();
    private final JPasswordField passwordField = new JPasswordField();
    private final JButton loginButton = new JButton("Zaloguj");
    private final JButton cancelButton = new JButton("Anuluj");


    private Uzytkownik loggedInUser = null;

    public LoginDialog(Frame parent) {
        super(parent, "Logowanie", true);
        setLayout(new BorderLayout());
        setSize(350, 180);
        setResizable(false);
        setLocationRelativeTo(parent);

        JPanel formPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        formPanel.add(new JLabel("Login:"));
        formPanel.add(loginField);
        formPanel.add(new JLabel("Hasło:"));
        formPanel.add(passwordField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(loginButton);
        buttonPanel.add(cancelButton);

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        loginButton.addActionListener(e -> attemptLogin());

        cancelButton.addActionListener(e -> {
            loggedInUser = null;
            dispose();
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                loggedInUser = null;
                dispose();
            }
        });

    }

    private void attemptLogin() {
        String login = loginField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (login.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Proszę wypełnić oba pola.",
                    "Błąd logowania",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        AuthService authService = new AuthService();
        authService.login(login, password).ifPresentOrElse(
                user -> {
                    loggedInUser = user;
                    dispose();
                },
                () -> {
                    JOptionPane.showMessageDialog(
                            this,
                            "Niepoprawny login lub hasło.",
                            "Błąd logowania",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
        );
    }

    public Uzytkownik getLoggedInUser() {
        return loggedInUser;
    }
}
