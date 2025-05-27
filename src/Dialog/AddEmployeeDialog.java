package Dialog;

import Model.Dzial;
import Model.Pracownik;
import Services.DzialService;
import Services.PracownikService;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class AddEmployeeDialog extends JDialog {
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JSpinner dateSpinner;
    private JComboBox<String> departmentCombo;
    private boolean confirmed = false;
    private Pracownik createdPracownik;
    private Pracownik pracownikDoEdycji;

    public AddEmployeeDialog(Frame parent, Pracownik pracownikDoEdycji) {
        super(parent, pracownikDoEdycji == null ? "Dodaj pracownika" : "Edytuj Praocnwika", true);
        this.pracownikDoEdycji = pracownikDoEdycji;
        setLayout(new BorderLayout());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);


        // --- Panel pól ---
        JPanel fields = new JPanel(new GridLayout(4, 2, 5, 5));
        fields.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        fields.add(new JLabel("Imię:"));
        firstNameField = new JTextField(20);
        fields.add(firstNameField);

        fields.add(new JLabel("Nazwisko:"));
        lastNameField = new JTextField(20);
        fields.add(lastNameField);

        fields.add(new JLabel("Data urodzenia:"));
        SpinnerDateModel dateModel = new SpinnerDateModel(new Date(), null, new Date(), java.util.Calendar.DAY_OF_MONTH);
        dateSpinner = new JSpinner(dateModel);
        dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd"));
        fields.add(dateSpinner);

        fields.add(new JLabel("Dział:"));
        List<Dzial> dzialy = DzialService.getDzialy();
        if (dzialy.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Brak działów. Najpierw dodaj dział.");
            dispose();
            return;
        }
        String[] names = dzialy.stream().map(Dzial::getNazwa_dzialu).toArray(String[]::new);
        departmentCombo = new JComboBox<>(names);
        fields.add(departmentCombo);

        add(fields, BorderLayout.CENTER);

        if (pracownikDoEdycji != null) {
            firstNameField.setText(pracownikDoEdycji.getImie());
            lastNameField.setText(pracownikDoEdycji.getNazwisko());
            dateSpinner.setValue(Date.from(pracownikDoEdycji.getDataUrodzenia().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
            departmentCombo.setSelectedItem(pracownikDoEdycji.getDzial().getNazwa_dzialu());
        }
        // --- Panel przycisków ---
        JPanel buttons = new JPanel();
        JButton ok = new JButton("OK");
        JButton cancel = new JButton("Anuluj");
        buttons.add(ok);
        buttons.add(cancel);
        add(buttons, BorderLayout.SOUTH);

        ok.addActionListener(e -> onOK(dzialy));
        cancel.addActionListener(e -> dispose());

        pack();
        setLocationRelativeTo(parent);
    }

    private void onOK(List<Dzial> dzialy) {
        try {
            String imie = firstNameField.getText().trim();
            String nazwisko = lastNameField.getText().trim();
            if (imie.isEmpty() || nazwisko.isEmpty()) {
                throw new IllegalArgumentException("Imię i nazwisko nie mogą być puste.");
            }
            Date picked = (Date) dateSpinner.getValue();
            LocalDate dataUrodzenia = picked.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            String wybrany = (String) departmentCombo.getSelectedItem();
            Dzial dzial = dzialy.stream()
                    .filter(d -> d.getNazwa_dzialu().equals(wybrany))
                    .findFirst()
                    .orElseThrow();

            // Tworzymy obiekt
            if (pracownikDoEdycji == null) {
                createdPracownik = new Pracownik(imie, nazwisko, dataUrodzenia, dzial);
                PracownikService.addPracownik(createdPracownik);
            } else {
                pracownikDoEdycji.setImie(imie);
                pracownikDoEdycji.setNazwisko(nazwisko);
                pracownikDoEdycji.setDataUrodzenia(dataUrodzenia);
                pracownikDoEdycji.setDzial(dzial);
                createdPracownik = pracownikDoEdycji;
                PracownikService.updatePracownik(createdPracownik);
            }

            confirmed = true;
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Błąd: " + ex.getMessage(), "Uwaga", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static Optional<Pracownik> showDialog(Frame parent) {
        return showDialog(parent, null);
    }

    public static Optional<Pracownik> showDialog(Frame parent, Pracownik pracownikDoEdycji) {
        AddEmployeeDialog dlg = new AddEmployeeDialog(parent, pracownikDoEdycji);
        dlg.setVisible(true);
        return dlg.confirmed
                ? Optional.of(dlg.createdPracownik)
                : Optional.empty();
    }
}
