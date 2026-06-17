package presentation.table;

import domain.model.Customer;

import javax.swing.table.AbstractTableModel;
import java.util.List;

/**
 * Table model for displaying customer simulation rows.
 */
public class CustomerTableModel extends AbstractTableModel {
    private static final String[] COLUMN_NAMES = {
            "Customer",
            "IAT",
            "Arrival Time",
            "Service Time",
            "Service Start",
            "Service End",
            "Waiting Time In Queue",
            "Time In System",
            "Server Idle Time",
            "Queue Length",
            "Number In System"
    };

    private final List<Customer> customers;

    public CustomerTableModel(List<Customer> customers) {
        this.customers = customers;
    }

    @Override
    public int getRowCount() {
        return customers.size();
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
    public Class<?> getColumnClass(int columnIndex) {
        return Integer.class;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Customer customer = customers.get(rowIndex);

        return switch (columnIndex) {
            case 0 -> customer.getCustomerNumber();
            case 1 -> customer.getInterArrivalTime();
            case 2 -> customer.getArrivalTime();
            case 3 -> customer.getServiceTime();
            case 4 -> customer.getServiceStartTime();
            case 5 -> customer.getServiceEndTime();
            case 6 -> customer.getWaitingTimeInQueue();
            case 7 -> customer.getTimeInSystem();
            case 8 -> customer.getServerIdleTime();
            case 9 -> customer.getNumberInQueue();
            case 10 -> customer.getNumberInSystem();
            default -> throw new IllegalArgumentException("Unknown column index: " + columnIndex);
        };
    }
}
