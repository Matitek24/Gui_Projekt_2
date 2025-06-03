package Dialog;

import Model.Praca;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Optional;

public class AddPracaDialog extends JDialog {
    private final JComboBox<Praca.rodzaj_pracy> rodzajCombo;
    private final JTextField czasField;
    private final JTextField opisField;
    private final JCheckBox zrealizowaneCheck;
    private boolean confirmed = false;
    private Praca praca; // jeśli edycja - referencja do istniejącego obiektu

    private AddPracaDialog(Frame owner, Praca existing) {
        super(owner, true); // modalny dialog
        this.praca = existing;
        setTitle(existing == null ? "Dodaj pracę" : "Edytuj pracę");
        setSize(350, 250);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));

        // --- Panel pól formularza ---
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        // 1) ComboBox - rodzaj pracy
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Rodzaj pracy:"), gbc);
        rodzajCombo = new JComboBox<>(Praca.rodzaj_pracy.values());
        gbc.gridx = 1;
        formPanel.add(rodzajCombo, gbc);

        // 2) Pole tekstowe - czas trwania (w minutach)
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Czas (min):"), gbc);
        czasField = new JTextField(10);
        gbc.gridx = 1;
        formPanel.add(czasField, gbc);

        // 3) Pole tekstowe - opis
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Opis:"), gbc);
        opisField = new JTextField(15);
        gbc.gridx = 1;
        formPanel.add(opisField, gbc);

        // 4) CheckBox - czy zrealizowane
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Zrealizowane:"), gbc);
        zrealizowaneCheck = new JCheckBox();
        gbc.gridx = 1;
        formPanel.add(zrealizowaneCheck, gbc);

        add(formPanel, BorderLayout.CENTER);

        // --- Panel przycisków ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton okBtn = new JButton("OK");
        JButton cancelBtn = new JButton("Anuluj");
        buttonPanel.add(okBtn);
        buttonPanel.add(cancelBtn);
        add(buttonPanel, BorderLayout.SOUTH);

        // --- Jeśli edycja: wypełniamy pola istniejącą wartością ---
        if (existing != null) {
            rodzajCombo.setSelectedItem(existing.getRodzajPracy());
            czasField.setText(String.valueOf(existing.getCzasPracy()));
            opisField.setText(existing.getOpis());
            zrealizowaneCheck.setSelected(existing.isCzyZrealizowane());
        }

        // Obsługa przycisku OK
        okBtn.addActionListener(e -> {
            if (!validateInput()) {
                JOptionPane.showMessageDialog(this, "Uzupełnij poprawnie wszystkie pola.", "Błąd", JOptionPane.ERROR_MESSAGE);
                return;
            }
            confirmed = true;
            setVisible(false);
        });

        // Obsługa przycisku Anuluj
        cancelBtn.addActionListener(e -> {
            confirmed = false;
            setVisible(false);
        });

        // Obsługa ESC – zamyka dialog
        getRootPane().registerKeyboardAction(e -> {
            confirmed = false;
            setVisible(false);
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);

        // Domyślne focus w polu „czas”
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                czasField.requestFocusInWindow();
            }
        });
    }

    // Sprawdza, czy pola są poprawnie wypełnione
    private boolean validateInput() {
        String czasTxt = czasField.getText().trim();
        String opisTxt = opisField.getText().trim();
        if (opisTxt.isEmpty()) return false;
        try {
            int c = Integer.parseInt(czasTxt);
            return c >= 0;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    // Po zamknięciu dialogu, jeśli potwierdzono: zwraca Optional<Praca>. W przeciwnym razie Optional.empty()
    private Optional<Praca> getResult() {
        if (!confirmed) {
            return Optional.empty();
        }

        Praca.rodzaj_pracy rodzaj = (Praca.rodzaj_pracy) rodzajCombo.getSelectedItem();
        int czas = Integer.parseInt(czasField.getText().trim());
        String opis = opisField.getText().trim();
        boolean zreal = zrealizowaneCheck.isSelected();

        if (praca == null) {
            // Tworzymy nową instancję
            Praca nowa = new Praca(rodzaj, czas, opis);
            nowa.setCzyZrealizowane(zreal);
            return Optional.of(nowa);
        } else {
            // Edytujemy istniejącą instancję
            praca.setRodzajPracy(rodzaj);
            praca.setCzasPracy(czas);
            praca.setOpis(opis);
            praca.setCzyZrealizowane(zreal);
            return Optional.of(praca);
        }
    }

    /**
     * Wywołanie statyczne dla dodawania nowej pracy.
     * @param owner – referencja do ramki macierzystej
     * @return Optional<Praca> – jeżeli użytkownik potwierdzi, zwróci nową instancję; w przeciwnym razie Optional.empty().
     */
    public static Optional<Praca> showDialog(Frame owner) {
        AddPracaDialog dialog = new AddPracaDialog(owner, null);
        dialog.setVisible(true);
        return dialog.getResult();
    }

    /**
     * Wywołanie statyczne dla edycji istniejącej pracy.
     * @param owner – referencja do ramki macierzystej
     * @param existing – obiekt Praca do edycji (dialog wypełni pola jego wartościami).
     * @return Optional<Praca> – jeżeli użytkownik potwierdzi, zwróci zaktualizowaną instancję; w przeciwnym razie Optional.empty().
     */
    public static Optional<Praca> showDialog(Frame owner, Praca existing) {
        AddPracaDialog dialog = new AddPracaDialog(owner, existing);
        dialog.setVisible(true);
        return dialog.getResult();
    }
}
