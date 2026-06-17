package presentation.output;

import application.SimulationController;
import domain.model.SimulationResult;
import domain.model.SimulationStatistics;
import domain.service.QueueStatusExplanationService;
import presentation.components.CalculationBreakdownPanel;
import presentation.components.DashboardHeaderPanel;
import presentation.components.StatisticCard;
import presentation.components.SummaryPanel;
import presentation.table.CustomerTableModel;
import util.AppBranding;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.ScrollPaneConstants;
import javax.swing.ScrollPaneLayout;
import javax.swing.Scrollable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.time.LocalDateTime;

/**
 * Dashboard output window for simulation analytics and detailed results.
 */
public class OutputFrame extends JFrame {
    private static final int FRAME_WIDTH = 1320;
    private static final int FRAME_HEIGHT = 820;
    private static final Font UI_FONT_MEDIUM = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font UI_FONT_BOLD = new Font("Segoe UI", Font.BOLD, 13);

    private final DecimalFormat twoDecimals = new DecimalFormat("0.00");

    private final SimulationController simulationController;
    private final SimulationResult simulationResult;
    private final QueueStatusExplanationService queueStatusExplanationService;

    private JTabbedPane mainTabbedPane;
    private JTable customerTable;
    private JButton explainSelectedButton;

    public OutputFrame(SimulationController simulationController, SimulationResult simulationResult) {
        this.simulationController = simulationController;
        this.simulationResult = simulationResult;
        this.queueStatusExplanationService = new QueueStatusExplanationService();
        initializeUi();
    }

