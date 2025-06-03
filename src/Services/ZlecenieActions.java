package Services;

import Factory.DeleteUtil;
import Factory.TablePanel;
import Interface.EntityActions;
import Model.Zlecenie;
import View.CenterPanel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Optional;
import Dialog.AddZlecenieDialog;
import java.util.List;

public class ZlecenieActions implements EntityActions {
    private final CenterPanel centerPanel;
    private final Component parent;

    public ZlecenieActions(CenterPanel centerPanel, Component parent) {
        this.centerPanel = centerPanel;
        this.parent = parent;
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
    }

    public void onEdit() {
        List<Zlecenie> list = ZlecenieService.getZlecenia();
        if (list.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Brak zleceń do edycji.");
            return;
        }

        String[] opts = list.stream()
                .map(z -> z.getId() + " - " + z.getStan_zlecenia() + " (Brygada: " +
                        (z.getBrygada() != null ? z.getBrygada().getName() : "Brak") + ")")
                .toArray(String[]::new);

        String sel = (String) JOptionPane.showInputDialog(
                parent,
                "Wybierz zlecenie:",
                "Edytuj zlecenie",
                JOptionPane.PLAIN_MESSAGE,
                null,
                opts,
                opts[0]
        );
        if (sel == null) return;

        int id = Integer.parseInt(sel.split(" - ")[0]);
        Optional<Zlecenie> opt = ZlecenieService.getById(id);
        if (opt.isEmpty()) return;

        Frame frame = JOptionPane.getFrameForComponent(parent);
        Optional<Zlecenie> updated = AddZlecenieDialog.showDialog(frame, opt.get());

        updated.ifPresent(z -> {
            TablePanel tp = centerPanel.getZleceniePanel();
            DefaultTableModel m = tp.getTableModel();
            for (int r = 0; r < m.getRowCount(); r++) {
                int rowId = ((Number) m.getValueAt(r, 0)).intValue();
                if (rowId == id) {
                    m.setValueAt(z.getStan_zlecenia(), r, 1);
                    m.setValueAt(z.getBrygada() != null ? z.getBrygada().getName() : "Brak", r, 2);
                    m.setValueAt(z.getPraca().size(), r, 3);
                    m.setValueAt(z.getDataUtworzenia().toString(), r, 4);
                    m.setValueAt(z.getDataRozpoczecia() != null ? z.getDataRozpoczecia().toString() : "", r, 5);
                    m.setValueAt(z.getDataZakonczenia() != null ? z.getDataZakonczenia().toString() : "", r, 6);
                    break;
                }
            }
        });
    }
}
