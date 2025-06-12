package Services;

import Dialog.InputDialog;
import Exception.NotUniqueNameException;
import Factory.DeleteUtil;
import Factory.TablePanel;
import Interface.EntityActions;
import Model.Dzial;
import Model.Pracownik;
import Services.DzialService;
import Services.PracownikService;
import View.CenterPanel;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class DzialActions implements EntityActions {
    private final CenterPanel centerPanel;
    private final Component parent;

    // Przycisk do podglądu pracowników danego działu
    private final JButton btnPracownicyDzialu;

    public DzialActions(CenterPanel centerPanel, Component parent) {
        this.centerPanel = centerPanel;
        this.parent = parent;

        // ─── Tworzymy i stylizujemy przycisk "Pracownicy Działu" ────────────────────
        btnPracownicyDzialu = new JButton("Pracownicy Działu");
        btnPracownicyDzialu.setFocusPainted(false);
        btnPracownicyDzialu.setMargin(new Insets(6, 10, 6, 10));
        btnPracownicyDzialu.setBackground(new Color(30, 144, 255)); // niebieski
        btnPracownicyDzialu.setForeground(Color.WHITE);
        btnPracownicyDzialu.setOpaque(true);
        btnPracownicyDzialu.setBorderPainted(false);
        btnPracownicyDzialu.setEnabled(false); // domyślnie wyłączony

        // Po kliknięciu otwieramy dialog z listą pracowników...
        btnPracownicyDzialu.addActionListener(e -> pokarzPracownikowZDzialu());

        // ─── Nasłuchujemy zaznaczenia w tabeli działów, żeby włączyć/wyłączyć guzik ───
        TablePanel tp = centerPanel.getDzialPanel();
        JTable table = tp.getTable();
        table.getSelectionModel().addListSelectionListener((ListSelectionListener) evt -> {
            if (!evt.getValueIsAdjusting()) {
                int sel = table.getSelectedRow();
                btnPracownicyDzialu.setEnabled(sel != -1);
            }
        });
    }

    @Override
    public void onAdd() {
        String nazwa = InputDialog.showDialog(parent, "Podaj nazwę nowego działu", "Dodaj dział");
        if (nazwa == null || nazwa.trim().isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Nie podano nazwy działu");
            return;
        }

        try {
            Dzial dzial = Dzial.createDzial(nazwa.trim());
            DzialService.addDzial(dzial);

            TablePanel tp = centerPanel.getDzialPanel();
            tp.getTableModel().addRow(new Object[]{dzial.getId(), dzial.getNazwa_dzialu()});
        } catch (NotUniqueNameException ex) {
            JOptionPane.showMessageDialog(parent, ex.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void onDelete() {
        DeleteUtil.deleteFromTable(
                parent,
                centerPanel.getDzialPanel(),
                DzialService.getDzialy(),
                d -> d.getId() + " – " + d.getNazwa_dzialu(),
                DzialService::removeDzial,
                Dzial::getId
        );
        centerPanel.refreshAllTabs();
    }

    @Override
    public void onEdit() {
        TablePanel tp = centerPanel.getDzialPanel();
        JTable table = tp.getTable();
        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(parent, "Nie zaznaczono żadnego działu do edycji.");
            return;
        }

        int id = (Integer) tp.getTableModel().getValueAt(selectedRow, 0);

        Dzial dzial = DzialService.getDzialy().stream()
                .filter(d -> d.getId() == id)
                .findFirst()
                .orElse(null);

        if (dzial == null) {
            JOptionPane.showMessageDialog(parent, "Nie znaleziono działu o ID " + id);
            return;
        }

        String newName = InputDialog.showDialog(
                parent,
                "Nowa nazwa działu (poprzednio: " + dzial.getNazwa_dzialu() + ")",
                "Edytuj dział"
        );

        if (newName == null || newName.trim().isEmpty()) return;

        try {
            dzial.rename(newName.trim());
            DzialService.updateDzial(dzial);

            tp.getTableModel().setValueAt(newName.trim(), selectedRow, 1);

        } catch (NotUniqueNameException ex) {
            JOptionPane.showMessageDialog(parent, ex.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
        }
    }


    @Override
    public List<JButton> getExtraButtons() {
        // Zwracamy przycisk "Pracownicy Działu"
        return List.of(btnPracownicyDzialu);
    }

    // ────────────────────────────────────────────────────────────────────────────────
    /**
     * Otwiera modalny dialog z tabelą pracowników przypisanych do
     * aktualnie zaznaczonego działu w tabeli "Działy".
     */
    private void pokarzPracownikowZDzialu() {
        TablePanel tp = centerPanel.getDzialPanel();
        JTable table = tp.getTable();
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) return;

        // Pobieramy ID zaznaczonego działu:
        int idDzialu = ((Number) tp.getTableModel().getValueAt(selectedRow, 0)).intValue();

        // Pobieramy obiekt Dział (jeśli go potrzebujesz, np. do tytułu dialogu):
        Dzial wybranyDzial = DzialService.getDzialy().stream()
                .filter(d -> d.getId() == idDzialu)
                .findFirst()
                .orElse(null);

        // Filtrujemy pracowników, którzy mają pole getDzial().getId() == idDzialu
        List<Pracownik> pracownicyWDziale = PracownikService.getPracownicy().stream()
                .filter(p -> {
                    if (p.getDzial() == null) return false;
                    return p.getDzial().getId() == idDzialu;
                })
                .collect(Collectors.toList());

        // Tworzymy kolumny i dane do tabeli:
        String[] columnNames = { "ID", "Imię", "Nazwisko", "Data urodzenia" };
        Object[][] rowData = pracownicyWDziale.stream()
                .map(p -> new Object[]{
                        p.getId(),
                        p.getImie(),
                        p.getNazwisko(),
                        p.getDataUrodzenia(),

                })
                .toArray(Object[][]::new);

        // Budujemy JTable w JScrollPane wewnątrz JDialog:
        JTable pracTable = new JTable(new DefaultTableModel(rowData, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
        pracTable.setFillsViewportHeight(true);

        // Przygotowujemy dialog:
        JDialog dialog = new JDialog(
                JOptionPane.getFrameForComponent(parent),
                (wybranyDzial != null
                        ? "Pracownicy działu: " + wybranyDzial.getNazwa_dzialu()
                        : "Pracownicy działu"),
                true // modalny
        );
        dialog.setSize(600, 400);
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
    // ────────────────────────────────────────────────────────────────────────────────
}
