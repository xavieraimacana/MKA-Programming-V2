package ec.espe.edu.coffeeshop.view;
import com.formdev.flatlaf.FlatClientProperties;
import ec.espe.edu.coffeeshop.controller.KdsController;
import ec.espe.edu.coffeeshop.model.Order;
import ec.espe.edu.coffeeshop.utils.ThemeManager;
import javax.swing.*;
import java.awt.*;
import java.util.List;
public class KdsView extends JFrame {
    private final KdsController controller;
    private JPanel pendingPanel;
    private JPanel preparingPanel;
    private JPanel readyPanel;
    public KdsView() {
        this.controller = new KdsController();
        setTitle("Sistema de Visualización de Cocina (KDS) - Kanban");
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(ThemeManager.COLOR_BACKGROUND);
        ((javax.swing.JComponent)getContentPane()).setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setLayout(new BorderLayout(10, 20));
        JPanel headerContainer = new JPanel(new BorderLayout());
        headerContainer.setOpaque(false);
        JLabel headerLabel = new JLabel("SISTEMA DE VISUALIZACIÓN DE COCINA");
        headerLabel.setFont(ThemeManager.TITLE_FONT);
        headerLabel.setForeground(ThemeManager.COLOR_GOLD);
        headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerContainer.add(headerLabel, BorderLayout.CENTER);
        JButton btnRefresh = new JButton("Actualizar Tablero");
        btnRefresh.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnRefresh.setBackground(ThemeManager.COLOR_WHITE);
        btnRefresh.setForeground(Color.BLACK);
        btnRefresh.addActionListener(e -> loadKanbanBoard());
        headerContainer.add(btnRefresh, BorderLayout.EAST);
        add(headerContainer, BorderLayout.NORTH);
        JPanel boardPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        boardPanel.setOpaque(false);
        pendingPanel = createColumn("PENDIENTE (Nuevo)");
        preparingPanel = createColumn("PREPARANDO");
        readyPanel = createColumn("LISTO PARA ENTREGAR");
        boardPanel.add(new JScrollPane(pendingPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
        boardPanel.add(new JScrollPane(preparingPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
        boardPanel.add(new JScrollPane(readyPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
        add(boardPanel, BorderLayout.CENTER);
        loadKanbanBoard();
    }
    private JPanel createColumn(String title) {
        JPanel column = new JPanel();
        column.setLayout(new BoxLayout(column, BoxLayout.Y_AXIS));
        column.setBackground(new Color(240, 240, 245));
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        column.add(lblTitle);
        return column;
    }
    private void loadKanbanBoard() {
        clearColumn(pendingPanel, "PENDIENTE (Nuevo)");
        clearColumn(preparingPanel, "PREPARANDO");
        clearColumn(readyPanel, "LISTO PARA ENTREGAR");
        List<Order> pending = controller.getOrdersByStatus("PENDING");
        for (Order o : pending) {
            pendingPanel.add(createOrderCard(o, "Iniciar Prep.", "PREPARING", ThemeManager.COLOR_GOLD));
            pendingPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }
        List<Order> preparing = controller.getOrdersByStatus("PREPARING");
        for (Order o : preparing) {
            preparingPanel.add(createOrderCard(o, "Marcar Listo", "READY", new Color(255, 165, 0)));
            preparingPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }
        List<Order> ready = controller.getOrdersByStatus("READY");
        for (Order o : ready) {
            readyPanel.add(createOrderCard(o, "Entregar (Completar)", "COMPLETED", new Color(50, 205, 50)));
            readyPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }
        pendingPanel.revalidate(); pendingPanel.repaint();
        preparingPanel.revalidate(); preparingPanel.repaint();
        readyPanel.revalidate(); readyPanel.repaint();
    }
    private void clearColumn(JPanel column, String title) {
        column.removeAll();
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        column.add(lblTitle);
        column.add(Box.createRigidArea(new Dimension(0, 10)));
    }
    private JPanel createOrderCard(Order order, String buttonText, String targetStatus, Color buttonColor) {
        JPanel card = new JPanel(new BorderLayout(5, 5));
        card.setBackground(ThemeManager.COLOR_WHITE);
        card.putClientProperty(FlatClientProperties.STYLE, "arc: 15");
        card.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        card.setMaximumSize(new Dimension(300, 120));
        JPanel infoPanel = new JPanel(new GridLayout(2, 1));
        infoPanel.setOpaque(false);
        JLabel lblId = new JLabel("Pedido: " + order.getOrderId());
        lblId.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JLabel lblAmount = new JLabel("Total: $" + String.format("%.2f", order.getItems().stream().mapToDouble(ec.espe.edu.coffeeshop.model.OrderItem::getSubtotal).sum()));
        infoPanel.add(lblId);
        infoPanel.add(lblAmount);
        card.add(infoPanel, BorderLayout.CENTER);
        JButton btnAction = new JButton(buttonText);
        btnAction.setBackground(buttonColor);
        btnAction.setForeground(Color.BLACK);
        btnAction.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnAction.putClientProperty(FlatClientProperties.STYLE, "arc: 10");
        btnAction.addActionListener(e -> {
            if (controller.updateOrderStatus(order.getOrderId(), targetStatus)) {
                loadKanbanBoard();
            } else {
                JOptionPane.showMessageDialog(this, "Error al actualizar el estado del pedido.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        card.add(btnAction, BorderLayout.SOUTH);
        return card;
    }
}
