package app;

import application.SimulationController;
import com.formdev.flatlaf.FlatLightLaf;
import domain.service.QueueSimulationService;
import domain.service.QueueSimulationServiceImpl;
import domain.service.RandomGeneratorService;
import infrastructure.export.CsvExporter;
import infrastructure.random.UniformRandomGenerator;
import presentation.input.InputFrame;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Application entry point.
 */
public final class Main {
    private Main() {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            setupLookAndFeel();

            RandomGeneratorService randomGeneratorService = new UniformRandomGenerator();
            QueueSimulationService queueSimulationService = new QueueSimulationServiceImpl(randomGeneratorService);
            SimulationController simulationController = new SimulationController(queueSimulationService, new CsvExporter());

            InputFrame inputFrame = new InputFrame(simulationController);
            inputFrame.setVisible(true);
        });
    }

    private static void setupLookAndFeel() {
        try {
            FlatLightLaf.setup();
        } catch (Throwable ignored) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception fallbackIgnored) {
                // Falls back to default look and feel.
            }
        }
    }
}
