package Services;

import Dialog.ZleceniaCheckerDialog;
import Factory.DeleteUtil;
import Factory.TablePanel;
import Interface.EntityActions;
import Model.Brygadzista;
import Model.Praca;
import Model.Uzytkownik;
import Model.Zlecenie;
import Services.ZlecenieService;
import Services.PracaService;
import View.CenterPanel;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import Dialog.AddZlecenieDialog;


public class ZlecenieActions implements EntityActions {
    private final CenterPanel centerPanel;
    private final Component parent;
    private final Uzytkownik loggedUser;

    private final JButton btnZakoncz;
    private final JButton btnPraceZlecenia;

    public ZlecenieActions(CenterPanel centerPanel, Component parent, Uzytkownik loggedUser) {
        this.centerPanel = centerPanel;
        this.parent = parent;
        this.loggedUser = loggedUser;

        // ─── Tworzymy i stylizujemy przycisk "Zakończ zlecenie" ──────────────────
        btnZakoncz = new JButton("Zakończ zlecenie");
        btnZakoncz.setFocusPainted(false);
        btnZakoncz.setMargin(new Insets(8, 12, 8, 12));
        btnZakoncz.setBackground(new Color(220, 20, 60)); // czerwień
        btnZakoncz.setForeground(Color.WHITE);
        btnZakoncz.setOpaque(true);
        btnZakoncz.setBorderPainted(false);
        btnZakoncz.setEnabled(false); // na start wyłączony

        btnPraceZlecenia = new JButton("Prace Zlecenia");
        btnPraceZlecenia.setFocusPainted(false);
        btnPraceZlecenia.setMargin(new Insets(8, 12, 8, 12));
        btnPraceZlecenia.setBackground(new Color(30, 144, 255)); // niebieski
        btnPraceZlecenia.setForeground(Color.WHITE);
        btnPraceZlecenia.setOpaque(true);
        btnPraceZlecenia.setBorderPainted(false);
        btnPraceZlecenia.setEnabled(false); // domyślnie wyłączony

        btnPraceZlecenia.addActionListener(e -> pokazPraceZDziela());

        btnZakoncz.addActionListener(e -> {
            zakonczZlecenieIRefresh();
        });

        TablePanel tp = centerPanel.getZleceniePanel();
        JTable table = tp.getTable();
        table.getSelectionModel().addListSelectionListener((ListSelectionListener) event -> {
            if (!event.getValueIsAdjusting()) {
                int sel = table.getSelectedRow();
                boolean maZaznaczenie = (sel != -1);
                btnZakoncz.setEnabled(maZaznaczenie);
                btnPraceZlecenia.setEnabled(maZaznaczenie);
            }
        });
    }

    @Override
    public void onAdd() {
        Frame frame = JOptionPane.getFrameForComponent(parent);
        Optional<Zlecenie> dlg = AddZlecenieDialog.showDialog(frame);
        dlg.ifPresent(z -> {
            TablePanel tp = centerPanel.getZleceniePanel();
            DefaultTableModel m = tp.getTableModel();
            m.addRow(new Object[]{
                    z.getId(),
                    z.getStan_zlecenia(),
                    z.getBrygada() != null ? z.getBrygada().getName() : "Brak",
                    z.getPraca().size(),
                    z.getDataUtworzenia().toString(),
                    z.getDataRozpoczecia() != null ? z.getDataRozpoczecia().toString() : "",
                    z.getDataZakonczenia() != null ? z.getDataZakonczenia().toString() : ""
            });
        });
        centerPanel.refreshAllTabs();
        centerPanel.setSelectedTab("Zlecenie");
    }

    @Override
    public void onEdit() {
        TablePanel tp = centerPanel.getZleceniePanel();
        JTable table = tp.getTable();
        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(parent, "Nie zaznaczono żadnego zlecenia.");
            return;
        }

        int id = ((Number) tp.getTableModel().getValueAt(selectedRow, 0)).intValue();
        Optional<Zlecenie> opt = ZlecenieService.getById(id);
        if (opt.isEmpty()) return;

        Frame frame = JOptionPane.getFrameForComponent(parent);
        Optional<Zlecenie> updated = AddZlecenieDialog.showDialog(frame, opt.get());

