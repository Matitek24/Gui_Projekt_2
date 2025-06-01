package Services;

import Dialog.DeleteDialog;
import Dialog.EditDialog;
import Dialog.InputDialog;
import Exception.NotUniqueNameException;
import Factory.DeleteHelper;
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
        List<Dzial> dzialy = DzialService.getDzialy();
        if (dzialy.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Brak działów");
            return;
        }

        DeleteHelper.deleteMultiple(
                parent,
                dzialy,
                d -> d.getId() + " – " + d.getNazwa_dzialu(),
                DzialService::removeDzial,
                deletedList -> {
                    TablePanel tp = centerPanel.getDzialPanel();
                    DefaultTableModel model = tp.getTableModel();
                    for (int row = model.getRowCount() - 1; row >= 0; row--) {
                        int idInRow = (Integer) model.getValueAt(row, 0);
                        if (deletedList.stream().anyMatch(d -> d.getId() == idInRow)) {
                            model.removeRow(row);
                        }
                    }
                }
        );
    }


    @Override
    public void onEdit() {
        // Pobierz listę istniejących działów
        List<Dzial> dzialy = DzialService.getDzialy();
        if (dzialy.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Brak działów do edycji.");
            return;
        }

        // Zbuduj tablicę wyboru w formacie "ID – Nazwa"
        String[] options = dzialy.stream()
                .map(d -> d.getId() + " – " + d.getNazwa_dzialu())
                .toArray(String[]::new);

        // Pytamy, którego działu chcemy edytować
        String selected = (String) JOptionPane.showInputDialog(
                parent,
                "Wybierz dział do edycji:",
                "Edytuj dział",
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]
        );
        if (selected == null) {
            // anulowano wybór
            return;
        }

        // Parsujemy ID z wybranej opcji
        int id = Integer.parseInt(selected.split(" – ")[0]);
        Dzial oldDzial = dzialy.stream()
                .filter(d -> d.getId() == id)
                .findFirst()
                .orElse(null);
        if (oldDzial == null) {
            JOptionPane.showMessageDialog(parent, "Nie znaleziono działu o ID " + id);
            return;
        }

        // Używamy InputDialog tak samo jak w onAdd, ale z wstępną wartością
        String newName = InputDialog.showDialog(
                parent,
                "Nowa nazwa działu (poprzednio: " + oldDzial.getNazwa_dzialu() + ")",
                "Edytuj dział"
        );
        if (newName == null || newName.trim().isEmpty()) {
            // anulowano lub puste
            return;
        }

        try {
            // zmiana w modelu
            oldDzial.rename(newName.trim());
            DzialService.updateDzial(oldDzial);

            // aktualizacja tabeli
            TablePanel tp = centerPanel.getDzialPanel();
            DefaultTableModel model = tp.getTableModel();
            for (int row = 0; row < model.getRowCount(); row++) {
                if (((Integer) model.getValueAt(row, 0)) == id) {
                    model.setValueAt(newName.trim(), row, 1);
                    break;
                }
            }
        } catch (NotUniqueNameException e) {
            JOptionPane.showMessageDialog(parent, e.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
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
