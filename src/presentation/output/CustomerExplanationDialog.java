package presentation.output;

import util.AppBranding;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

/**
 * Modal dialog that displays one selected customer's detailed explanation.
 */
public class CustomerExplanationDialog extends JDialog {
    private static final int DIALOG_WIDTH = 820;
    private static final int DIALOG_HEIGHT = 600;

    public CustomerExplanationDialog(OutputFrame parent, String explanationText) {
        super(parent, "Customer Explanation", true);
        initializeUi(explanationText, parent);
    }

    private void initializeUi(String explanationText, OutputFrame parent) {
        setLayout(new BorderLayout(10, 10));
        setPreferredSize(new Dimension(DIALOG_WIDTH, DIALOG_HEIGHT));
        setIconImage(AppBranding.createAppIcon());

        JTextArea contentArea = new JTextArea();
        contentArea.setEditable(false);
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        contentArea.setBackground(new Color(250, 252, 255));
        contentArea.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 12, 10, 12));
        contentArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        contentArea.setText(explanationText);
        contentArea.setCaretPosition(0);

        JScrollPane scrollPane = new JScrollPane(contentArea);
        scrollPane.setBorder(javax.swing.BorderFactory.createTitledBorder("Customer Explanation"));
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);
        JButton closeButton = new JButton("Close");
        closeButton.setToolTipText("Close this explanation dialog");
        closeButton.setPreferredSize(new Dimension(140, 34));
        closeButton.addActionListener(event -> dispose());
        buttonPanel.add(closeButton);
        add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(parent);
        getRootPane().setDefaultButton(closeButton);
    }
}