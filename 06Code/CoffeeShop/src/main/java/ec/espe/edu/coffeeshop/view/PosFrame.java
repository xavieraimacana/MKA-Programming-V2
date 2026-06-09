package ec.espe.edu.coffeeshop.view;

import ec.espe.edu.coffeeshop.discount.*;
import ec.espe.edu.coffeeshop.model.*;
import ec.espe.edu.coffeeshop.payment.*;
import ec.espe.edu.coffeeshop.repository.*;
import ec.espe.edu.coffeeshop.utils.InvoiceExporter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Modern POS Interface for the Coffeeshop system.
 * Implements the dark theme specified in GUIDE_MATEO_CASHIER.md.
 * 
 * @author Mateo Artieda, MKA Programmer, @ESPE
 */
public class PosFrame extends JFrame {
    private final Employee cashier;
    private Shift currentShift;
    private Order currentOrder;
    
    private final ProductRepository productRepo;
    private final OrderRepository orderRepo;
    private final ShiftRepository shiftRepo;
    private final InvoiceRepository invoiceRepo;

    // Colors from GUIDE_MATEO_CASHIER.md
    private final Color bgColor = new Color(28, 25, 23); // Stone 900
    private final Color inputColor = new Color(41, 37, 36); // Stone 800
    private final Color textColor = new Color(231, 229, 228); // Stone 200
    private final Color amberPrimary = new Color(245, 158, 11); // Amber 500
    private final Color amberSecondary = new Color(217, 119, 6); // Amber 600

    // UI Components
    private JPanel productsPanel;
    private JTable cartTable;
    private DefaultTableModel cartModel;
    private JLabel subtotalLabel, taxLabel, discountLabel, totalLabel;
    private JButton checkoutButton;
    private JLabel shiftStatusLabel;

    public PosFrame(Employee cashier) {
        this.cashier = cashier;
        this.productRepo = new MongoProductRepository();
        this.orderRepo = new MongoOrderRepository();
        this.shiftRepo = new MongoShiftRepository();
        this.invoiceRepo = new MongoInvoiceRepository();
        
        this.currentOrder = new Order();
        this.currentOrder.setId(UUID.randomUUID().toString());

        setTitle("MKA Coffeeshop POS - " + cashier.getName());
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(bgColor);
        setLayout(new BorderLayout());

        initializeUI();
        loadProducts();
    }

