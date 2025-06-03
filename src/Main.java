import Interface.AbstractCounterService;
import Services.*;
import View.MainFrame;
import java.util.List;

import javax.swing.*;

public class Main {
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
            new MainFrame().setVisible(true);
        });
    }
}