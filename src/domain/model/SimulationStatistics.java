package domain.model;

/**
 * Aggregate statistics calculated after the simulation run.
 */
public class SimulationStatistics {
    private double averageWaitingTime;
    private double probabilityCustomerWaits;
    private double proportionServerIdle;
    private double probabilityServerBusy;
    private double averageServiceTime;
    private double averageWaitingTimeForThoseWhoWait;
    private double averageTimeBetweenArrivals;
    private double averageTimeSpentInSystem;

    private int totalCustomers;
    private int customersWhoWaited;
    private int totalIdleTime;
    private int totalSimulationTime;

    public SimulationStatistics() {
    }

    public SimulationStatistics(
            double averageWaitingTime,
            double probabilityCustomerWaits,
            double proportionServerIdle,
            double probabilityServerBusy,
            double averageServiceTime,
            double averageWaitingTimeForThoseWhoWait,
            double averageTimeBetweenArrivals,
            double averageTimeSpentInSystem,
            int totalCustomers,
            int customersWhoWaited,
            int totalIdleTime,
            int totalSimulationTime) {
        this.averageWaitingTime = averageWaitingTime;
        this.probabilityCustomerWaits = probabilityCustomerWaits;
        this.proportionServerIdle = proportionServerIdle;
        this.probabilityServerBusy = probabilityServerBusy;
        this.averageServiceTime = averageServiceTime;
        this.averageWaitingTimeForThoseWhoWait = averageWaitingTimeForThoseWhoWait;
        this.averageTimeBetweenArrivals = averageTimeBetweenArrivals;
        this.averageTimeSpentInSystem = averageTimeSpentInSystem;
        this.totalCustomers = totalCustomers;
        this.customersWhoWaited = customersWhoWaited;
        this.totalIdleTime = totalIdleTime;
        this.totalSimulationTime = totalSimulationTime;
    }

    public double getAverageWaitingTime() {
        return averageWaitingTime;
    }

    public void setAverageWaitingTime(double averageWaitingTime) {
        this.averageWaitingTime = averageWaitingTime;
    }

    public double getProbabilityCustomerWaits() {
        return probabilityCustomerWaits;
    }

    public void setProbabilityCustomerWaits(double probabilityCustomerWaits) {
        this.probabilityCustomerWaits = probabilityCustomerWaits;
    }

    public double getProportionServerIdle() {
        return proportionServerIdle;
    }

    public void setProportionServerIdle(double proportionServerIdle) {
        this.proportionServerIdle = proportionServerIdle;
    }

    public double getProbabilityServerBusy() {
        return probabilityServerBusy;
    }

    public void setProbabilityServerBusy(double probabilityServerBusy) {
        this.probabilityServerBusy = probabilityServerBusy;
    }

    public double getAverageServiceTime() {
        return averageServiceTime;
    }

    public void setAverageServiceTime(double averageServiceTime) {
        this.averageServiceTime = averageServiceTime;
    }

    public double getAverageWaitingTimeForThoseWhoWait() {
        return averageWaitingTimeForThoseWhoWait;
    }

    public void setAverageWaitingTimeForThoseWhoWait(double averageWaitingTimeForThoseWhoWait) {
        this.averageWaitingTimeForThoseWhoWait = averageWaitingTimeForThoseWhoWait;
    }

    public double getAverageTimeBetweenArrivals() {
        return averageTimeBetweenArrivals;
    }

    public void setAverageTimeBetweenArrivals(double averageTimeBetweenArrivals) {
        this.averageTimeBetweenArrivals = averageTimeBetweenArrivals;
    }

    public double getAverageTimeSpentInSystem() {
        return averageTimeSpentInSystem;
    }

    public void setAverageTimeSpentInSystem(double averageTimeSpentInSystem) {
        this.averageTimeSpentInSystem = averageTimeSpentInSystem;
    }

    public int getTotalCustomers() {
        return totalCustomers;
    }

    public void setTotalCustomers(int totalCustomers) {
        this.totalCustomers = totalCustomers;
    }

    public int getCustomersWhoWaited() {
        return customersWhoWaited;
    }

    public void setCustomersWhoWaited(int customersWhoWaited) {
        this.customersWhoWaited = customersWhoWaited;
    }

    public int getTotalIdleTime() {
        return totalIdleTime;
    }

    public void setTotalIdleTime(int totalIdleTime) {
        this.totalIdleTime = totalIdleTime;
    }

    public int getTotalSimulationTime() {
        return totalSimulationTime;
    }

    public void setTotalSimulationTime(int totalSimulationTime) {
        this.totalSimulationTime = totalSimulationTime;
    }
}