    private void initializeUI() {
        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(bgColor);
        header.setBorder(new EmptyBorder(10, 20, 10, 20));
        
        JLabel title = new JLabel("COFFEE SHOP POS");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(amberPrimary);
        header.add(title, BorderLayout.WEST);

        shiftStatusLabel = new JLabel("Shift: CLOSED");
        shiftStatusLabel.setForeground(textColor);
        
        JButton shiftButton = createStyledButton("Open Shift", amberSecondary);
        shiftButton.addActionListener(e -> toggleShift());
        
        JPanel shiftPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        shiftPanel.setBackground(bgColor);
        shiftPanel.add(shiftStatusLabel);
        shiftPanel.add(shiftButton);
        header.add(shiftPanel, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        // Center Panel: Products and Cart
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        centerPanel.setBackground(bgColor);
        centerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Left side: Products Grid
        productsPanel = new JPanel(new GridLayout(0, 3, 10, 10));
        productsPanel.setBackground(bgColor);
        JScrollPane prodScroll = new JScrollPane(productsPanel);
        prodScroll.setBorder(null);
        prodScroll.getViewport().setBackground(bgColor);
        centerPanel.add(prodScroll);

        // Right side: Shopping Cart
        JPanel cartPanel = new JPanel(new BorderLayout());
        cartPanel.setBackground(inputColor);
        cartPanel.setBorder(BorderFactory.createLineBorder(new Color(68, 64, 60)));

        cartModel = new DefaultTableModel(new Object[]{"Product", "Qty", "Price", "Total"}, 0);
        cartTable = new JTable(cartModel);
        cartTable.setBackground(inputColor);
        cartTable.setForeground(Color.WHITE);
        cartTable.setGridColor(new Color(68, 64, 60));
        cartTable.getTableHeader().setBackground(new Color(28, 25, 23));
        cartTable.getTableHeader().setForeground(textColor);
        
        cartPanel.add(new JScrollPane(cartTable), BorderLayout.CENTER);

        // Cart Summary
        JPanel summaryPanel = new JPanel(new GridBagLayout());
        summaryPanel.setBackground(inputColor);
        summaryPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridx = 0;

        subtotalLabel = createSummaryLabel("Subtotal: $0.00", 0, summaryPanel, gbc);
        taxLabel = createSummaryLabel("Tax (15%): $0.00", 1, summaryPanel, gbc);
        discountLabel = createSummaryLabel("Discount: $0.00", 2, summaryPanel, gbc);
        totalLabel = createSummaryLabel("TOTAL: $0.00", 3, summaryPanel, gbc);
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        totalLabel.setForeground(amberPrimary);

        checkoutButton = createStyledButton("PROCESS PAYMENT", amberPrimary);
        checkoutButton.setEnabled(false);
        checkoutButton.addActionListener(e -> processCheckout());
        gbc.gridy = 4;
        gbc.insets = new Insets(10, 0, 0, 0);
        summaryPanel.add(checkoutButton, gbc);

        cartPanel.add(summaryPanel, BorderLayout.SOUTH);
        centerPanel.add(cartPanel);

        add(centerPanel, BorderLayout.CENTER);
    }

    private JLabel createSummaryLabel(String text, int y, JPanel panel, GridBagConstraints gbc) {
        JLabel label = new JLabel(text);
        label.setForeground(textColor);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridy = y;
        panel.add(label, gbc);
        return label;
    }

    private JButton createStyledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void loadProducts() {
        List<Product> products = productRepo.findAll();
        productsPanel.removeAll();
        for (Product p : products) {
            JButton pBtn = createStyledButton(p.getName() + " - $" + p.getPrice(), new Color(68, 64, 60));
            pBtn.addActionListener(e -> addToCart(p));
            productsPanel.add(pBtn);
        }
        productsPanel.revalidate();
        productsPanel.repaint();
    }

    private void addToCart(Product p) {
        if (currentShift == null) {
            JOptionPane.showMessageDialog(this, "Please open a shift first!");
            return;
        }
        
        OrderItem item = null;
        for (OrderItem oi : currentOrder.getItems()) {
            if (oi.getProduct().getId().equals(p.getId())) {
                item = oi;
                break;
            }
        }

        if (item != null) {
            item.setQuantity(item.getQuantity() + 1);
        } else {
            item = new OrderItem(p, 1, p.getPrice());
            currentOrder.getItems().add(item);
        }

        updateCartUI();
    }

    private void updateCartUI() {
        cartModel.setRowCount(0);
        BigDecimal subtotal = BigDecimal.ZERO;
        
        for (OrderItem item : currentOrder.getItems()) {
            BigDecimal itemTotal = item.getPricePaidSnapshot().multiply(new BigDecimal(item.getQuantity()));
            subtotal = subtotal.add(itemTotal);
            cartModel.addRow(new Object[]{
                item.getProduct().getName(),
                item.getQuantity(),
                item.getPricePaidSnapshot(),
                itemTotal
            });
        }

        currentOrder.setSubtotal(subtotal);
        currentOrder.setTax(subtotal.multiply(new BigDecimal("0.15")).setScale(2, RoundingMode.HALF_UP));
        currentOrder.setTotal(subtotal.add(currentOrder.getTax()).subtract(currentOrder.getDiscount()));

        subtotalLabel.setText("Subtotal: $" + currentOrder.getSubtotal());
        taxLabel.setText("Tax (15%): $" + currentOrder.getTax());
        discountLabel.setText("Discount: $" + currentOrder.getDiscount());
        totalLabel.setText("TOTAL: $" + currentOrder.getTotal());

        checkoutButton.setEnabled(!currentOrder.getItems().isEmpty());
    }

    private void toggleShift() {
        if (currentShift == null) {
            String initial = JOptionPane.showInputDialog(this, "Enter starting cash:");
            if (initial != null) {
                currentShift = new Shift(UUID.randomUUID().toString(), cashier.getName(), new BigDecimal(initial));
                shiftRepo.save(currentShift);
                shiftStatusLabel.setText("Shift: OPEN (ID: " + currentShift.getId().substring(0, 8) + ")");
            }
        } else {
            String declared = JOptionPane.showInputDialog(this, "Enter declared ending cash:");
            if (declared != null) {
                currentShift.setEndTime(java.time.LocalDateTime.now());
                currentShift.setDeclaredEndingCash(new BigDecimal(declared));
                // In a real scenario, systemEndingCash would be calculated from orders
                currentShift.setDifference(currentShift.getDeclaredEndingCash().subtract(currentShift.getSystemEndingCash()));
                
                if (currentShift.getDifference().compareTo(BigDecimal.ZERO) == 0) 
                    currentShift.setReconciliationStatus(ReconciliationStatus.BALANCED);
                else 
                    currentShift.setReconciliationStatus(currentShift.getDifference().compareTo(BigDecimal.ZERO) > 0 ? ReconciliationStatus.OVERAGE : ReconciliationStatus.SHORTAGE);
                
                shiftRepo.save(currentShift);
                JOptionPane.showMessageDialog(this, "Shift Closed. Status: " + currentShift.getReconciliationStatus());
                currentShift = null;
                shiftStatusLabel.setText("Shift: CLOSED");
            }
        }
    }

    private void processCheckout() {
        // Simple selection for demo
        String[] options = {"Cash", "Card", "Transfer"};
        int choice = JOptionPane.showOptionDialog(this, "Select Payment Method", "Payment",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        Payment payment = null;
        if (choice == 0) { // Cash
            String tendered = JOptionPane.showInputDialog(this, "Total: $" + currentOrder.getTotal() + "\nEnter amount tendered:");
            if (tendered != null) payment = new CashPayment(currentOrder.getTotal(), new BigDecimal(tendered));
        } else if (choice == 1) { // Card
            payment = new CardPayment(currentOrder.getTotal(), "TOKEN-" + UUID.randomUUID().toString().substring(0, 8));
        } else if (choice == 2) { // Transfer
            payment = new BankTransferPayment(currentOrder.getTotal(), "Pichincha", "TRX-" + UUID.randomUUID().toString().substring(0, 8));
        }

        if (payment != null) {
            PaymentProcessor processor = new PaymentProcessor();
            payment.accept(processor);

            if (processor.isAuthorized()) {
                currentOrder.setPayment(payment);
                currentOrder.setStatus(OrderStatus.COMPLETED);
                orderRepo.save(currentOrder);

                // Create and save invoice
                Invoice invoice = new Invoice(UUID.randomUUID().toString(), invoiceRepo.getNextInvoiceNumber(), currentOrder);
                invoiceRepo.save(invoice);

                try {
                    InvoiceExporter.exportToText(invoice);
                    JOptionPane.showMessageDialog(this, "Success! " + processor.getStatusMessage() + "\nInvoice printed: " + invoice.getInvoiceNumber());
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error printing invoice: " + ex.getMessage());
                }

                // Reset for next order
                currentOrder = new Order();
                currentOrder.setId(UUID.randomUUID().toString());
                updateCartUI();
            } else {
                JOptionPane.showMessageDialog(this, "Payment Failed: " + processor.getStatusMessage());
            }
        }
    }
}
