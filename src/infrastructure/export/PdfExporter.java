package infrastructure.export;

import application.CalculationBreakdown;
import domain.model.Customer;
import domain.model.SimulationResult;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Exports simulation customer rows to a simple PDF report.
 */
public class PdfExporter {
    private static final float MARGIN = 50f;
    private static final float START_Y = 780f;
    private static final float LINE_HEIGHT = 14f;

    private static final DateTimeFormatter STAMP_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void exportSimulationReport(SimulationResult result, File destination) throws IOException {
        List<Customer> customers = result.customers();
        CalculationBreakdown.CalculationData breakdownData = new CalculationBreakdown().build(result);

        try (PDDocument document = new PDDocument()) {
            PDFont sansFont = loadFirstAvailableFont(document,
                    "C:/Windows/Fonts/segoeui.ttf",
                    "C:/Windows/Fonts/arial.ttf",
                    "C:/Windows/Fonts/calibri.ttf");
            PDFont monoFont = loadFirstAvailableFont(document,
                    "C:/Windows/Fonts/consola.ttf",
                    "C:/Windows/Fonts/lucon.ttf",
                    "C:/Windows/Fonts/cour.ttf");

            if (sansFont == null) {
                sansFont = PDType1Font.HELVETICA;
            }
            if (monoFont == null) {
                monoFont = PDType1Font.COURIER;
            }

            PDPage page = new PDPage();
            document.addPage(page);

            PDPageContentStream content = new PDPageContentStream(document, page);

            float y = START_Y;
            y = writeHeading(content, y, "Bank Queue Simulation Dashboard Report", sansFont);
            y = writeLine(content, y, "Generated: " + LocalDateTime.now().format(STAMP_FORMAT), 11, sansFont);
            y = writeLine(content, y, "Total customers: " + customers.size(), 11, sansFont);
            y -= 4;

            y = writeLine(content, y, "Totals Used", 12, sansFont);
            for (CalculationBreakdown.TotalValue total : breakdownData.totals()) {
                if (y < 80) {
                    content.close();
                    page = new PDPage();
                    document.addPage(page);
                    content = new PDPageContentStream(document, page);
                    y = START_Y;
                }
                y = writeLine(content, y, total.label() + " = " + total.value(), 11, monoFont);
            }

            y -= 4;
            y = writeLine(content, y, "Calculation Breakdown", 12, sansFont);

            String calcHeader = String.format("%-36s %-30s %-24s %-12s", "Metric", "Formula", "Substitution", "Result");
            y = writeLine(content, y, calcHeader, 9, monoFont);

            for (CalculationBreakdown.CalculationRow row : breakdownData.rows()) {
                if (y < 90) {
                    content.close();
                    page = new PDPage();
                    document.addPage(page);
                    content = new PDPageContentStream(document, page);
                    y = START_Y;
                    y = writeLine(content, y, calcHeader, 9, monoFont);
                }

                y = writeWrappedCalcRow(content, y, row, monoFont);
            }

            y -= 4;
            y = writeLine(content, y, "Customer Results (Excerpt)", 12, sansFont);

            String header = String.format("%-9s %-4s %-4s %-4s %-7s %-7s %-5s %-5s %-4s",
                    "Customer", "IAT", "Arr", "Svc", "Start", "End", "Wq", "Ts", "Q");
                y = writeLine(content, y, header, 10, monoFont);

            for (Customer customer : customers) {
                if (y < 70) {
                    content.close();
                    page = new PDPage();
                    document.addPage(page);
                    content = new PDPageContentStream(document, page);
                    y = START_Y;
                    y = writeLine(content, y, header, 10, monoFont);
                }

                String row = String.format("%-9d %-4d %-4d %-4d %-7d %-7d %-5d %-5d %-4d",
                        customer.getCustomerNumber(),
                        customer.getInterArrivalTime(),
                        customer.getArrivalTime(),
                        customer.getServiceTime(),
                        customer.getServiceStartTime(),
                        customer.getServiceEndTime(),
                        customer.getWaitingTimeInQueue(),
                        customer.getTimeInSystem(),
                        customer.getNumberInQueue());
                y = writeLine(content, y, row, 10, monoFont);
            }

            content.close();
            document.save(destination);
        }
    }

    private float writeWrappedCalcRow(PDPageContentStream content, float y, CalculationBreakdown.CalculationRow row, PDFont font) throws IOException {
        String metric = truncate(row.metric(), 36);
        String formula = truncate(row.formula(), 30);
        String substitution = truncate(row.substitution(), 24);
        String result = truncate(row.result(), 12);

        String line = String.format("%-36s %-30s %-24s %-12s", metric, formula, substitution, result);
        return writeLine(content, y, line, 9, font);
    }

    private String truncate(String value, int maxLength) {
        if (value == null) {
            return "";
        }
        if (value.length() <= maxLength) {
            return value;
        }
        return value.substring(0, Math.max(0, maxLength - 3)) + "...";
    }

    private float writeHeading(PDPageContentStream content, float y, String text, PDFont font) throws IOException {
        return writeLine(content, y, text, 15, font);
    }

    private float writeLine(PDPageContentStream content, float y, String text, int size, PDFont font) throws IOException {
        content.beginText();
        content.setFont(font, size);
        content.newLineAtOffset(MARGIN, y);
        content.showText(sanitizeForFont(text, font));
        content.endText();
        return y - LINE_HEIGHT;
    }

    private PDFont loadFirstAvailableFont(PDDocument document, String... candidatePaths) {
        for (String path : candidatePaths) {
            try {
                File file = new File(path);
                if (file.exists()) {
                    return PDType0Font.load(document, file);
                }
            } catch (IOException ignored) {
                // Try next candidate.
            }
        }
        return null;
    }

    private String sanitizeForFont(String value, PDFont font) {
        if (!(font instanceof PDType1Font)) {
            return value;
        }

        return value
                .replace("Σ", "Sigma")
                .replace("μ", "mu")
                .replace("−", "-")
                .replace("≤", "<=")
                .replace("≥", ">=")
                .replace("…", "...");
    }
}
