package ec.espe.edu.coffeeshop.view;

import ec.espe.edu.coffeeshop.reports.Report;
import ec.espe.edu.coffeeshop.utils.I18nHelper;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.io.PrintWriter;

/**
 * Custom dialog to view generated reports in a mono-spaced text area (Light & Gold Theme).
 * Includes options to export the report to a physical text file.
 * 
 * @author MKA Programmers, ESPE
 */
public class ReportViewerDialog extends JDialog {

    // Colors
    private final Color bgColor = new Color(250, 248, 245); // Warm White
    private final Color inputColor = new Color(255, 255, 255); // Pure White
    private final Color textColor = new Color(41, 37, 36); // Stone 800
    private final Color goldPrimary = new Color(197, 160, 89); // Gold
    private final Color goldSecondary = new Color(163, 128, 62); // Dark Gold
    private final Color borderColor = new Color(224, 220, 214); // Sand Border

    public ReportViewerDialog(Frame parent, String title, Report report) {
        super(parent, title, true);
        setSize(600, 450);
        setLocationRelativeTo(parent);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(bgColor);
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        setContentPane(mainPanel);

        // Header
        String reportTitle = I18nHelper.getLocale().getLanguage().equals("es") ? "VISTA DE REPORTE: " : "REPORT VIEW: ";
        JLabel headerLabel = new JLabel(reportTitle + title.toUpperCase(), JLabel.CENTER);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        headerLabel.setForeground(goldSecondary);
        mainPanel.add(headerLabel, BorderLayout.NORTH);

        // Report Text Area
        String reportText = report.getReportText();
        JTextArea txtReport = new JTextArea(reportText);
        txtReport.setEditable(false);
        txtReport.setFont(new Font("Consolas", Font.PLAIN, 12));
        txtReport.setBackground(inputColor);
        txtReport.setForeground(textColor);
        txtReport.setCaretColor(textColor);
        txtReport.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(borderColor),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JScrollPane scrollPane = new JScrollPane(txtReport);
        scrollPane.setBorder(null);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Footer Actions
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(bgColor);
        buttonPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

        String exportTxt = I18nHelper.getLocale().getLanguage().equals("es") ? "EXPORTAR A TXT" : "EXPORT TO FILE";
        JButton btnExport = new JButton(exportTxt);
        btnExport.setBackground(goldPrimary);
        btnExport.setForeground(Color.WHITE);
        btnExport.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnExport.setFocusPainted(false);
        btnExport.setBorderPainted(false);
        btnExport.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        btnExport.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnExport.addActionListener(e -> exportToFile(title, reportText));
        buttonPanel.add(btnExport);

        String closeTxt = I18nHelper.getLocale().getLanguage().equals("es") ? "CERRAR" : "CLOSE";
        JButton btnClose = new JButton(closeTxt);
        btnClose.setBackground(new Color(120, 113, 108)); // Muted Gray
        btnClose.setForeground(Color.WHITE);
        btnClose.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnClose.setFocusPainted(false);
        btnClose.setBorderPainted(false);
        btnClose.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        btnClose.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnClose.addActionListener(e -> dispose());
        buttonPanel.add(btnClose);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void exportToFile(String title, String content) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new File(title.toLowerCase().replace(" ", "_") + ".txt"));
        int option = fileChooser.showSaveDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (PrintWriter out = new PrintWriter(file)) {
                out.println(content);
                String msg = I18nHelper.getLocale().getLanguage().equals("es") ? "Reporte exportado con éxito a:\n" : "Report exported successfully to:\n";
                JOptionPane.showMessageDialog(this, msg + file.getAbsolutePath(), I18nHelper.getMessage("eq.success_title"), JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                String errMsg = I18nHelper.getLocale().getLanguage().equals("es") ? "Error exportando reporte: " : "Error exporting report: ";
                JOptionPane.showMessageDialog(this, errMsg + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
