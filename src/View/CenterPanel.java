package View;

import Factory.TablePanel;
import Factory.TablePanelFactory;
import Services.DzialService;

import javax.swing.*;
import java.awt.*;

public class CenterPanel extends JPanel {

    private CardLayout cardLayout;
    private TablePanel dzialPanel;

    public CenterPanel() {

        cardLayout = new CardLayout();
        setLayout(cardLayout);

        dzialPanel = createDzialPanel();
        add(dzialPanel, "Dział pracowników");

        add(TablePanelFactory.createTablePanel(
                "Pracownicy",
                new String[] {"Pracownicy Lista"},
                new Object[][] {
                        {"Item 2"}, {"Item 4"}, {"Item 3"},
                }
        ),"Pracownik");

        add(TablePanelFactory.createTablePanel(
                "Uzytkownik",
                new String[] {"Uzytkownicy Lista"},
                new Object[][] {
                        {"Janek"},
                }
        ),"Użytkownik");

        add(TablePanelFactory.createTablePanel(
                "Brygadzista",
                new String[] {"Brygadzistów Lista"},
                new Object[][] {
                        {"Karol"},
                }
        ),"Brygadzista");

        add(TablePanelFactory.createTablePanel(
                "Brygada",
                new String[] {"Brygadzistów Lista"},
                new Object[][] {
                        {"Karol"}, {"Robert"},
                }
        ),"Brygada");

        add(TablePanelFactory.createTablePanel(
                "Zlecenie",
                new String[] {"Brygadzistów Lista"},
                new Object[][] {
                        {"Anna"}, {"Michal"},
                }
        ),"Zlecenie");

        add(TablePanelFactory.createTablePanel(
                "Praca",
                new String[] {"Brygadzistów Lista"},
                new Object[][] {
                        {"Michalina"}, {"Marcelina"},
                }
        ),"Praca");
    }

    private TablePanel createDzialPanel() {
        Object[][] rowData = DzialService.getDzialy().stream()
                .map(d -> new Object[]{ d.getNazwa_dzialu() })
                .toArray(Object[][]::new);

        return TablePanelFactory.createTablePanel(
                "Działy",
                new String[]{ "Nazwa" },
                rowData
        );
    }

    public void showPanel(String name) {
        cardLayout.show(this, name);
        revalidate();
        repaint();
    }

    public TablePanel getDzialPanel() {
        return dzialPanel;
    }

}
