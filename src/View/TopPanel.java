package View;

import Factory.EntityActionsFactory;
import Interface.EntityActions;
import Model.Uzytkownik;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import Dialog.AddUserDialog;
public class TopPanel extends JPanel {

    public enum ActionType {
        NOWY("Nowy"),
        EDYCJA("Edycja"),
        USUN("Usuń"),
        PRACOWNICY_DZIALU("Pracownicy Działu");
        private final String name;

        ActionType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
    private final CenterPanel centerPanel;
    private final JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    private final Uzytkownik user;
    public TopPanel(CenterPanel centerPanel, Uzytkownik user) {
        this.centerPanel = centerPanel;
        this.user = user;
        setLayout(new BorderLayout());


        leftPanel.setBorder(BorderFactory.createEmptyBorder(5, 200, 5, 5));

        String currentEntity = "Dział pracowników";
        EntityActions actions = EntityActionsFactory.createActions(currentEntity, centerPanel, this, user);

        setupEntityButtons(actions);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        String initials = user.getInicial();
        String welcomeText = "Witaj " + initials;
        rightPanel.add(createWelcomeButton(welcomeText));

        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.EAST);
    }

    private void addActionListener(JButton btn, ActionType name, EntityActions actions) {
        switch (name) {
            case NOWY -> btn.addActionListener(e -> actions.onAdd());
            case EDYCJA -> btn.addActionListener(e -> actions.onEdit());
            case USUN -> btn.addActionListener(e -> actions.onDelete());
            case PRACOWNICY_DZIALU-> btn.addActionListener(e ->
                    JOptionPane.showMessageDialog(this, "Akcja dodatkowa dla działu"));
        }
    }

    public void setEntity(String entityName) {
        leftPanel.removeAll();
        EntityActions actions = EntityActionsFactory.createActions(entityName, centerPanel, this, user);
        setupEntityButtons(actions);
    }

    private void setupEntityButtons(EntityActions actions) {
        List<ActionType> buttonNames = List.of(ActionType.NOWY, ActionType.EDYCJA, ActionType.USUN);
        buttonNames.forEach(name -> {
            JButton btn = createButton(name.getName());
            leftPanel.add(btn);
            addActionListener(btn, name, actions);
        });

        for(JButton extraBtn : actions.getExtraButtons())
        {
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

        // Dodajemy ActionListener, który otworzy AddUserDialog w trybie edycji samego zalogowanego
        button.addActionListener(e -> {
            // SwingUtilities.getWindowAncestor(this) zwraca okno (Frame) zawierające TopPanel
            Window parentWindow = SwingUtilities.getWindowAncestor(this);

            // Wywołujemy AddUserDialog w trybie "edytuj własne konto":
            Optional<Uzytkownik> result = AddUserDialog.showDialog(
                    (Frame) parentWindow,   // rodzic dialogu
                    user,                   // toEdit = zalogowany użytkownik
                    user                    // logged = ten sam obiekt
            );

            // Jeśli użytkownik kliknął "OK" i wynik jest obecny, to możemy zaktualizować inicjały w przycisku
            result.ifPresent(updatedUser -> {
                // Pobieramy nowe inicjały (choć ponieważ edytować można tylko hasło, inicjały w praktyce się nie zmienią).
                String newInit = updatedUser.getInicial();
                button.setText("Witaj " + newInit);
            });
        });

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
