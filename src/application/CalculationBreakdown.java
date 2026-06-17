package application;

import domain.model.Customer;
import domain.model.SimulationResult;
import domain.model.SimulationStatistics;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Builds tabular queue-characteristic calculations and totals for the output UI.
 */
public class CalculationBreakdown {
    private final DecimalFormat twoDecimals = new DecimalFormat("0.00");

    public CalculationData build(SimulationResult result) {
        List<Customer> customers = result.customers();
        SimulationStatistics statistics = result.statistics();

    int totalCustomers = statistics.getTotalCustomers();
    int customersWaited = statistics.getCustomersWhoWaited();

    int totalIdleTime = sumIdleTime(customers);
    int totalRunTime = computeTotalRunTime(customers);
        int sumService = sumServiceTime(customers);
        int sumWaiting = sumWaitingTime(customers);
        int sumTimeInSystem = sumTimeInSystem(customers);

    double averageWaiting = totalCustomers == 0 ? 0.0 : (double) sumWaiting / totalCustomers;
    double probabilityCustomerWaits = totalCustomers == 0 ? 0.0 : (double) customersWaited / totalCustomers;
    double proportionIdle = totalRunTime == 0 ? 0.0 : (double) totalIdleTime / totalRunTime;
    double probabilityBusy = 1.0 - proportionIdle;
    double averageService = totalCustomers == 0 ? 0.0 : (double) sumService / totalCustomers;
    double averageWaitingForWaited = customersWaited == 0 ? 0.0 : (double) sumWaiting / customersWaited;
    double averageTimeInSystem = totalCustomers == 0 ? 0.0 : (double) sumTimeInSystem / totalCustomers;

        List<TotalValue> totals = List.of(
                new TotalValue("ΣWq", String.valueOf(sumWaiting)),
        new TotalValue("ΣS", String.valueOf(sumService)),
                new TotalValue("ΣTs", String.valueOf(sumTimeInSystem)),
                new TotalValue("Customers Waited", String.valueOf(customersWaited)),
                new TotalValue("Total Idle Time", String.valueOf(totalIdleTime)),
        new TotalValue("Total Run Time", String.valueOf(totalRunTime))
        );

        List<CalculationRow> rows = new ArrayList<>();
        rows.add(new CalculationRow(
        "Average Waiting Time For A Customer",
                "μ(Wq) = ΣWq / N",
                sumWaiting + " / " + totalCustomers,
        formatWithUnit(averageWaiting, "min")));

        rows.add(new CalculationRow(
        "Probability That A Customer Has To Wait",
                "P(Wq > 0) = Customers Waited / N",
                customersWaited + " / " + totalCustomers,
        twoDecimals.format(probabilityCustomerWaits)));

        rows.add(new CalculationRow(
        "Proportion Of Idle Time Of Server",
        "P(Idle) = Total Idle Time / Total Run Time",
        totalIdleTime + " / " + totalRunTime,
        twoDecimals.format(proportionIdle)));

        rows.add(new CalculationRow(
        "Probability Server Is Busy",
        "P(Busy) = 1 − P(Idle)",
        "1 − " + twoDecimals.format(proportionIdle),
        twoDecimals.format(probabilityBusy)));

        rows.add(new CalculationRow(
                "Average Service Time",
                "μ(S) = ΣS / N",
                sumService + " / " + totalCustomers,
        formatWithUnit(averageService, "min")));

        rows.add(new CalculationRow(
        "Average Waiting Time For Those Who Waited",
                "μ(Wq | Wq > 0) = ΣWq / Customers Waited",
                sumWaiting + " / " + customersWaited,
        formatWithUnit(averageWaitingForWaited, "min")));

        rows.add(new CalculationRow(
        "Average Time Spent In System",
                "μ(Ts) = ΣTs / N",
                sumTimeInSystem + " / " + totalCustomers,
        formatWithUnit(averageTimeInSystem, "min")));

        return new CalculationData(totals, rows);
    }

    private String formatWithUnit(double value, String unit) {
        return twoDecimals.format(value) + " " + unit;
    }

    private int sumIdleTime(List<Customer> customers) {
        int total = 0;
        for (Customer customer : customers) {
            total += customer.getServerIdleTime();
        }
        return total;
    }

    private int computeTotalRunTime(List<Customer> customers) {
        int totalRunTime = 0;
        for (Customer customer : customers) {
            totalRunTime = Math.max(totalRunTime, customer.getServiceEndTime());
        }
        return totalRunTime;
    }

    private int sumServiceTime(List<Customer> customers) {
        int total = 0;
        for (Customer customer : customers) {
            total += customer.getServiceTime();
        }
        return total;
    }

    private int sumWaitingTime(List<Customer> customers) {
        int total = 0;
        for (Customer customer : customers) {
            total += customer.getWaitingTimeInQueue();
        }
        return total;
    }

    private int sumTimeInSystem(List<Customer> customers) {
        int total = 0;
        for (Customer customer : customers) {
            total += customer.getTimeInSystem();
        }
        return total;
    }

    public record CalculationData(List<TotalValue> totals, List<CalculationRow> rows) {
    }

    public record TotalValue(String label, String value) {
    }

    public record CalculationRow(String metric, String formula, String substitution, String result) {
    }
}
