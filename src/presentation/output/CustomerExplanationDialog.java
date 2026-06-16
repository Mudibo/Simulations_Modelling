package presentation.output;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

/**
 * Modal dialog that displays one selected customer's detailed explanation.
 */
public class CustomerExplanationDialog extends JDialog {
    private static final int DIALOG_WIDTH = 760;
    private static final int DIALOG_HEIGHT = 560;

    public CustomerExplanationDialog(OutputFrame parent, String explanationText) {
        super(parent, "Customer Explanation", true);
        initializeUi(explanationText, parent);
    }

    private void initializeUi(String explanationText, OutputFrame parent) {
        setLayout(new BorderLayout(10, 10));
        setPreferredSize(new Dimension(DIALOG_WIDTH, DIALOG_HEIGHT));

        JTextArea contentArea = new JTextArea();
        contentArea.setEditable(false);
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        contentArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        contentArea.setText(explanationText);
        contentArea.setCaretPosition(0);

        JScrollPane scrollPane = new JScrollPane(contentArea);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(event -> dispose());
        buttonPanel.add(closeButton);
        add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(parent);
    }
}