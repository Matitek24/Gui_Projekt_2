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
                        try {
                            Dzial dzial1 = Dzial.createDzial(nazwa_dzialu);
                            DzialService.addDzial(dzial1);

                            TablePanel tp = centralny.getDzialPanel();
                            tp.getTableModel().addRow(new Object[]{nazwa_dzialu});
                        } catch (NotUniqueNameException b) {
                            System.out.println("Błąd: " + b.getMessage());
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
                        String selected = selectObject(nazwy,"Wybierz dział do usuniecia", "Usun dzial");
                        Dzial toRemove = DzialService.getDzialy().stream()
                                .filter(d -> d.getNazwa_dzialu().equals(selected))
                                .findFirst().orElse(null);

                        if(toRemove != null){
                            DzialService.removeDzial(toRemove);
                            TablePanel tp = centralny.getDzialPanel();
                            DefaultTableModel model = tp.getTableModel();
                            for(int i = 0; i < model.getRowCount(); i++){
                                if(selected.equals(model.getValueAt(i, 0))){
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
                        String selected = selectObject(nazwy,"Wybierz dział do Edycji", "Edytuj Dzial");
                        if(selected == null) return;
                        String newName = promptForInput("Podaje nazwe nowego dzialu", "Edycja Dzialu");
                        if(newName == null ) return ;
                        try{
                           Dzial oldDzial = DzialService.getDzialy().stream()
                                   .filter(d->d.getNazwa_dzialu().equals(selected))
                                   .findFirst().orElse(null);

                            if(oldDzial != null){
                                DzialService.removeDzial(oldDzial);
                                Dzial update = Dzial.createDzial(newName);
                            }
                        }catch (NotUniqueNameException b){
                            JOptionPane.showMessageDialog(this, "Błąd: " + b.getMessage());
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
