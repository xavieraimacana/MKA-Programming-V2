package ec.espe.edu.coffeeshop.view;
import ec.espe.edu.coffeeshop.controller.ProductModifierController;
import ec.espe.edu.coffeeshop.model.ProductModifier;
import ec.espe.edu.coffeeshop.utils.ThemeManager;
import javax.swing.*;
import java.awt.*;
public class ProductModifierAddView extends JPanel {
    private JTextField txtId, txtName, txtPrice;
    private ProductModifierController controller;
    private ProductModifierView parent;
    public ProductModifierAddView(ProductModifierView parent, ProductModifierController controller) {
        this.parent = parent;
        this.controller = controller;
        setLayout(new BorderLayout(10, 10));
        setBackground(ThemeManager.COLOR_BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JLabel titleLabel = new JLabel("AGREGAR NUEVO MODIFICADOR DE PRODUCTO", SwingConstants.CENTER);
        titleLabel.setFont(ThemeManager.TITLE_FONT);
        add(titleLabel, BorderLayout.NORTH);
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setBackground(ThemeManager.COLOR_BACKGROUND);
        formPanel.add(new JLabel("ID del Modificador:"));
        txtId = new JTextField();
        formPanel.add(txtId);
        formPanel.add(new JLabel("Nombre:"));
        txtName = new JTextField();
        formPanel.add(txtName);
        formPanel.add(new JLabel("Precio Adicional:"));
        txtPrice = new JTextField();
        formPanel.add(txtPrice);
        add(formPanel, BorderLayout.CENTER);
        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(ThemeManager.COLOR_BACKGROUND);
        JButton btnSave = new JButton("Guardar");
        btnSave.addActionListener(e -> saveModifier());
        JButton btnCancel = new JButton("Cancelar");
        btnCancel.addActionListener(e -> {
            clearFields();
            parent.showCard("List");
        });
        btnPanel.add(btnSave);
        btnPanel.add(btnCancel);
        add(btnPanel, BorderLayout.SOUTH);
    }
    private void saveModifier() {
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
            ProductModifier m = new ProductModifier(id, name, price);
            if (controller.addModifier(m)) {
                JOptionPane.showMessageDialog(this, "Modificador guardado exitosamente");
                clearFields();
                parent.loadTableData();
                parent.showCard("List");
            } else {
                JOptionPane.showMessageDialog(this, "Error al guardar el modificador");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Formato de precio inválido");
        }
    }
    private void clearFields() {
        txtId.setText("");
        txtName.setText("");
        txtPrice.setText("");
    }
}
