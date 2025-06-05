package View;

import Factory.TablePanel;
import Factory.TablePanelFactory;
import Services.*;

import javax.swing.*;

public class CenterPanel extends JTabbedPane {

    private TablePanel dzialPanel;
    private TablePanel pracownikPanel;
    private TablePanel uzytkownikPanel;
    private TablePanel brygadzistaPanel;
    private TablePanel brygadaPanel;
    private TablePanel zleceniePanel;
    private TablePanel pracaPanel;

    public CenterPanel() {
        // Dodajemy kolejne zakładki w tej samej kolejności,
        // w jakiej kiedyś mieliśmy karty w CardLayout.

        dzialPanel = createDzialPanel();
        addTab("Dział pracowników", dzialPanel);

        pracownikPanel = createPracownikPanel();
        addTab("Pracownik", pracownikPanel);

        uzytkownikPanel = createUzytkownikPanel();
        addTab("Użytkownik", uzytkownikPanel);

        brygadzistaPanel = createBrygadzistaPanel();
        addTab("Brygadzista", brygadzistaPanel);

        brygadaPanel = createBrygadaPanel();
        addTab("Brygada", brygadaPanel);

        zleceniePanel = createZleceniePanel();
        addTab("Zlecenie", zleceniePanel);

        pracaPanel = createPracaPanel();
        addTab("Praca", pracaPanel);
    }

    /**
     * Pozwala wybrać zakładkę po tytule (używane w LeftPanel zamiast showPanel()).
     */
    public void setSelectedTab(String title) {
        int idx = indexOfTab(title);
        if (idx >= 0) {
            setSelectedIndex(idx);
        }
    }

    /**
     * Jeśli gdziekolwiek dotychczas wywoływałeś refreshPanels(),
     * możesz teraz wywołać refreshAllTabs() – usunie wszystkie i doda ponownie.
     */
    public void refreshAllTabs() {
        removeAll();

        dzialPanel = createDzialPanel();
        addTab("Dział pracowników", dzialPanel);

        pracownikPanel = createPracownikPanel();
        addTab("Pracownik", pracownikPanel);

        uzytkownikPanel = createUzytkownikPanel();
        addTab("Użytkownik", uzytkownikPanel);

        brygadzistaPanel = createBrygadzistaPanel();
        addTab("Brygadzista", brygadzistaPanel);

        brygadaPanel = createBrygadaPanel();
        addTab("Brygada", brygadaPanel);

        zleceniePanel = createZleceniePanel();
        addTab("Zlecenie", zleceniePanel);

        pracaPanel = createPracaPanel();
        addTab("Praca", pracaPanel);

        revalidate();
        repaint();
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
                new String[]{ "ID", "Imię", "Nazwisko", "Dział", "Data urodzenia" },
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
                "Użytkownicy",
                new String[]{ "ID", "Imię", "Nazwisko", "Dział", "Login", "Inicjały" },
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
                new String[]{ "ID", "Imię", "Nazwisko", "Dział", "Login", "Inicjały", "Liczba Brygad" },
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
                new String[]{ "ID", "Nazwa brygady", "Brygadzista", "Liczba pracowników" },
                data
        );
    }

    private TablePanel createZleceniePanel() {
        Object[][] data = ZlecenieService.getZlecenia().stream()
                .map(z -> new Object[]{
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
                new String[]{ "ID", "Stan", "Brygada", "Liczba prac", "Data utworzenia", "Data rozpoczęcia", "Data zakończenia" },
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
                new String[]{ "ID", "Opis", "Rodzaj", "Czas (min)", "Zrealizowane", "Zlecenie ID" },
                data
        );
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
        return pracaPanel;
    }
}
