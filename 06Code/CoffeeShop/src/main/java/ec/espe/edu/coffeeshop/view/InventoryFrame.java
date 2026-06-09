package ec.espe.edu.coffeeshop.view;

import ec.espe.edu.coffeeshop.reports.InventoryReport;
import ec.espe.edu.coffeeshop.reports.Report;
import ec.espe.edu.coffeeshop.reports.SalesReport;
import ec.espe.edu.coffeeshop.reports.ZReport;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Administrative Backoffice Frame for Inventory, Equipment, and Reports.
 * 
 * @author Kevin Albán, MKA Programmer, @ESPE
 */
public class InventoryFrame extends JFrame {

    public InventoryFrame() {
        setTitle("Inventory & Control Backoffice");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(28, 25, 23)); // Stone 900
        mainPanel.setBorder(new EmptyBorder(30, 40, 30, 40));
        setContentPane(mainPanel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;

        JLabel titleLabel = new JLabel("BACKOFFICE CONTROL", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(new Color(245, 158, 11)); // Amber 500
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;

        // Inventory Button
        JButton inventoryBtn = createStyledButton("MANAGE INVENTORY");
        mainPanel.add(inventoryBtn, gbc);

        // Equipment Button
        gbc.gridx = 1;
        JButton equipmentBtn = createStyledButton("MONITOR EQUIPMENT");
        mainPanel.add(equipmentBtn, gbc);

        // Reports Section
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        JLabel reportsLabel = new JLabel("GENERATIONAL REPORTS", JLabel.CENTER);
        reportsLabel.setForeground(new Color(168, 162, 158)); // Stone 400
        reportsLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        mainPanel.add(reportsLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 3;
        
        JButton zReportBtn = createStyledButton("Z-REPORT (SALES)");
        zReportBtn.addActionListener(e -> runReport(new ZReport()));
        mainPanel.add(zReportBtn, gbc);

        gbc.gridx = 1;
        JButton invReportBtn = createStyledButton("INVENTORY REPORT");
        invReportBtn.addActionListener(e -> runReport(new InventoryReport()));
        mainPanel.add(invReportBtn, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        JButton salesReportBtn = createStyledButton("BEST-SELLING PRODUCTS REPORT");
        salesReportBtn.addActionListener(e -> runReport(new SalesReport()));
        mainPanel.add(salesReportBtn, gbc);
    }

    private JButton createStyledButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(new Color(41, 37, 36)); // Stone 800
        btn.setForeground(new Color(231, 229, 228)); // Stone 200
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(68, 64, 60), 1),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover effect
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(68, 64, 60));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(41, 37, 36));
            }
        });
        
        return btn;
    }

    private void runReport(Report report) {
        // For now, reports output to console as per Step 6 implementation
        report.generateReport();
        JOptionPane.showMessageDialog(this, "Report generated in console output.", "Success", JOptionPane.INFORMATION_MESSAGE);
    }
}
