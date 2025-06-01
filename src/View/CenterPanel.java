package View;

import Factory.TablePanel;
import Factory.TablePanelFactory;
import Model.Brygadzista;
import Services.BrygadzistaService;
import Services.DzialService;
import Services.PracownikService;
import Services.UzytkownikService;

import javax.swing.*;
import java.awt.*;

public class CenterPanel extends JPanel {

    private CardLayout cardLayout;
    private TablePanel dzialPanel;
    private TablePanel pracownikPanel;
    private TablePanel uzytkownikPanel;
    private TablePanel brygadzistaPanel;

    public CenterPanel() {

        cardLayout = new CardLayout();
        setLayout(cardLayout);

        dzialPanel = createDzialPanel();
        add(dzialPanel, "Dział pracowników");

        pracownikPanel = createPracownikPanel();
        add(pracownikPanel, "Pracownik");

        uzytkownikPanel = createUzytkownikPanel();
        add(uzytkownikPanel, "Użytkownik");

        brygadzistaPanel = createBrygadzistaPanel();
        add(brygadzistaPanel, "Brygadzista");


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
    private TablePanel createUzytkownikPanel() {
        Object[][] data = UzytkownikService.getUzytkownicy().stream()
                .map(u -> new Object[]{
                        u.getId(),
                        u.getImie(),
                        u.getNazwisko(),
                        u.getDzial().getNazwa_dzialu(),
                        u.getLogin(),
                        u.getInicial()
                }).toArray(Object[][]::new);
        return TablePanelFactory.createTablePanel(
                "Uzytkownicy",
                new String[]{"ID","Imię","Nazwisko","Dział","Login","Inicjały"},
                data
        );
    }

    private TablePanel createBrygadzistaPanel() {
        Object[][] data = BrygadzistaService.getBrygadzisci().stream()
                .map(b -> new Object[]{
                        b.getBrygadzistaId(),
                        b.getImie(),
                        b.getNazwisko(),
                        b.getDzial().getNazwa_dzialu(),
                        b.getLogin(),
                        b.getInicial(),
                        b.getListaBrygad().size() + " brygad"
                }).toArray(Object[][]::new);
        return TablePanelFactory.createTablePanel(
                "Brygadziści",
                new String[]{"ID","Imię","Nazwisko","Dział","Login","Inicjały", "Liczba Brygad"},
                data
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

    public TablePanel getUzytkownikPanel() {
        return uzytkownikPanel;
    }

    public TablePanel getBrygadzistaPanel() {
        return brygadzistaPanel;
    }

}
