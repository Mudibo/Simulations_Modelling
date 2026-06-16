package presentation.components;

import domain.model.SimulationStatistics;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.DecimalFormat;

/**
 * Displays queue characteristics summary.
 */
public class StatisticsPanel extends JPanel {
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.00");

    public StatisticsPanel(SimulationStatistics statistics) {
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                "Queue Characteristics",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 13)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 8, 5, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;
        addMetric(gbc, row++, "Average Waiting Time", format(statistics.getAverageWaitingTime()));
        addMetric(gbc, row++, "Probability Customer Waits", format(statistics.getProbabilityCustomerWaits()));
        addMetric(gbc, row++, "Proportion Server Idle", format(statistics.getProportionServerIdle()));
        addMetric(gbc, row++, "Probability Server Busy", format(statistics.getProbabilityServerBusy()));
        addMetric(gbc, row++, "Average Service Time", format(statistics.getAverageServiceTime()));
        addMetric(gbc, row++, "Average Waiting Time For Those Who Waited", format(statistics.getAverageWaitingTimeForThoseWhoWait()));
        addMetric(gbc, row++, "Average Time Between Arrivals", format(statistics.getAverageTimeBetweenArrivals()));
        addMetric(gbc, row++, "Average Time Spent In System", format(statistics.getAverageTimeSpentInSystem()));
    }

    private void addMetric(GridBagConstraints gbc, int row, String name, String value) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.7;
        add(new JLabel(name + ":"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.3;
        add(new JLabel(value), gbc);
    }

    private String format(double value) {
        return DECIMAL_FORMAT.format(value);
    }
}
