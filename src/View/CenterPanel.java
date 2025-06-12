package View;

import Factory.TablePanel;
import Factory.TablePanelFactory;
import Services.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

public class CenterPanel extends JTabbedPane {
    // Pola paneli i suppliers
    private TablePanel dzialPanel;
    private TablePanel pracownikPanel;
    private TablePanel uzytkownikPanel;
    private TablePanel brygadzistaPanel;
    private TablePanel brygadaPanel;
    private TablePanel zleceniePanel;
    private TablePanel pracaPanel;

    private final Map<String, Supplier<TablePanel>> panelSuppliers = new LinkedHashMap<>();

    public CenterPanel() {

        panelSuppliers.put("Dział pracowników", () -> { dzialPanel = createDzialPanel(); return dzialPanel; });
        panelSuppliers.put("Pracownik",          () -> { pracownikPanel = createPracownikPanel(); return pracownikPanel; });
        panelSuppliers.put("Użytkownik",         () -> { uzytkownikPanel = createUzytkownikPanel(); return uzytkownikPanel; });
        panelSuppliers.put("Brygadzista",        () -> { brygadzistaPanel = createBrygadzistaPanel(); return brygadzistaPanel; });
        panelSuppliers.put("Brygada",            () -> { brygadaPanel = createBrygadaPanel(); return brygadaPanel; });
        panelSuppliers.put("Zlecenie",           () -> { zleceniePanel = createZleceniePanel(); return zleceniePanel; });
        panelSuppliers.put("Praca",              () -> { pracaPanel = createPracaPanel(); return pracaPanel; });
        setSelectedTab("Dział pracowników");
    }


    public void setSelectedTab(String title) {
        int idx = indexOfTab(title);
        if (idx < 0) {
            Supplier<TablePanel> supplier = panelSuppliers.get(title);
            if (supplier != null) {
                TablePanel panel = supplier.get();
                addClosableTab(title, panel);
                idx = indexOfTab(title);
            }
        }
        if (idx >= 0) {
            setSelectedIndex(idx);
        }
    }


    public void refreshAllTabs() {
        removeAll();
        for (Map.Entry<String, Supplier<TablePanel>> entry : panelSuppliers.entrySet()) {
            String title = entry.getKey();
            TablePanel panel = entry.getValue().get();
            addClosableTab(title, panel);
        }
        revalidate();
        repaint();
    }

    private void addClosableTab(String title, TablePanel comp) {
        addTab(title, comp);
        int idx = indexOfComponent(comp);

        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        header.setOpaque(true);
        header.setBackground(getBackground());
        JLabel lbl = new JLabel(title);
        JButton btnClose = new JButton("✕");
        btnClose.setBorder(null);
        btnClose.setOpaque(false);
        btnClose.setFocusable(false);
        btnClose.setPreferredSize(new Dimension(16, 16));
        btnClose.addActionListener(e -> {
            removeTabAt(indexOfComponent(comp));
        });

        header.add(lbl);
        header.add(Box.createHorizontalStrut(4));
        header.add(btnClose);
        setTabComponentAt(idx, header);
    }



    // Metody tworzenia paneli:
    private TablePanel createDzialPanel() {
        Object[][] data = DzialService.getDzialy().stream()
                .map(d -> new Object[]{
                        d.getId(),
                        d.getNazwa_dzialu()})
                .toArray(Object[][]::new);
        return TablePanelFactory.createTablePanel("Działy", new String[]{"ID", "Nazwa"}, data);
    }

    private TablePanel createPracownikPanel() {
        Object[][] data = PracownikService.getPracownicy().stream()
                .map(p -> new Object[]{
                        p.getId(),
                        p.getImie(),
                        p.getNazwisko(),
                        p.getDzial() != null ? p.getDzial().getNazwa_dzialu() : "Brak działu",
                        p.getDataUrodzenia()})
                .toArray(Object[][]::new);
        return TablePanelFactory.createTablePanel("Pracownicy",
                new String[]{"ID","Imię","Nazwisko","Dział","Data urodzenia"}, data);
    }

