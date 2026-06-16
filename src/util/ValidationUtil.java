package util;

/**
 * Validation helper methods for simulation inputs.
 */
public final class ValidationUtil {
    private ValidationUtil() {
    }

    public static int parseRequiredInteger(String fieldName, String rawValue) {
        if (rawValue == null || rawValue.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " is required.");
        }

        try {
            return Integer.parseInt(rawValue.trim());
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException(fieldName + " must be a valid integer.");
        }
    }

    public static void validateSimulationInputs(
            int numberOfCustomers,
            int minInterArrivalTime,
            int maxInterArrivalTime,
            int minServiceTime,
            int maxServiceTime) {
        if (numberOfCustomers <= 0) {
            throw new IllegalArgumentException("Number of Customers must be greater than 0.");
        }

        if (minInterArrivalTime < 0 || maxInterArrivalTime < 0 || minServiceTime < 0 || maxServiceTime < 0) {
            throw new IllegalArgumentException("Time values cannot be negative.");
        }

        if (minInterArrivalTime > maxInterArrivalTime) {
            throw new IllegalArgumentException("Minimum Inter-Arrival Time must be less than or equal to Maximum Inter-Arrival Time.");
        }

        if (minServiceTime > maxServiceTime) {
            throw new IllegalArgumentException("Minimum Service Time must be less than or equal to Maximum Service Time.");
        }
    }
}
