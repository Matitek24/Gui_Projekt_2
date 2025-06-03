package Dialog;

import Model.Brygada;
import Model.Zlecenie;
import Services.BrygadaService;
import Services.ZlecenieService;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class AddZlecenieDialog extends JDialog {
    private JComboBox<Zlecenie.stan_zlecenia> stanCombo;
    private JComboBox<String> brygadaCombo;

    private JComboBox<Integer> dataUtwYear, dataUtwMonth, dataUtwDay;
    private JComboBox<Integer> dataRozpYear, dataRozpMonth, dataRozpDay;
    private JComboBox<Integer> dataZakYear, dataZakMonth, dataZakDay;

    private boolean confirmed = false;
    private Zlecenie createdZlecenie;
    private Zlecenie toEdit;

    public AddZlecenieDialog(Frame parent, Zlecenie toEdit) {
        super(parent, toEdit == null ? "Dodaj zlecenie" : "Edytuj zlecenie", true);
        this.toEdit = toEdit;
        setLayout(new BorderLayout());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel fields = new JPanel(new GridLayout(5, 2, 5, 5));
        fields.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Stan zlecenia
        fields.add(new JLabel("Stan zlecenia:"));
        stanCombo = new JComboBox<>(Zlecenie.stan_zlecenia.values());
        fields.add(stanCombo);

        // Brygada
        fields.add(new JLabel("Brygada:"));
        List<Brygada> brygady = BrygadaService.getBrygady();
        if (brygady.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Brak brygad. Dodaj brygadę najpierw!");
            dispose();
            return;
        }
        brygadaCombo = new JComboBox<>(
                brygady.stream()
                        .map(b -> b.getId() + " - " + b.getName())
                        .toArray(String[]::new)
        );
        fields.add(brygadaCombo);

        // Data utworzenia
        fields.add(new JLabel("Data utworzenia:"));
        dataUtwYear = new JComboBox<>(generateYears());
        dataUtwMonth = new JComboBox<>(generateMonthOrDay(12));
        dataUtwDay = new JComboBox<>(generateMonthOrDay(31));
        fields.add(createDatePanel(dataUtwYear, dataUtwMonth, dataUtwDay));

        // Data rozpoczęcia
        fields.add(new JLabel("Data rozpoczęcia:"));
        dataRozpYear = new JComboBox<>(generateYears());
        dataRozpMonth = new JComboBox<>(generateMonthOrDay(12));
        dataRozpDay = new JComboBox<>(generateMonthOrDay(31));
        fields.add(createDatePanel(dataRozpYear, dataRozpMonth, dataRozpDay));

        // Data zakończenia
        fields.add(new JLabel("Data zakończenia:"));
        dataZakYear = new JComboBox<>(generateYears());
        dataZakMonth = new JComboBox<>(generateMonthOrDay(12));
        dataZakDay = new JComboBox<>(generateMonthOrDay(31));
        fields.add(createDatePanel(dataZakYear, dataZakMonth, dataZakDay));

        add(fields, BorderLayout.CENTER);

        // Wypełnij dane jeśli edycja
        if (toEdit != null) {
            stanCombo.setSelectedItem(toEdit.stan);
            if (toEdit.getBrygada() != null) {
                String sel = toEdit.getBrygada().getId() + " - " + toEdit.getBrygada().getName();
                brygadaCombo.setSelectedItem(sel);
            }

            LocalDate dUtw = toEdit.getDataUtworzenia().toLocalDate();
            dataUtwYear.setSelectedItem(dUtw.getYear());
            dataUtwMonth.setSelectedItem(dUtw.getMonthValue());
            dataUtwDay.setSelectedItem(dUtw.getDayOfMonth());

            if (toEdit.getDataRozpoczecia() != null) {
                LocalDate dRozp = toEdit.getDataRozpoczecia().toLocalDate();
                dataRozpYear.setSelectedItem(dRozp.getYear());
                dataRozpMonth.setSelectedItem(dRozp.getMonthValue());
                dataRozpDay.setSelectedItem(dRozp.getDayOfMonth());
            }

            if (toEdit.getDataZakonczenia() != null) {
                LocalDate dZak = toEdit.getDataZakonczenia().toLocalDate();
                dataZakYear.setSelectedItem(dZak.getYear());
                dataZakMonth.setSelectedItem(dZak.getMonthValue());
                dataZakDay.setSelectedItem(dZak.getDayOfMonth());
            }
        }

        JPanel btns = new JPanel();
        JButton ok = new JButton("OK");
        JButton cancel = new JButton("Anuluj");
        btns.add(ok);
        btns.add(cancel);
        add(btns, BorderLayout.SOUTH);

        ok.addActionListener(e -> onOK(brygady));
        cancel.addActionListener(e -> dispose());

        pack();
        setLocationRelativeTo(parent);
    }

    private void onOK(List<Brygada> brygady) {
        try {
            Zlecenie.stan_zlecenia stan = (Zlecenie.stan_zlecenia) stanCombo.getSelectedItem();

            String sel = (String) brygadaCombo.getSelectedItem();
            long brygadaId = Long.parseLong(sel.split(" - ")[0]);
            Brygada chosen = brygady.stream()
                    .filter(b -> b.getId() == brygadaId)
                    .findFirst()
                    .orElseThrow();

            LocalDateTime dataUtwDT = LocalDate.of(
                    (Integer) dataUtwYear.getSelectedItem(),
                    (Integer) dataUtwMonth.getSelectedItem(),
                    (Integer) dataUtwDay.getSelectedItem()
            ).atStartOfDay();

            LocalDateTime dataRozpDT = null;
            if (dataRozpYear.getSelectedItem() != null &&
                    dataRozpMonth.getSelectedItem() != null &&
                    dataRozpDay.getSelectedItem() != null) {
                dataRozpDT = LocalDate.of(
                        (Integer) dataRozpYear.getSelectedItem(),
                        (Integer) dataRozpMonth.getSelectedItem(),
                        (Integer) dataRozpDay.getSelectedItem()
                ).atStartOfDay();
            }

            LocalDateTime dataZakDT = null;
            if (dataZakYear.getSelectedItem() != null &&
                    dataZakMonth.getSelectedItem() != null &&
                    dataZakDay.getSelectedItem() != null) {
                dataZakDT = LocalDate.of(
                        (Integer) dataZakYear.getSelectedItem(),
                        (Integer) dataZakMonth.getSelectedItem(),
                        (Integer) dataZakDay.getSelectedItem()
                ).atStartOfDay();
            }

            if (toEdit == null) {
                createdZlecenie = new Zlecenie(true);
                createdZlecenie.setStan(stan);
                createdZlecenie.setBrygada(chosen);
                createdZlecenie.setDataUtworzenia(dataUtwDT);
                createdZlecenie.setDataRozpoczecia(dataRozpDT);
                createdZlecenie.setDataZakonczenia(dataZakDT);

                ZlecenieService.addZlecenie(createdZlecenie);
            } else {
                toEdit.setStan(stan);
                toEdit.setBrygada(chosen);
                toEdit.setDataUtworzenia(dataUtwDT);
                toEdit.setDataRozpoczecia(dataRozpDT);
                toEdit.setDataZakonczenia(dataZakDT);
                createdZlecenie = toEdit;
                ZlecenieService.updateZlecenie(createdZlecenie);
            }

            confirmed = true;
            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Błąd: " + ex.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static Optional<Zlecenie> showDialog(Frame parent, Zlecenie toEdit) {
        AddZlecenieDialog dlg = new AddZlecenieDialog(parent, toEdit);
        dlg.setVisible(true);
        return dlg.confirmed ? Optional.of(dlg.createdZlecenie) : Optional.empty();
    }

    public static Optional<Zlecenie> showDialog(Frame parent) {
        return showDialog(parent, null);
    }

    private JPanel createDatePanel(JComboBox<Integer> year, JComboBox<Integer> month, JComboBox<Integer> day) {
        JPanel panel = new JPanel();
        panel.add(year);
        panel.add(new JLabel("-"));
        panel.add(month);
        panel.add(new JLabel("-"));
        panel.add(day);
        return panel;
    }

    private Integer[] generateYears() {
        int currentYear = LocalDate.now().getYear();
        Integer[] years = new Integer[30];
        for (int i = 0; i < 30; i++) {
            years[i] = currentYear - 20 + i;
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
