package Services;

import Factory.DeleteHelper;
import Factory.TablePanel;
import Interface.EntityActions;
import Model.Brygadzista;
import Model.Uzytkownik;
import View.CenterPanel;
import Dialog.AddBrygadzistaDialog;
import java.util.List;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Optional;

public class BrygadzistaActions implements EntityActions {

    private final CenterPanel centerPanel;
    private final Component parent;

    public BrygadzistaActions(CenterPanel centerPanel, Component parent) {
        this.centerPanel = centerPanel;
        this.parent = parent;
    }

    public void onAdd(){
        Frame frame = JOptionPane.getFrameForComponent(parent);
        Optional<Brygadzista> dlg = AddBrygadzistaDialog.showDialog(frame);
        dlg.ifPresent(b -> {
            TablePanel tp = centerPanel.getBrygadzistaPanel();
            DefaultTableModel m = tp.getTableModel();
            m.addRow(new Object[]{
                    b.getBrygadzistaId(),
                    b.getImie(),
                    b.getNazwisko(),
                    b.getDzial().getNazwa_dzialu(),
                    b.getLogin(),
                    b.getInicial(),
                    b.getListaBrygad().size() + " brygad"
            });
        });
    }

    public void onDelete() {
        List<Brygadzista> brygadzisci = BrygadzistaService.getBrygadzisci();

        DeleteHelper.deleteMultiple(
                parent,
                brygadzisci,
                b -> b.getBrygadzistaId() + " - " + b.getLogin(),
                BrygadzistaService::removeBrygadzista,
                deleted -> {
                    TablePanel tp = centerPanel.getBrygadzistaPanel();
                    DefaultTableModel m = tp.getTableModel();
                    for (int row = m.getRowCount() - 1; row >= 0; row--) {
                        long id = ((Number) m.getValueAt(row, 0)).longValue();
                        if (deleted.stream().anyMatch(b -> b.getBrygadzistaId() == id)) {
                            m.removeRow(row);
                        }
                    }
                }
        );
    }


    @Override
    public void onEdit() {
        List<Brygadzista> list = BrygadzistaService.getBrygadzisci();
        if (list.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Brak brygadzistów do edycji.");
            return;
        }

        String[] opts = list.stream()
                .map(b -> b.getBrygadzistaId() + " - " + b.getLogin() + " - " + b.getImie())
                .toArray(String[]::new);

        String sel = (String) JOptionPane.showInputDialog(
                parent,
                "Wybierz brygadzistę:",
                "Edytuj brygadzistę",
                JOptionPane.PLAIN_MESSAGE,
                null,
                opts,
                opts[0]
        );
        if (sel == null) {
            return;
        }

        int id = Integer.parseInt(sel.split(" - ")[0]);
        Optional<Brygadzista> opt = BrygadzistaService.getById(id);
        if (opt.isEmpty()) {
            return;
        }

        Frame frame = JOptionPane.getFrameForComponent(parent);
        Optional<Brygadzista> upd = AddBrygadzistaDialog.showDialog(frame, opt.get());

        upd.ifPresent(b -> {
            TablePanel tp = centerPanel.getBrygadzistaPanel(); // <<— Zwróć uwagę: brygadzistaPanel
            DefaultTableModel m = tp.getTableModel();
            for (int r = 0; r < m.getRowCount(); r++) {
                int rowId = ((Number) m.getValueAt(r, 0)).intValue();
                if (rowId == id) {
                    m.setValueAt(b.getImie(), r, 1);
                    m.setValueAt(b.getNazwisko(), r, 2);
                    m.setValueAt(b.getDzial().getNazwa_dzialu(), r, 3);
                    m.setValueAt(b.getLogin(), r, 4);
                    m.setValueAt(b.getInicial(), r, 5);
                    m.setValueAt(b.getListaBrygad().size() + " brygad", r, 6);
                    break;
                }
            }
        });
    }

}
