package ec.espe.edu.coffeeshop.view;
import ec.espe.edu.coffeeshop.controller.PaymentController;
import ec.espe.edu.coffeeshop.model.Payment;
import ec.espe.edu.coffeeshop.utils.ThemeManager;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
public class PaymentView extends JFrame {
    private final PaymentController paymentController;
    private DefaultTableModel tableModel;
    private JTable paymentTable;
    public PaymentView() {
        paymentController = new PaymentController();
        setTitle("Historial de Pagos");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(ThemeManager.COLOR_BACKGROUND);
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(ThemeManager.COLOR_BACKGROUND);
        JLabel titleLabel = new JLabel("REGISTRO DE PAGOS");
        titleLabel.setFont(ThemeManager.TITLE_FONT);
        titleLabel.setForeground(ThemeManager.COLOR_GOLD);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        String[] cols = {"ID Pago", "ID Pedido", "Monto", "Método", "¿Exitoso?"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        paymentTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(paymentTable);
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
        List<Payment> payments = paymentController.getAllPayments();
        for (Payment pay : payments) {
            tableModel.addRow(new Object[]{
                    pay.getPaymentId(),
                    pay.getOrder() != null ? pay.getOrder().getOrderId() : "N/A",
                    String.format("$%.2f", pay.getAmount()),
                    pay.getMethod(),
                    pay.isSuccessful() ? "Sí" : "No"
            });
        }
    }
}
