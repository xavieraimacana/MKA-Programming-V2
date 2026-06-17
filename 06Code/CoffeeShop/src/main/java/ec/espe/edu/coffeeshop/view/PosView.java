package ec.espe.edu.coffeeshop.view;
import ec.espe.edu.coffeeshop.controller.OrderController;
import ec.espe.edu.coffeeshop.controller.ProductController;
import ec.espe.edu.coffeeshop.model.Order;
import ec.espe.edu.coffeeshop.model.Product;
import ec.espe.edu.coffeeshop.utils.ThemeManager;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ec.espe.edu.coffeeshop.model.OrderStatus;
import ec.espe.edu.coffeeshop.model.Invoice;
import ec.espe.edu.coffeeshop.controller.InvoiceController;
import ec.espe.edu.coffeeshop.model.Payment;
import ec.espe.edu.coffeeshop.controller.PaymentController;
public class PosView extends JFrame {
    private final ProductController productController;
    private final OrderController orderController;
    private DefaultTableModel cartModel;
    private JTable cartTable;
    private JLabel lblTotal;
    private double currentTotal = 0.0;
    private int orderCounter = 1002;
    private int invoiceCounter = 5001;
    private int paymentCounter = 9001;
    private Map<Product, Integer> currentCart = new HashMap<>();
    public PosView() {
        productController = new ProductController();
        orderController = new OrderController();
        setTitle("Punto de Venta (POS)");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(ThemeManager.COLOR_BACKGROUND);
        ((javax.swing.JComponent)getContentPane()).setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setLayout(new BorderLayout(10, 10));
        JLabel headerLabel = new JLabel("PUNTO DE VENTA");
        headerLabel.setFont(ThemeManager.TITLE_FONT);
        headerLabel.setForeground(ThemeManager.COLOR_GOLD);
        headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        add(headerLabel, BorderLayout.NORTH);
        JPanel catalogPanel = new JPanel(new GridLayout(0, 3, 10, 10));
        catalogPanel.setBackground(ThemeManager.COLOR_WHITE);
        catalogPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(ThemeManager.COLOR_GOLD), "Artículos del Menú"));
        List<Product> products = productController.getAllProducts();
        for (Product p : products) {
            if (p.isAvailable()) {
                JButton btnProd = new JButton("<html><center><b>" + p.getName() + "</b><br/>$" + String.format("%.2f", p.getBasePrice()) + "</center></html>");
                btnProd.setBackground(ThemeManager.COLOR_WHITE);
                btnProd.setForeground(ThemeManager.COLOR_TEXT);
                btnProd.setBorder(BorderFactory.createEmptyBorder());
                btnProd.setFocusPainted(false);
                btnProd.setPreferredSize(new Dimension(140, 90));
                btnProd.setCursor(new Cursor(Cursor.HAND_CURSOR));
                btnProd.addActionListener(e -> addToCart(p));
                catalogPanel.add(btnProd);
            }
        }
        JScrollPane catalogScroll = new JScrollPane(catalogPanel);
        catalogScroll.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(catalogScroll, BorderLayout.CENTER);
        JPanel cartPanel = new JPanel(new BorderLayout(5, 5));
        cartPanel.setBackground(ThemeManager.COLOR_WHITE);
        cartPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(ThemeManager.COLOR_GOLD), "Pedido Actual"));
        cartPanel.setPreferredSize(new Dimension(400, getHeight()));
        String[] cols = {"Artículo", "Precio"};
        cartModel = new DefaultTableModel(cols, 0);
        cartTable = new JTable(cartModel);
        JScrollPane cartScroll = new JScrollPane(cartTable);
        cartPanel.add(cartScroll, BorderLayout.CENTER);
        JPanel checkoutPanel = new JPanel(new BorderLayout());
        checkoutPanel.setBackground(ThemeManager.COLOR_WHITE);
        lblTotal = new JLabel("Total: $0.00");
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTotal.setForeground(ThemeManager.COLOR_GOLD);
        lblTotal.setHorizontalAlignment(SwingConstants.RIGHT);
        lblTotal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        JButton btnCheckout = new JButton("PROCESAR PAGO");
        btnCheckout.setBackground(ThemeManager.COLOR_GOLD);
        btnCheckout.setForeground(Color.BLACK);
        btnCheckout.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnCheckout.setPreferredSize(new Dimension(200, 60));
        btnCheckout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCheckout.addActionListener(e -> processCheckout());
        JButton btnReceipt = new JButton("IMPRIMIR RECIBO");
        btnReceipt.setBackground(ThemeManager.COLOR_GOLD);
        btnReceipt.setForeground(Color.BLACK);
        btnReceipt.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnReceipt.setPreferredSize(new Dimension(200, 60));
        btnReceipt.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnReceipt.addActionListener(e -> printReceipt());
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        buttonPanel.setBackground(ThemeManager.COLOR_WHITE);
        buttonPanel.add(btnCheckout);
        buttonPanel.add(btnReceipt);
        checkoutPanel.add(lblTotal, BorderLayout.NORTH);
        checkoutPanel.add(buttonPanel, BorderLayout.SOUTH);
        cartPanel.add(checkoutPanel, BorderLayout.SOUTH);
        add(cartPanel, BorderLayout.EAST);
    }
    private void addToCart(Product p) {
        cartModel.addRow(new Object[]{p.getName(), String.format("$%.2f", p.getBasePrice())});
        currentCart.put(p, currentCart.getOrDefault(p, 0) + 1);
        currentTotal += p.getBasePrice();
        lblTotal.setText(String.format("Total: $%.2f", currentTotal));
    }
    private void processCheckout() {
        if (currentCart.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El carrito está vacío.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String orderId = "ORD-" + (orderCounter++);
        Order newOrder = new Order(orderId, LocalDateTime.now(), OrderStatus.PENDING, "");
        for (Map.Entry<Product, Integer> entry : currentCart.entrySet()) {
            newOrder.addItem(entry.getKey(), entry.getValue());
        }
        if (orderController.addOrder(newOrder)) {
            String paymentId = "PAY-" + (paymentCounter++);
            Payment payment = new Payment(paymentId, newOrder, currentTotal, "Cash/Card");
            payment.processPayment();
            PaymentController pc = new PaymentController();
            pc.addPayment(payment);
            String invoiceId = "INV-" + (invoiceCounter++);
            Invoice invoice = new Invoice(invoiceId, newOrder, LocalDateTime.now());
            InvoiceController ic = new InvoiceController();
            ic.addInvoice(invoice);
            ec.espe.edu.coffeeshop.controller.InventoryController invCtrl = new ec.espe.edu.coffeeshop.controller.InventoryController();
            com.mongodb.client.MongoDatabase db = ec.espe.edu.coffeeshop.utils.MongoDBConnection.getDatabase();
            for (Map.Entry<Product, Integer> entry : currentCart.entrySet()) {
                String prodName = entry.getKey().getName();
                int qtyToAdd = entry.getValue();
                org.bson.Document recipe = db.getCollection("Recipes")
                        .find(com.mongodb.client.model.Filters.eq("productName", prodName)).first();
                if (recipe != null) {
                    String recipeId = recipe.getString("_id");
                    for (org.bson.Document ingredient : db.getCollection("RecipeIngredients")
                            .find(com.mongodb.client.model.Filters.eq("recipeId", recipeId))) {
                        String itemId = ingredient.getString("itemId");
                        double qty = ingredient.getDouble("quantity") * qtyToAdd;
                        ec.espe.edu.coffeeshop.controller.InventoryTransactionController transCtrl = new ec.espe.edu.coffeeshop.controller.InventoryTransactionController();
                        ec.espe.edu.coffeeshop.model.InventoryTransaction trans = new ec.espe.edu.coffeeshop.model.InventoryTransaction(
                            "IT-" + System.currentTimeMillis(),
                            LocalDateTime.now(),
                            ec.espe.edu.coffeeshop.model.TransactionType.SALE_DEDUCTION,
                            -qty,
                            orderId,
                            new ec.espe.edu.coffeeshop.model.InventoryItem(itemId, "", null, 0, 0)
                        );
                        transCtrl.addTransaction(trans);
                    }
                }
            }
            JOptionPane.showMessageDialog(this, "¡Pago exitoso! Pedido enviado al KDS: " + orderId, "Éxito", JOptionPane.INFORMATION_MESSAGE);
            cartModel.setRowCount(0);
            currentCart.clear();
            currentTotal = 0.0;
            lblTotal.setText("Total: $0.00");
        } else {
            JOptionPane.showMessageDialog(this, "Error al guardar el pedido en la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void printReceipt() {
        if (cartModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "El carrito está vacío. No se puede imprimir el recibo.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar Recibo");
        fileChooser.setSelectedFile(new java.io.File("Receipt.pdf"));
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            java.io.File fileToSave = fileChooser.getSelectedFile();
            List<String[]> items = new java.util.ArrayList<>();
            for (int i = 0; i < cartModel.getRowCount(); i++) {
                String name = (String) cartModel.getValueAt(i, 0);
                String price = (String) cartModel.getValueAt(i, 1);
                items.add(new String[]{name, price});
            }
            ec.espe.edu.coffeeshop.utils.PdfGenerator.generateReceipt(items, currentTotal, fileToSave.getAbsolutePath());
            JOptionPane.showMessageDialog(this, "¡Recibo guardado exitosamente!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
