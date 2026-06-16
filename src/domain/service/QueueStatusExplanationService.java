package domain.service;

import domain.model.Customer;

import java.util.List;

/**
 * Generates readable queue status explanations for customer-level results.
 */
public class QueueStatusExplanationService {
    /**
     * Builds an explanation for one customer using current and previous customer state.
     */
    public String explainCustomerJourney(List<Customer> customers, int selectedIndex) {
        if (customers == null || customers.isEmpty()) {
            return "No customer results available.";
        }

        if (selectedIndex < 0 || selectedIndex >= customers.size()) {
            return "Select a customer row in the Customer Results table to view detailed queue status.";
        }

        Customer current = customers.get(selectedIndex);
        Customer previous = selectedIndex > 0 ? customers.get(selectedIndex - 1) : null;
        int numberBeingServed = current.getNumberInSystem() - current.getNumberInQueue();

        StringBuilder explanation = new StringBuilder();
        explanation.append("Customer Number: ").append(current.getCustomerNumber()).append("\n\n");

        explanation.append("Arrival Information").append("\n")
            .append("- Inter-Arrival Time: ").append(current.getInterArrivalTime()).append("\n")
            .append("- Arrival Time: ").append(current.getArrivalTime()).append("\n");

        if (previous != null) {
            explanation.append("- Previous Customer Service End: ").append(previous.getServiceEndTime()).append("\n");
        }

        explanation.append("\nQueue Information").append("\n");

        if (previous == null) {
            explanation.append("This is the first customer in the simulation.").append("\n")
                .append("The server was immediately available.").append("\n")
                .append("Queue Length upon arrival = 0.").append("\n");
        } else if (current.getArrivalTime() < previous.getServiceEndTime()) {
            explanation.append("The customer arrived before the previous customer completed service.").append("\n")
                .append("Therefore the customer joined the queue.").append("\n")
                .append("Queue Length upon arrival = ").append(current.getNumberInQueue()).append('.').append("\n");
        } else if (current.getArrivalTime() > previous.getServiceEndTime()) {
            int idleGap = current.getArrivalTime() - previous.getServiceEndTime();
            explanation.append("The server had already completed service before this customer arrived.").append("\n")
                .append("Queue Length upon arrival = 0.").append("\n\n")
                .append("Idle Time Calculations").append("\n")
                .append("Server Idle Time = ")
                .append(current.getArrivalTime())
                .append(" - ")
                .append(previous.getServiceEndTime())
                .append(" = ")
                .append(idleGap)
                .append(" minutes.").append("\n");
        } else {
            explanation.append("The customer arrived exactly when the previous service ended.").append("\n")
                .append("Queue Length upon arrival = 0.").append("\n");
        }

        explanation.append("Number in System upon arrival = Queue Length + Number Being Served = ")
            .append(current.getNumberInQueue())
            .append(" + ")
            .append(numberBeingServed)
            .append(" = ")
            .append(current.getNumberInSystem())
            .append(".").append("\n\n");

        explanation.append("Service Information").append("\n")
            .append("Service started at time ")
            .append(current.getServiceStartTime())
            .append(".\n")
            .append("Service time was ")
            .append(current.getServiceTime())
            .append(" minutes.\n")
            .append("Service ended at time ")
            .append(current.getServiceEndTime())
            .append(".\n\n");

        explanation.append("Waiting Time Calculations").append("\n");
        if (current.getWaitingTimeInQueue() > 0) {
            explanation.append("Waiting Time in Queue = ")
                    .append(current.getServiceStartTime())
                .append(" - ")
                .append(current.getArrivalTime())
                .append(" = ")
                .append(current.getWaitingTimeInQueue())
                .append(" minutes.\n");
        } else {
            explanation.append("Waiting Time in Queue = 0 minutes.\n");
        }

        explanation.append("\nTime in System Calculations").append("\n")
            .append("Time spent in system = ")
                .append(current.getWaitingTimeInQueue())
                .append(" + ")
                .append(current.getServiceTime())
                .append(" = ")
                .append(current.getTimeInSystem())
            .append(" minutes.");

        return explanation.toString();
    }
}