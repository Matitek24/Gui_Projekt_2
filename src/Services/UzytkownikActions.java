package Services;

import Factory.DeleteUtil;
import Interface.EntityActions;
import View.CenterPanel;
import Dialog.AddUserDialog;
import Factory.TablePanel;
import Factory.DeleteHelper;
import Model.Uzytkownik;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Optional;

public class UzytkownikActions implements EntityActions {
    private final CenterPanel centerPanel;
    private final Component parent;
    private final Uzytkownik loggedUser;

    public UzytkownikActions(CenterPanel centerPanel, Component parent, Uzytkownik loggedUser) {
        this.centerPanel = centerPanel;
        this.parent = parent;
        this.loggedUser = loggedUser;
    }

    @Override
    public void onAdd() {
        Frame frame = JOptionPane.getFrameForComponent(parent);
        Optional<Uzytkownik> maybe = AddUserDialog.showDialog(frame, loggedUser);
        maybe.ifPresent(u -> {
            TablePanel tp = centerPanel.getUzytkownikPanel();
            DefaultTableModel m = tp.getTableModel();
            m.addRow(new Object[]{
                    u.getId(),
                    u.getImie(),
                    u.getNazwisko(),
                    u.getDzial().getNazwa_dzialu(),
                    u.getLogin(),
                    u.getInicial()
            });
        });
    }

    @Override
    public void onDelete() {
        DeleteUtil.deleteFromTable(
                parent,
                centerPanel.getUzytkownikPanel(),
                UzytkownikService.getUzytkownicy(),
                u -> u.getId() + " – " + u.getLogin(),
                UzytkownikService::removeUzytkownik,
                Uzytkownik::getId
        );
    }


    @Override
    public void onEdit() {
        TablePanel tp = centerPanel.getUzytkownikPanel();
        JTable table = tp.getTable();
        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(parent, "Nie zaznaczono żadnego użytkownika.");
            return;
        }

        int id = ((Number) tp.getTableModel().getValueAt(selectedRow, 0)).intValue();
        Optional<Uzytkownik> opt = UzytkownikService.getById(id);
        if (opt.isEmpty()) return;

        Frame frame = JOptionPane.getFrameForComponent(parent);
        Optional<Uzytkownik> upd = AddUserDialog.showDialog(frame, opt.get(), loggedUser);

        upd.ifPresent(u -> {
            DefaultTableModel m = tp.getTableModel();
            m.setValueAt(u.getImie(), selectedRow, 1);
            m.setValueAt(u.getNazwisko(), selectedRow, 2);
            m.setValueAt(u.getDzial().getNazwa_dzialu(), selectedRow, 3);
            m.setValueAt(u.getLogin(), selectedRow, 4);
            m.setValueAt(u.getInicial(), selectedRow, 5);
        });
    }

}
