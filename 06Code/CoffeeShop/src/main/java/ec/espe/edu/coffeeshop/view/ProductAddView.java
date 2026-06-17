package ec.espe.edu.coffeeshop.view;
import ec.espe.edu.coffeeshop.controller.ProductController;
import ec.espe.edu.coffeeshop.model.Product;
import ec.espe.edu.coffeeshop.utils.ThemeManager;
import javax.swing.*;
import java.awt.*;
public class ProductAddView extends JPanel {
    private JTextField txtId, txtName, txtPrice;
    private JCheckBox chkAvailable;
    private ProductController controller;
    private ProductView parent;
    public ProductAddView(ProductView parent, ProductController controller) {
        this.parent = parent;
        this.controller = controller;
        setLayout(new BorderLayout(10, 10));
        setBackground(ThemeManager.COLOR_BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JLabel titleLabel = new JLabel("AGREGAR NUEVO PRODUCTO", SwingConstants.CENTER);
        titleLabel.setFont(ThemeManager.TITLE_FONT);
        add(titleLabel, BorderLayout.NORTH);
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBackground(ThemeManager.COLOR_BACKGROUND);
        formPanel.add(new JLabel("ID del Producto:"));
        txtId = new JTextField();
        formPanel.add(txtId);
        formPanel.add(new JLabel("Nombre:"));
        txtName = new JTextField();
        formPanel.add(txtName);
        formPanel.add(new JLabel("Precio Base:"));
        txtPrice = new JTextField();
        formPanel.add(txtPrice);
        formPanel.add(new JLabel("Disponible:"));
        chkAvailable = new JCheckBox();
        chkAvailable.setBackground(ThemeManager.COLOR_BACKGROUND);
        formPanel.add(chkAvailable);
        add(formPanel, BorderLayout.CENTER);
        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(ThemeManager.COLOR_BACKGROUND);
        JButton btnSave = new JButton("Guardar");
        btnSave.addActionListener(e -> saveProduct());
        JButton btnCancel = new JButton("Cancelar");
        btnCancel.addActionListener(e -> {
            clearFields();
            parent.showCard("List");
        });
        btnPanel.add(btnSave);
        btnPanel.add(btnCancel);
        add(btnPanel, BorderLayout.SOUTH);
    }
    private void saveProduct() {
        String id = txtId.getText().trim();
        String name = txtName.getText().trim();
        String priceStr = txtPrice.getText().trim();
        if (id.isEmpty() || name.isEmpty() || priceStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios");
            return;
        }
        try {
            double price = Double.parseDouble(priceStr);
            if (price < 0) {
                JOptionPane.showMessageDialog(this, "El precio no puede ser negativo");
                return;
            }
            Product p = new Product(id, name, price, chkAvailable.isSelected());
            if (controller.addProduct(p)) {
                JOptionPane.showMessageDialog(this, "Producto guardado exitosamente");
                clearFields();
                parent.loadTableData();
                parent.showCard("List");
            } else {
                JOptionPane.showMessageDialog(this, "Error al guardar el producto");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Formato de precio inválido");
        }
    }
    private void clearFields() {
        txtId.setText("");
        txtName.setText("");
        txtPrice.setText("");
        chkAvailable.setSelected(false);
    }
}
