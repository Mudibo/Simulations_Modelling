package presentation.components;

import util.AppBranding;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Dashboard header panel with title, completion status, and generation timestamp.
 */
public class DashboardHeaderPanel extends JPanel {
    private static final DateTimeFormatter TIMESTAMP_FORMAT =
            DateTimeFormatter.ofPattern("dd MMMM yyyy hh:mm a", Locale.ENGLISH);

    public DashboardHeaderPanel(LocalDateTime generatedAt) {
        setLayout(new BorderLayout(0, 8));
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(0, 0, 6, 0));

        JLabel titleLabel = new JLabel(AppBranding.APP_TITLE);
        titleLabel.setFont(AppBranding.TITLE_FONT);
        titleLabel.setForeground(new Color(28, 44, 67));

        JLabel statusLabel = new JLabel("Simulation Completed Successfully");
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        statusLabel.setForeground(new Color(36, 114, 66));

        JLabel generatedLabel = new JLabel("Generated: " + generatedAt.format(TIMESTAMP_FORMAT));
        generatedLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        generatedLabel.setForeground(new Color(83, 99, 120));

        JPanel subtitlePanel = new JPanel(new BorderLayout(0, 4));
        subtitlePanel.setOpaque(false);
        subtitlePanel.add(statusLabel, BorderLayout.NORTH);
        subtitlePanel.add(generatedLabel, BorderLayout.SOUTH);

        add(titleLabel, BorderLayout.NORTH);
        add(subtitlePanel, BorderLayout.SOUTH);
    }
}
