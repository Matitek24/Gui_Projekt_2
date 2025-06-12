package Dialog;

import Model.Brygada;
import Model.Pracownik;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AssignPracownicyDialog {
    public static class Result {
        private final Brygada brygada;
        private final List<Pracownik> pracownicy;
        public Result(Brygada b, List<Pracownik> p) {
            brygada = b;
            pracownicy = p;
        }
        public Brygada getBrygada() { return brygada; }
        public List<Pracownik> getPracownicy() { return pracownicy; }
    }

    public static Result showDialog(Frame owner, List<Brygada> brygady, List<Pracownik> pracownicy) {
        JDialog dlg = new JDialog(owner, "Przypisz pracowników", true);
        dlg.setSize(500, 400);
        dlg.setLocationRelativeTo(owner);
        dlg.setLayout(new BorderLayout(10,10));

        // --- Tabela brygad ---
        String[] colsB = { "ID", "Nazwa", "Brygadzista", "Ilość prac." };
        Object[][] dataB = brygady.stream().map(b ->
                new Object[]{ b.getId(), b.getName(),
                        b.getBrygadzista().getLogin(),
                        b.getListaPracownikow().size() }
        ).toArray(Object[][]::new);
        JTable tblB = new JTable(new DefaultTableModel(dataB, colsB) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        });
        tblB.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // --- Lista pracowników ---
        DefaultListModel<Pracownik> lm = new DefaultListModel<>();
        pracownicy.forEach(lm::addElement);
        JList<Pracownik> listP = new JList<>(lm);
        listP.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        // --- Panel centralny z dwoma komponentami ---
        JSplitPane split = new JSplitPane(
                JSplitPane.VERTICAL_SPLIT,
                new JScrollPane(tblB),
                new JScrollPane(listP)
        );
        split.setResizeWeight(0.5);
        dlg.add(split, BorderLayout.CENTER);

        // --- Przyciski OK / Anuluj ---
        JPanel pnl = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton ok = new JButton("OK"), cancel = new JButton("Anuluj");
        pnl.add(ok); pnl.add(cancel);
        dlg.add(pnl, BorderLayout.SOUTH);

        final Result[] out = new Result[1];
        ok.addActionListener(e -> {
            int sel = tblB.getSelectedRow();
            if (sel == -1) {
                JOptionPane.showMessageDialog(dlg, "Wybierz brygadę.");
                return;
            }
            Brygada selB = brygady.get(sel);
            List<Pracownik> selP = listP.getSelectedValuesList();
            if (selP.isEmpty()) {
                JOptionPane.showMessageDialog(dlg, "Wybierz przynajmniej jednego pracownika.");
                return;
            }
            out[0] = new Result(selB, selP);
            dlg.dispose();
        });
        cancel.addActionListener(e -> dlg.dispose());

        dlg.setVisible(true);
        return out[0];
    }
}