    private TablePanel createUzytkownikPanel() {
        Object[][] data = UzytkownikService.getUzytkownicy().stream()
                .map(u -> new Object[]{
                        u.getId(),
                        u.getImie(),
                        u.getNazwisko(),
                        u.getDzial()!=null?u.getDzial().getNazwa_dzialu():"Brak działu",
                        u.getLogin(), u.getInicial()})
                .toArray(Object[][]::new);
        return TablePanelFactory.createTablePanel("Użytkownicy",
                new String[]{"ID","Imię","Nazwisko","Dział","Login","Inicjały"}, data);
    }

    private TablePanel createBrygadzistaPanel() {
        Object[][] data = BrygadzistaService.getBrygadzisci().stream()
                .map(b -> new Object[]{
                        b.getBrygadzistaId(),
                        b.getImie(),
                        b.getNazwisko(),
                        b.getDzial()!=null?b.getDzial().getNazwa_dzialu():"Brak działu",
                        b.getLogin(),b.getInicial(),b.getListaBrygad().size()+" brygad"})
                .toArray(Object[][]::new);
        return TablePanelFactory.createTablePanel("Brygadziści",
                new String[]{"ID","Imię","Nazwisko","Dział","Login","Inicjały","Liczba Brygad"}, data);
    }

    private TablePanel createBrygadaPanel() {
        Object[][] data = BrygadaService.getBrygady().stream()
                .map(br -> new Object[]{
                        br.getId(),
                        br.getName(),
                        br.getBrygadzista()!=null?br.getBrygadzista().getImie()+" "+br.getBrygadzista().getNazwisko():"Brak brygadzisty",
                        br.getListaPracownikow().size()+" prac"})
                .toArray(Object[][]::new);
        return TablePanelFactory.createTablePanel("Brygady",
                new String[]{"ID","Nazwa brygady","Brygadzista","Liczba pracowników"}, data);
    }

    private TablePanel createZleceniePanel() {
        Object[][] data = ZlecenieService.getZlecenia().stream()
                .map(z -> new Object[]{
                        z.getId(),
                        z.getStan_zlecenia(),
                        z.getBrygada()!=null?z.getBrygada().getName():"Brak",
                        z.getPraca().size(),z.getDataUtworzenia(),
                        z.getDataRozpoczecia()!=null?z.getDataRozpoczecia():"",
                        z.getDataZakonczenia()!=null?z.getDataZakonczenia():""})
                .toArray(Object[][]::new);
        return TablePanelFactory.createTablePanel("Zlecenia",
                new String[]{"ID","Stan","Brygada","Liczba prac","Data utworzenia","Data rozpoczęcia","Data zakończenia"}, data);
    }

    private TablePanel createPracaPanel() {
        Object[][] data = PracaService.getPrace().stream()
                .map(p -> new Object[]{
                        p.getId(),
                        p.getOpis(),
                        p.getRodzajPracy(),
                        p.getCzasPracy(),
                        p.isCzyZrealizowane(),
                        p.getZlecenie()!=null?p.getZlecenie().getId():"Brak"})
                .toArray(Object[][]::new);
        return TablePanelFactory.createTablePanel("Prace",
                new String[]{"ID","Opis","Rodzaj","Czas (min)","Zrealizowane","Zlecenie ID"}, data);
    }

    public TablePanel getDzialPanel()
    {
        return dzialPanel;
    }
    public TablePanel getPracownikPanel()
    {
        return pracownikPanel;
    }
    public TablePanel getUzytkownikPanel()
    {
        return uzytkownikPanel;
    }
    public TablePanel getBrygadzistaPanel()
    {
        return brygadzistaPanel;
    }
    public TablePanel getBrygadaPanel()
    {
        return brygadaPanel;
    }
    public TablePanel getZleceniePanel()
    {
        return zleceniePanel;
    }
    public TablePanel getPracaPanel()
    {
        return pracaPanel;
    }
}
