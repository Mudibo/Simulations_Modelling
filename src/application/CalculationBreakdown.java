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
        int totalIdleTime = statistics.getTotalIdleTime();
        int totalSimulationTime = statistics.getTotalSimulationTime();

        int sumIat = sumInterArrivalTime(customers);
        int sumService = sumServiceTime(customers);
        int sumWaiting = sumWaitingTime(customers);
        int sumTimeInSystem = sumTimeInSystem(customers);

        List<TotalValue> totals = List.of(
                new TotalValue("ΣIAT", String.valueOf(sumIat)),
                new TotalValue("ΣS", String.valueOf(sumService)),
                new TotalValue("ΣWq", String.valueOf(sumWaiting)),
                new TotalValue("ΣTs", String.valueOf(sumTimeInSystem)),
                new TotalValue("Customers Waited", String.valueOf(customersWaited)),
                new TotalValue("Total Idle Time", String.valueOf(totalIdleTime)),
                new TotalValue("Total Simulation Time", String.valueOf(totalSimulationTime))
        );

        List<CalculationRow> rows = new ArrayList<>();
        rows.add(new CalculationRow(
                "Average Waiting Time",
                "μ(Wq) = ΣWq / N",
                sumWaiting + " / " + totalCustomers,
                formatWithUnit(statistics.getAverageWaitingTime(), "min")));

        rows.add(new CalculationRow(
                "Probability Customer Waits",
                "P(Wq > 0) = Customers Waited / N",
                customersWaited + " / " + totalCustomers,
                twoDecimals.format(statistics.getProbabilityCustomerWaits())));

        rows.add(new CalculationRow(
                "Proportion Server Idle",
                "P(Idle) = Total Idle Time / Total Simulation Time",
                totalIdleTime + " / " + totalSimulationTime,
                twoDecimals.format(statistics.getProportionServerIdle())));

        rows.add(new CalculationRow(
                "Probability Server Busy",
                "ρ = ΣS / Total Simulation Time",
                sumService + " / " + totalSimulationTime,
                twoDecimals.format(statistics.getProbabilityServerBusy())));

        rows.add(new CalculationRow(
                "Average Service Time",
                "μ(S) = ΣS / N",
                sumService + " / " + totalCustomers,
                formatWithUnit(statistics.getAverageServiceTime(), "min")));

        rows.add(new CalculationRow(
                "Average Waiting Time (Customers Who Waited)",
                "μ(Wq | Wq > 0) = ΣWq / Customers Waited",
                sumWaiting + " / " + customersWaited,
                formatWithUnit(statistics.getAverageWaitingTimeForThoseWhoWait(), "min")));

        rows.add(new CalculationRow(
                "Average Inter-Arrival Time",
                "μ(IAT) = ΣIAT / N",
                sumIat + " / " + totalCustomers,
                formatWithUnit(statistics.getAverageTimeBetweenArrivals(), "min")));

        rows.add(new CalculationRow(
                "Average Time In System",
                "μ(Ts) = ΣTs / N",
                sumTimeInSystem + " / " + totalCustomers,
                formatWithUnit(statistics.getAverageTimeSpentInSystem(), "min")));

        return new CalculationData(totals, rows);
    }

    private String formatWithUnit(double value, String unit) {
        return twoDecimals.format(value) + " " + unit;
    }

    private int sumInterArrivalTime(List<Customer> customers) {
        int total = 0;
        for (Customer customer : customers) {
            total += customer.getInterArrivalTime();
        }
        return total;
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
