package Services;

import Factory.DeleteHelper;
import Factory.TablePanel;
import Interface.EntityActions;
import Model.Brygada;
import Model.Brygadzista;
import View.CenterPanel;
import Dialog.AddBrygadaDialog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Optional;

public class BrygadaActions implements EntityActions {
    private final CenterPanel centerPanel;
    private final Component parent;

    public BrygadaActions(CenterPanel centerPanel, Component parent) {
        this.centerPanel = centerPanel;
        this.parent = parent;
    }

    public void onAdd(){
        Frame frame = JOptionPane.getFrameForComponent(parent);
        Optional<Brygada> dlg = AddBrygadaDialog.showDialog(frame);
        dlg.ifPresent(b -> {
            TablePanel tp = centerPanel.getBrygadaPanel();
            DefaultTableModel m = tp.getTableModel();
            m.addRow(new Object[]{
                    b.getId(),
                    b.getName(),
                    b.getBrygadzista().getLogin(),
                    b.getListaPracownikow().size() + " prac"
            });
        });
    }
    public void onDelete(){
        List<Brygada> brygady = BrygadaService.getBrygady();

        DeleteHelper.deleteMultiple(
                parent,
                brygady,
                b -> b.getId() + " - " + b.getName(),
                BrygadaService::removeBrygada,
                deletedList -> {
                    TablePanel tp = centerPanel.getBrygadaPanel();
                    DefaultTableModel m = tp.getTableModel();
                    for (int row = m.getRowCount() - 1; row >= 0; row--) {
                        long rowId = ((Number) m.getValueAt(row, 0)).longValue();
                        if (deletedList.stream().anyMatch(b -> b.getId() == rowId)) {
                            m.removeRow(row);
                        }
                    }
                }
        );
    }
    public void onEdit(){
        List<Brygada> list = BrygadaService.getBrygady();
        if (list.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Brak brygad do edycji.");
            return;
        }

        String[] opts = list.stream()
                .map(b -> b.getId() + " - " + b.getName() + " (" + b.getBrygadzista().getLogin() + ")")
                .toArray(String[]::new);

        String sel = (String) JOptionPane.showInputDialog(
                parent,
                "Wybierz brygadę:",
                "Edytuj brygadę",
                JOptionPane.PLAIN_MESSAGE,
                null,
                opts,
                opts[0]
        );
        if (sel == null) return;

        long id = Long.parseLong(sel.split(" - ")[0]);
        Optional<Brygada> opt = BrygadaService.getById(id);
        if (opt.isEmpty()) return;

        Frame frame = JOptionPane.getFrameForComponent(parent);
        Optional<Brygada> updated = AddBrygadaDialog.showDialog(frame, opt.get());

        updated.ifPresent(b -> {
            TablePanel tp = centerPanel.getBrygadaPanel();
            DefaultTableModel m = tp.getTableModel();
            for (int r = 0; r < m.getRowCount(); r++) {
                long rowId = ((Number) m.getValueAt(r, 0)).longValue();
                if (rowId == id) {
                    m.setValueAt(b.getName(), r, 1);
                    m.setValueAt(b.getBrygadzista().getLogin(), r, 2);
                    m.setValueAt(b.getListaPracownikow().size() + " prac.", r, 3);
                    break;
                }
            }
        });
    }
    }

