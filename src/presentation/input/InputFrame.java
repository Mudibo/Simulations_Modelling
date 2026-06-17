package presentation.input;

import application.SimulationController;
import util.AppBranding;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

/**
 * Input window for simulation parameters.
 */
public class InputFrame extends JFrame {
    private static final String TITLE = AppBranding.APP_TITLE;
    private static final int FRAME_WIDTH = 760;
    private static final int FRAME_HEIGHT = 520;

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
    private JButton runButton;

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
        setIconImage(AppBranding.createAppIcon());

        JPanel rootPanel = new JPanel(new BorderLayout());
        rootPanel.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));
        rootPanel.setBackground(new Color(245, 248, 253));

        JPanel centeredPanel = new JPanel(new GridBagLayout());
        centeredPanel.setOpaque(false);
        centeredPanel.add(createCardPanel());

        rootPanel.add(centeredPanel, BorderLayout.CENTER);

        setContentPane(rootPanel);
        if (runButton != null) {
            getRootPane().setDefaultButton(runButton);
        }
    }

    private JPanel createCardPanel() {
        JPanel cardPanel = new JPanel(new BorderLayout(0, 20));
        cardPanel.setPreferredSize(new Dimension(620, 420));
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(213, 223, 238), 1, true),
                BorderFactory.createEmptyBorder(22, 24, 18, 24)));

        JPanel headingPanel = new JPanel(new BorderLayout(0, 6));
        headingPanel.setOpaque(false);

        JLabel appTitleLabel = new JLabel(AppBranding.APP_TITLE);
        appTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        appTitleLabel.setForeground(new Color(30, 48, 73));

        JLabel subtitleLabel = new JLabel("Simulation Parameters");
        subtitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        subtitleLabel.setForeground(new Color(74, 90, 111));

        headingPanel.add(appTitleLabel, BorderLayout.NORTH);
        headingPanel.add(subtitleLabel, BorderLayout.SOUTH);

        cardPanel.add(headingPanel, BorderLayout.NORTH);
        cardPanel.add(createFormPanel(), BorderLayout.CENTER);
        cardPanel.add(createButtonPanel(), BorderLayout.SOUTH);
        return cardPanel;
    }

    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        formPanel.setBorder(BorderFactory.createTitledBorder("Simulation Parameters"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 8, 10, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        configureField(customersField, "Total number of customers to simulate");
        configureField(minInterArrivalField, "Minimum inter-arrival time");
        configureField(maxInterArrivalField, "Maximum inter-arrival time");
        configureField(minServiceField, "Minimum service time");
        configureField(maxServiceField, "Maximum service time");

        addSingleField(formPanel, gbc, 0, "Number of Customers", customersField);
        addRangeField(formPanel, gbc, 1, "Inter-Arrival Time Range", minInterArrivalField, maxInterArrivalField);
        addRangeField(formPanel, gbc, 2, "Service Time Range", minServiceField, maxServiceField);

        return formPanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);

        runButton = createRunButtonReference();
        runButton.setToolTipText("Run simulation with the current parameter ranges");

        JButton clearButton = new JButton("Clear");
        clearButton.setToolTipText("Reset fields to recommended default values");
        clearButton.addActionListener(event -> resetToDefaultValues());

        JButton exitButton = new JButton("Exit");
        exitButton.setToolTipText("Close the dashboard application");
        exitButton.addActionListener(event -> {
            dispose();
            SwingUtilities.invokeLater(() -> System.exit(0));
        });

        runButton.setMnemonic('R');
        clearButton.setMnemonic('C');
        exitButton.setMnemonic('E');

        Dimension buttonSize = new Dimension(150, 34);
        runButton.setPreferredSize(buttonSize);
        clearButton.setPreferredSize(buttonSize);
        exitButton.setPreferredSize(buttonSize);

        gbc.gridx = 0;
        buttonPanel.add(runButton, gbc);
        gbc.gridx = 1;
        buttonPanel.add(clearButton, gbc);
        gbc.gridx = 2;
        buttonPanel.add(exitButton, gbc);

        return buttonPanel;
    }

    private JButton createRunButtonReference() {
        JButton runButton = new JButton("Run Simulation");
        runButton.addActionListener(event -> simulationController.runSimulation(
                this,
                customersField.getText(),
                minInterArrivalField.getText(),
                maxInterArrivalField.getText(),
                minServiceField.getText(),
                maxServiceField.getText()));
        return runButton;
    }

    private void addSingleField(JPanel panel, GridBagConstraints gbc, int row, String label, JTextField field) {
        JLabel labelComponent = new JLabel(label);
        labelComponent.setFont(new Font("Segoe UI", Font.BOLD, 13));

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.42;
        panel.add(labelComponent, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.58;
        panel.add(field, gbc);
    }

    private void addRangeField(
            JPanel panel,
            GridBagConstraints gbc,
            int row,
            String label,
            JTextField minField,
            JTextField maxField) {
        JLabel labelComponent = new JLabel(label);
        labelComponent.setFont(new Font("Segoe UI", Font.BOLD, 13));

        JPanel rangePanel = new JPanel(new GridBagLayout());
        rangePanel.setOpaque(false);

        GridBagConstraints rangeGbc = new GridBagConstraints();
        rangeGbc.insets = new Insets(0, 0, 0, 6);
        rangePanel.add(minField, rangeGbc);

        rangeGbc.gridx = 1;
        rangeGbc.insets = new Insets(0, 0, 0, 6);
        JLabel toLabel = new JLabel("to");
        toLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        rangePanel.add(toLabel, rangeGbc);

        rangeGbc.gridx = 2;
        rangeGbc.insets = new Insets(0, 0, 0, 0);
        rangePanel.add(maxField, rangeGbc);

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.42;
        panel.add(labelComponent, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.58;
        panel.add(rangePanel, gbc);
    }

    private void configureField(JTextField field, String toolTip) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setToolTipText(toolTip);
    }

    private void resetToDefaultValues() {
        customersField.setText(DEFAULT_CUSTOMERS);
        minInterArrivalField.setText(DEFAULT_MIN_IAT);
        maxInterArrivalField.setText(DEFAULT_MAX_IAT);
        minServiceField.setText(DEFAULT_MIN_SERVICE);
        maxServiceField.setText(DEFAULT_MAX_SERVICE);
    }
}
