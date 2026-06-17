package ec.espe.edu.coffeeshop.view;
import ec.espe.edu.coffeeshop.controller.InvoiceController;
import ec.espe.edu.coffeeshop.model.Invoice;
import ec.espe.edu.coffeeshop.utils.ThemeManager;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
public class InvoiceView extends JFrame {
    private final InvoiceController invoiceController;
    private DefaultTableModel tableModel;
    private JTable invoiceTable;
    public InvoiceView() {
        invoiceController = new InvoiceController();
        setTitle("Facturas / Recibos Fiscales");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(ThemeManager.COLOR_BACKGROUND);
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(ThemeManager.COLOR_BACKGROUND);
        JLabel titleLabel = new JLabel("HISTORIAL DE FACTURAS");
        titleLabel.setFont(ThemeManager.TITLE_FONT);
        titleLabel.setForeground(ThemeManager.COLOR_GOLD);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        String[] cols = {"ID Factura", "ID Pedido", "Fecha de Emisión", "Subtotal", "Impuesto", "Monto Total"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        invoiceTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(invoiceTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        JButton btnRefresh = new JButton("Actualizar");
        btnRefresh.setBackground(ThemeManager.COLOR_GOLD);
        btnRefresh.setForeground(Color.BLACK);
        btnRefresh.addActionListener(e -> refreshTable());
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(ThemeManager.COLOR_BACKGROUND);
        bottomPanel.add(btnRefresh);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        add(mainPanel);
        refreshTable();
    }
    private void refreshTable() {
        tableModel.setRowCount(0);
        List<Invoice> invoices = invoiceController.getAllInvoices();
        for (Invoice inv : invoices) {
            tableModel.addRow(new Object[]{
                    inv.getInvoiceId(),
                    inv.getOrder() != null ? inv.getOrder().getOrderId() : "N/A",
                    inv.getIssueDate().toString(),
                    String.format("$%.2f", inv.getSubtotal()),
                    String.format("$%.2f", inv.getTaxAmount()),
                    String.format("$%.2f", inv.getTotalAmount())
            });
        }
    }
}
