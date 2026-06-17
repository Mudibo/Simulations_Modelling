package presentation.components;

import application.CalculationBreakdown;
import domain.model.SimulationResult;
import presentation.table.CalculationTableModel;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

/**
 * Encapsulates the totals panel and calculation breakdown table.
 */
public class CalculationBreakdownPanel extends JPanel {
    public CalculationBreakdownPanel(SimulationResult result) {
        setLayout(new BorderLayout(0, 10));
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        CalculationBreakdown breakdownBuilder = new CalculationBreakdown();
        CalculationBreakdown.CalculationData data = breakdownBuilder.build(result);

        add(createTotalsUsedPanel(data.totals()), BorderLayout.NORTH);
        add(createCalculationTablePanel(data.rows()), BorderLayout.CENTER);
    }

    private JPanel createTotalsUsedPanel(List<CalculationBreakdown.TotalValue> totals) {
        JPanel totalsPanel = new JPanel(new GridBagLayout());
        totalsPanel.setBorder(BorderFactory.createTitledBorder("Totals Used"));
        totalsPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 10, 4, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        for (int index = 0; index < totals.size(); index++) {
            CalculationBreakdown.TotalValue total = totals.get(index);
            JLabel label = new JLabel(total.label() + " = " + total.value());
            label.setFont(new Font("Segoe UI", Font.BOLD, 14));

            gbc.gridx = index % 2;
            gbc.gridy = index / 2;
            totalsPanel.add(label, gbc);
        }

        return totalsPanel;
    }

    private JPanel createCalculationTablePanel(List<CalculationBreakdown.CalculationRow> rows) {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setOpaque(false);

        JTable calculationTable = new JTable(new CalculationTableModel(rows));
        calculationTable.setAutoCreateRowSorter(false);
        calculationTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        calculationTable.setFillsViewportHeight(true);
        calculationTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        calculationTable.setRowHeight(30);
        calculationTable.setRowSelectionAllowed(false);
        calculationTable.setCellSelectionEnabled(false);
        calculationTable.setFocusable(false);
        calculationTable.setBackground(Color.WHITE);
        calculationTable.setGridColor(new Color(220, 226, 235));

        JTableHeader header = calculationTable.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setReorderingAllowed(false);
        header.setBackground(new Color(236, 241, 248));
        header.setForeground(new Color(25, 35, 52));
        header.setPreferredSize(new java.awt.Dimension(10, 32));

        calculationTable.getColumnModel().getColumn(0).setPreferredWidth(360);
        calculationTable.getColumnModel().getColumn(1).setPreferredWidth(620);
        calculationTable.getColumnModel().getColumn(2).setPreferredWidth(190);

        DefaultTableCellRenderer stripedLeftRenderer = createStripedRenderer(SwingConstants.LEFT, new Font("Segoe UI", Font.PLAIN, 13));
        DefaultTableCellRenderer stripedResultRenderer = createStripedRenderer(SwingConstants.CENTER, new Font("Segoe UI", Font.PLAIN, 13));

        calculationTable.getColumnModel().getColumn(0).setCellRenderer(stripedLeftRenderer);
        calculationTable.getColumnModel().getColumn(1).setCellRenderer(stripedLeftRenderer);
        calculationTable.getColumnModel().getColumn(2).setCellRenderer(stripedResultRenderer);

        JScrollPane scrollPane = new JScrollPane(calculationTable);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getViewport().setBackground(Color.WHITE);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        return tablePanel;
    }

    private DefaultTableCellRenderer createStripedRenderer(int alignment, Font font) {
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable table,
                    Object value,
                    boolean isSelected,
                    boolean hasFocus,
                    int row,
                    int column) {
                Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                component.setBackground((row % 2 == 0) ? Color.WHITE : new Color(248, 250, 253));
                return component;
            }
        };
        renderer.setHorizontalAlignment(alignment);
        renderer.setFont(font);
        renderer.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
        return renderer;
    }

}
