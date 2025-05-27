package View;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    
    public MainFrame() {
        setTitle("S33334 Mateusz Skrzypek");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        CenterPanel centerPanel = new CenterPanel();
        TopPanel topPanel = new TopPanel(centerPanel);
        LeftPanel leftPanel = new LeftPanel(centerPanel, topPanel);


        add(topPanel, BorderLayout.NORTH);
        add(leftPanel, BorderLayout.WEST);
        add(centerPanel, BorderLayout.CENTER);

    }
}