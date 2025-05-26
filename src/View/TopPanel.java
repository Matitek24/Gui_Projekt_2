package View;

import Factory.TablePanel;
import Model.Dzial;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.*;
import Dialog.*;
import javax.swing.table.DefaultTableModel;

import Exception.NotUniqueNameException;
import Services.DzialService;

public class TopPanel extends JPanel {
    private final CenterPanel centralny;
    public TopPanel(CenterPanel centerPanel) {
        this.centralny = centerPanel;
        setLayout(new BorderLayout());

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(5, 200, 5, 5));
        List<String> buttonNames = Arrays.asList("Nowy", "Edycja", "Usun","Pracownicy Działu");


        buttonNames.stream()
                .map(this::createButton)
                .forEach(leftPanel::add);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.add(createWelcomeButton("Witaj AS"));


        for(Component component : leftPanel.getComponents()){
            if(component instanceof JButton){
                JButton button = (JButton) component;
                // dodawanie
                if(button.getText().equals("Nowy")){
                    button.addActionListener(e -> {
                     String nazwa_dzialu = promptForInput("Podaj nazwe nowego dzialu", "Tworzenie Dzialu");

                        if (nazwa_dzialu == null || nazwa_dzialu.trim().isEmpty()) {
                            JOptionPane.showMessageDialog(this, "Nie podano nazwy działu");
                            return;
                        }

                        try {
                            Dzial dzial1 = Dzial.createDzial(nazwa_dzialu.trim());
                            DzialService.addDzial(dzial1);

                            TablePanel tp = centralny.getDzialPanel();
                            tp.getTableModel().addRow(new Object[]{
                                    dzial1.getId(),
                                    dzial1.getNazwa_dzialu()
                            });
                        } catch (NotUniqueNameException b) {
                            JOptionPane.showMessageDialog(
                                    this,
                                    b.getMessage(),
                                    "Niepowodzenie operacji",
                                    JOptionPane.ERROR_MESSAGE
                            );
                        }
                    });
                }

                // usuowanie
                if(button.getText().equals("Usun")){
                    button.addActionListener(e -> {
                        List<String> nazwy = DzialService.getNazwyDzialow();
                        if(nazwy.isEmpty()){
                            JOptionPane.showMessageDialog(this,"Brak działów");
                            return;
                        }

                        // Użycie DeleteDialog
                        String selected = DeleteDialog.showDialog(
                                this,
                                nazwy,
                                "Wybierz dział do usunięcia:",
                                "Usuń Dział"
                        );

                        if(selected == null) return;

                        Dzial toRemove = DzialService.getDzialy().stream()
                                .filter(d -> d.getNazwa_dzialu().equals(selected))
                                .findFirst().orElse(null);

                        if(toRemove != null){
                            DzialService.removeDzial(toRemove);
                            TablePanel tp = centralny.getDzialPanel();
                            DefaultTableModel model = tp.getTableModel();
                            for(int i = 0; i < model.getRowCount(); i++){
                                if(selected.equals(model.getValueAt(i, 1))){
                                    model.removeRow(i);
                                    break;
                                }
                            }
                        }
                    });
                }
                if(button.getText().equals("Edycja")){
                    button.addActionListener(e -> {
                        List<String> nazwy = DzialService.getNazwyDzialow();
                        if(nazwy.isEmpty()){
                            JOptionPane.showMessageDialog(this,"Brak działów");
                            return;
                        }

                        String[] result = EditDialog.showDialog(
                                this,
                                nazwy,
                                "Wybierz dział do edycji",
                                "Podaj nazwe nowego działu",
                                "Edycja działu"
                        );

                        if (result != null) {
                            String selectedName = result[0];
                            String newName = result[1];

                            try{
                                Dzial oldDzial = DzialService.getDzialy().stream()
                                        .filter(d -> d.getNazwa_dzialu().equals(selectedName))
                                        .findFirst().orElse(null);

                                if(oldDzial != null){
                                    oldDzial.rename(newName);
                                    DzialService.removeDzial(oldDzial);
                                    Dzial update = Dzial.createDzial(newName);
                                    DzialService.addDzial(update);

                                    TablePanel tp = centralny.getDzialPanel();
                                    DefaultTableModel model = tp.getTableModel();
                                    for(int i = 0; i < model.getRowCount(); i++){
                                        if(selectedName.equals(model.getValueAt(i, 1))){
                                            model.setValueAt(newName, i, 1);
                                            break;
                                        }
                                    }

                                }
                            }catch (NotUniqueNameException b){
                                JOptionPane.showMessageDialog(
                                        this,
                                        b.getMessage(),
                                        "Niepowodzenie operacji",
                                        JOptionPane.ERROR_MESSAGE
                                );
                            }
                        }
                    });
                }
            }
        }


        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.EAST);

    }
    private JButton createButton(String text){
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setMargin(new Insets(10, 15, 10, 15));
        button.setBackground(new Color(30, 144, 255));
        button.setForeground(Color.WHITE);
        button.setOpaque(true);
        button.setBorderPainted(false);
        return button;
    }
    private String selectObject(List<String> nazwy, String messeage, String title){
        String selected = (String) JOptionPane.showInputDialog(
                this,
                messeage,
                title,
                JOptionPane.PLAIN_MESSAGE,
                null,
                nazwy.toArray(),
                nazwy.get(0)
        );
        return selected;
    }
    private JButton createWelcomeButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setMargin(new Insets(10, 15, 10, 15));
        button.setForeground(Color.black);
        button.setOpaque(true);
        return button;
    }
    private String promptForInput(String message, String title){
        return InputDialog.showDialog(this, message, title);
    }
}
