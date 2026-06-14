package ec.espe.edu.coffeeshop.view;

import ec.espe.edu.coffeeshop.controller.KitchenManager;
import ec.espe.edu.coffeeshop.kds.KdsObserver;
import ec.espe.edu.coffeeshop.model.Order;
import ec.espe.edu.coffeeshop.model.OrderItem;
import ec.espe.edu.coffeeshop.model.OrderStatus;
import ec.espe.edu.coffeeshop.utils.I18nHelper;
import ec.espe.edu.coffeeshop.utils.NavigationMenuHelper;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

/**
 * Kitchen Display System (KDS) Frame for Baristas (Light & Gold Theme).
 * 
 * @author Kevin Albán, MKA Programmer, @ESPE
 */
public class KdsFrame extends JFrame implements KdsObserver {
    private final KitchenManager kitchenManager;
    private JPanel ordersPanel;

    // Colors
    private final Color bgColor = new Color(250, 248, 245); // Warm White
    private final Color cardColor = new Color(255, 255, 255); // Pure White
    private final Color textColor = new Color(41, 37, 36); // Dark Gray
    private final Color goldPrimary = new Color(197, 160, 89); // Gold
    private final Color goldSecondary = new Color(163, 128, 62); // Darker Gold
    private final Color borderColor = new Color(224, 220, 214); // Sand Border

    public KdsFrame(KitchenManager kitchenManager) {
        super(I18nHelper.getMessage("menu.kds_screen"));
        this.kitchenManager = kitchenManager;
        this.kitchenManager.addObserver(this);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 650);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        // 1. Native JMenuBar Navigation
        setJMenuBar(NavigationMenuHelper.createMenuBar(this, "KDS"));

        // 2. Add Content Panel to Center
        JPanel contentPanel = new JPanel(new BorderLayout(15, 15));
        contentPanel.setBackground(bgColor);
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Subheader Panel for title
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel(I18nHelper.getMessage("kds.panel"), JLabel.LEFT);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(goldSecondary);
        titlePanel.add(titleLabel, BorderLayout.WEST);

        JLabel statusLabel = new JLabel("Real-time preparation sync active");
        statusLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        statusLabel.setForeground(new Color(16, 185, 129));
        titlePanel.add(statusLabel, BorderLayout.EAST);

        contentPanel.add(titlePanel, BorderLayout.NORTH);

        ordersPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        ordersPanel.setBackground(bgColor);
        
        JScrollPane scrollPane = new JScrollPane(ordersPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getViewport().setBackground(bgColor);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        add(contentPanel, BorderLayout.CENTER);

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
        card.setLayout(new BorderLayout(10, 10));
        card.setBackground(cardColor);
        card.setPreferredSize(new Dimension(240, 320));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(borderColor, 1),
            BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));

        // Card Header (Order # and Time)
        JPanel cardHeader = new JPanel(new BorderLayout());
        cardHeader.setOpaque(false);

        JLabel idLabel = new JLabel(I18nHelper.getMessage("kds.order") + ": #" + order.getId().substring(0, 5).toUpperCase());
        idLabel.setForeground(goldSecondary);
        idLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        cardHeader.add(idLabel, BorderLayout.WEST);

        // Add a status label "PREPARING"
        JLabel badge = new JLabel("PREPARING");
        badge.setFont(new Font("Segoe UI", Font.BOLD, 9));
        badge.setForeground(goldSecondary);
        badge.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(goldPrimary, 1),
            BorderFactory.createEmptyBorder(2, 6, 2, 6)
        ));
        cardHeader.add(badge, BorderLayout.EAST);

        card.add(cardHeader, BorderLayout.NORTH);

        // Card Body (Items list)
        JPanel cardBody = new JPanel();
        cardBody.setLayout(new BoxLayout(cardBody, BoxLayout.Y_AXIS));
        cardBody.setOpaque(false);
        cardBody.setBorder(new EmptyBorder(10, 0, 10, 0));

        for (OrderItem item : order.getItems()) {
            JPanel itemRow = new JPanel(new BorderLayout());
            itemRow.setOpaque(false);
            itemRow.setBorder(new EmptyBorder(2, 0, 2, 0));

            JLabel nameLbl = new JLabel(item.getProduct().getName());
            nameLbl.setForeground(textColor);
            nameLbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));

            JLabel qtyLbl = new JLabel("x" + item.getQuantity());
            qtyLbl.setForeground(goldSecondary);
            qtyLbl.setFont(new Font("Segoe UI", Font.BOLD, 13));

            itemRow.add(nameLbl, BorderLayout.WEST);
            itemRow.add(qtyLbl, BorderLayout.EAST);
            cardBody.add(itemRow);
        }

        JScrollPane itemsScroll = new JScrollPane(cardBody);
        itemsScroll.setBorder(null);
        itemsScroll.setOpaque(false);
        itemsScroll.getViewport().setOpaque(false);
        card.add(itemsScroll, BorderLayout.CENTER);

        // Card Footer (Action Button)
        JButton readyButton = new JButton(I18nHelper.getMessage("kds.btn_ready"));
        readyButton.setBackground(goldPrimary);
        readyButton.setForeground(Color.WHITE);
        readyButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        readyButton.setFocusPainted(false);
        readyButton.setBorderPainted(false);
        readyButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        readyButton.setPreferredSize(new Dimension(0, 36));
        readyButton.addActionListener(e -> {
            kitchenManager.markOrderAsReady(order.getId());
            refreshOrders();
        });

        // Hover effect on ready button
        readyButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                readyButton.setBackground(goldSecondary);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                readyButton.setBackground(goldPrimary);
            }
        });

        card.add(readyButton, BorderLayout.SOUTH);

        return card;
    }

    @Override
    public void update(Order order) {
        SwingUtilities.invokeLater(this::refreshOrders);
    }
}
