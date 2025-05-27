package Services;

import Interface.EntityActions;
import View.CenterPanel;
import Dialog.AddEmployeeDialog;
import Factory.TablePanel;
import Interface.EntityActions;
import Model.Dzial;
import Model.Pracownik;
import View.CenterPanel;

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

    }
    @Override
    public void onEdit() {

    }
}
