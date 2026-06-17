package util;

import java.util.List;

/**
 * Formats university-style mathematical expressions for report rendering.
 */
public final class FormulaFormatter {
    private FormulaFormatter() {
    }

    public static void appendReportHeader(StringBuilder builder) {
        builder.append("CALCULATION BREAKDOWN\n\n");
        builder.append("Mathematical notation is used for all formulas and substitutions.\n\n");
        builder.append("Notation: Σ (summation), μ (mean), λ (arrival rate), ρ (utilization).\n");
        builder.append("Arrival rate identity: λ = N ÷ Simulation Time.\n");
        builder.append("Checks: 0 ≤ P(Wq > 0) ≤ 1, 0 ≤ ρ ≤ 1, N × μ(S) = ΣS, Simulation Time ≥ ΣS.\n\n");
    }

    public static void appendSummation(StringBuilder builder, String sigmaTerm, List<Integer> values, int total) {
        builder.append(sigmaTerm).append("\n\n");
        builder.append("=\n\n");
        builder.append(joinTerms(values)).append("\n\n");
        builder.append("=\n\n");
        builder.append(total).append("\n\n");
    }

    public static void appendMetric(
            StringBuilder builder,
            String title,
            String formulaLeft,
            String formulaNumerator,
            String formulaDenominator,
            int substitutionNumerator,
            int substitutionDenominator,
            String answer) {
        builder.append(title).append("\n\n");
        builder.append(formulaLeft).append("\n\n");
        builder.append("=\n\n");
        builder.append(asFraction(formulaNumerator, formulaDenominator)).append("\n\n");
        builder.append("Substitution:\n\n");
        builder.append(asFraction(String.valueOf(substitutionNumerator), String.valueOf(substitutionDenominator))).append("\n\n");
        builder.append("=\n\n");
        builder.append(answer).append("\n\n");
    }

    private static String asFraction(String numerator, String denominator) {
        int width = Math.max(numerator.length(), denominator.length());
        String normalizedNumerator = center(numerator, width);
        String normalizedDenominator = center(denominator, width);
        String line = "─".repeat(width);
        return normalizedNumerator + "\n" + line + "\n" + normalizedDenominator;
    }

    private static String center(String value, int width) {
        if (value.length() >= width) {
            return value;
        }
        int totalPadding = width - value.length();
        int leftPadding = totalPadding / 2;
        int rightPadding = totalPadding - leftPadding;
        return " ".repeat(leftPadding) + value + " ".repeat(rightPadding);
    }

    private static String joinTerms(List<Integer> values) {
        if (values.isEmpty()) {
            return "0";
        }

        if (values.size() <= 7) {
            return joinAll(values);
        }

        StringBuilder builder = new StringBuilder();
        int leadingTerms = Math.min(5, values.size());
        for (int index = 0; index < leadingTerms; index++) {
            if (index > 0) {
                builder.append(" + ");
            }
            builder.append(values.get(index));
        }

        builder.append(" + ... + ").append(values.get(values.size() - 1));
        return builder.toString();
    }

    private static String joinAll(List<Integer> values) {
        StringBuilder builder = new StringBuilder();
        for (int index = 0; index < values.size(); index++) {
            if (index > 0) {
                builder.append(" + ");
            }
            builder.append(values.get(index));
        }
        return builder.toString();
    }
}