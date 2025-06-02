package View;

import Factory.TablePanel;
import Factory.TablePanelFactory;
import Model.Brygadzista;
import Services.BrygadzistaService;
import Services.DzialService;
import Services.PracownikService;
import Services.UzytkownikService;
import Services.BrygadaService;

import javax.swing.*;
import java.awt.*;

public class CenterPanel extends JPanel {

    private CardLayout cardLayout;
    private TablePanel dzialPanel;
    private TablePanel pracownikPanel;
    private TablePanel uzytkownikPanel;
    private TablePanel brygadzistaPanel;
    private TablePanel brygadaPanel;

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

        brygadaPanel = createBrygadaPanel();
        add(brygadaPanel, "Brygada");

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

    private TablePanel createBrygadaPanel() {
        Object[][] data = BrygadaService.getBrygady().stream()
                .map(br -> new Object[]{
                        br.getId(),
                        br.getName(),
                        br.getBrygadzista().getImie() + " " +
                                br.getBrygadzista().getNazwisko() +
                                " (" + br.getBrygadzista().getBrygadzistaId() + ")",
                        br.getListaPracownikow().size() + " prac"
                })
                .toArray(Object[][]::new);

        return TablePanelFactory.createTablePanel(
                "Brygady",
                new String[]{"Id","Nazwa brygady", "Brygadzista", "Liczba pracowników"},
                data
        );
    };



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
    public TablePanel getBrygadaPanel() {
        return brygadaPanel;
    }

}
