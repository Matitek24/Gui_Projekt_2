package Factory;

import javax.swing.*;

public class TablePanelFactory extends JPanel {
    public static TablePanel createTablePanel(String title, String[] colNames, Object[][] rowData) {
        return new TablePanel(title, colNames, rowData);
    }
}
