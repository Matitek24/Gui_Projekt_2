import Dialog.LoginDialog;
import Interface.AbstractCounterService;
import Model.Uzytkownik;
import Services.*;
import View.MainFrame;
import java.util.List;

import javax.swing.*;

public class S33334 {
    public static void main(String[] args) {
        List<AbstractCounterService<?>> services = List.of(
                new DzialService(),
                new PracownikService(),
                new BrygadzistaService(),
                new BrygadaService(),
                new ZlecenieService(),
                new PracaService()
        );

        services.forEach(AbstractCounterService::initializeCounter);

        SwingUtilities.invokeLater(() -> {
            LoginDialog loginDialog = new LoginDialog(null);
            loginDialog.setVisible(true);
            Uzytkownik user = loginDialog.getLoggedInUser();
            if(user == null) {
                System.exit(0);
            }
            new MainFrame(user).setVisible(true);
        });
    }
}