package Services;

import Dialog.DeleteDialog;
import Dialog.EditDialog;
import Dialog.InputDialog;
import Exception.NotUniqueNameException;
import Factory.TablePanel;
import Interface.EntityActions;
import Model.Dzial;
import View.CenterPanel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class DzialActions implements EntityActions {
    private final CenterPanel centerPanel;
    private final Component parent;

    public DzialActions(CenterPanel centerPanel, Component parent) {
        this.centerPanel = centerPanel;
        this.parent = parent;
    }

    @Override
    public void onAdd() {
        String nazwa = InputDialog.showDialog(parent, "Podaj nazwę nowego działu", "Dodaj dział");
        if (nazwa == null || nazwa.trim().isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Nie podano nazwy działu");
            return;
        }

        try {
            Dzial dzial = Dzial.createDzial(nazwa.trim());
            DzialService.addDzial(dzial);
            TablePanel tp = centerPanel.getDzialPanel();
            tp.getTableModel().addRow(new Object[]{dzial.getId(), dzial.getNazwa_dzialu()});
        } catch (NotUniqueNameException e) {
            JOptionPane.showMessageDialog(parent, e.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void onDelete() {
        List<String> nazwy = DzialService.getNazwyDzialow();
        if (nazwy.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Brak działów");
            return;
        }

        // Nowe dialog z wielokrotnym wyborem:
        List<String> selectedList = DeleteDialog.showDialog(parent, nazwy, "Wybierz działy do usunięcia", "Usuń działy");
        if (selectedList.isEmpty()) return;

        List<Dzial> doUsuniecia = DzialService.getDzialy().stream()
                .filter(d -> selectedList.contains(d.getNazwa_dzialu()))
                .toList();

        for (Dzial dzial : doUsuniecia) {
            DzialService.removeDzial(dzial);
        }

        // Usuń z tabeli
        TablePanel tp = centerPanel.getDzialPanel();
        DefaultTableModel model = tp.getTableModel();

        // Iteracja wstecz — bezpieczniejsze przy usuwaniu z modelu
        for (int i = model.getRowCount() - 1; i >= 0; i--) {
            String nazwaZTabeli = model.getValueAt(i, 1).toString();
            if (selectedList.contains(nazwaZTabeli)) {
                model.removeRow(i);
            }
        }
    }


    @Override
    public void onEdit() {
        List<String> nazwy = DzialService.getNazwyDzialow();
        if (nazwy.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Brak działów");
            return;
        }

        String[] result = EditDialog.showDialog(parent, nazwy, "Wybierz dział", "Nowa nazwa", "Edytuj dział");
        if (result != null) {
            String selected = result[0];
            String newName = result[1];

            try {
                Dzial oldDzial = DzialService.getDzialy().stream()
                        .filter(d -> d.getNazwa_dzialu().equals(selected))
                        .findFirst().orElse(null);

                if (oldDzial != null) {
                    oldDzial.rename(newName);
                    DzialService.removeDzial(oldDzial);
                    Dzial updated = Dzial.createDzial(newName);
                    DzialService.addDzial(updated);

                    TablePanel tp = centerPanel.getDzialPanel();
                    DefaultTableModel model = tp.getTableModel();
                    for (int i = 0; i < model.getRowCount(); i++) {
                        if (selected.equals(model.getValueAt(i, 1))) {
                            model.setValueAt(newName, i, 1);
                            break;
                        }
                    }
                }
            } catch (NotUniqueNameException e) {
                JOptionPane.showMessageDialog(parent, e.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public List<JButton> getExtraButtons() {
        JButton extraBtn = new JButton("Pracownicy Działów");
        extraBtn.addActionListener(e -> {
           JOptionPane.showMessageDialog(parent,"Lista pracowników");
        });
        return List.of(extraBtn);
    }
}
