package Dialog;

import Model.Brygadzista;
import Model.Zlecenie;
import Services.ZlecenieService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ZleceniaCheckerDialog extends JDialog {
    private final Brygadzista brygadzista;

    public ZleceniaCheckerDialog(Frame parent, Brygadzista brygadzista) {
        super(parent, "Niezakończone zlecenia – " + brygadzista.getImie() + " " + brygadzista.getNazwisko(), true);
        this.brygadzista = brygadzista;

        setLayout(new BorderLayout());
        setSize(600, 400);
        setLocationRelativeTo(parent);

        List<Zlecenie> allOrders = ZlecenieService.getZlecenia();

        List<Zlecenie> unfinished = allOrders.stream()
                .filter(z -> {
                    if (z.getBrygada() == null) return false;
                    if (z.getBrygada().getBrygadzista() == null) return false;

                    boolean tenSamBrygadzista =
                            z.getBrygada()
                                    .getBrygadzista()
                                    .getBrygadzistaId()
                                    == brygadzista.getBrygadzistaId();
                    boolean nieZakonczone = z.stan != Zlecenie.stan_zlecenia.ZAKOŃCZONE;

                    return tenSamBrygadzista && nieZakonczone;
                })
                .toList();

        String[] columnNames = {
                "ID", "Stan", "Brygada", "Liczba prac", "Data utworzenia", "Data rozpoczęcia"
        };
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        for (Zlecenie z : unfinished) {
            model.addRow(new Object[]{
                    z.getId(),
                    z.getStan_zlecenia(),
                    z.getBrygada().getName(),
                    z.getPraca().size(),
                    z.getDataUtworzenia() != null ? z.getDataUtworzenia().format(fmt) : "brak",
                    z.getDataRozpoczecia() != null ? z.getDataRozpoczecia().format(fmt) : "brak"
            });
        }

        JTable table = new JTable(model);
        table.setFillsViewportHeight(true);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JButton closeBtn = new JButton("Zamknij");
        closeBtn.addActionListener(e -> dispose());
        JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        south.add(closeBtn);
        add(south, BorderLayout.SOUTH);

    }
}
