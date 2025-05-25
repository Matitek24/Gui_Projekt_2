package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;

public class LeftPanel extends JPanel {
    private CenterPanel centerPanel;
    public LeftPanel(CenterPanel centerPanel) {
        this.centerPanel = centerPanel;


        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        List<String> buttonNames = Arrays.asList( "Dział pracowników", "Pracownik", "Użytkownik", "Brygadzista",
                "Brygada", "Zlecenie", "Praca");

        buttonNames.stream()
                .map(this::createButton)
                .forEach(button ->{
                    button.setAlignmentX(Component.CENTER_ALIGNMENT);
                    button.addActionListener(e -> centerPanel.showPanel(button.getText()));
                    add(Box.createVerticalStrut(5));
                    add(button);
                });



        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JButton logoutButton = createButton("Wyloguj się");
        logoutButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        bottomPanel.add(logoutButton, BorderLayout.SOUTH);

        add(bottomPanel, BorderLayout.SOUTH);

    }
    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setMargin(new Insets(10, 15, 10, 15));
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, button.getPreferredSize().height));
        return button;

    }
}
