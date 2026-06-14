package ec.espe.edu.coffeeshop.view;

import ec.espe.edu.coffeeshop.controller.InventoryManager;
import ec.espe.edu.coffeeshop.discount.*;
import ec.espe.edu.coffeeshop.model.*;
import ec.espe.edu.coffeeshop.payment.*;
import ec.espe.edu.coffeeshop.repository.*;
import ec.espe.edu.coffeeshop.utils.I18nHelper;
import ec.espe.edu.coffeeshop.utils.InvoiceExporter;
import ec.espe.edu.coffeeshop.utils.NavigationMenuHelper;
import ec.espe.edu.coffeeshop.utils.SystemContext;

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
 * Modern POS Interface for the Coffeeshop system (Light & Gold Theme).
 * Web-navbar navigation, shift control, CRM, and preemptive blocks integrated.
 * 
 * @author MKA Programmers, ESPE
 */
public class PosFrame extends JFrame {
    private final Employee cashier;
    private Shift currentShift;
    private Order currentOrder;
    
    private final ProductRepository productRepo;
    private final OrderRepository orderRepo;
    private final ShiftRepository shiftRepo;
    private final InvoiceRepository invoiceRepo;

    // Light & Gold Theme Colors
    private final Color bgColor = new Color(250, 248, 245); // Warm White
    private final Color inputColor = new Color(255, 255, 255); // Pure White
    private final Color textColor = new Color(41, 37, 36); // Stone 800
    private final Color textMutedColor = new Color(120, 113, 108); // Stone 500
    private final Color goldPrimary = new Color(197, 160, 89); // Gold
    private final Color goldSecondary = new Color(163, 128, 62); // Darker Gold
    private final Color borderColor = new Color(224, 220, 214); // Sand Border

    // UI Components
    private JPanel productsPanel;
    private JTable cartTable;
    private DefaultTableModel cartModel;
    private JLabel subtotalLabel, taxLabel, discountLabel, totalLabel;
    private JButton checkoutButton;
    private JLabel shiftStatusLabel;
    private JButton shiftButton;

    // CRM & Discount selectors
    private JComboBox<String> customerCombo;
    private JComboBox<String> discountCombo;
    private List<Customer> customersList;

    public PosFrame(Employee cashier) {
        super(I18nHelper.getMessage("menu.pos_terminal"));
        this.cashier = cashier;
        this.productRepo = new MongoProductRepository();
        this.orderRepo = new MongoOrderRepository();
        this.shiftRepo = new MongoShiftRepository();
        this.invoiceRepo = new MongoInvoiceRepository();
        
        this.currentOrder = new Order();
        this.currentOrder.setId(UUID.randomUUID().toString());

        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(bgColor);
        setLayout(new BorderLayout());

        initializeUI();
        loadProducts();
        loadCustomers();
    }

