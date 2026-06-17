package ec.espe.edu.coffeeshop.view;
import ec.espe.edu.coffeeshop.model.Employee;
import ec.espe.edu.coffeeshop.utils.ThemeManager;
import javax.swing.*;
import java.awt.*;
public class MainMenuView extends JFrame {
    public MainMenuView(Employee emp) {
        setTitle("Coffee Shop - Main Menu (" + emp.getRole() + ")");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(ThemeManager.COLOR_BACKGROUND);
        ((javax.swing.JComponent)getContentPane()).setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setLayout(new BorderLayout());
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(ThemeManager.COLOR_WHITE);
        String role = emp.getRole() != null ? emp.getRole().toUpperCase() : "CASHIER";
        JMenu menuCashier = new JMenu("Cajero");
        menuCashier.setFont(ThemeManager.MAIN_FONT);
        JMenuItem itemPos = new JMenuItem("Punto de Venta (POS)");
        itemPos.addActionListener(e -> openView(new PosView()));
        menuCashier.add(itemPos);
        JMenuItem itemSession = new JMenuItem("Manejar Caja Registradora");
        itemSession.addActionListener(e -> openView(new CashRegisterSessionView()));
        menuCashier.add(itemSession);
        JMenuItem itemCust = new JMenuItem("Registrar Cliente");
        itemCust.addActionListener(e -> openView(new CustomerView()));
        menuCashier.add(itemCust);
        JMenuItem itemRefund = new JMenuItem("Procesar Reembolsos");
        itemRefund.addActionListener(e -> openView(new InvoiceView()));
        menuCashier.add(itemRefund);
        JMenu menuBarista = new JMenu("Barista");
        menuBarista.setFont(ThemeManager.MAIN_FONT);
        JMenuItem itemKds = new JMenuItem("Monitor de Cocina (KDS)");
        itemKds.addActionListener(e -> openView(new KdsView()));
        menuBarista.add(itemKds);
        JMenuItem itemWaste = new JMenuItem("Registrar Desperdicio (Inventario)");
        itemWaste.addActionListener(e -> openView(new InventoryTransactionView()));
        menuBarista.add(itemWaste);
        JMenu menuManager = new JMenu("Gerente");
        menuManager.setFont(ThemeManager.MAIN_FONT);
        JMenu menuSupply = new JMenu("Cadena de Suministro");
        JMenuItem itemProd = new JMenuItem("Productos");
        itemProd.addActionListener(e -> openView(new ProductView()));
        JMenuItem itemModifiers = new JMenuItem("Modificadores de Producto");
        itemModifiers.addActionListener(e -> openView(new ProductModifierView()));
        JMenuItem itemRecipe = new JMenuItem("Recetas");
        itemRecipe.addActionListener(e -> openView(new RecipeView()));
        JMenuItem itemInv = new JMenuItem("Artículos de Inventario");
        itemInv.addActionListener(e -> openView(new InventoryItemView()));
        JMenuItem itemSup = new JMenuItem("Proveedores");
        itemSup.addActionListener(e -> openView(new SupplierView()));
        menuSupply.add(itemProd);
        menuSupply.add(itemModifiers);
        menuSupply.add(itemRecipe);
        menuSupply.add(itemInv);
        menuSupply.add(itemSup);
        menuManager.add(menuSupply);
        JMenuItem itemPurch = new JMenuItem("Órdenes de Compra y Entradas");
        itemPurch.addActionListener(e -> openView(new StockEntryView()));
        menuManager.add(itemPurch);
        JMenuItem itemEmp = new JMenuItem("Administrar Empleados");
        itemEmp.addActionListener(e -> openView(new EmployeeView()));
        menuManager.add(itemEmp);
        JMenuItem itemReports = new JMenuItem("Reportes Financieros");
        itemReports.addActionListener(e -> openView(new ReportView()));
        menuManager.add(itemReports);
        JMenuItem itemInvoices = new JMenuItem("Ver Historial de Facturas");
        itemInvoices.addActionListener(e -> openView(new InvoiceView()));
        menuManager.add(itemInvoices);
        JMenuItem itemPayments = new JMenuItem("Ver Historial de Pagos");
        itemPayments.addActionListener(e -> openView(new PaymentView()));
        menuManager.add(itemPayments);
        JMenuItem itemOrders = new JMenuItem("Ver Historial de Órdenes");
        itemOrders.addActionListener(e -> openView(new OrderView()));
        menuManager.add(itemOrders);
        JMenu menuSystem = new JMenu("Sistema");
        menuSystem.setFont(ThemeManager.MAIN_FONT);
        JMenuItem itemLogout = new JMenuItem("Cerrar Sesión");
        itemLogout.addActionListener(e -> {
            this.dispose();
            new LoginView().setVisible(true);
        });
        menuSystem.add(itemLogout);
        JMenuItem itemExit = new JMenuItem("Salir de la Aplicación");
        itemExit.addActionListener(e -> System.exit(0));
        menuSystem.add(itemExit);
        if (role.equals("CASHIER") || role.equals("MANAGER")) {
            menuBar.add(menuCashier);
        }
        if (role.equals("BARISTA") || role.equals("MANAGER")) {
            menuBar.add(menuBarista);
        }
        if (role.equals("MANAGER")) {
            menuBar.add(menuManager);
        }
        menuBar.add(menuSystem);
        setJMenuBar(menuBar);
        JLabel welcomeLabel = new JLabel("Bienvenido " + emp.getName() + " [" + role + "]");
        welcomeLabel.setFont(ThemeManager.TITLE_FONT);
        welcomeLabel.setForeground(ThemeManager.COLOR_GOLD);
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(welcomeLabel, BorderLayout.CENTER);
    }
    private void openView(JFrame view) {
        SwingUtilities.invokeLater(() -> {
            view.setVisible(true);
        });
    }
}
