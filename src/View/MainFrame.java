package View;

import Model.Brygadzista;
import Model.Uzytkownik;

import javax.swing.*;
import java.awt.*;
import Dialog.LoginDialog;
import Dialog.ZleceniaCheckerDialog;

public class MainFrame extends JFrame {
    private final Uzytkownik user;
    
    public MainFrame(Uzytkownik user) {
        super("Aplikacja – zalogowany: " + user.getLogin());
        this.user = user;

        setTitle("S33334 Mateusz Skrzypek");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        CenterPanel centerPanel = new CenterPanel();
        TopPanel topPanel = new TopPanel(centerPanel, user);
        LeftPanel leftPanel = new LeftPanel(centerPanel, topPanel, this);


        add(topPanel, BorderLayout.NORTH);
        add(leftPanel, BorderLayout.WEST);
        add(centerPanel, BorderLayout.CENTER);
        if (user instanceof Brygadzista) {
            Brygadzista bz = (Brygadzista) user;
            SwingUtilities.invokeLater(() -> {
                ZleceniaCheckerDialog dlg = new ZleceniaCheckerDialog(this, bz);
                dlg.setVisible(true);
            });
        }
    }

    public void logout() {
        dispose();
        LoginDialog loginDialog = new LoginDialog(null);
        loginDialog.setVisible(true);

        Uzytkownik newUser = loginDialog.getLoggedInUser();
        if (newUser != null) {
            new MainFrame(newUser).setVisible(true);
        } else {
            System.exit(0);
        }
    }
}