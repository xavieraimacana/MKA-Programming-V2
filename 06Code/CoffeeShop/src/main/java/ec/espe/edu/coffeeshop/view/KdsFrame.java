package ec.espe.edu.coffeeshop.view;

import ec.espe.edu.coffeeshop.controller.KitchenManager;
import ec.espe.edu.coffeeshop.kds.KdsObserver;
import ec.espe.edu.coffeeshop.model.Order;
import ec.espe.edu.coffeeshop.model.OrderItem;
import ec.espe.edu.coffeeshop.model.OrderStatus;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

/**
 * Kitchen Display System (KDS) Frame for Baristas.
 * 
 * @author Kevin Albán, MKA Programmer, @ESPE
 */
public class KdsFrame extends JFrame implements KdsObserver {
    private final KitchenManager kitchenManager;
    private JPanel ordersPanel;

    public KdsFrame(KitchenManager kitchenManager) {
        this.kitchenManager = kitchenManager;
        this.kitchenManager.addObserver(this);

        setTitle("Kitchen Display System (KDS)");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(28, 25, 23)); // Stone 900
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        setContentPane(mainPanel);

        JLabel titleLabel = new JLabel("PENDING ORDERS (KITCHEN)", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(245, 158, 11)); // Amber 500
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        ordersPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        ordersPanel.setBackground(new Color(28, 25, 23));
        
        JScrollPane scrollPane = new JScrollPane(ordersPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getViewport().setBackground(new Color(28, 25, 23));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        refreshOrders();
    }

    private void refreshOrders() {
        ordersPanel.removeAll();
        List<Order> queue = kitchenManager.getPreparationQueue();
        for (Order order : queue) {
            if (order.getStatus() == OrderStatus.PREPARING) {
                ordersPanel.add(createOrderCard(order));
            }
        }
        ordersPanel.revalidate();
        ordersPanel.repaint();
    }

    private JPanel createOrderCard(Order order) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(new Color(41, 37, 36)); // Stone 800
        card.setPreferredSize(new Dimension(220, 300));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(68, 64, 60), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JLabel idLabel = new JLabel("Order: #" + order.getId().substring(0, 5));
        idLabel.setForeground(new Color(245, 158, 11)); // Amber 500
        idLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        idLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(idLabel);
        card.add(Box.createVerticalStrut(10));

        for (OrderItem item : order.getItems()) {
            JLabel itemLabel = new JLabel("- " + item.getQuantity() + "x " + item.getProduct().getName());
            itemLabel.setForeground(new Color(231, 229, 228)); // Stone 200
            itemLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            itemLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            card.add(itemLabel);
        }

        card.add(Box.createVerticalGlue());

        JButton readyButton = new JButton("MARK AS READY");
        readyButton.setBackground(new Color(217, 119, 6)); // Amber 600
        readyButton.setForeground(Color.WHITE);
        readyButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        readyButton.setFocusPainted(false);
        readyButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        readyButton.addActionListener(e -> {
            kitchenManager.markOrderAsReady(order.getId());
            refreshOrders();
        });
        card.add(readyButton);

        return card;
    }

    @Override
    public void update(Order order) {
        SwingUtilities.invokeLater(this::refreshOrders);
    }
}
