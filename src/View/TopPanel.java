package View;

import Factory.EntityActionsFactory;
import Interface.EntityActions;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class TopPanel extends JPanel {
    private final CenterPanel centerPanel;
    private final Map<String, EntityActions> actionsMap;
    private final JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

    public TopPanel(CenterPanel centerPanel) {
        this.centerPanel = centerPanel;
        setLayout(new BorderLayout());


        leftPanel.setBorder(BorderFactory.createEmptyBorder(5, 200, 5, 5));

        String currentEntity = "Dział pracowników";
        EntityActions actions = EntityActionsFactory.createActions(currentEntity, centerPanel, this);
        actionsMap = Map.of(currentEntity, actions);

        List<String> buttonNames = List.of("Nowy", "Edycja", "Usun");
        buttonNames.forEach(name -> {
            JButton btn = createButton(name);
            leftPanel.add(btn);
            addActionListener(btn, name, actions);
        });

        for(JButton extraBtn : actions.getExtraButtons())
        {
            styleExtraButton(extraBtn);
            leftPanel.add(extraBtn);
        }

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.add(createWelcomeButton("Witaj AS"));

        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.EAST);
    }

    private void addActionListener(JButton btn, String name, EntityActions actions) {
        switch (name) {
            case "Nowy" -> btn.addActionListener(e -> actions.onAdd());
            case "Edycja" -> btn.addActionListener(e -> actions.onEdit());
            case "Usun" -> btn.addActionListener(e -> actions.onDelete());
            case "Pracownicy Działu" -> btn.addActionListener(e ->
                    JOptionPane.showMessageDialog(this, "Akcja dodatkowa dla działu"));
        }
    }

    public void setEntity(String entityName) {
        leftPanel.removeAll();

        EntityActions actions = EntityActionsFactory.createActions(entityName, centerPanel, this);

        List<String> buttonNames = List.of("Nowy", "Edycja", "Usun");
        buttonNames.forEach(name -> {
            JButton btn = createButton(name);
            leftPanel.add(btn);
            addActionListener(btn, name, actions);
        });

        for (JButton extraBtn : actions.getExtraButtons()) {
            styleExtraButton(extraBtn);
            leftPanel.add(extraBtn);
        }

        revalidate();
        repaint();
    }


    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setMargin(new Insets(10, 15, 10, 15));
        button.setBackground(new Color(30, 144, 255));
        button.setForeground(Color.WHITE);
        button.setOpaque(true);
        button.setBorderPainted(false);
        return button;
    }

    private JButton createWelcomeButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setMargin(new Insets(10, 15, 10, 15));
        button.setForeground(Color.black);
        button.setOpaque(true);
        return button;
    }
    private void styleExtraButton(JButton btn) {
    btn.setFocusPainted(false);
    btn.setMargin(new Insets(10, 15, 10, 15));
    btn.setBackground(new Color(60, 179, 113));
    btn.setForeground(Color.WHITE);
    btn.setOpaque(true);
    btn.setBorderPainted(false);
    }

}
