package presentation.input;

import application.SimulationController;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

/**
 * Input window for simulation parameters.
 */
public class InputFrame extends JFrame {
    private static final String TITLE = "Bank Queue Simulation System";
    private static final int FRAME_WIDTH = 560;
    private static final int FRAME_HEIGHT = 360;

    private static final String DEFAULT_CUSTOMERS = "100";
    private static final String DEFAULT_MIN_IAT = "1";
    private static final String DEFAULT_MAX_IAT = "8";
    private static final String DEFAULT_MIN_SERVICE = "1";
    private static final String DEFAULT_MAX_SERVICE = "6";

    private final SimulationController simulationController;

    private final JTextField customersField = new JTextField(12);
    private final JTextField minInterArrivalField = new JTextField(12);
    private final JTextField maxInterArrivalField = new JTextField(12);
    private final JTextField minServiceField = new JTextField(12);
    private final JTextField maxServiceField = new JTextField(12);

    public InputFrame(SimulationController simulationController) {
        this.simulationController = simulationController;
        initializeUi();
        resetToDefaultValues();
    }

    private void initializeUi() {
        setTitle(TITLE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setLocationByPlatform(true);

        JPanel rootPanel = new JPanel(new BorderLayout(12, 12));
        rootPanel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JLabel heading = new JLabel("Bank Queue Simulation Parameters");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 18));
        rootPanel.add(heading, BorderLayout.NORTH);

        rootPanel.add(createFormPanel(), BorderLayout.CENTER);
        rootPanel.add(createButtonPanel(), BorderLayout.SOUTH);

        setContentPane(rootPanel);
    }

    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;
        addField(formPanel, gbc, row++, "Number of Customers", customersField);
        addField(formPanel, gbc, row++, "Minimum Inter-Arrival Time", minInterArrivalField);
        addField(formPanel, gbc, row++, "Maximum Inter-Arrival Time", maxInterArrivalField);
        addField(formPanel, gbc, row++, "Minimum Service Time", minServiceField);
        addField(formPanel, gbc, row, "Maximum Service Time", maxServiceField);

        return formPanel;
    }

    private void addField(JPanel panel, GridBagConstraints gbc, int row, String label, JTextField field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.45;
        panel.add(new JLabel(label + ":"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.55;
        panel.add(field, gbc);
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 8, 5, 8);

        JButton runButton = new JButton("Run Simulation");
        runButton.addActionListener(event -> simulationController.runSimulation(
                this,
                customersField.getText(),
                minInterArrivalField.getText(),
                maxInterArrivalField.getText(),
                minServiceField.getText(),
                maxServiceField.getText()));

        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(event -> resetToDefaultValues());

        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(event -> {
            dispose();
            SwingUtilities.invokeLater(() -> System.exit(0));
        });

        gbc.gridx = 0;
        buttonPanel.add(runButton, gbc);
        gbc.gridx = 1;
        buttonPanel.add(clearButton, gbc);
        gbc.gridx = 2;
        buttonPanel.add(exitButton, gbc);

        return buttonPanel;
    }

    private void resetToDefaultValues() {
        customersField.setText(DEFAULT_CUSTOMERS);
        minInterArrivalField.setText(DEFAULT_MIN_IAT);
        maxInterArrivalField.setText(DEFAULT_MAX_IAT);
        minServiceField.setText(DEFAULT_MIN_SERVICE);
        maxServiceField.setText(DEFAULT_MAX_SERVICE);
    }
}
