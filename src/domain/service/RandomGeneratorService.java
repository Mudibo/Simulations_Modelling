package domain.service;

/**
 * Contract for integer random number generation.
 */
public interface RandomGeneratorService {
    /**
     * Generates a uniformly distributed integer between min and max, inclusive.
     */
    int nextIntInclusive(int min, int max);
}
