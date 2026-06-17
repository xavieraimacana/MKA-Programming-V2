package ec.espe.edu.coffeeshop.view;
import ec.espe.edu.coffeeshop.controller.OrderController;
import ec.espe.edu.coffeeshop.model.Order;
import ec.espe.edu.coffeeshop.utils.ThemeManager;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
public class OrderView extends JFrame {
    private final OrderController orderController;
    private DefaultTableModel tableModel;
    private JTable orderTable;
    public OrderView() {
        orderController = new OrderController();
        setTitle("Historial de Pedidos");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(ThemeManager.COLOR_BACKGROUND);
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(ThemeManager.COLOR_BACKGROUND);
        JLabel titleLabel = new JLabel("HISTORIAL DE PEDIDOS");
        titleLabel.setFont(ThemeManager.TITLE_FONT);
        titleLabel.setForeground(ThemeManager.COLOR_GOLD);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        String[] cols = {"ID Pedido", "Fecha", "Estado", "Notas"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        orderTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(orderTable);
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
        List<Order> orders = orderController.getAllOrders();
        for (Order ord : orders) {
            tableModel.addRow(new Object[]{
                    ord.getOrderId(),
                    ord.getOrderDate().toString(),
                    ord.getStatus().name(),
                    ord.getPreparationNotes()
            });
        }
    }
}
