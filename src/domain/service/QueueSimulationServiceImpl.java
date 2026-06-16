package domain.service;

import domain.model.Customer;
import domain.model.SimulationConfig;
import domain.model.SimulationResult;
import domain.model.SimulationStatistics;

import java.util.ArrayList;
import java.util.List;

/**
 * Default discrete-event implementation for a single-server bank queue.
 */
public class QueueSimulationServiceImpl implements QueueSimulationService {
    private final RandomGeneratorService randomGeneratorService;

    public QueueSimulationServiceImpl(RandomGeneratorService randomGeneratorService) {
        this.randomGeneratorService = randomGeneratorService;
    }

    @Override
    public SimulationResult runSimulation(SimulationConfig config) {
        List<Customer> customers = generateCustomers(config);
        SimulationStatistics statistics = calculateStatistics(customers);
        return new SimulationResult(customers, statistics);
    }

    private List<Customer> generateCustomers(SimulationConfig config) {
        List<Customer> customers = new ArrayList<>();

        int previousArrivalTime = 0;
        int previousServiceEndTime = 0;

        for (int customerNumber = 1; customerNumber <= config.numberOfCustomers(); customerNumber++) {
            int interArrivalTime = randomGeneratorService.nextIntInclusive(
                    config.minInterArrivalTime(),
                    config.maxInterArrivalTime());

            int arrivalTime = previousArrivalTime + interArrivalTime;
            int serviceTime = randomGeneratorService.nextIntInclusive(config.minServiceTime(), config.maxServiceTime());
            int serviceStartTime = Math.max(arrivalTime, previousServiceEndTime);
            int serviceEndTime = serviceStartTime + serviceTime;
            int waitingTimeInQueue = serviceStartTime - arrivalTime;
            int timeInSystem = waitingTimeInQueue + serviceTime;
            int serverIdleTime = customerNumber == 1 ? 0 : Math.max(0, arrivalTime - previousServiceEndTime);

            int waitingBeforeArrival = countCustomersWaitingAt(arrivalTime, customers);
            int numberInQueueAtArrival = waitingBeforeArrival + (waitingTimeInQueue > 0 ? 1 : 0);
                int numberBeingServedAtArrival = 1;
                int numberInSystemAtArrival = numberInQueueAtArrival + numberBeingServedAtArrival;

            validateCustomerMeasures(
                    waitingTimeInQueue,
                    serverIdleTime,
                    timeInSystem,
                    serviceTime,
                    numberInQueueAtArrival,
                    numberInSystemAtArrival,
                    numberBeingServedAtArrival,
                    customerNumber);

            Customer customer = new Customer(
                    customerNumber,
                    interArrivalTime,
                    arrivalTime,
                    serviceTime,
                    serviceStartTime,
                    serviceEndTime,
                    waitingTimeInQueue,
                    timeInSystem,
                    serverIdleTime,
                    numberInQueueAtArrival,
                    numberInSystemAtArrival);

            customers.add(customer);
            previousArrivalTime = arrivalTime;
            previousServiceEndTime = serviceEndTime;
        }

        return customers;
    }

    private void validateCustomerMeasures(
            int waitingTimeInQueue,
            int serverIdleTime,
            int timeInSystem,
            int serviceTime,
            int numberInQueue,
            int numberInSystem,
            int numberBeingServed,
            int customerNumber) {
        if (timeInSystem != waitingTimeInQueue + serviceTime) {
            throw new IllegalStateException("Invalid time in system for customer " + customerNumber);
        }

        if (waitingTimeInQueue > 0 && serverIdleTime > 0) {
            throw new IllegalStateException(
                    "Invalid queue state for customer " + customerNumber
                            + ": waiting time and idle time cannot both be positive.");
        }

            if (waitingTimeInQueue > 0 && numberInQueue < 1) {
                throw new IllegalStateException(
                    "Invalid queue size for customer " + customerNumber
                        + ": waiting customer must be counted in queue.");
            }

            if (numberInSystem != numberInQueue + numberBeingServed) {
                throw new IllegalStateException(
                    "Invalid system size for customer " + customerNumber
                        + ": NumberInSystem must equal QueueLength + NumberBeingServed.");
            }
    }

    private int countCustomersWaitingAt(int arrivalTime, List<Customer> priorCustomers) {
        int count = 0;
        for (Customer prior : priorCustomers) {
            if (prior.getArrivalTime() <= arrivalTime && prior.getServiceStartTime() > arrivalTime) {
                count++;
            }
        }
        return count;
    }

    private SimulationStatistics calculateStatistics(List<Customer> customers) {
        int totalCustomers = customers.size();
        int sumWaitingTime = 0;
        int sumServiceTime = 0;
        int sumInterArrivalTime = 0;
        int sumTimeInSystem = 0;
        int totalIdleTime = 0;
        int customersWhoWaited = 0;

        for (Customer customer : customers) {
            sumWaitingTime += customer.getWaitingTimeInQueue();
            sumServiceTime += customer.getServiceTime();
            sumTimeInSystem += customer.getTimeInSystem();
            totalIdleTime += customer.getServerIdleTime();
            sumInterArrivalTime += customer.getInterArrivalTime();

            if (customer.getWaitingTimeInQueue() > 0) {
                customersWhoWaited++;
            }
        }

        int totalSimulationTime = totalCustomers == 0 ? 0 : customers.get(totalCustomers - 1).getServiceEndTime();

        double averageWaitingTime = totalCustomers == 0 ? 0.0 : (double) sumWaitingTime / totalCustomers;
        double probabilityCustomerWaits = totalCustomers == 0 ? 0.0 : (double) customersWhoWaited / totalCustomers;
        double proportionServerIdle = totalSimulationTime == 0 ? 0.0 : (double) totalIdleTime / totalSimulationTime;
        double probabilityServerBusy = totalSimulationTime == 0 ? 0.0 : (double) sumServiceTime / totalSimulationTime;
        double averageServiceTime = totalCustomers == 0 ? 0.0 : (double) sumServiceTime / totalCustomers;
        double averageWaitingTimeForThoseWhoWait = customersWhoWaited == 0
                ? 0.0
                : (double) sumWaitingTime / customersWhoWaited;
        double averageTimeBetweenArrivals = totalCustomers == 0 ? 0.0 : (double) sumInterArrivalTime / totalCustomers;
        double averageTimeSpentInSystem = totalCustomers == 0 ? 0.0 : (double) sumTimeInSystem / totalCustomers;

        return new SimulationStatistics(
                averageWaitingTime,
                probabilityCustomerWaits,
                proportionServerIdle,
                probabilityServerBusy,
                averageServiceTime,
                averageWaitingTimeForThoseWhoWait,
                averageTimeBetweenArrivals,
                averageTimeSpentInSystem,
                totalCustomers,
                customersWhoWaited,
                totalIdleTime,
                totalSimulationTime);
    }
}
