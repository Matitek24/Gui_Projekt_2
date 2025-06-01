package Dialog;

import Model.Dzial;
import Model.Uzytkownik;
import Services.DzialService;
import Services.UzytkownikService;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class AddUserDialog extends JDialog {
    private JTextField firstNameField, lastNameField, loginField, passwordField;
    private JSpinner dateSpinner;
    private JComboBox<String> departmentCombo;
    private boolean confirmed = false;
    private Uzytkownik createdUzytkownik;
    private Uzytkownik toEdit;

    public AddUserDialog(Frame parent, Uzytkownik toEdit) {
        super(parent, toEdit == null ? "Dodaj użytkownika" : "Edytuj użytkownika", true);
        this.toEdit = toEdit;
        setLayout(new BorderLayout());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);


        JPanel fields = new JPanel(new GridLayout(6, 2, 5, 5));
        fields.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        fields.add(new JLabel("Imię:"));     firstNameField = new JTextField(20);  fields.add(firstNameField);
        fields.add(new JLabel("Nazwisko:")); lastNameField  = new JTextField(20);  fields.add(lastNameField);
        fields.add(new JLabel("Data ur.:"));
        dateSpinner = new JSpinner(new SpinnerDateModel(new Date(), null, new Date(), Calendar.DAY_OF_MONTH));
        dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd"));
        fields.add(dateSpinner);

        fields.add(new JLabel("Dział:"));
        List<Dzial> dzialy = DzialService.getDzialy();
        if (dzialy.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Brak działów. Dodaj dział najpierw!");
            dispose(); return;
        }
        departmentCombo = new JComboBox<>(
                dzialy.stream().map(Dzial::getNazwa_dzialu).toArray(String[]::new)
        );
        fields.add(departmentCombo);

        fields.add(new JLabel("Login:"));    loginField    = new JTextField(20);  fields.add(loginField);
        fields.add(new JLabel("Hasło:"));    passwordField = new JTextField(20);  fields.add(passwordField);

        add(fields, BorderLayout.CENTER);

        // jeśli edycja – wypełnij istniejącymi danymi
        if (toEdit != null) {
            firstNameField.setText(toEdit.getImie());
            lastNameField .setText(toEdit.getNazwisko());
            dateSpinner.setValue(Date.from(toEdit.getDataUrodzenia().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
            departmentCombo.setSelectedItem(toEdit.getDzial().getNazwa_dzialu());
            loginField.setText(toEdit.getLogin());
            passwordField.setText(toEdit.getHaslo());
        }

        // --- przyciski ---
        JPanel btns = new JPanel();
        JButton ok = new JButton("OK"), cancel = new JButton("Anuluj");
        btns.add(ok); btns.add(cancel);
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
            String pass  = passwordField.getText().trim();
            if (imie.isEmpty()||nazw.isEmpty()||login.isEmpty()||pass.isEmpty())
                throw new IllegalArgumentException("Wszystkie pola muszą być wypełnione.");

            LocalDate data = ((Date)dateSpinner.getValue())
                    .toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            Dzial dz = dzialy.stream()
                    .filter(d->d.getNazwa_dzialu()
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
            JOptionPane.showMessageDialog(this, "Błąd: "+ex.getMessage(),
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
}
