package Dialog;

import Model.Brygadzista;
import Model.Dzial;
import Services.BrygadzistaService;
import Services.DzialService;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class AddBrygadzistaDialog extends JDialog {
    private JTextField firstNameField, lastNameField, loginField, passwordField;
    private JComboBox<Integer> dayCombo, monthCombo, yearCombo;
    private JComboBox<String> departmentCombo;
    private boolean confirmed = false;
    private Brygadzista createdBrygadzista;
    private Brygadzista toEdit;

    public AddBrygadzistaDialog(Frame parent, Brygadzista toEdit) {
        super(parent, toEdit == null ? "Dodaj brygadzistę" : "Edytuj brygadzistę", true);
        this.toEdit = toEdit;
        setLayout(new BorderLayout());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel fields = new JPanel(new GridLayout(6, 2, 5, 5));
        fields.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        fields.add(new JLabel("Imię:"));
        firstNameField = new JTextField(20);
        fields.add(firstNameField);

        fields.add(new JLabel("Nazwisko:"));
        lastNameField = new JTextField(20);
        fields.add(lastNameField);

        fields.add(new JLabel("Data ur.:"));
        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 0));

        // Dzień (1-31)
        dayCombo = new JComboBox<>();
        for (int i = 1; i <= 31; i++) {
            dayCombo.addItem(i);
        }

        // Miesiąc (1-12)
        monthCombo = new JComboBox<>();
        for (int i = 1; i <= 12; i++) {
            monthCombo.addItem(i);
        }

        // Rok (1950-2023)
        yearCombo = new JComboBox<>();
        for (int i = 2023; i >= 1950; i--) {
            yearCombo.addItem(i);
        }

        datePanel.add(dayCombo);
        datePanel.add(new JLabel("/"));
        datePanel.add(monthCombo);
        datePanel.add(new JLabel("/"));
        datePanel.add(yearCombo);
        fields.add(datePanel);

        fields.add(new JLabel("Dział:"));
        List<Dzial> dzialy = DzialService.getDzialy();
        if (dzialy.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Brak działów. Dodaj dział najpierw!");
            dispose();
            return;
        }
        departmentCombo = new JComboBox<>(
                dzialy.stream().map(Dzial::getNazwa_dzialu).toArray(String[]::new)
        );
        fields.add(departmentCombo);

        fields.add(new JLabel("Login:"));
        loginField = new JTextField(20);
        fields.add(loginField);

        fields.add(new JLabel("Hasło:"));
        passwordField = new JTextField(20);
        fields.add(passwordField);

        add(fields, BorderLayout.CENTER);

        if (toEdit != null) {
            firstNameField.setText(toEdit.getImie());
            lastNameField.setText(toEdit.getNazwisko());

            LocalDate birthDate = toEdit.getDataUrodzenia();
            dayCombo.setSelectedItem(birthDate.getDayOfMonth());
            monthCombo.setSelectedItem(birthDate.getMonthValue());
            yearCombo.setSelectedItem(birthDate.getYear());

            Dzial dzial = toEdit.getDzial();
            if (dzial != null) {
                departmentCombo.setSelectedItem(dzial.getNazwa_dzialu());
            } else {
                departmentCombo.setSelectedIndex(-1);
            }
            loginField.setText(toEdit.getLogin());
            passwordField.setText(toEdit.getHaslo());
        }

        JPanel btns = new JPanel();
        JButton ok = new JButton("OK");
        JButton cancel = new JButton("Anuluj");
        btns.add(ok);
        btns.add(cancel);
        add(btns, BorderLayout.SOUTH);

        ok.addActionListener(e -> onOK(dzialy));
        cancel.addActionListener(e -> dispose());

        pack();
        setLocationRelativeTo(parent);
    }

    private void onOK(List<Dzial> dzialy) {
        try {
            String imie = firstNameField.getText().trim();
            String nazw = lastNameField.getText().trim();
            String login = loginField.getText().trim();
            String pass = passwordField.getText().trim();
            if (imie.isEmpty() || nazw.isEmpty() || login.isEmpty() || pass.isEmpty()) {
                throw new IllegalArgumentException("Wszystkie pola muszą być wypełnione.");
            }

            int day = (Integer) dayCombo.getSelectedItem();
            int month = (Integer) monthCombo.getSelectedItem();
            int year = (Integer) yearCombo.getSelectedItem();
            LocalDate data = LocalDate.of(year, month, day);

            Dzial dz = dzialy.stream()
                    .filter(d -> d.getNazwa_dzialu().equals(departmentCombo.getSelectedItem()))
                    .findFirst()
                    .orElseThrow();

            if (toEdit == null) {
                // Tworzymy nowego brygadzistę i zapisujemy go wyłącznie w BrygadzistaService
                createdBrygadzista = new Brygadzista(imie, nazw, data, dz, login, pass);
                BrygadzistaService.addBrygadzista(createdBrygadzista);
            } else {
                // Edytujemy istniejącego: zmieniamy pola i zapisujemy przez BrygadzistaService.updateBrygadzista()
                toEdit.setImie(imie);
                toEdit.setNazwisko(nazw);
                toEdit.setDataUrodzenia(data);
                toEdit.setDzial(dz);
                toEdit.setLogin(login);
                toEdit.setHaslo(pass);
                createdBrygadzista = toEdit;
                BrygadzistaService.updateBrygadzista(createdBrygadzista);
            }

            confirmed = true;
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Błąd: " + ex.getMessage(),
                    "Uwaga", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static Optional<Brygadzista> showDialog(Frame parent, Brygadzista toEdit) {
        AddBrygadzistaDialog dlg = new AddBrygadzistaDialog(parent, toEdit);
        dlg.setVisible(true);
        return dlg.confirmed ? Optional.of(dlg.createdBrygadzista) : Optional.empty();
    }

    public static Optional<Brygadzista> showDialog(Frame parent) {
        return showDialog(parent, null);
    }
}