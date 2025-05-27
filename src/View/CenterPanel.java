package View;

import Factory.TablePanel;
import Factory.TablePanelFactory;
import Model.Brygada;
import Services.DzialService;
import Services.PracownikService;

import javax.swing.*;
import java.awt.*;

public class CenterPanel extends JPanel {

    private CardLayout cardLayout;
    private TablePanel dzialPanel;
    private TablePanel pracownikPanel;

    public CenterPanel() {

        cardLayout = new CardLayout();
        setLayout(cardLayout);

        dzialPanel = createDzialPanel();
        add(dzialPanel, "Dział pracowników");

        pracownikPanel = createPracownikPanel();
        add(pracownikPanel, "Pracownik");

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
                .map(d -> new Object[]{
                        d.getId(),
                        d.getNazwa_dzialu()
                })
                .toArray(Object[][]::new);

        return TablePanelFactory.createTablePanel(
                "Działy",
                new String[]{ "ID", "Nazwa" },
                rowData
        );
    }

    private TablePanel createPracownikPanel() {
        Object[][] rowData = PracownikService.getPracownicy().stream()
                .map(p -> new Object[]{
                        p.getId(),
                        p.getImie(),
                        p.getNazwisko(),
                        p.getDzial().getNazwa_dzialu(),
                        p.getDataUrodzenia()
                })
                .toArray(Object[][]::new);

        return TablePanelFactory.createTablePanel(
                "Pracownicy",
                new String[]{"ID", "Imię", "Nazwisko", "Dział", "Data urodzenia"},
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

    public TablePanel getPracownikPanel() {
        return pracownikPanel;
    }

}
