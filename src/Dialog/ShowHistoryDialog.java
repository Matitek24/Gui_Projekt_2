package Dialog;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ShowHistoryDialog extends JDialog {
    public ShowHistoryDialog(Frame parent, String title, List<String> history) {
        super(parent, title, true);
        setLayout(new BorderLayout());

        JList<String> list = new JList<>(history.toArray(new String[0]));
        list.setEnabled(false);
        add(new JScrollPane(list), BorderLayout.CENTER);

        JButton zamknij = new JButton("Zamknij");
        zamknij.addActionListener(e -> dispose());
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        p.add(zamknij);
        add(p, BorderLayout.SOUTH);

        setSize(300, 200);
        setLocationRelativeTo(parent);
    }

    public static void show(Frame parent, List<String> history) {
        new ShowHistoryDialog(parent, "Historia brygad", history).setVisible(true);
    }
}
