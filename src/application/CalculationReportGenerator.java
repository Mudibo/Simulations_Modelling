package application;

import domain.model.Customer;
import domain.model.SimulationResult;
import domain.model.SimulationStatistics;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Builds a detailed calculation report for queue characteristics.
 */
public class CalculationReportGenerator {
    private static final String DOUBLE_NEWLINE = "\n\n";

    private final DecimalFormat twoDecimals = new DecimalFormat("0.00");
    private final DecimalFormat threeDecimals = new DecimalFormat("0.000");
    private final DecimalFormat oneDecimal = new DecimalFormat("0.0");

    /**
     * Generates a full plain-text report with totals, formulas, substitutions, and answers.
     */
    public String generateReport(SimulationResult result) {
        List<Customer> customers = result.customers();
        SimulationStatistics statistics = result.statistics();

        int totalInterArrivalTime = sumInterArrivalTime(customers);
        int totalServiceTime = sumServiceTime(customers);
        int totalWaitingTime = sumWaitingTime(customers);
        int totalTimeInSystem = sumTimeInSystem(customers);
        int totalIdleTime = statistics.getTotalIdleTime();
        int customersWhoWaited = statistics.getCustomersWhoWaited();
        int totalCustomers = statistics.getTotalCustomers();
        int totalSimulationTime = statistics.getTotalSimulationTime();

        StringBuilder report = new StringBuilder();
        report.append("CALCULATION BREAKDOWN").append(DOUBLE_NEWLINE);
        report.append("Totals are generated from actual simulation values.").append(DOUBLE_NEWLINE);

        appendSummationSection(report, "Total Inter Arrival Time", toInterArrivalTerms(customers), totalInterArrivalTime);
        appendSummationSection(report, "Total Service Time", toServiceTerms(customers), totalServiceTime);
        appendSummationSection(report, "Total Waiting Time", toWaitingTerms(customers), totalWaitingTime);
        appendSummationSection(report, "Total Time In System", toTimeInSystemTerms(customers), totalTimeInSystem);
        appendSummationSection(report, "Total Idle Time", toIdleTerms(customers), totalIdleTime);

        report.append("QUEUE CHARACTERISTIC CALCULATIONS").append(DOUBLE_NEWLINE);

        appendMetricSection(
                report,
                "Average Waiting Time",
                "Sigma Waiting Time / N",
                totalWaitingTime + " / " + totalCustomers,
                twoDecimals.format(statistics.getAverageWaitingTime()) + " minutes");

        appendMetricSection(
                report,
                "Probability Customer Waits",
                "Customers Who Waited / Total Customers",
                customersWhoWaited + " / " + totalCustomers,
                threeDecimals.format(statistics.getProbabilityCustomerWaits())
                        + " ("
                        + oneDecimal.format(statistics.getProbabilityCustomerWaits() * 100)
                        + "%)");

        appendMetricSection(
                report,
                "Proportion Server Idle",
                "Total Idle Time / Total Simulation Time",
                totalIdleTime + " / " + totalSimulationTime,
                threeDecimals.format(statistics.getProportionServerIdle())
                        + " ("
                        + oneDecimal.format(statistics.getProportionServerIdle() * 100)
                        + "%)");

        appendMetricSection(
                report,
                "Probability Server Busy",
                "Total Service Time / Total Simulation Time",
                totalServiceTime + " / " + totalSimulationTime,
                threeDecimals.format(statistics.getProbabilityServerBusy())
                        + " ("
                        + oneDecimal.format(statistics.getProbabilityServerBusy() * 100)
                        + "%)");

        appendMetricSection(
                report,
                "Average Service Time",
                "Sigma Service Times / N",
                totalServiceTime + " / " + totalCustomers,
                twoDecimals.format(statistics.getAverageServiceTime()) + " minutes");

        String avgWaitWhoWaitedSubstitution = customersWhoWaited == 0
                ? "0 / 0"
                : totalWaitingTime + " / " + customersWhoWaited;
        appendMetricSection(
                report,
                "Average Waiting Time For Customers Who Waited",
                "Total Waiting Time / Customers Who Waited",
                avgWaitWhoWaitedSubstitution,
                twoDecimals.format(statistics.getAverageWaitingTimeForThoseWhoWait()) + " minutes");

        appendMetricSection(
                report,
                "Average Time Between Arrivals",
                "Sigma Inter Arrival Times / N",
                totalInterArrivalTime + " / " + totalCustomers,
                twoDecimals.format(statistics.getAverageTimeBetweenArrivals()) + " minutes");

        appendMetricSection(
                report,
                "Average Time Spent In System",
                "Sigma Time In System / N",
                totalTimeInSystem + " / " + totalCustomers,
                twoDecimals.format(statistics.getAverageTimeSpentInSystem()) + " minutes");

        return report.toString();
    }

    private void appendSummationSection(StringBuilder report, String title, List<Integer> terms, int total) {
        report.append(title).append("\n");
        report.append("= ").append(joinTerms(terms)).append("\n");
        report.append("= ").append(total).append(DOUBLE_NEWLINE);
    }

    private void appendMetricSection(
            StringBuilder report,
            String title,
            String formula,
            String substitution,
            String answer) {
        report.append(title).append("\n");
        report.append("Formula: ").append(formula).append("\n");
        report.append("Substitution: ").append(substitution).append("\n");
        report.append("Answer: ").append(answer).append(DOUBLE_NEWLINE);
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

    private List<Integer> toInterArrivalTerms(List<Customer> customers) {
        return customers.stream().map(Customer::getInterArrivalTime).toList();
    }

    private List<Integer> toServiceTerms(List<Customer> customers) {
        return customers.stream().map(Customer::getServiceTime).toList();
    }

    private List<Integer> toWaitingTerms(List<Customer> customers) {
        return customers.stream().map(Customer::getWaitingTimeInQueue).toList();
    }

    private List<Integer> toTimeInSystemTerms(List<Customer> customers) {
        return customers.stream().map(Customer::getTimeInSystem).toList();
    }

    private List<Integer> toIdleTerms(List<Customer> customers) {
        return customers.stream().map(Customer::getServerIdleTime).toList();
    }

    private String joinTerms(List<Integer> values) {
        if (values.isEmpty()) {
            return "0";
        }

        StringBuilder builder = new StringBuilder();
        for (int index = 0; index < values.size(); index++) {
            if (index > 0) {
                builder.append(" + ");
            }
            builder.append(values.get(index));
        }
        return builder.toString();
    }
}