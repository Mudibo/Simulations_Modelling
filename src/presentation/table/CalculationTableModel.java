package presentation.table;

import application.CalculationBreakdown;

import javax.swing.table.AbstractTableModel;
import java.util.List;

/**
 * Read-only table model for queue characteristic calculation rows.
 */
public class CalculationTableModel extends AbstractTableModel {
    private static final String[] COLUMN_NAMES = {"Metric", "Formula", "Substitution", "Result"};

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
            case 1 -> row.formula();
            case 2 -> row.substitution();
            case 3 -> row.result();
            default -> throw new IllegalArgumentException("Unknown column index: " + columnIndex);
        };
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