    private void initializeUI() {
        // 1. Native JMenuBar Navigation
        setJMenuBar(NavigationMenuHelper.createMenuBar(this, "POS"));

        // 2. Center Panel: Products and Cart
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        centerPanel.setBackground(bgColor);
        centerPanel.setBorder(new EmptyBorder(20, 15, 20, 15));

        // Left side: Products Grid
        productsPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        productsPanel.setBackground(bgColor);
        JScrollPane prodScroll = new JScrollPane(productsPanel);
        prodScroll.setBorder(null);
        prodScroll.getViewport().setBackground(bgColor);
        centerPanel.add(prodScroll);

        // Right side: Shopping Cart Panel
        JPanel cartPanel = new JPanel(new BorderLayout());
        cartPanel.setBackground(inputColor);
        cartPanel.setBorder(BorderFactory.createLineBorder(borderColor));

        // Controls Panel (Shift control + CRM + Discounts)
        JPanel crmPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        crmPanel.setBackground(inputColor);
        crmPanel.setBorder(new EmptyBorder(12, 12, 12, 12));

        boolean es = I18nHelper.getLocale().getLanguage().equals("es");
        // Row 1: Shift Control
        shiftStatusLabel = new JLabel(es ? "Turno: CERRADO" : "Shift: CLOSED");
        shiftStatusLabel.setForeground(textColor);
        shiftStatusLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        crmPanel.add(shiftStatusLabel);

        shiftButton = createStyledButton(es ? "Abrir Turno" : "Open Shift", goldSecondary);
        shiftButton.addActionListener(e -> toggleShift());
        crmPanel.add(shiftButton);

        // Row 2: Customer CRM
        JLabel custLbl = new JLabel(es ? "CLIENTE (CRM):" : "CUSTOMER (CRM):");
        custLbl.setForeground(textColor);
        custLbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
        crmPanel.add(custLbl);

        customerCombo = new JComboBox<>();
        customerCombo.setBackground(inputColor);
        customerCombo.setForeground(textColor);
        customerCombo.addActionListener(e -> updateCartUI());
        crmPanel.add(customerCombo);

        // Row 3: Discount Strategy
        JLabel discLbl = new JLabel(I18nHelper.getMessage("pos.discount_strategy"));
        discLbl.setForeground(textColor);
        discLbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
        crmPanel.add(discLbl);

        String[] discounts = es 
            ? new String[]{"Sin Descuento (0%)", "Descuento Porcentaje (10%)", "Descuento Fijo ($2.00)", "Descuento Lealtad (por puntos)"} 
            : new String[]{"No Discount (0%)", "Percentage Discount (10%)", "Fixed Discount ($2.00)", "Loyalty Discount (points-based)"};
        discountCombo = new JComboBox<>(discounts);
        discountCombo.setBackground(inputColor);
        discountCombo.setForeground(textColor);
        discountCombo.addActionListener(e -> updateCartUI());
        crmPanel.add(discountCombo);

        cartPanel.add(crmPanel, BorderLayout.NORTH);

        // Cart Table with modern styling (row height and centered)
        String[] columns = es ? new String[]{"Producto", "Cant.", "Precio", "Total"} : new String[]{"Product", "Qty", "Price", "Total"};
        cartModel = new DefaultTableModel(columns, 0);
        cartTable = new JTable(cartModel);
        cartTable.setBackground(inputColor);
        cartTable.setForeground(textColor);
        cartTable.setGridColor(borderColor);
        cartTable.setRowHeight(25);
        cartTable.getTableHeader().setBackground(new Color(245, 242, 237));
        cartTable.getTableHeader().setForeground(textColor);
        cartTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 11));
        
        cartPanel.add(new JScrollPane(cartTable), BorderLayout.CENTER);

        // Cart Summary
        JPanel summaryPanel = new JPanel(new GridBagLayout());
        summaryPanel.setBackground(inputColor);
        summaryPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridx = 0;

        subtotalLabel = createSummaryLabel(es ? "Subtotal: $0.00" : "Subtotal: $0.00", 0, summaryPanel, gbc);
        taxLabel = createSummaryLabel(es ? "IVA (15%): $0.00" : "Tax (15%): $0.00", 1, summaryPanel, gbc);
        discountLabel = createSummaryLabel(es ? "Descuento: $0.00" : "Discount: $0.00", 2, summaryPanel, gbc);
        totalLabel = createSummaryLabel("TOTAL: $0.00", 3, summaryPanel, gbc);
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        totalLabel.setForeground(goldSecondary);

        checkoutButton = createStyledButton(es ? "PROCESAR PAGO" : "PROCESS PAYMENT", goldPrimary);
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
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        return btn;
    }

    private void loadProducts() {
        List<Product> products = productRepo.findAll();
        productsPanel.removeAll();
        for (Product p : products) {
            JButton pBtn = new JButton(p.getName() + " - $" + p.getPrice());
            pBtn.setBackground(new Color(245, 242, 237)); // Light Sand Card
            pBtn.setForeground(textColor);
            pBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
            pBtn.setFocusPainted(false);
            pBtn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderColor, 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
            ));
            pBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            pBtn.addActionListener(e -> addToCart(p));
            
            // Hover effect
            pBtn.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    pBtn.setBackground(new Color(230, 226, 220));
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    pBtn.setBackground(new Color(245, 242, 237));
                }
            });
            
            productsPanel.add(pBtn);
        }
        productsPanel.revalidate();
        productsPanel.repaint();
    }

    private void loadCustomers() {
        MongoCustomerRepository custRepo = new MongoCustomerRepository();
        customersList = custRepo.findAll();
        customerCombo.removeAllItems();
        for (Customer c : customersList) {
            customerCombo.addItem(c.getName() + " (" + c.getTaxId() + ")");
        }
    }

    private void addToCart(Product p) {
        boolean es = I18nHelper.getLocale().getLanguage().equals("es");
        if (currentShift == null) {
            JOptionPane.showMessageDialog(this, es ? "¡Por favor, abra un turno primero!" : "Please open a shift first!");
            return;
        }

        // 1. Validacion de Equipos (Equipment Check - FR-11)
        MongoEquipmentRepository eqRepo = new MongoEquipmentRepository();
        List<Equipment> machines = eqRepo.findAll();
        boolean espressoOn = true;
        boolean grinderOn = true;
        boolean frotherOn = true;

        for (Equipment eq : machines) {
            if (eq.getId().equals("1") && eq.getStatus() != EquipmentStatus.OPERATIONAL) espressoOn = false;
            if (eq.getId().equals("2") && eq.getStatus() != EquipmentStatus.OPERATIONAL) grinderOn = false;
            if (eq.getId().equals("3") && eq.getStatus() != EquipmentStatus.OPERATIONAL) frotherOn = false;
        }

        if (p.getCategory() == ProductCategory.BEVERAGE) {
            if (!espressoOn) {
                JOptionPane.showMessageDialog(this, I18nHelper.getMessage("eq.error_espresso"), I18nHelper.getMessage("eq.error_title"), JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!grinderOn) {
                JOptionPane.showMessageDialog(this, es ? "ERROR: Molino fuera de servicio." : "ERROR: Coffee Grinder is not operational (OFF/MAINTENANCE). Cannot grind beans!", "Equipment Blocked", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        
        if (p.getName().equalsIgnoreCase("Cappuccino") || p.getName().equalsIgnoreCase("Hot Chocolate")) {
            if (!frotherOn) {
                JOptionPane.showMessageDialog(this, es ? "ERROR: Espumador de leche fuera de servicio." : "ERROR: Milk Frother is not operational (OFF/MAINTENANCE). Cannot froth milk!", "Equipment Blocked", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        // 2. Validacion de Stock (Preemptive Block - F4.3)
        int qtyInCart = 0;
        for (OrderItem item : currentOrder.getItems()) {
            if (item.getProduct().getId().equals(p.getId())) {
                qtyInCart = item.getQuantity();
                break;
            }
        }
        int targetQty = qtyInCart + 1;

        Recipe recipe = p.getRecipe();
        if (recipe != null && recipe.getItems() != null) {
            MongoIngredientRepository ingRepo = new MongoIngredientRepository();
            for (RecipeItem item : recipe.getItems()) {
                Ingredient ing = ingRepo.findById(item.getIngredient().getId());
                if (ing != null) {
                    double totalNeeded = item.getQuantityNeeded() * targetQty;
                    if (ing.getStock() < totalNeeded) {
                        String msg = es ? "ERROR: ¡Agotado!\nNo hay suficiente " + ing.getName() + " en inventario.\nRequerido: " + totalNeeded + ", Disponible: " + ing.getStock() 
                                        : "ERROR: Out of Stock!\nNot enough " + ing.getName() + " in inventory.\nRequired: " + totalNeeded + ", Available: " + ing.getStock();
                        JOptionPane.showMessageDialog(this, msg, es ? "Inventario Bloqueado" : "Inventory Blocked", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
            }
        }

        // 3. Añadir item a la orden
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

        // Vincular Cliente Seleccionado
        int cIndex = customerCombo.getSelectedIndex();
        if (cIndex >= 0 && customersList != null && cIndex < customersList.size()) {
            currentOrder.setClient(customersList.get(cIndex));
        }

        // Vincular Estrategia de Descuento
        int discountChoice = discountCombo.getSelectedIndex();
        BigDecimal discountVal = BigDecimal.ZERO;
        Discount discountStrategy = null;

        if (discountChoice == 1) { // 10%
            discountStrategy = new PercentageDiscount(new BigDecimal("10"));
        } else if (discountChoice == 2) { // $2.00
            discountStrategy = new FixedDiscount(new BigDecimal("2.00"));
        } else if (discountChoice == 3) { // Loyalty
            discountStrategy = new LoyaltyDiscount();
        }

        if (discountStrategy != null) {
            discountVal = discountStrategy.calculateDiscount(currentOrder);
        }
        currentOrder.setDiscount(discountVal);

        // Total
        BigDecimal total = subtotal.add(currentOrder.getTax()).subtract(currentOrder.getDiscount());
        if (total.compareTo(BigDecimal.ZERO) < 0) total = BigDecimal.ZERO;
        currentOrder.setTotal(total);

        boolean es = I18nHelper.getLocale().getLanguage().equals("es");
        subtotalLabel.setText("Subtotal: $" + currentOrder.getSubtotal());
        taxLabel.setText((es ? "IVA (15%): $" : "Tax (15%): $") + currentOrder.getTax());
        discountLabel.setText((es ? "Descuento: $" : "Discount: $") + currentOrder.getDiscount());
        totalLabel.setText("TOTAL: $" + currentOrder.getTotal());

        checkoutButton.setEnabled(!currentOrder.getItems().isEmpty());
    }

    private void toggleShift() {
        boolean es = I18nHelper.getLocale().getLanguage().equals("es");
        if (currentShift == null) {
            String initial = JOptionPane.showInputDialog(this, es ? "Ingrese efectivo inicial:" : "Enter starting cash:");
            if (initial != null && !initial.trim().isEmpty()) {
                try {
                    BigDecimal cash = new BigDecimal(initial.trim());
                    currentShift = new Shift(UUID.randomUUID().toString(), cashier.getName(), cash);
                    shiftRepo.save(currentShift);
                    shiftStatusLabel.setText((es ? "Turno: ABIERTO (" : "Shift: OPEN (") + currentShift.getId().substring(0, 5) + ")");
                    shiftButton.setText(es ? "Cerrar Turno" : "Close Shift");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, es ? "Cantidad de efectivo inicial inválida." : "Invalid starting cash amount.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            String declared = JOptionPane.showInputDialog(this, es ? "Ingrese efectivo final declarado:" : "Enter declared ending cash:");
            if (declared != null && !declared.trim().isEmpty()) {
                try {
                    BigDecimal declaredCash = new BigDecimal(declared.trim());
                    currentShift.setEndTime(java.time.LocalDateTime.now());
                    currentShift.setDeclaredEndingCash(declaredCash);
                    
                    // Calcular sistema cash a partir de las ordenes
                    BigDecimal salesSum = BigDecimal.ZERO;
                    for (Order o : orderRepo.findAll()) {
                        if (o.getPayment() != null) {
                            salesSum = salesSum.add(o.getTotal());
                        }
                    }
                    currentShift.setSystemEndingCash(currentShift.getStartingCash().add(salesSum));
                    currentShift.setDifference(currentShift.getDeclaredEndingCash().subtract(currentShift.getSystemEndingCash()));
                    
                    if (currentShift.getDifference().compareTo(BigDecimal.ZERO) == 0) 
                        currentShift.setReconciliationStatus(ec.espe.edu.coffeeshop.model.ReconciliationStatus.BALANCED);
                    else 
                        currentShift.setReconciliationStatus(currentShift.getDifference().compareTo(BigDecimal.ZERO) > 0 ? ec.espe.edu.coffeeshop.model.ReconciliationStatus.OVERAGE : ec.espe.edu.coffeeshop.model.ReconciliationStatus.SHORTAGE);
                    
                    shiftRepo.save(currentShift);
                    String msg = es ? "Turno Cerrado.\nEstado: " : "Shift Closed.\nStatus: ";
                    msg += currentShift.getReconciliationStatus() + (es ? "\nDiferencia: $" : "\nDifference: $") + currentShift.getDifference();
                    JOptionPane.showMessageDialog(this, msg);
                    currentShift = null;
                    shiftStatusLabel.setText(es ? "Turno: CERRADO" : "Shift: CLOSED");
                    shiftButton.setText(es ? "Abrir Turno" : "Open Shift");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, es ? "Cantidad declarada inválida." : "Invalid declared cash amount.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void processCheckout() {
        boolean es = I18nHelper.getLocale().getLanguage().equals("es");
        String[] options = es ? new String[]{"Efectivo", "Tarjeta", "Transferencia"} : new String[]{"Cash", "Card", "Transfer"};
        String title = es ? "Seleccionar Método de Pago" : "Select Payment Method";
        String gateway = es ? "Pasarela de Pago" : "Payment Gateway";
        int choice = JOptionPane.showOptionDialog(this, title, gateway,
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        Payment payment = null;
        if (choice == 0) { // Cash
            String prompt = es ? "Total: $" + currentOrder.getTotal() + "\nIngrese monto recibido:" : "Total: $" + currentOrder.getTotal() + "\nEnter amount tendered:";
            String tendered = JOptionPane.showInputDialog(this, prompt);
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

                // 1. Descontar Inventario (Recipe Deduction - FR-09)
                InventoryManager inventoryManager = new InventoryManager();
                for (OrderItem item : currentOrder.getItems()) {
                    for (int i = 0; i < item.getQuantity(); i++) {
                        try {
                            inventoryManager.deductStock(item.getProduct());
                        } catch (Exception ex) {
                            // Ya validamos al agregar...
                        }
                    }
                }

                // 2. Registrar en Cocina (KDS Sync - FR-06)
                SystemContext.getInstance().getKitchenManager().receiveOrder(currentOrder);
                
                // Guardar la orden con estado actualizado
                orderRepo.save(currentOrder);

                // 3. Crear y guardar factura (FR-05)
                Invoice invoice = new Invoice(UUID.randomUUID().toString(), invoiceRepo.getNextInvoiceNumber(), currentOrder);
                invoiceRepo.save(invoice);

                // 4. Acumular puntos de lealtad (1 punto por cada $10 gastados)
                if (currentOrder.getClient() != null && !currentOrder.getClient().getId().equals("1")) {
                    Customer c = currentOrder.getClient();
                    int pointsEarned = currentOrder.getTotal().divide(new BigDecimal("10"), 0, RoundingMode.DOWN).intValue();
                    if (pointsEarned > 0) {
                        c.setLoyaltyPoints(c.getLoyaltyPoints() + pointsEarned);
                        new MongoCustomerRepository().save(c);
                        System.out.println("CRM: " + c.getName() + " earned " + pointsEarned + " loyalty points!");
                    }
                }

                try {
                    InvoiceExporter.exportToText(invoice);
                    String sMsg = es ? "¡Pago Exitoso!\n" + processor.getStatusMessage() + "\nFactura impresa: " + invoice.getInvoiceNumber() 
                                     : "Checkout Success!\n" + processor.getStatusMessage() + "\nInvoice printed: " + invoice.getInvoiceNumber();
                    JOptionPane.showMessageDialog(this, sMsg);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, (es ? "Error al imprimir factura: " : "Error printing invoice: ") + ex.getMessage());
                }

                // Reset for next order
                currentOrder = new Order();
                currentOrder.setId(UUID.randomUUID().toString());
                updateCartUI();
            } else {
                JOptionPane.showMessageDialog(this, (es ? "Pago Fallido: " : "Payment Failed: ") + processor.getStatusMessage());
            }
        }
    }
}
