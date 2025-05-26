import Services.DzialService;
import View.MainFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        DzialService.initializeCounter();
        SwingUtilities.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }
}