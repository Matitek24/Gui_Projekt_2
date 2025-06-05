package Dialog;

import Model.Praca;
import Model.Zlecenie;
import Services.PracaService;
import Services.ZlecenieService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Optional;

public class AddPracaDialog extends JDialog {
    private final JComboBox<Praca.rodzaj_pracy> rodzajCombo;
    private final JTextField czasField;
    private final JTextField opisField;
    private final JCheckBox zrealizowaneCheck;
    private final JComboBox<Zlecenie> zlecenieCombo;

    private boolean confirmed = false;
    private Praca praca;

    private AddPracaDialog(Frame owner, Praca existing) {
        super(owner, true); // modal
        this.praca = existing;
        setTitle(existing == null ? "Dodaj pracę" : "Edytuj pracę");
        setSize(400, 300);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        // --- Rodzaj pracy ---
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Rodzaj pracy:"), gbc);
        rodzajCombo = new JComboBox<>(Praca.rodzaj_pracy.values());
        gbc.gridx = 1;
        formPanel.add(rodzajCombo, gbc);

        // --- Czas pracy ---
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Czas (min):"), gbc);
        czasField = new JTextField(10);
        gbc.gridx = 1;
        formPanel.add(czasField, gbc);

        // --- Opis ---
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Opis:"), gbc);
        opisField = new JTextField(15);
        gbc.gridx = 1;
        formPanel.add(opisField, gbc);

        // --- Zlecenie (opcjonalnie) ---
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Zlecenie:"), gbc);
        zlecenieCombo = new JComboBox<>(ZlecenieService.getZlecenia().toArray(new Zlecenie[0]));
        gbc.gridx = 1;
        formPanel.add(zlecenieCombo, gbc);

        // --- Zrealizowane ---
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Zrealizowane:"), gbc);
        zrealizowaneCheck = new JCheckBox();
        gbc.gridx = 1;
        formPanel.add(zrealizowaneCheck, gbc);

        add(formPanel, BorderLayout.CENTER);

        // --- Przyciski ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton okBtn = new JButton("OK");
        JButton cancelBtn = new JButton("Anuluj");
        buttonPanel.add(okBtn);
        buttonPanel.add(cancelBtn);
        add(buttonPanel, BorderLayout.SOUTH);

        // --- Wypełnienie pól przy edycji ---
        if (existing != null) {
            rodzajCombo.setSelectedItem(existing.getRodzajPracy());
            czasField.setText(String.valueOf(existing.getCzasPracy()));
            opisField.setText(existing.getOpis());
            zrealizowaneCheck.setSelected(existing.isCzyZrealizowane());
            if (existing.getZlecenie() != null) {
                for (int i = 0; i < zlecenieCombo.getItemCount(); i++) {
                    Zlecenie z = zlecenieCombo.getItemAt(i);
                    if (z.getId() == existing.getZlecenie().getId()) {
                        zlecenieCombo.setSelectedIndex(i);
                        break;
                    }
                }
            }

        }

        // --- Obsługa OK ---
        okBtn.addActionListener(e -> {
            if (!validateInput()) {
                JOptionPane.showMessageDialog(this, "Uzupełnij poprawnie wszystkie pola.", "Błąd", JOptionPane.ERROR_MESSAGE);
                return;
            }
            saveResult();
            confirmed = true;
            setVisible(false);
        });

        // --- Anuluj ---
        cancelBtn.addActionListener(e -> {
            confirmed = false;
            setVisible(false);
        });

        // --- ESC zamyka dialog ---
        getRootPane().registerKeyboardAction(e -> {
            confirmed = false;
            setVisible(false);
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);

        // Focus w polu czas
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                czasField.requestFocusInWindow();
            }
        });
    }

    private boolean validateInput() {
        try {
            int c = Integer.parseInt(czasField.getText().trim());
            return !opisField.getText().trim().isEmpty() && c >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void saveResult() {
        Praca.rodzaj_pracy rodzaj = (Praca.rodzaj_pracy) rodzajCombo.getSelectedItem();
        int czas = Integer.parseInt(czasField.getText().trim());
        String opis = opisField.getText().trim();
        boolean zreal = zrealizowaneCheck.isSelected();

        Zlecenie wybraneZlecenie = (Zlecenie) zlecenieCombo.getSelectedItem();

        if (praca == null) {
            Praca nowa = new Praca(rodzaj, czas, opis);
            nowa.setCzyZrealizowane(zreal);
            nowa.setZlecenie(wybraneZlecenie);
            PracaService.addPraca(nowa);
            if (wybraneZlecenie != null) {
                wybraneZlecenie.addPraca(nowa);
                ZlecenieService.updateZlecenie(wybraneZlecenie);
            }

            praca = nowa;
        } else {
            Zlecenie poprzednie = praca.getZlecenie();
            if (poprzednie != null && poprzednie.getId() != wybraneZlecenie.getId()) {
                poprzednie.getPraca().remove(praca);
                ZlecenieService.updateZlecenie(poprzednie);
            }
            praca.setRodzajPracy(rodzaj);
            praca.setCzasPracy(czas);
            praca.setOpis(opis);
            praca.setCzyZrealizowane(zreal);
            praca.setZlecenie(wybraneZlecenie);
            if (wybraneZlecenie != null && !wybraneZlecenie.getPraca().contains(praca)) {
                wybraneZlecenie.addPraca(praca);
                ZlecenieService.updateZlecenie(wybraneZlecenie);
            }
            PracaService.updatePraca(praca);
        }

    }

    private Optional<Praca> getResult() {
        return confirmed ? Optional.of(praca) : Optional.empty();
    }


    public static Optional<Praca> showDialog(Frame owner) {
        AddPracaDialog dialog = new AddPracaDialog(owner, null);
        dialog.setVisible(true);
        return dialog.getResult();
    }

    public static Optional<Praca> showDialog(Frame owner, Praca existing) {
        AddPracaDialog dialog = new AddPracaDialog(owner, existing);
        dialog.setVisible(true);
        return dialog.getResult();
    }
}
