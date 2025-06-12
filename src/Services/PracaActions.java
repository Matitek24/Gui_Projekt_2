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
        centerPanel.refreshAllTabs();
        centerPanel.setSelectedTab("Praca");

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
        TablePanel tp = centerPanel.getPracaPanel();
        JTable table = tp.getTable();
        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(parent, "Nie zaznaczono żadnej pracy.");
            return;
        }

        int id = ((Number) tp.getTableModel().getValueAt(selectedRow, 0)).intValue();
        Optional<Praca> opt = PracaService.getById(id);
        if (opt.isEmpty()) return;

        Frame frame = JOptionPane.getFrameForComponent(parent);
        Optional<Praca> updated = AddPracaDialog.showDialog(frame, opt.get());

        updated.ifPresent(p -> {
            PracaService.updatePraca(p);
            DefaultTableModel m = tp.getTableModel();
            m.setValueAt(p.getOpis(), selectedRow, 1);
            m.setValueAt(p.getRodzajPracy(), selectedRow, 2);
            m.setValueAt(p.getCzasPracy(), selectedRow, 3);
            m.setValueAt(p.isCzyZrealizowane(), selectedRow, 4);
        });
        centerPanel.refreshAllTabs();
        centerPanel.setSelectedTab("Praca");
    }

}
