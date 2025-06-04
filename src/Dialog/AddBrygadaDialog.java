// package Dialog/AddBrygadaDialog.java

package Dialog;

import Model.Brygada;
import Model.Brygadzista;
import Services.BrygadaService;
import Services.BrygadzistaService;
import Exception.NotGetClassException;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Optional;

public class AddBrygadaDialog extends JDialog {
    private JTextField nameField;
    private JComboBox<String> brygadzistaCombo;
    private boolean confirmed = false;
    private Brygada createdBrygada;
    private Brygada toEdit;

    public AddBrygadaDialog(Frame parent, Brygada toEdit) {

        super(parent, toEdit == null ? "Dodaj brygadę" : "Edytuj brygadę", true);
        this.toEdit = toEdit;
        setLayout(new BorderLayout());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);


        JPanel fields = new JPanel(new GridLayout(2, 2, 5, 5));
        fields.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Pole nazwy brygady
        fields.add(new JLabel("Nazwa brygady:"));
        nameField = new JTextField(20);
        fields.add(nameField);

        // Pole wyboru brygadzisty
        fields.add(new JLabel("Brygadzista:"));
        List<Brygadzista> brygadzisci = BrygadzistaService.getBrygadzisci();
        if (brygadzisci.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Brak brygadzistów. Dodaj brygadzistę najpierw!");
            throw new NotGetClassException("Brak brygadzistów. Dodaj brygadzistę najpierw!");
        }

        brygadzistaCombo = new JComboBox<>(
                brygadzisci.stream()
                        .map(b -> b.getBrygadzistaId() + " - " + b.getLogin())
                        .toArray(String[]::new)
        );
        fields.add(brygadzistaCombo);

        add(fields, BorderLayout.CENTER);

        // Jeśli edytujemy, wstępnie wypełnij pola
        if (toEdit != null) {
            nameField.setText(toEdit.getName());

            Brygadzista b = toEdit.getBrygadzista();
            if (b != null) {
                String sel = b.getBrygadzistaId() + " - " + b.getLogin();
                brygadzistaCombo.setSelectedItem(sel);
            } else {
                // Uwaga dla użytkownika
                JOptionPane.showMessageDialog(this,
                        "Uwaga: ta brygada nie ma już przypisanego brygadzisty (został usunięty).",
                        "Informacja",
                        JOptionPane.WARNING_MESSAGE);
            }
        }

        JPanel btns = new JPanel();
        JButton ok = new JButton("OK");
        JButton cancel = new JButton("Anuluj");
        btns.add(ok);
        btns.add(cancel);
        add(btns, BorderLayout.SOUTH);

        ok.addActionListener(e -> onOK(brygadzisci));
        cancel.addActionListener(e -> dispose());

        pack();
        setLocationRelativeTo(parent);
    }

    private void onOK(List<Brygadzista> brygadzisci) {
        try {
            String nazwa = nameField.getText().trim();
            if (nazwa.isEmpty()) {
                throw new IllegalArgumentException("Nazwa brygady nie może być pusta.");
            }


            String sel = (String) brygadzistaCombo.getSelectedItem();
            long brygadzistaId = Long.parseLong(sel.split(" - ")[0]);
            Brygadzista chosen = brygadzisci.stream()
                    .filter(b -> b.getBrygadzistaId() == brygadzistaId)
                    .findFirst()
                    .orElseThrow();

            if (toEdit == null) {
                // Tworzymy nową brygadę i zapisujemy przez serwis
                createdBrygada = new Brygada(nazwa, chosen);
                BrygadaService.addBrygada(createdBrygada);
            } else {
                // Edytujemy istniejącą brygadę
                toEdit.setName(nazwa);
                toEdit.changeBrygadzista(chosen);
                createdBrygada = toEdit;
                BrygadaService.updateBrygada(createdBrygada);
            }

            confirmed = true;
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Błąd: " + ex.getMessage(),
                    "Uwaga",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public static Optional<Brygada> showDialog(Frame parent, Brygada toEdit) {
        AddBrygadaDialog dlg = new AddBrygadaDialog(parent, toEdit);
        dlg.setVisible(true);
        return dlg.confirmed ? Optional.of(dlg.createdBrygada) : Optional.empty();
    }

    public static Optional<Brygada> showDialog(Frame parent) {
        return showDialog(parent, null);
    }
}
