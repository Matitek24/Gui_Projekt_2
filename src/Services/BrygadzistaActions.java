package Services;

import Factory.DeleteHelper;
import Factory.DeleteUtil;
import Factory.TablePanel;
import Interface.EntityActions;
import Model.Brygadzista;
import Model.Uzytkownik;
import View.CenterPanel;
import Dialog.AddBrygadzistaDialog;

import java.util.ArrayList;
import java.util.List;
import Dialog.ShowHistoryDialog;


import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Optional;

public class BrygadzistaActions implements EntityActions {

    private final CenterPanel centerPanel;
    private final Component parent;
    private final Uzytkownik loggedInUser;
    private final JButton btnHistoria;

    public BrygadzistaActions(CenterPanel centerPanel, Component parent, Uzytkownik loggedInUser) {
        this.centerPanel = centerPanel;
        this.parent = parent;
        this.loggedInUser = loggedInUser;

        btnHistoria = new JButton("Historia");
        btnHistoria.setFocusPainted(false);
        btnHistoria.setMargin(new Insets(6, 10, 6, 10));
        btnHistoria.setBackground(new Color(30, 144, 255));
        btnHistoria.setForeground(Color.WHITE);
        btnHistoria.setOpaque(true);
        btnHistoria.setBorderPainted(false);
        btnHistoria.setEnabled(false);

        btnHistoria.addActionListener(e -> pokazHistorieBrygad());
        TablePanel tp = centerPanel.getBrygadzistaPanel();
        tp.getTable().getSelectionModel().addListSelectionListener((ListSelectionListener) evt -> {
            if (!evt.getValueIsAdjusting()) {
                btnHistoria.setEnabled(tp.getTable().getSelectedRow() != -1);
            }
        });
    }

    public void onAdd(){
        Frame frame = JOptionPane.getFrameForComponent(parent);
        Optional<Brygadzista> dlg = AddBrygadzistaDialog.showDialog(frame, loggedInUser);
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
        DeleteUtil.deleteFromTable(
                parent,
                centerPanel.getBrygadzistaPanel(),
                BrygadzistaService.getBrygadzisci(),
                b -> b.getBrygadzistaId() + " – " + b.getLogin(),
                BrygadzistaService::removeBrygadzista,
                Brygadzista::getBrygadzistaId
        );

        centerPanel.refreshAllTabs();
        centerPanel.setSelectedTab("Brygadzista");
    }


    @Override
    public void onEdit() {
        TablePanel tp = centerPanel.getBrygadzistaPanel();
        JTable table = tp.getTable();
        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(parent, "Nie zaznaczono żadnego brygadzisty do edycji.");
            return;
        }

        int id = ((Number) tp.getTableModel().getValueAt(selectedRow, 0)).intValue();
        Optional<Brygadzista> opt = BrygadzistaService.getById(id);
        if (opt.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Nie znaleziono brygadzisty o ID " + id);
            return;
        }

        Frame frame = JOptionPane.getFrameForComponent(parent);
        Optional<Brygadzista> upd = AddBrygadzistaDialog.showDialog(frame, opt.get(), loggedInUser);

        upd.ifPresent(b -> {
            DefaultTableModel m = tp.getTableModel();
            m.setValueAt(b.getImie(), selectedRow, 1);
            m.setValueAt(b.getNazwisko(), selectedRow, 2);
            m.setValueAt(b.getDzial().getNazwa_dzialu(), selectedRow, 3);
            m.setValueAt(b.getLogin(), selectedRow, 4);
            m.setValueAt(b.getInicial(), selectedRow, 5);
            m.setValueAt(b.getListaBrygad().size() + " brygad", selectedRow, 6);
        });
    }

    @Override
    public List<JButton> getExtraButtons() {
        List<JButton> extra = new ArrayList<>();
        extra.add(btnHistoria);
        return extra;
    }

    private void pokazHistorieBrygad() {
        TablePanel tp = centerPanel.getBrygadzistaPanel();
        int row = tp.getTable().getSelectedRow();
        if (row == -1) return;

        int id = ((Number) tp.getTableModel().getValueAt(row, 0)).intValue();
        Brygadzista b = BrygadzistaService.getById(id).orElse(null);
        if (b == null) return;

        // pokazujemy listę nazw brygad z historii
        ShowHistoryDialog.show(
                JOptionPane.getFrameForComponent(parent),
                b.getHistoriaBrygad()
        );
    }



}
