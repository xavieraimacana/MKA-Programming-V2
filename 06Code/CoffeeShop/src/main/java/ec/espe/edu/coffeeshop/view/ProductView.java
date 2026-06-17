package ec.espe.edu.coffeeshop.view;
import ec.espe.edu.coffeeshop.controller.ProductController;
import ec.espe.edu.coffeeshop.model.Product;
import ec.espe.edu.coffeeshop.utils.ThemeManager;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
public class ProductView extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private ProductController controller;
    private DefaultTableModel tableModel;
    private JTable table;
    public ProductView() {
        controller = new ProductController();
        setTitle("Gestión de Productos");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        JPanel listPanel = createListPanel();
        mainPanel.add(listPanel, "List");
        ProductAddView addView = new ProductAddView(this, controller);
        mainPanel.add(addView, "Add");
        add(mainPanel);
        cardLayout.show(mainPanel, "List");
    }
    private JPanel createListPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(ThemeManager.COLOR_BACKGROUND);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JLabel titleLabel = new JLabel("MÓDULO DE PRODUCTOS", SwingConstants.CENTER);
        titleLabel.setFont(ThemeManager.TITLE_FONT);
        titleLabel.setForeground(ThemeManager.COLOR_GOLD);
        panel.add(titleLabel, BorderLayout.NORTH);
        String[] columns = {"ID del Producto", "Nombre", "Precio Base", "Disponible"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        loadTableData();
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(ThemeManager.COLOR_BACKGROUND);
        JButton btnAdd = new JButton("Agregar Nuevo Producto");
        btnAdd.addActionListener(e -> showCard("Add"));
        JButton btnDelete = new JButton("Eliminar Seleccionado");
        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                String id = (String) tableModel.getValueAt(row, 0);
                if (controller.deleteProduct(id)) {
                    JOptionPane.showMessageDialog(this, "Producto eliminado");
                    loadTableData();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al eliminar");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione un producto para eliminar");
            }
        });
        btnPanel.add(btnAdd);
        btnPanel.add(btnDelete);
        panel.add(btnPanel, BorderLayout.SOUTH);
        return panel;
    }
    public void showCard(String cardName) {
        cardLayout.show(mainPanel, cardName);
    }
    public void loadTableData() {
        tableModel.setRowCount(0);
        List<Product> products = controller.getAllProducts();
        for (Product p : products) {
            tableModel.addRow(new Object[]{p.getProductId(), p.getName(), String.format("$%.2f", p.getBasePrice()), p.isAvailable() ? "Sí" : "No"});
        }
    }
}
