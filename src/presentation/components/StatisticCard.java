package presentation.components;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

/**
 * Reusable metric card with title and emphasized value.
 */
public class StatisticCard extends JPanel {
    public StatisticCard(String title, String value) {
        setLayout(new BorderLayout());
    setMinimumSize(new Dimension(220, 116));
    setPreferredSize(new Dimension(250, 128));
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(214, 223, 236), 1, true),
        BorderFactory.createEmptyBorder(14, 16, 14, 16)));
        setBackground(Color.WHITE);

    JPanel contentPanel = new JPanel(new GridLayout(2, 1, 0, 6));
        contentPanel.setOpaque(false);

        JLabel titleLabel = new JLabel(title);
    titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
    titleLabel.setForeground(new Color(58, 74, 96));

        JLabel valueLabel = new JLabel(value);
    valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
    valueLabel.setForeground(new Color(18, 38, 68));

        contentPanel.add(titleLabel);
        contentPanel.add(valueLabel);
        add(contentPanel, BorderLayout.CENTER);
    }
}
