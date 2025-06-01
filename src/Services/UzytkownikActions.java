package Services;

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

    public UzytkownikActions(CenterPanel centerPanel, Component parent) {
        this.centerPanel = centerPanel;
        this.parent = parent;
    }

    @Override
    public void onAdd() {
        Frame frame = JOptionPane.getFrameForComponent(parent);
        Optional<Uzytkownik> maybe = AddUserDialog.showDialog(frame);
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
        List<Uzytkownik> list = UzytkownikService.getUzytkownicy();
        if (list.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Brak użytkowników");
            return;
        }
        DeleteHelper.deleteMultiple(
                parent,
                list,
                u -> u.getId()+" – "+u.getLogin(),
                UzytkownikService::removeUzytkownik,
                deleted -> {
                    TablePanel tp = centerPanel.getUzytkownikPanel();
                    DefaultTableModel m = tp.getTableModel();
                    for (int row=m.getRowCount()-1; row>=0; row--) {
                        int id = (Integer)m.getValueAt(row,0);
                        if (deleted.stream().anyMatch(u->u.getId()==id)) {
                            m.removeRow(row);
                        }
                    }
                }
        );
    }

    @Override
    public void onEdit() {
        List<Uzytkownik> list = UzytkownikService.getUzytkownicy();
        if (list.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Brak użytkowników do edycji.");
            return;
        }
        String[] opts = list.stream()
                .map(u-> u.getId()+" – "+u.getLogin())
                .toArray(String[]::new);

        String sel = (String) JOptionPane.showInputDialog(
                parent, "Wybierz użytkownika:", "Edytuj",
                JOptionPane.PLAIN_MESSAGE, null, opts, opts[0]
        );
        if (sel==null) return;
        int id = Integer.parseInt(sel.split(" – ")[0]);
        Optional<Uzytkownik> opt = UzytkownikService.getById(id);
        if (opt.isEmpty()) return;

        Frame frame = JOptionPane.getFrameForComponent(parent);
        Optional<Uzytkownik> upd = AddUserDialog.showDialog(frame, opt.get());
        upd.ifPresent(u -> {
            TablePanel tp = centerPanel.getUzytkownikPanel();
            DefaultTableModel m = tp.getTableModel();
            for (int r=0; r<m.getRowCount(); r++) {
                if (((Integer)m.getValueAt(r,0))==id) {
                    m.setValueAt(u.getImie(), r,1);
                    m.setValueAt(u.getNazwisko(),r,2);
                    m.setValueAt(u.getDzial().getNazwa_dzialu(),r,3);
                    m.setValueAt(u.getLogin(), r,4);
                    m.setValueAt(u.getInicial(),r,5);
                    break;
                }
            }
        });
    }
}
