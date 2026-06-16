package domain.model;

/**
 * Immutable simulation input configuration.
 */
public record SimulationConfig(
        int numberOfCustomers,
        int minInterArrivalTime,
        int maxInterArrivalTime,
        int minServiceTime,
        int maxServiceTime) {
}
