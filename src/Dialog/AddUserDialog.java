package Dialog;

import Model.Dzial;
import Model.Uzytkownik;
import Services.DzialService;
import Services.UzytkownikService;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import Exception.*;

public class AddUserDialog extends JDialog {
    private JTextField firstNameField, lastNameField, loginField, passwordField;
    private JComboBox<String> departmentCombo;
    private boolean confirmed = false;
    private Uzytkownik createdUzytkownik;
    private Uzytkownik toEdit;
    private JComboBox<Integer> yearCombo, monthCombo, dayCombo;

    public AddUserDialog(Frame parent, Uzytkownik toEdit) {
        super(parent, toEdit == null ? "Dodaj użytkownika" : "Edytuj użytkownika", true);
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

        // Dodajemy panel z comboboxami daty
        fields.add(new JLabel("Data ur.:"));
        yearCombo = new JComboBox<>(generateYears());
        monthCombo = new JComboBox<>(generateMonthOrDay(12));
        dayCombo = new JComboBox<>(generateMonthOrDay(31));
        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        datePanel.add(yearCombo);
        datePanel.add(new JLabel("-"));
        datePanel.add(monthCombo);
        datePanel.add(new JLabel("-"));
        datePanel.add(dayCombo);
        fields.add(datePanel);

        fields.add(new JLabel("Dział:"));
        List<Dzial> dzialy = DzialService.getDzialy();
        if (dzialy.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Brak działów. Dodaj dział najpierw!");
            throw new NotGetClassException("Brak działów. Dodaj dział");
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

        // jeśli edycja – wypełnij istniejącymi danymi
        if (toEdit != null) {
            firstNameField.setText(toEdit.getImie());
            lastNameField.setText(toEdit.getNazwisko());

            LocalDate data = toEdit.getDataUrodzenia();
            yearCombo.setSelectedItem(data.getYear());
            monthCombo.setSelectedItem(data.getMonthValue());
            dayCombo.setSelectedItem(data.getDayOfMonth());

            Dzial dzial = toEdit.getDzial();
            if (dzial != null) {
                departmentCombo.setSelectedItem(dzial.getNazwa_dzialu());
            } else {
                departmentCombo.setSelectedIndex(-1);
            }

            loginField.setText(toEdit.getLogin());
            passwordField.setText(toEdit.getHaslo());
        } else {
            // ustaw domyślne wartości na dzisiaj, żeby nie było null
            LocalDate now = LocalDate.now();
            yearCombo.setSelectedItem(now.getYear());
            monthCombo.setSelectedItem(now.getMonthValue());
            dayCombo.setSelectedItem(now.getDayOfMonth());
        }

        // --- przyciski ---
        JPanel btns = new JPanel();
        JButton ok = new JButton("OK"), cancel = new JButton("Anuluj");
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
            if (imie.isEmpty() || nazw.isEmpty() || login.isEmpty() || pass.isEmpty())
                throw new IllegalArgumentException("Wszystkie pola muszą być wypełnione.");

            LocalDate data = LocalDate.of(
                    (Integer) yearCombo.getSelectedItem(),
                    (Integer) monthCombo.getSelectedItem(),
                    (Integer) dayCombo.getSelectedItem()
            );

            Dzial dz = dzialy.stream()
                    .filter(d -> d.getNazwa_dzialu()
                            .equals(departmentCombo.getSelectedItem()))
                    .findFirst().orElseThrow();

            if (toEdit == null) {
                createdUzytkownik = new Uzytkownik(imie, nazw, data, dz, login, pass);
                UzytkownikService.addUzytkownik(createdUzytkownik);
            } else {
                toEdit.setImie(imie);
                toEdit.setNazwisko(nazw);
                toEdit.setDataUrodzenia(data);
                toEdit.setDzial(dz);
                toEdit.setLogin(login);
                toEdit.setHaslo(pass);
                createdUzytkownik = toEdit;
                UzytkownikService.updateUzytkownik(createdUzytkownik);
            }
            confirmed = true;
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Błąd: " + ex.getMessage(),
                    "Uwaga", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static Optional<Uzytkownik> showDialog(Frame parent, Uzytkownik toEdit) {
        AddUserDialog dlg = new AddUserDialog(parent, toEdit);
        dlg.setVisible(true);
        return dlg.confirmed ? Optional.of(dlg.createdUzytkownik) : Optional.empty();
    }

    public static Optional<Uzytkownik> showDialog(Frame parent) {
        return showDialog(parent, null);
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
