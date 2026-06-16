package presentation.output;

import application.CalculationReportGenerator;
import application.SimulationController;
import domain.model.SimulationResult;
import domain.service.QueueStatusExplanationService;
import presentation.components.StatisticsPanel;
import presentation.table.CustomerTableModel;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Output window for customer-by-customer simulation results and statistics.
 */
public class OutputFrame extends JFrame {
    private static final String TITLE = "Bank Queue Simulation System - Results";
    private static final int FRAME_WIDTH = 1200;
    private static final int FRAME_HEIGHT = 700;

    private final SimulationController simulationController;
    private final SimulationResult simulationResult;
    private final QueueStatusExplanationService queueStatusExplanationService;

    private JTable customerTable;

    public OutputFrame(SimulationController simulationController, SimulationResult simulationResult) {
        this.simulationController = simulationController;
        this.simulationResult = simulationResult;
        this.queueStatusExplanationService = new QueueStatusExplanationService();
        initializeUi();
    }

    private void initializeUi() {
        setTitle(TITLE);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setLocationByPlatform(true);

        JPanel rootPanel = new JPanel(new BorderLayout(12, 12));
        rootPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Customer Results", createCustomerResultsTab());
        tabbedPane.addTab("Queue Characteristics", createQueueCharacteristicsTab());
        tabbedPane.addTab("Calculation Breakdown", createCalculationBreakdownTab());
        rootPanel.add(tabbedPane, BorderLayout.CENTER);

        rootPanel.add(createButtonPanel(), BorderLayout.SOUTH);

        setContentPane(rootPanel);
    }

    private JPanel createCustomerResultsTab() {
        JPanel panel = new JPanel(new BorderLayout());
        customerTable = createTable();
        customerTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                if (event.getClickCount() == 2) {
                    showExplanationForSelectedCustomer();
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(customerTable);
        scrollPane.setPreferredSize(new Dimension(1120, 560));
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createQueueCharacteristicsTab() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        StatisticsPanel statisticsPanel = new StatisticsPanel(simulationResult.statistics());
        panel.add(statisticsPanel, BorderLayout.NORTH);
        return panel;
    }

    private JPanel createCalculationBreakdownTab() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JTextArea reportArea = new JTextArea();
        reportArea.setEditable(false);
        reportArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        reportArea.setLineWrap(true);
        reportArea.setWrapStyleWord(true);

        CalculationReportGenerator reportGenerator = new CalculationReportGenerator();
        reportArea.setText(reportGenerator.generateReport(simulationResult));
        reportArea.setCaretPosition(0);

        JScrollPane scrollPane = new JScrollPane(reportArea);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private void showExplanationForSelectedCustomer() {
        int selectedViewRow = customerTable.getSelectedRow();
        if (selectedViewRow < 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please select a customer row first.",
                    "Explain Selected Customer",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int selectedModelRow = customerTable.convertRowIndexToModel(selectedViewRow);
        String explanation = queueStatusExplanationService.explainCustomerJourney(
                simulationResult.customers(),
                selectedModelRow);

        CustomerExplanationDialog dialog = new CustomerExplanationDialog(this, explanation);
        dialog.setVisible(true);
    }

    private JTable createTable() {
        JTable table = new JTable(new CustomerTableModel(simulationResult.customers()));
        table.setAutoCreateRowSorter(true);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setRowHeight(24);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        for (int column = 0; column < table.getColumnCount(); column++) {
            table.getColumnModel().getColumn(column).setPreferredWidth(125);
            table.getColumnModel().getColumn(column).setCellRenderer(centerRenderer);
        }

        return table;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);

        JButton runNewSimulationButton = new JButton("Run New Simulation");
        runNewSimulationButton.addActionListener(event -> simulationController.runNewSimulation(this));

        JButton exportButton = new JButton("Export Results");
        exportButton.addActionListener(event -> simulationController.exportResults(this, simulationResult.customers()));

        JButton explainSelectedButton = new JButton("Explain Selected Customer");
        explainSelectedButton.addActionListener(event -> showExplanationForSelectedCustomer());

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(event -> dispose());

        gbc.gridx = 0;
        panel.add(runNewSimulationButton, gbc);
        gbc.gridx = 1;
        panel.add(exportButton, gbc);
        gbc.gridx = 2;
        panel.add(explainSelectedButton, gbc);
        gbc.gridx = 3;
        panel.add(closeButton, gbc);

        return panel;
    }
}
