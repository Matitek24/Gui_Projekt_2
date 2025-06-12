package Services;

import Factory.DeleteUtil;
import Interface.EntityActions;
import View.CenterPanel;
import Dialog.AddEmployeeDialog;
import Factory.TablePanel;
import Interface.EntityActions;
import Model.Dzial;
import Model.Pracownik;
import View.CenterPanel;
import Factory.DeleteHelper;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

import java.awt.*;
import java.util.Optional;

public class PracownikActions implements EntityActions {

    private final CenterPanel centerPanel;
    private final Component parent;

    public PracownikActions(CenterPanel centerPanel, Component parent) {
        this.centerPanel = centerPanel;
        this.parent = parent;
    }


    @Override
    public void onAdd() {
        Frame frame = JOptionPane.getFrameForComponent(parent);

        Optional<Pracownik> maybe = AddEmployeeDialog.showDialog(frame);


        maybe.ifPresent(p -> {

            TablePanel tp = centerPanel.getPracownikPanel();
            DefaultTableModel model = tp.getTableModel();
            model.addRow(new Object[]{
                    p.getId(),
                    p.getImie(),
                    p.getNazwisko(),
                    p.getDzial().getNazwa_dzialu(),
                    p.getDataUrodzenia()
            });
        });
    }
    @Override
    public void onDelete() {
        DeleteUtil.deleteFromTable(
                parent,
                centerPanel.getPracownikPanel(),
                PracownikService.getPracownicy(),
                p -> p.getId() + " – " + p.getImie() + " " + p.getNazwisko(),
                PracownikService::removePracownik,
                Pracownik::getId
        );
    }


    @Override
    public void onEdit() {
        TablePanel tp = centerPanel.getPracownikPanel();
        JTable table = tp.getTable();
        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(parent, "Nie zaznaczono żadnego pracownika.");
            return;
        }

        int id = ((Number) tp.getTableModel().getValueAt(selectedRow, 0)).intValue();
        Optional<Pracownik> opt = PracownikService.getPracownikById(id);
        if (opt.isEmpty()) return;

        Frame frame = JOptionPane.getFrameForComponent(parent);
        Optional<Pracownik> updated = AddEmployeeDialog.showDialog(frame, opt.get());

        updated.ifPresent(p -> {
            DefaultTableModel model = tp.getTableModel();
            model.setValueAt(p.getImie(), selectedRow, 1);
            model.setValueAt(p.getNazwisko(), selectedRow, 2);
            model.setValueAt(p.getDzial().getNazwa_dzialu(), selectedRow, 3);
            model.setValueAt(p.getDataUrodzenia(), selectedRow, 4);
        });
    }


}
