package presentation.components;

import domain.model.SimulationStatistics;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.DecimalFormat;

/**
 * Summary panel for core simulation totals and utilization.
 */
public class SummaryPanel extends JPanel {
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.00");

    public SummaryPanel(SimulationStatistics statistics) {
        setLayout(new GridBagLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(214, 223, 236), 1, true),
                BorderFactory.createEmptyBorder(12, 14, 12, 14)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 8, 4, 8);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel heading = new JLabel("Simulation Summary");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 15));
        heading.setForeground(new Color(35, 53, 78));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(heading, gbc);

        gbc.gridwidth = 1;
        addRow(gbc, 1, "Customers Simulated", String.valueOf(statistics.getTotalCustomers()));
        addRow(gbc, 2, "Total Simulation Time", statistics.getTotalSimulationTime() + " min");
        addRow(gbc, 3, "Customers Who Waited", String.valueOf(statistics.getCustomersWhoWaited()));
        addRow(gbc, 4, "Server Utilization", DECIMAL_FORMAT.format(statistics.getProbabilityServerBusy() * 100) + "%");
    }

    private void addRow(GridBagConstraints gbc, int row, String label, String value) {
        JLabel labelComponent = new JLabel(label + ":");
        labelComponent.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        labelComponent.setForeground(new Color(74, 89, 110));

        JLabel valueComponent = new JLabel(value);
        valueComponent.setFont(new Font("Segoe UI", Font.BOLD, 13));
        valueComponent.setForeground(new Color(28, 44, 67));

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.55;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(labelComponent, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.45;
        add(valueComponent, gbc);
    }
}
