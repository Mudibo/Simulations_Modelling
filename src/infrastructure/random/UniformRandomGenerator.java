package infrastructure.random;

import domain.service.RandomGeneratorService;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Thread-safe uniform random integer generator.
 */
public class UniformRandomGenerator implements RandomGeneratorService {
    @Override
    public int nextIntInclusive(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }
}