    private void initializeUi() {
        setTitle(AppBranding.APP_TITLE + " - Results");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setMinimumSize(new Dimension(1120, 720));
        setLocationRelativeTo(null);
        setIconImage(AppBranding.createAppIcon());

        JPanel dashboardPanel = new ScrollablePanel();
        dashboardPanel.setLayout(new BoxLayout(dashboardPanel, BoxLayout.Y_AXIS));
        dashboardPanel.setBackground(new Color(245, 248, 253));
        dashboardPanel.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));

        JPanel topPanel = createDashboardTopPanel();
        topPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel centerPanel = createCenterPanel();
        centerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel buttonPanel = createButtonPanel();
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        dashboardPanel.add(topPanel);
        dashboardPanel.add(Box.createVerticalStrut(10));
        dashboardPanel.add(centerPanel);
        dashboardPanel.add(Box.createVerticalStrut(10));
        dashboardPanel.add(buttonPanel);

        JScrollPane rootScrollPane = new JScrollPane(dashboardPanel);
        rootScrollPane.setLayout(new ScrollPaneLayout.UIResource());
        rootScrollPane.setBorder(BorderFactory.createEmptyBorder());
        rootScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        rootScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        rootScrollPane.getViewport().setBackground(new Color(245, 248, 253));
        rootScrollPane.getVerticalScrollBar().setUnitIncrement(16);

        setContentPane(rootScrollPane);
    }

    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel(new BorderLayout(0, 8));
        centerPanel.setOpaque(false);
        centerPanel.add(createQuickActionsBar(), BorderLayout.NORTH);
        centerPanel.add(createMainTabs(), BorderLayout.CENTER);
        centerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1500));
        return centerPanel;
    }

    private JPanel createQuickActionsBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setOpaque(false);

        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        buttonRow.setOpaque(false);

        JButton exportButton = new JButton("Export CSV");
        exportButton.setFont(UI_FONT_BOLD);
        exportButton.setToolTipText("Export customer results to a CSV file");
        exportButton.addActionListener(event -> simulationController.exportResults(this, simulationResult.customers()));

        JButton runNewSimulationButton = new JButton("Run New Simulation");
        runNewSimulationButton.setFont(UI_FONT_BOLD);
        runNewSimulationButton.setToolTipText("Return to parameter entry and run another simulation");
        runNewSimulationButton.addActionListener(event -> simulationController.runNewSimulation(this));

        buttonRow.add(exportButton);
        buttonRow.add(runNewSimulationButton);

        bar.add(buttonRow, BorderLayout.WEST);
        return bar;
    }

    private JPanel createDashboardTopPanel() {
        SimulationStatistics statistics = simulationResult.statistics();

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setOpaque(false);

        topPanel.add(new DashboardHeaderPanel(LocalDateTime.now()));
        topPanel.add(Box.createVerticalStrut(10));
        topPanel.add(createStatisticsCardsPanel(statistics));
        topPanel.add(Box.createVerticalStrut(10));
        topPanel.add(new SummaryPanel(statistics));

        return topPanel;
    }

    private JPanel createStatisticsCardsPanel(SimulationStatistics statistics) {
        JPanel cardsPanel = new JPanel(new GridLayout(1, 4, 12, 12));
        cardsPanel.setOpaque(false);

        cardsPanel.add(new StatisticCard("Average Waiting Time", twoDecimals.format(statistics.getAverageWaitingTime()) + " min"));
        cardsPanel.add(new StatisticCard("Average Service Time", twoDecimals.format(statistics.getAverageServiceTime()) + " min"));
        cardsPanel.add(new StatisticCard("Probability Customer Waits", twoDecimals.format(statistics.getProbabilityCustomerWaits() * 100) + "%"));
        cardsPanel.add(new StatisticCard("Server Busy Probability", twoDecimals.format(statistics.getProbabilityServerBusy() * 100) + "%"));

        return cardsPanel;
    }

    private JTabbedPane createMainTabs() {
        mainTabbedPane = new JTabbedPane();
        mainTabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));

        mainTabbedPane.addTab("Customer Results", createCustomerResultsTab());
        mainTabbedPane.addTab("Queue Characteristics", createQueueCharacteristicsTab());
        mainTabbedPane.addTab("Calculation Breakdown", new CalculationBreakdownPanel(simulationResult));
        return mainTabbedPane;
    }

    private JPanel createCustomerResultsTab() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        customerTable = createCustomerTable();
        customerTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                if (event.getClickCount() == 2 && customerTable.getSelectedRow() >= 0) {
                    showExplanationForSelectedCustomer();
                }
            }
        });

        customerTable.getSelectionModel().addListSelectionListener(this::onCustomerSelectionChanged);

        JScrollPane scrollPane = new JScrollPane(customerTable);
        scrollPane.getViewport().setBackground(Color.WHITE);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private void onCustomerSelectionChanged(ListSelectionEvent event) {
        if (!event.getValueIsAdjusting() && explainSelectedButton != null) {
            explainSelectedButton.setEnabled(customerTable.getSelectedRow() >= 0);
        }
    }

    private JTable createCustomerTable() {
        JTable table = new JTable(new CustomerTableModel(simulationResult.customers()));
        table.setAutoCreateRowSorter(true);
        table.setFont(UI_FONT_MEDIUM);
        table.setRowHeight(28);
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(true);
        table.setGridColor(new Color(220, 226, 235));
        table.setSelectionBackground(new Color(217, 232, 252));
        table.setSelectionForeground(new Color(25, 35, 52));
        table.getTableHeader().setFont(UI_FONT_BOLD);
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setPreferredSize(new Dimension(10, 32));
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        DefaultTableCellRenderer stripedRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable table,
                    Object value,
                    boolean isSelected,
                    boolean hasFocus,
                    int row,
                    int column) {
                Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    component.setBackground((row % 2 == 0) ? Color.WHITE : new Color(248, 250, 253));
                }
                return component;
            }
        };
        stripedRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        for (int column = 0; column < table.getColumnCount(); column++) {
            table.getColumnModel().getColumn(column).setPreferredWidth(120);
            table.getColumnModel().getColumn(column).setCellRenderer(stripedRenderer);
        }
        table.getColumnModel().getColumn(0).setPreferredWidth(90);
        table.getColumnModel().getColumn(6).setPreferredWidth(165);
        table.getColumnModel().getColumn(7).setPreferredWidth(130);
        table.getColumnModel().getColumn(8).setPreferredWidth(130);

        return table;
    }

    private JPanel createQueueCharacteristicsTab() {
        SimulationStatistics statistics = simulationResult.statistics();

        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        panel.add(createQueueCharacteristicsHeader(), BorderLayout.NORTH);

        JTabbedPane queuePages = new JTabbedPane();
        queuePages.setFont(new Font("Segoe UI", Font.BOLD, 13));
        queuePages.addTab("Visual Analytics", createQueueVisualAnalyticsPage(statistics));
        queuePages.addTab("Calculation Derivation", createQueueDerivationPage());

        panel.add(queuePages, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createQueueVisualAnalyticsPage(SimulationStatistics statistics) {
        JPanel pagePanel = new JPanel(new BorderLayout(0, 12));
        pagePanel.setOpaque(false);
        pagePanel.setBorder(BorderFactory.createEmptyBorder(6, 2, 2, 2));

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);

        JPanel cardsGrid = new JPanel(new GridLayout(0, 2, 14, 14));
        cardsGrid.setOpaque(false);

        cardsGrid.add(new StatisticCard("Average Waiting Time", twoDecimals.format(statistics.getAverageWaitingTime()) + " min"));
        cardsGrid.add(new StatisticCard("Average Service Time", twoDecimals.format(statistics.getAverageServiceTime()) + " min"));
        cardsGrid.add(new StatisticCard("Average Time In System", twoDecimals.format(statistics.getAverageTimeSpentInSystem()) + " min"));
        cardsGrid.add(new StatisticCard("Average Inter-Arrival Time", twoDecimals.format(statistics.getAverageTimeBetweenArrivals()) + " min"));
        cardsGrid.add(new StatisticCard("Probability Customer Waits", twoDecimals.format(statistics.getProbabilityCustomerWaits() * 100) + "%"));
        cardsGrid.add(new StatisticCard("Probability Server Busy", twoDecimals.format(statistics.getProbabilityServerBusy() * 100) + "%"));
        cardsGrid.add(new StatisticCard("Probability Server Idle", twoDecimals.format(statistics.getProportionServerIdle() * 100) + "%"));
        cardsGrid.add(new StatisticCard(
                "Average Waiting (Who Waited)",
                twoDecimals.format(statistics.getAverageWaitingTimeForThoseWhoWait()) + " min"));

        contentPanel.add(cardsGrid);
        contentPanel.add(Box.createVerticalStrut(14));
        contentPanel.add(createIndicatorsPanel(statistics));

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.getViewport().setBackground(new Color(245, 248, 253));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        pagePanel.add(scrollPane, BorderLayout.CENTER);
        return pagePanel;
    }

    private JPanel createQueueDerivationPage() {
        JPanel pagePanel = new JPanel(new BorderLayout(0, 12));
        pagePanel.setOpaque(false);
        pagePanel.setBorder(BorderFactory.createEmptyBorder(6, 2, 2, 2));

        JPanel calculationSection = new JPanel(new BorderLayout());
        calculationSection.setOpaque(false);
        calculationSection.setBorder(BorderFactory.createTitledBorder("Calculation Derivation (Queue Characteristics)"));
        calculationSection.add(new CalculationBreakdownPanel(simulationResult), BorderLayout.CENTER);

        JScrollPane derivationScrollPane = new JScrollPane(calculationSection);
        derivationScrollPane.setBorder(BorderFactory.createEmptyBorder());
        derivationScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        derivationScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        derivationScrollPane.getViewport().setBackground(new Color(245, 248, 253));
        derivationScrollPane.getVerticalScrollBar().setUnitIncrement(16);

        pagePanel.add(derivationScrollPane, BorderLayout.CENTER);
        return pagePanel;
    }

    private JPanel createQueueCharacteristicsHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout(10, 0));
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 6, 0));

        JLabel hintLabel = new JLabel("");
        hintLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        hintLabel.setForeground(new Color(48, 66, 95));

        JButton openBreakdownButton = new JButton("Open Calculation Breakdown");
        openBreakdownButton.setFont(UI_FONT_BOLD);
        openBreakdownButton.setToolTipText("Switch to the full calculation formulas table");
        openBreakdownButton.addActionListener(event -> {
            if (mainTabbedPane != null) {
                mainTabbedPane.setSelectedIndex(2);
            }
        });

        headerPanel.add(hintLabel, BorderLayout.WEST);
        headerPanel.add(openBreakdownButton, BorderLayout.EAST);
        return headerPanel;
    }

    private JPanel createIndicatorsPanel(SimulationStatistics statistics) {
        JPanel indicatorsPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        indicatorsPanel.setOpaque(false);
        indicatorsPanel.setBorder(BorderFactory.createTitledBorder("Visual Indicators"));

        indicatorsPanel.add(createProbabilityBar("Probability Customer Waits", statistics.getProbabilityCustomerWaits()));
        indicatorsPanel.add(createProbabilityBar("Server Busy Probability", statistics.getProbabilityServerBusy()));
        indicatorsPanel.add(createProbabilityBar("Server Idle Probability", statistics.getProportionServerIdle()));

        return indicatorsPanel;
    }

    private JPanel createProbabilityBar(String label, double value) {
        JPanel rowPanel = new JPanel(new BorderLayout(10, 0));
        rowPanel.setOpaque(false);

        JLabel labelComponent = new JLabel(label);
        labelComponent.setFont(UI_FONT_BOLD);

        JProgressBar progressBar = new JProgressBar(0, 100);
        int percentage = (int) Math.round(value * 100);
        progressBar.setValue(Math.min(100, Math.max(0, percentage)));
        progressBar.setStringPainted(true);
        progressBar.setString(percentage + "%");
        progressBar.setForeground(new Color(52, 115, 192));
        progressBar.setPreferredSize(new Dimension(180, 28));
        progressBar.setFont(UI_FONT_BOLD);

        rowPanel.add(labelComponent, BorderLayout.WEST);
        rowPanel.add(progressBar, BorderLayout.CENTER);
        return rowPanel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        buttonRow.setOpaque(false);

        Dimension buttonSize = new Dimension(215, 40);

        explainSelectedButton = new JButton("Explain Selected Customer");
        explainSelectedButton.setPreferredSize(buttonSize);
        explainSelectedButton.setFont(UI_FONT_BOLD);
        explainSelectedButton.setEnabled(false);
        explainSelectedButton.setToolTipText("Open a detailed explanation for the selected customer");
        explainSelectedButton.addActionListener(event -> showExplanationForSelectedCustomer());

        JButton exportButton = new JButton("Export CSV");
        exportButton.setPreferredSize(buttonSize);
        exportButton.setFont(UI_FONT_BOLD);
        exportButton.setToolTipText("Export customer results to a CSV file");
        exportButton.addActionListener(event -> simulationController.exportResults(this, simulationResult.customers()));

        JButton runNewSimulationButton = new JButton("Run New Simulation");
        runNewSimulationButton.setPreferredSize(buttonSize);
        runNewSimulationButton.setFont(UI_FONT_BOLD);
        runNewSimulationButton.setToolTipText("Return to parameter entry and run another simulation");
        runNewSimulationButton.addActionListener(event -> simulationController.runNewSimulation(this));

        JButton closeButton = new JButton("Close");
        closeButton.setPreferredSize(buttonSize);
        closeButton.setFont(UI_FONT_BOLD);
        closeButton.setToolTipText("Close the simulation output dashboard");
        closeButton.addActionListener(event -> dispose());

        explainSelectedButton.setMnemonic('E');
        exportButton.setMnemonic('X');
        runNewSimulationButton.setMnemonic('N');
        closeButton.setMnemonic('L');

        buttonRow.add(explainSelectedButton);
        buttonRow.add(exportButton);
        buttonRow.add(runNewSimulationButton);
        buttonRow.add(closeButton);

        panel.add(buttonRow, BorderLayout.WEST);
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

    private static final class ScrollablePanel extends JPanel implements Scrollable {
        @Override
        public Dimension getPreferredScrollableViewportSize() {
            return getPreferredSize();
        }

        @Override
        public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
            return 16;
        }

        @Override
        public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
            return Math.max(32, visibleRect.height - 48);
        }

        @Override
        public boolean getScrollableTracksViewportWidth() {
            return true;
        }

        @Override
        public boolean getScrollableTracksViewportHeight() {
            return false;
        }
    }
}
