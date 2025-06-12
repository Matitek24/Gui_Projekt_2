package Services;


import Factory.DeleteUtil;
import Factory.TablePanel;
import Interface.EntityActions;
import Model.Brygada;
import Model.Pracownik;
import View.CenterPanel;
import Dialog.AddBrygadaDialog;
import Exception.NotAcceptClassException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import Dialog.AssignPracownicyDialog;

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
        centerPanel.refreshAllTabs();
        centerPanel.setSelectedTab("Brygada");
    }
    public void onDelete() {
        DeleteUtil.deleteFromTable(
                parent,
                centerPanel.getBrygadaPanel(),
                BrygadaService.getBrygady(),
                b -> b.getId() + " – " + b.getName(),
                BrygadaService::removeBrygada,
                Brygada::getId
        );
    }


    public void onEdit(){
        TablePanel tp = centerPanel.getBrygadaPanel();
        JTable table = tp.getTable();
        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(parent, "Nie zaznoczono zadnej brygady");
            return;
        }

        long id = ((Number) tp.getTableModel().getValueAt(selectedRow, 0)).longValue();
        Optional<Brygada> opt = BrygadaService.getById(id);
        if (opt.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Nie znaleziono brygady o ID " + id);
            return;
        }

        Frame frame = JOptionPane.getFrameForComponent(parent);
        Optional<Brygada> updated = AddBrygadaDialog.showDialog(frame, opt.get());

        updated.ifPresent(b -> {
            DefaultTableModel m = tp.getTableModel();
            m.setValueAt(b.getName(), selectedRow, 1);
            m.setValueAt(b.getBrygadzista().getLogin(), selectedRow, 2);
            m.setValueAt(b.getListaPracownikow().size() + " prac.", selectedRow, 3);
        });

        centerPanel.refreshAllTabs();
        centerPanel.setSelectedTab("Brygada");
    }

    public List<JButton> getExtraButtons() {
        List<JButton> extra = new ArrayList<>();

        JButton btnDodajPrac = new JButton("Dodaj pracowników");
        btnDodajPrac.setFocusPainted(false);
        btnDodajPrac.setMargin(new Insets(8,12,8,12));
        btnDodajPrac.setOpaque(true);
        btnDodajPrac.setBorderPainted(false);
        btnDodajPrac.addActionListener(e -> pokazAssignDialog());
        extra.add(btnDodajPrac);

        return extra;
    }

    private void pokazAssignDialog() {
        // wykorzystujemy statyczny dialog:
        AssignPracownicyDialog.Result result =
                AssignPracownicyDialog.showDialog(
                        JOptionPane.getFrameForComponent(parent),
                        BrygadaService.getBrygady(),
                        PracownikService.getPracownicy()
                );
        if (result == null) return;

        Brygada b = result.getBrygada();
        List<Pracownik> wybrani = result.getPracownicy();
        try {
            BrygadaService.appendPracownik(b, wybrani);
        } catch (NotAcceptClassException ex) {
            JOptionPane.showMessageDialog(parent, "Błąd: " + ex.getMessage());
            return;
        }

        // odśwież kolumnę w tabeli brygad:
        TablePanel tp = centerPanel.getBrygadaPanel();
        DefaultTableModel m = tp.getTableModel();
        for (int r = 0; r < m.getRowCount(); r++) {
            if (((Number) m.getValueAt(r, 0)).intValue() == b.getId()) {
                m.setValueAt(b.getListaPracownikow().size() + " prac", r, 3);
                break;
            }
        }
        centerPanel.refreshAllTabs();
        centerPanel.setSelectedTab("Brygada");
    }

    }

