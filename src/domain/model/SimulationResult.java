package domain.model;

import java.util.List;

/**
 * Output of one simulation run.
 */
public record SimulationResult(List<Customer> customers, SimulationStatistics statistics) {
}
