package domain.model;

import java.util.Objects;

/**
 * Represents one customer in the single-server queue simulation.
 */
public class Customer {
    private int customerNumber;
    private int interArrivalTime;
    private int arrivalTime;
    private int serviceTime;
    private int serviceStartTime;
    private int serviceEndTime;
    private int waitingTimeInQueue;
    private int timeInSystem;
    private int serverIdleTime;
    private int numberInQueue;
    private int numberInSystem;

    /**
     * Creates a customer with all simulation values.
     */
    public Customer(
            int customerNumber,
            int interArrivalTime,
            int arrivalTime,
            int serviceTime,
            int serviceStartTime,
            int serviceEndTime,
            int waitingTimeInQueue,
            int timeInSystem,
            int serverIdleTime,
            int numberInQueue,
            int numberInSystem) {
        this.customerNumber = customerNumber;
        this.interArrivalTime = interArrivalTime;
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
        this.serviceStartTime = serviceStartTime;
        this.serviceEndTime = serviceEndTime;
        this.waitingTimeInQueue = waitingTimeInQueue;
        this.timeInSystem = timeInSystem;
        this.serverIdleTime = serverIdleTime;
        this.numberInQueue = numberInQueue;
        this.numberInSystem = numberInSystem;
    }

    public int getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(int customerNumber) {
        this.customerNumber = customerNumber;
    }

    public int getInterArrivalTime() {
        return interArrivalTime;
    }

    public void setInterArrivalTime(int interArrivalTime) {
        this.interArrivalTime = interArrivalTime;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(int serviceTime) {
        this.serviceTime = serviceTime;
    }

    public int getServiceStartTime() {
        return serviceStartTime;
    }

    public void setServiceStartTime(int serviceStartTime) {
        this.serviceStartTime = serviceStartTime;
    }

    public int getServiceEndTime() {
        return serviceEndTime;
    }

    public void setServiceEndTime(int serviceEndTime) {
        this.serviceEndTime = serviceEndTime;
    }

    public int getWaitingTimeInQueue() {
        return waitingTimeInQueue;
    }

    public void setWaitingTimeInQueue(int waitingTimeInQueue) {
        this.waitingTimeInQueue = waitingTimeInQueue;
    }

    public int getTimeInSystem() {
        return timeInSystem;
    }

    public void setTimeInSystem(int timeInSystem) {
        this.timeInSystem = timeInSystem;
    }

    public int getServerIdleTime() {
        return serverIdleTime;
    }

    public void setServerIdleTime(int serverIdleTime) {
        this.serverIdleTime = serverIdleTime;
    }

    public int getNumberInQueue() {
        return numberInQueue;
    }

    public void setNumberInQueue(int numberInQueue) {
        this.numberInQueue = numberInQueue;
    }

    public int getNumberInSystem() {
        return numberInSystem;
    }

    public void setNumberInSystem(int numberInSystem) {
        this.numberInSystem = numberInSystem;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Customer customer)) {
            return false;
        }
        return customerNumber == customer.customerNumber
                && interArrivalTime == customer.interArrivalTime
                && arrivalTime == customer.arrivalTime
                && serviceTime == customer.serviceTime
                && serviceStartTime == customer.serviceStartTime
                && serviceEndTime == customer.serviceEndTime
                && waitingTimeInQueue == customer.waitingTimeInQueue
                && timeInSystem == customer.timeInSystem
                && serverIdleTime == customer.serverIdleTime
                && numberInQueue == customer.numberInQueue
                && numberInSystem == customer.numberInSystem;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                customerNumber,
                interArrivalTime,
                arrivalTime,
                serviceTime,
                serviceStartTime,
                serviceEndTime,
                waitingTimeInQueue,
                timeInSystem,
                serverIdleTime,
                numberInQueue,
                numberInSystem);
    }

    @Override
    public String toString() {
        return "Customer{"
                + "customerNumber=" + customerNumber
                + ", interArrivalTime=" + interArrivalTime
                + ", arrivalTime=" + arrivalTime
                + ", serviceTime=" + serviceTime
                + ", serviceStartTime=" + serviceStartTime
                + ", serviceEndTime=" + serviceEndTime
                + ", waitingTimeInQueue=" + waitingTimeInQueue
                + ", timeInSystem=" + timeInSystem
                + ", serverIdleTime=" + serverIdleTime
                + ", numberInQueue=" + numberInQueue
                + ", numberInSystem=" + numberInSystem
                + '}';
    }
}
