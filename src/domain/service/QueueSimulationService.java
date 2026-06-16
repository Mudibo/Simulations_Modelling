package domain.service;

import domain.model.SimulationConfig;
import domain.model.SimulationResult;

/**
 * Runs a single-server queue simulation using the given input configuration.
 */
public interface QueueSimulationService {
    /**
     * Executes a full simulation run.
     */
    SimulationResult runSimulation(SimulationConfig config);
}
