package Factory;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;

public class TablePanel extends JPanel {
    private DefaultTableModel tableModel;
    private JTable table;
    public TablePanel(String title, String[] columnNames, Object[][] data) {
        super(new BorderLayout());
        setBorder(new LineBorder(Color.BLACK));

        JLabel label = new JLabel(title);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 18));
        add(label, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(data, columnNames){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);

        add(new JScrollPane(table), BorderLayout.CENTER);
        }
        public JTable getTable() {
            return table;
        }
        public DefaultTableModel getTableModel() {
            return tableModel;
        }
}
