package application;

import domain.model.Customer;
import domain.model.SimulationConfig;
import domain.model.SimulationResult;
import domain.service.QueueSimulationService;
import infrastructure.export.CsvExporter;
import presentation.input.InputFrame;
import presentation.output.OutputFrame;
import util.AppBranding;
import util.ValidationUtil;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Coordinates user actions between UI and simulation services.
 */
public class SimulationController {
    private static final String APP_TITLE = AppBranding.APP_TITLE;

    private final QueueSimulationService queueSimulationService;
    private final CsvExporter csvExporter;

    public SimulationController(QueueSimulationService queueSimulationService, CsvExporter csvExporter) {
        this.queueSimulationService = queueSimulationService;
        this.csvExporter = csvExporter;
    }

    public void runSimulation(
            InputFrame sourceFrame,
            String customers,
            String minInterArrival,
            String maxInterArrival,
            String minService,
            String maxService) {
        try {
            int numberOfCustomers = ValidationUtil.parseRequiredInteger("Number of Customers", customers);
            int minInterArrivalTime = ValidationUtil.parseRequiredInteger("Minimum Inter-Arrival Time", minInterArrival);
            int maxInterArrivalTime = ValidationUtil.parseRequiredInteger("Maximum Inter-Arrival Time", maxInterArrival);
            int minServiceTime = ValidationUtil.parseRequiredInteger("Minimum Service Time", minService);
            int maxServiceTime = ValidationUtil.parseRequiredInteger("Maximum Service Time", maxService);

            ValidationUtil.validateSimulationInputs(
                    numberOfCustomers,
                    minInterArrivalTime,
                    maxInterArrivalTime,
                    minServiceTime,
                    maxServiceTime);

            SimulationConfig config = new SimulationConfig(
                    numberOfCustomers,
                    minInterArrivalTime,
                    maxInterArrivalTime,
                    minServiceTime,
                    maxServiceTime);

            SimulationResult result = queueSimulationService.runSimulation(config);
            OutputFrame outputFrame = new OutputFrame(this, result);
            outputFrame.setLocationRelativeTo(sourceFrame);
            outputFrame.setVisible(true);
            sourceFrame.dispose();
        } catch (IllegalArgumentException exception) {
            JOptionPane.showMessageDialog(sourceFrame, exception.getMessage(), "Validation Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(
                    sourceFrame,
                    "Unexpected error while running simulation: " + exception.getMessage(),
                    "Simulation Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void exportResults(JFrame parent, List<Customer> customers) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Simulation Results");
        fileChooser.setFileFilter(new FileNameExtensionFilter("CSV Files (*.csv)", "csv"));

        int option = fileChooser.showSaveDialog(parent);
        if (option != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File selectedFile = withCsvExtension(fileChooser.getSelectedFile());

        try {
            csvExporter.exportCustomers(customers, selectedFile);
            JOptionPane.showMessageDialog(
                    parent,
                    "✓ Results exported successfully",
                    APP_TITLE,
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException exception) {
            JOptionPane.showMessageDialog(
                    parent,
                    "Failed to export results: " + exception.getMessage(),
                    "Export Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void runNewSimulation(JFrame currentOutputFrame) {
        InputFrame inputFrame = new InputFrame(this);
        inputFrame.setLocationRelativeTo(currentOutputFrame);
        inputFrame.setVisible(true);
        currentOutputFrame.dispose();
    }

    private File withCsvExtension(File file) {
        if (file.getName().toLowerCase().endsWith(".csv")) {
            return file;
        }
        return new File(file.getParentFile(), file.getName() + ".csv");
    }
}
