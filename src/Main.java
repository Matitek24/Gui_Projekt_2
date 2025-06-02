import Interface.AbstractCounterService;
import Services.BrygadaService;
import Services.BrygadzistaService;
import Services.DzialService;
import Services.PracownikService;
import View.MainFrame;
import java.util.List;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        List<AbstractCounterService<?>> services = List.of(
                new DzialService(),
                new PracownikService(),
                new BrygadzistaService(),
                new BrygadaService()
        );

        services.forEach(AbstractCounterService::initializeCounter);

        SwingUtilities.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }
}