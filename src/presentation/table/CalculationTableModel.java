package presentation.table;

import application.CalculationBreakdown;

import javax.swing.table.AbstractTableModel;
import java.util.List;

/**
 * Read-only table model for queue characteristic calculation rows.
 */
public class CalculationTableModel extends AbstractTableModel {
    private static final String[] COLUMN_NAMES = {"Queue characteristic", "Calculation", "Result"};

    private final List<CalculationBreakdown.CalculationRow> rows;

    public CalculationTableModel(List<CalculationBreakdown.CalculationRow> rows) {
        this.rows = rows;
    }

    @Override
    public int getRowCount() {
        return rows.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMN_NAMES.length;
    }

    @Override
    public String getColumnName(int column) {
        return COLUMN_NAMES[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        CalculationBreakdown.CalculationRow row = rows.get(rowIndex);

        return switch (columnIndex) {
            case 0 -> row.metric();
            case 1 -> buildCalculationCell(row.formula(), row.substitution());
            case 2 -> row.result();
            default -> throw new IllegalArgumentException("Unknown column index: " + columnIndex);
        };
    }

    private String buildCalculationCell(String formula, String substitution) {
        if (substitution == null || substitution.isBlank()) {
            return formula;
        }
        return "<html>"
                + "<b>Formula:</b> " + escapeHtml(formula)
                + "<br/>"
                + "<b>Substitution:</b> " + escapeHtml(substitution)
                + "</html>";
    }

    private String escapeHtml(String value) {
        return value
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;");
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }
}
