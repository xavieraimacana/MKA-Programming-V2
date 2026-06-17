package ec.espe.edu.coffeeshop.view;
import ec.espe.edu.coffeeshop.controller.ReportController;
import ec.espe.edu.coffeeshop.utils.ThemeManager;
import org.bson.Document;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
public class ReportView extends JFrame {
    private final ReportController controller;
    private DefaultTableModel tableModel;
    private JTable table;
    public ReportView() {
        controller = new ReportController();
        setTitle("Reportes Financieros - Ventas");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(ThemeManager.COLOR_BACKGROUND);
        ((javax.swing.JComponent)getContentPane()).setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setLayout(new BorderLayout(10, 10));
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(ThemeManager.COLOR_WHITE);
        JLabel titleLabel = new JLabel("MÓDULO DE REPORTES FINANCIEROS");
        titleLabel.setFont(ThemeManager.TITLE_FONT);
        titleLabel.setForeground(ThemeManager.COLOR_GOLD);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        topPanel.add(titleLabel, BorderLayout.NORTH);
        add(topPanel, BorderLayout.NORTH);
        String[] columns = {"ID Pedido", "Fecha", "Estado", "Monto Total"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, BorderLayout.CENTER);
        JPanel actionPanel = new JPanel();
        actionPanel.setBackground(ThemeManager.COLOR_WHITE);
        actionPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        JButton btnRefresh = new JButton("Actualizar Datos");
        btnRefresh.setBackground(ThemeManager.COLOR_GOLD);
        btnRefresh.setForeground(Color.BLACK); 
        btnRefresh.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnRefresh.addActionListener(e -> loadTableData());
        JButton btnExport = new JButton("Exportar CSV");
        btnExport.setBackground(ThemeManager.COLOR_GOLD);
        btnExport.setForeground(Color.BLACK);
        btnExport.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnExport.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Guardar Archivo CSV");
            int userSelection = fileChooser.showSaveDialog(this);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                java.io.File fileToSave = fileChooser.getSelectedFile();
                String filePath = fileToSave.getAbsolutePath();
                if (!filePath.toLowerCase().endsWith(".csv")) {
                    filePath += ".csv";
                }
                ec.espe.edu.coffeeshop.utils.CsvExporter.exportTableToCsv(table, filePath);
                JOptionPane.showMessageDialog(this, "Exportado exitosamente a " + filePath, "Éxito al Exportar", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        actionPanel.add(btnRefresh);
        actionPanel.add(btnExport);
        add(actionPanel, BorderLayout.SOUTH);
        loadTableData();
    }
    public void loadTableData() {
        tableModel.setRowCount(0);
        List<Document> orders = controller.getSalesReport();
        for (Document order : orders) {
            tableModel.addRow(new Object[]{
                    order.getString("_id"),
                    order.getString("orderDate"),
                    order.getString("status"),
                    order.get("totalAmount") != null ? String.format("$%.2f", ((Number) order.get("totalAmount")).doubleValue()) : "$0.00"
            });
        }
    }
}
