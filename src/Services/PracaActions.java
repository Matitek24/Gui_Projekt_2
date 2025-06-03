package Services;

import Factory.DeleteUtil;
import Factory.TablePanel;
import Interface.EntityActions;
import Model.Praca;
import View.CenterPanel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Optional;

import Dialog.AddPracaDialog;

public class PracaActions implements EntityActions {
    private final CenterPanel centerPanel;
    private final Component parent;

    public PracaActions(CenterPanel centerPanel, Component parent) {
        this.centerPanel = centerPanel;
        this.parent = parent;
    }

    @Override
    public void onAdd() {
        Frame frame = JOptionPane.getFrameForComponent(parent);
        Optional<Praca> result = AddPracaDialog.showDialog(frame);
        result.ifPresent(p -> {
            PracaService.addPraca(p);

            TablePanel tp = centerPanel.getPracaPanel();
            DefaultTableModel m = tp.getTableModel();
            m.addRow(new Object[]{
                    p.getId(),
                    p.getOpis(),
                    p.getRodzajPracy(),
                    p.getCzasPracy(),
                    p.isCzyZrealizowane()
            });
        });
    }

    @Override
    public void onDelete() {
        DeleteUtil.deleteFromTable(
                parent,
                centerPanel.getPracaPanel(),
                PracaService.getPrace(),
                p -> p.getId() + " – " + p.getOpis(),
                PracaService::removePraca,
                Praca::getId
        );
    }

    @Override
    public void onEdit() {
        List<Praca> list = PracaService.getPrace();
        if (list.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Brak prac do edycji.");
            return;
        }

        // Tworzymy tablicę opcji w formacie "id - opis (rodzaj)"
        String[] opts = list.stream()
                .map(p -> p.getId() + " - " + p.getOpis() + " (" + p.getRodzajPracy() + ")")
                .toArray(String[]::new);

        String sel = (String) JOptionPane.showInputDialog(
                parent,
                "Wybierz pracę do edycji:",
                "Edytuj pracę",
                JOptionPane.PLAIN_MESSAGE,
                null,
                opts,
                opts[0]
        );
        if (sel == null) return;  // user anulował

        int id = Integer.parseInt(sel.split(" - ")[0]);
        Optional<Praca> opt = PracaService.getById(id);
        if (opt.isEmpty()) return;

        // Otwieramy dialog z wypełnionymi polami na podstawie istniejącej Praca
        Frame frame = JOptionPane.getFrameForComponent(parent);
        Optional<Praca> updated = AddPracaDialog.showDialog(frame, opt.get());

        updated.ifPresent(p -> {
            // Zapisujemy zmiany w bazie
            PracaService.updatePraca(p);

            // Odświeżamy wybrany wiersz w tabeli
            TablePanel tp = centerPanel.getPracaPanel();
            DefaultTableModel m = tp.getTableModel();
            for (int r = 0; r < m.getRowCount(); r++) {
                int rowId = ((Number) m.getValueAt(r, 0)).intValue();
                if (rowId == id) {
                    m.setValueAt(p.getOpis(), r, 1);
                    m.setValueAt(p.getRodzajPracy(), r, 2);
                    m.setValueAt(p.getCzasPracy(), r, 3);
                    m.setValueAt(p.isCzyZrealizowane(), r, 4);
                    break;
                }
            }
        });
    }
}
