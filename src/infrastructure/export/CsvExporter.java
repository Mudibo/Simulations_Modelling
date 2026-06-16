package infrastructure.export;

import domain.model.Customer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Exports simulation customer rows to CSV.
 */
public class CsvExporter {
    private static final String[] HEADERS = {
            "Customer",
            "Inter Arrival Time",
            "Arrival Time",
            "Service Time",
            "Service Start Time",
            "Service End Time",
            "Waiting Time In Queue",
            "Time In System",
            "Server Idle Time",
            "Number In Queue",
            "Number In System"
    };

    public void exportCustomers(List<Customer> customers, File destination) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(destination))) {
            writer.write(String.join(",", HEADERS));
            writer.newLine();

            for (Customer customer : customers) {
                writer.write(toCsvRow(customer));
                writer.newLine();
            }
        }
    }

    private String toCsvRow(Customer customer) {
        return String.join(",",
                String.valueOf(customer.getCustomerNumber()),
                String.valueOf(customer.getInterArrivalTime()),
                String.valueOf(customer.getArrivalTime()),
                String.valueOf(customer.getServiceTime()),
                String.valueOf(customer.getServiceStartTime()),
                String.valueOf(customer.getServiceEndTime()),
                String.valueOf(customer.getWaitingTimeInQueue()),
                String.valueOf(customer.getTimeInSystem()),
                String.valueOf(customer.getServerIdleTime()),
                String.valueOf(customer.getNumberInQueue()),
                String.valueOf(customer.getNumberInSystem()));
    }
}
