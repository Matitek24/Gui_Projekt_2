package Dialog;

import Model.Dzial;
import Model.Pracownik;
import Services.DzialService;
import Services.PracownikService;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class AddEmployeeDialog extends JDialog {
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JComboBox<Integer> yearCombo, monthCombo, dayCombo;
    private JComboBox<String> departmentCombo;
    private boolean confirmed = false;
    private Pracownik createdPracownik;
    private Pracownik pracownikDoEdycji;

    public AddEmployeeDialog(Frame parent, Pracownik pracownikDoEdycji) {
        super(parent, pracownikDoEdycji == null ? "Dodaj pracownika" : "Edytuj Pracownika", true);
        this.pracownikDoEdycji = pracownikDoEdycji;
        setLayout(new BorderLayout());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // --- Panel pól ---
        JPanel fields = new JPanel(new GridLayout(4, 2, 5, 5));
        fields.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        fields.add(new JLabel("Imię:"));
        firstNameField = new JTextField(20);
        fields.add(firstNameField);

        fields.add(new JLabel("Nazwisko:"));
        lastNameField = new JTextField(20);
        fields.add(lastNameField);

        fields.add(new JLabel("Data urodzenia:"));
        yearCombo = new JComboBox<>(generateYears());
        monthCombo = new JComboBox<>(generateMonthOrDay(12));
        dayCombo = new JComboBox<>(generateMonthOrDay(31));
        JPanel datePanel = new JPanel();
        datePanel.add(yearCombo);
        datePanel.add(new JLabel("-"));
        datePanel.add(monthCombo);
        datePanel.add(new JLabel("-"));
        datePanel.add(dayCombo);
        fields.add(datePanel);

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

            LocalDate birth = pracownikDoEdycji.getDataUrodzenia();
            yearCombo.setSelectedItem(birth.getYear());
            monthCombo.setSelectedItem(birth.getMonthValue());
            dayCombo.setSelectedItem(birth.getDayOfMonth());

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

            LocalDate dataUrodzenia = LocalDate.of(
                    (Integer) yearCombo.getSelectedItem(),
                    (Integer) monthCombo.getSelectedItem(),
                    (Integer) dayCombo.getSelectedItem()
            );

            String wybrany = (String) departmentCombo.getSelectedItem();
            Dzial dzial = dzialy.stream()
                    .filter(d -> d.getNazwa_dzialu().equals(wybrany))
                    .findFirst()
                    .orElseThrow();

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

    private Integer[] generateYears() {
        int currentYear = LocalDate.now().getYear();
        Integer[] years = new Integer[100];
        for (int i = 0; i < 100; i++) {
            years[i] = currentYear - i;
        }
        return years;
    }

    private Integer[] generateMonthOrDay(int max) {
        Integer[] values = new Integer[max];
        for (int i = 0; i < max; i++) {
            values[i] = i + 1;
        }
        return values;
    }
}
