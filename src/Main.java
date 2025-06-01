import Services.BrygadzistaService;
import Services.DzialService;
import Services.PracownikService;
import View.MainFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        DzialService.initializeCounter();
        PracownikService.initializeCounter();
        BrygadzistaService.initializeBrygadzistaCounter();
        SwingUtilities.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }
}