        updated.ifPresent(z -> {
            DefaultTableModel m = tp.getTableModel();
            m.setValueAt(z.getStan_zlecenia(), selectedRow, 1);
            m.setValueAt(z.getBrygada() != null ? z.getBrygada().getName() : "Brak", selectedRow, 2);
            m.setValueAt(z.getPraca().size(), selectedRow, 3);
            m.setValueAt(z.getDataUtworzenia().toString(), selectedRow, 4);
            m.setValueAt(z.getDataRozpoczecia() != null ? z.getDataRozpoczecia().toString() : "", selectedRow, 5);
            m.setValueAt(z.getDataZakonczenia() != null ? z.getDataZakonczenia().toString() : "", selectedRow, 6);
        });
        centerPanel.refreshAllTabs();
        centerPanel.setSelectedTab("Zlecenie");
    }


    @Override
    public void onDelete() {
        DeleteUtil.deleteFromTable(
                parent,
                centerPanel.getZleceniePanel(),
                ZlecenieService.getZlecenia(),
                z -> z.getId() + " – " + z.getStan_zlecenia(),
                ZlecenieService::removeZlecenie,
                Zlecenie::getId
        );
        // Po usunięciu od razu odświeżamy panel:
        centerPanel.refreshAllTabs();
        centerPanel.setSelectedTab("Zlecenie");

    }

    @Override
    public List<JButton> getExtraButtons() {
        List<JButton> extra = new ArrayList<>();

        // Jeśli użytkownik jest brygadzistą, dodajemy przycisk "Moje zlecenia"
        if (loggedUser instanceof Brygadzista) {
            JButton btnMojeZlecenia = new JButton("Moje zlecenia");
            btnMojeZlecenia.setFocusPainted(false);
            btnMojeZlecenia.setMargin(new Insets(8, 12, 8, 12));
            btnMojeZlecenia.setBackground(new Color(60, 179, 113));
            btnMojeZlecenia.setForeground(Color.WHITE);
            btnMojeZlecenia.setOpaque(true);
            btnMojeZlecenia.setBorderPainted(false);

            btnMojeZlecenia.addActionListener(e -> {
                Frame frame = JOptionPane.getFrameForComponent(parent);
                ZleceniaCheckerDialog dialog = new ZleceniaCheckerDialog(
                        frame,
                        (Brygadzista) loggedUser
                );
                dialog.setVisible(true);
            });
            extra.add(btnMojeZlecenia);
        }

        // Dodajemy przycisk do kończenia zlecenia (zawsze widoczny, ale początkowo wyłączony)
        extra.add(btnZakoncz);
        extra.add(btnPraceZlecenia);

        return extra;
    }


    private void zakonczZlecenieIRefresh() {
        TablePanel tp = centerPanel.getZleceniePanel();
        JTable table = tp.getTable();
        DefaultTableModel model = tp.getTableModel();

        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            return;
        }

        int idZlecenia = ((Number) model.getValueAt(selectedRow, 0)).intValue();
        Optional<Zlecenie> optZ = ZlecenieService.getById(idZlecenia);
        if (optZ.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Nie znaleziono zlecenia o ID " + idZlecenia);
            return;
        }
        Zlecenie z = optZ.get();
        ZlecenieService.finishZlecenie(z);
        model.setValueAt(z.getStan_zlecenia(), selectedRow, 1);
        model.setValueAt(
                z.getDataZakonczenia() != null
                        ? z.getDataZakonczenia().toString()
                        : "",
                selectedRow,
                6
        );

        // 2) Wyłączamy guzik (zlecenie już zakończone):
        btnZakoncz.setEnabled(false);
    }

    private void pokazPraceZDziela(){
        TablePanel tp = centerPanel.getZleceniePanel();
        JTable table = tp.getTable();
        DefaultTableModel model = tp.getTableModel();
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {return;}

        int idZlecenia = ((Number) model.getValueAt(selectedRow, 0)).intValue();

        Zlecenie wybraneZlecenie = ZlecenieService.getById(idZlecenia).orElse(null);

        if (wybraneZlecenie == null) {return;}

        List<Praca> praceWZleceniu = wybraneZlecenie.getPraca();

        String[] columnNames = { "ID", "Opis", "Rodzaj", "Czas (min)", "Zrealizowane" };
        Object[][] rowData = praceWZleceniu.stream()
                .map(p -> new Object[]{
                        p.getId(),
                        p.getOpis(),
                        p.getRodzajPracy(),
                        p.getCzasPracy(),
                        p.isCzyZrealizowane()
                })
                .toArray(Object[][]::new);

        JTable pracTable = new JTable(new DefaultTableModel(rowData, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });

        pracTable.setFillsViewportHeight(true);

        JDialog dialog = new JDialog(
                JOptionPane.getFrameForComponent(parent),
                "Prace zlecenia #" + idZlecenia,
                true // modalny
        );
        dialog.setSize(500, 350);
        dialog.setLocationRelativeTo(parent);
        dialog.setLayout(new BorderLayout());

        dialog.add(new JScrollPane(pracTable), BorderLayout.CENTER);

        JButton zamknij = new JButton("Zamknij");
        zamknij.addActionListener(e -> dialog.dispose());
        JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        south.add(zamknij);
        dialog.add(south, BorderLayout.SOUTH);

        dialog.setVisible(true);



    }
}
