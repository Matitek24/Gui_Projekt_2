package Dialog;

import Services.BrygadaService;
import Services.DzialService;
import Services.PracownikService;
import Services.ZlecenieService;

import javax.swing.*;
import java.awt.*;

public class StatsPanelDialog extends JDialog {
    private final int padding = 50;
    private final Color barColor = new Color(100, 149, 237);
    private final Font labelFont = new Font("SansSerif", Font.PLAIN, 12);

    private int numDzialy;
    private int numPracownicy;
    private int numBrygady;
    private int numZlecenia;

    public StatsPanelDialog(JFrame parent) {
        super(parent, "Statystyki", true);
        fetchData();
        setSize(500, 400);
        setLocationRelativeTo(parent);
        add(new StatsPanel());
    }

    private void fetchData() {
        numDzialy = DzialService.getDzialy().size();
        numPracownicy = PracownikService.getPracownicy().size();
        numBrygady = BrygadaService.getBrygady().size();
        numZlecenia = ZlecenieService.getZlecenia().size();
    }

    private class StatsPanel extends JPanel {
        private final String[] labels = {"Działy", "Pracownicy", "Brygady", "Zlecenia"};
        private final int[] values;

        public StatsPanel() {
            values = new int[]{numDzialy, numPracownicy, numBrygady, numZlecenia};
            setBackground(Color.WHITE);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            drawBarChart((Graphics2D) g);
        }

        private void drawBarChart(Graphics2D g) {
            int width = getWidth();
            int height = getHeight();

            int barWidth = 60;
            int gap = 40;

            // Wyznaczamy maksimum dla skalowania słupków
            int max = 0;
            for (int v : values) {
                max = Math.max(max, v);
            }
            if (max == 0) {
                max = 1; // Unikamy dzielenia przez zero
            }

            int x = padding;
            g.setFont(labelFont);

            for (int i = 0; i < values.length; i++) {
                int barHeight = (int) ((height - 2 * padding) * ((double) values[i] / max));

                // Rysujemy słupek
                g.setColor(barColor);
                g.fillRect(x, height - padding - barHeight, barWidth, barHeight);

                // Podpisy
                g.setColor(Color.BLACK);
                // Nazwa kategorii (pod spodem słupka)
                int labelWidth = g.getFontMetrics().stringWidth(labels[i]);
                g.drawString(labels[i], x + (barWidth - labelWidth) / 2, height - padding + 20);
                // Wartość nad słupkiem
                String strVal = String.valueOf(values[i]);
                int valWidth = g.getFontMetrics().stringWidth(strVal);
                g.drawString(strVal, x + (barWidth - valWidth) / 2, height - padding - barHeight - 10);

                x += barWidth + gap;
            }
        }
    }
}
