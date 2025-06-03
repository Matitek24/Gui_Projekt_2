package Services;

import Factory.DeleteUtil;
import Interface.EntityActions;
import View.CenterPanel;
import Dialog.AddEmployeeDialog;
import Factory.TablePanel;
import Interface.EntityActions;
import Model.Dzial;
import Model.Pracownik;
import View.CenterPanel;
import Factory.DeleteHelper;

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
        DeleteUtil.deleteFromTable(
                parent,
                centerPanel.getPracownikPanel(),
                PracownikService.getPracownicy(),
                p -> p.getId() + " – " + p.getImie() + " " + p.getNazwisko(),
                PracownikService::removePracownik,
                Pracownik::getId
        );
    }


    @Override
    public void onEdit() {
        // Pobierz wszystkich pracowników
        List<Pracownik> pracownicy = PracownikService.getPracownicy();
        if (pracownicy.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Brak pracowników do edycji.");
            return;
        }

        // Stwórz tablicę wyboru w formacie "ID – Imię Nazwisko"
        String[] options = pracownicy.stream()
                .map(p -> p.getId() + " – " + p.getImie() + " " + p.getNazwisko())
                .toArray(String[]::new);

        // Wyświetl okno wyboru
        String selected = (String) JOptionPane.showInputDialog(
                parent,
                "Wybierz pracownika do edycji:",
                "Edytuj pracownika",
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]
        );
        if (selected == null) {
            // użytkownik anulował
            return;
        }

        // Z parsowanego teksu wyciągnij ID (to, co było przed " – ")
        int id = Integer.parseInt(selected.split(" – ")[0]);
        Optional<Pracownik> opt = PracownikService.getPracownikById(id);
        if (opt.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Pracownik o ID " + id + " nie znaleziony.");
            return;
        }

        // Otwórz dialog edycji
        Frame frame = JOptionPane.getFrameForComponent(parent);
        Optional<Pracownik> updated = AddEmployeeDialog.showDialog(frame, opt.get());
        if (updated.isEmpty()) {
            // użytkownik anulował edycję
            return;
        }

        Pracownik p = updated.get();

        // Znajdź wiersz z tym ID w tabeli i zaktualizuj komórki
        TablePanel tp = centerPanel.getPracownikPanel();
        DefaultTableModel model = tp.getTableModel();
        JTable table = tp.getTable();
        for (int row = 0; row < model.getRowCount(); row++) {
            if (((Integer)model.getValueAt(row, 0)).intValue() == id) {
                model.setValueAt(p.getImie(), row, 1);
                model.setValueAt(p.getNazwisko(), row, 2);
                model.setValueAt(p.getDzial().getNazwa_dzialu(), row, 3);
                model.setValueAt(p.getDataUrodzenia(), row, 4);
                break;
            }
        }
    }

}
