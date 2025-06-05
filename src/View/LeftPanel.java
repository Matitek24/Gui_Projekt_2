package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;
import Dialog.StatsPanelDialog;

public class LeftPanel extends JPanel {
    private CenterPanel centerPanel;
    private TopPanel topPanel;
    private MainFrame mainFrame;
    public LeftPanel(CenterPanel centerPanel, TopPanel topPanel, MainFrame mainFrame) {
        this.centerPanel = centerPanel;
        this.topPanel = topPanel;
        this.mainFrame = mainFrame;

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
                    button.addActionListener(e -> {
                        centerPanel.showPanel(button.getText());
                        topPanel.setEntity(button.getText());
                    });
                    add(Box.createVerticalStrut(5));
                    add(button);
                });

        JButton statsButton = createButton("Statystyki");
        statsButton.setOpaque(true);
        statsButton.setBackground(new Color(70, 130, 180));
        statsButton.setForeground(Color.white);
        statsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        statsButton.addActionListener(e -> {
            StatsPanelDialog dialog = new StatsPanelDialog(mainFrame);
            dialog.setVisible(true);
        });
        statsButton.setBorderPainted(false);
        add(Box.createVerticalStrut(5));
        add(statsButton);



        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JButton logoutButton = createButton("Wyloguj się");
        logoutButton.setOpaque(true);
        logoutButton.setBackground(new Color(239, 84, 75));
        logoutButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoutButton.setForeground(Color.white);
        logoutButton.addActionListener(e -> handleLogout());
        logoutButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoutButton.setBorderPainted(false);
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
    private void handleLogout() {
        int choice = JOptionPane.showConfirmDialog(
                mainFrame,
                "Czy na pewno chcesz się wylogować?",
                "Potwierdzenie wylogowania",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (choice == JOptionPane.YES_OPTION) {
            mainFrame.logout();
        }
    }
}
