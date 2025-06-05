package View;

import Factory.TablePanel;
import Factory.TablePanelFactory;
import Model.Brygadzista;
import Services.*;

import javax.swing.*;
import java.awt.*;

public class CenterPanel extends JPanel {

    private CardLayout cardLayout;
    private TablePanel dzialPanel;
    private TablePanel pracownikPanel;
    private TablePanel uzytkownikPanel;
    private TablePanel brygadzistaPanel;
    private TablePanel brygadaPanel;
    private TablePanel zleceniePanel;
    private TablePanel PracaPanel;

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

        zleceniePanel = createZleceniePanel();
        add(zleceniePanel, "Zlecenie");

        PracaPanel = createPracaPanel();
        add(PracaPanel, "Praca");

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
                        p.getDzial() != null ? p.getDzial().getNazwa_dzialu() : "Brak działu",
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
                        u.getDzial() != null ? u.getDzial().getNazwa_dzialu() : "Brak działu",
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
                        b.getDzial() != null ? b.getDzial().getNazwa_dzialu() : "Brak działu",
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
                        (br.getBrygadzista() != null
                                ? br.getBrygadzista().getImie() + " " +
                                br.getBrygadzista().getNazwisko() +
                                " (" + br.getBrygadzista().getBrygadzistaId() + ")"
                                : "Brak brygadzisty"),
                        br.getListaPracownikow().size() + " prac"
                })
                .toArray(Object[][]::new);

        return TablePanelFactory.createTablePanel(
                "Brygady",
                new String[]{"Id","Nazwa brygady", "Brygadzista", "Liczba pracowników"},
                data
        );
    };

    private TablePanel createZleceniePanel() {
        Object[][] data = ZlecenieService.getZlecenia().stream()
                .map(z -> new Object[] {
                        z.getId(),
                        z.getStan_zlecenia(),
                        z.getBrygada() != null ? z.getBrygada().getName() : "Brak",
                        z.getPraca().size(),
                        z.getDataUtworzenia().toString(),
                        z.getDataRozpoczecia() != null ? z.getDataRozpoczecia().toString() : "",
                        z.getDataZakonczenia() != null ? z.getDataZakonczenia().toString() : ""
                })
                .toArray(Object[][]::new);

        return TablePanelFactory.createTablePanel(
                "Zlecenia",
                new String[] { "ID", "Stan", "Brygada", "Liczba prac", "Data utworzenia", "Data rozpoczęcia", "Data zakończenia" },
                data
        );
    }

    private TablePanel createPracaPanel() {
        Object[][] data = PracaService.getPrace().stream()
                .map(p -> new Object[]{
                        p.getId(),
                        p.getOpis(),
                        p.getRodzajPracy(),
                        p.getCzasPracy(),
                        p.isCzyZrealizowane(),
                        p.getZlecenie() != null ? p.getZlecenie().getId() : "Brak"
                })
                .toArray(Object[][]::new);

        return TablePanelFactory.createTablePanel(
                "Prace",
                new String[]{"ID", "Opis", "Rodzaj", "Czas (min)", "Zrealizowane", "Zlecenie ID"},
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
    public TablePanel getBrygadaPanel() {
        return brygadaPanel;
    }
    public TablePanel getZleceniePanel() {
        return zleceniePanel;
    }
    public TablePanel getPracaPanel() {
        return PracaPanel;
    }

    public void refreshPanels() {
        removeAll();

        dzialPanel = createDzialPanel();
        pracownikPanel = createPracownikPanel();
        uzytkownikPanel = createUzytkownikPanel();
        brygadzistaPanel = createBrygadzistaPanel();
        brygadaPanel = createBrygadaPanel();
        zleceniePanel = createZleceniePanel();
        PracaPanel = createPracaPanel();

        add(dzialPanel, "Dział pracowników");
        add(pracownikPanel, "Pracownik");
        add(uzytkownikPanel, "Użytkownik");
        add(brygadzistaPanel, "Brygadzista");
        add(brygadaPanel, "Brygada");
        add(zleceniePanel, "Zlecenie");
        add(PracaPanel, "Praca");

        revalidate();
        repaint();
    }



}
