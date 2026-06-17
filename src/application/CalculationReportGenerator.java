package application;

import domain.model.Customer;
import domain.model.SimulationResult;
import domain.model.SimulationStatistics;
import util.FormulaFormatter;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Builds a detailed calculation report for queue characteristics.
 */
public class CalculationReportGenerator {
    private final DecimalFormat twoDecimals = new DecimalFormat("0.00");
    private final DecimalFormat probabilityDecimals = new DecimalFormat("0.###");

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
        FormulaFormatter.appendReportHeader(report);

        report.append("SUMMATION DISPLAY\n\n");
        FormulaFormatter.appendSummation(report, "ΣIAT", toInterArrivalTerms(customers), totalInterArrivalTime);
        FormulaFormatter.appendSummation(report, "ΣS", toServiceTerms(customers), totalServiceTime);
        FormulaFormatter.appendSummation(report, "ΣWq", toWaitingTerms(customers), totalWaitingTime);
        FormulaFormatter.appendSummation(report, "ΣTs", toTimeInSystemTerms(customers), totalTimeInSystem);
        FormulaFormatter.appendSummation(report, "Σ Idle Time", toIdleTerms(customers), totalIdleTime);

        report.append("QUEUE CHARACTERISTIC CALCULATIONS\n\n");

        FormulaFormatter.appendMetric(
                report,
                "Average Waiting Time",
                "μ(Wq)",
                "ΣWq",
                "N",
                totalWaitingTime,
                totalCustomers,
                twoDecimals.format(statistics.getAverageWaitingTime()) + " minutes");

        FormulaFormatter.appendMetric(
                report,
                "Probability Customer Waits",
                "P(Wq > 0)",
                "Number Waiting",
                "N",
                customersWhoWaited,
                totalCustomers,
                probabilityDecimals.format(statistics.getProbabilityCustomerWaits()));

        FormulaFormatter.appendMetric(
                report,
                "Proportion Server Idle",
                "P(Idle)",
                "Σ Idle Time",
                "Simulation Time",
                totalIdleTime,
                totalSimulationTime,
                probabilityDecimals.format(statistics.getProportionServerIdle()));

        FormulaFormatter.appendMetric(
                report,
                "Probability Server Busy",
                "ρ",
                "Σ Service Time",
                "Simulation Time",
                totalServiceTime,
                totalSimulationTime,
                probabilityDecimals.format(statistics.getProbabilityServerBusy()));

        FormulaFormatter.appendMetric(
                report,
                "Average Service Time",
                "μ(S)",
                "ΣS",
                "N",
                totalServiceTime,
                totalCustomers,
                twoDecimals.format(statistics.getAverageServiceTime()) + " minutes");

        FormulaFormatter.appendMetric(
                report,
                "Average Waiting Time of Customers Who Waited",
                "μ(Wq | Wq > 0)",
                "ΣWq",
                "Customers Waited",
                totalWaitingTime,
                customersWhoWaited,
                twoDecimals.format(statistics.getAverageWaitingTimeForThoseWhoWait()) + " minutes");

        FormulaFormatter.appendMetric(
                report,
                "Average Time in System",
                "μ(Ts)",
                "ΣTs",
                "N",
                totalTimeInSystem,
                totalCustomers,
                twoDecimals.format(statistics.getAverageTimeSpentInSystem()) + " minutes");

        return report.toString();
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
}
