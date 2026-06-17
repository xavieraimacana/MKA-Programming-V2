package ec.espe.edu.coffeeshop.view;
import ec.espe.edu.coffeeshop.controller.InventoryController;
import ec.espe.edu.coffeeshop.model.InventoryItem;
import ec.espe.edu.coffeeshop.model.UnitOfMeasure;
import ec.espe.edu.coffeeshop.utils.ThemeManager;
import javax.swing.*;
import java.awt.*;
public class InventoryItemAddView extends JDialog {
    private final InventoryController controller;
    private JTextField txtId;
    private JTextField txtName;
    private JComboBox<UnitOfMeasure> cmbBaseUnit;
    private JTextField txtCurrentStock;
    private JTextField txtMinThreshold;
    public InventoryItemAddView(JFrame parent, InventoryController controller) {
        super(parent, "Añadir Nuevo Artículo de Inventario", true);
        this.controller = controller;
        setSize(600, 650);
        setLocationRelativeTo(parent);
        getContentPane().setBackground(ThemeManager.COLOR_BACKGROUND);
        setLayout(new GridBagLayout());
        JPanel cardPanel = new JPanel(new BorderLayout(20, 20));
        cardPanel.setBackground(ThemeManager.COLOR_WHITE);
        cardPanel.putClientProperty(com.formdev.flatlaf.FlatClientProperties.STYLE, "arc: 20");
        cardPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        JLabel titleLabel = new JLabel("Registrar Nuevo Artículo");
        titleLabel.setFont(ThemeManager.TITLE_FONT);
        titleLabel.setForeground(ThemeManager.COLOR_GOLD);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        cardPanel.add(titleLabel, BorderLayout.NORTH);
        JPanel formPanel = new JPanel(new GridLayout(5, 1, 10, 15));
        formPanel.setBackground(ThemeManager.COLOR_WHITE);
        txtId = new JTextField();
        txtId.setPreferredSize(new Dimension(250, 40));
        txtId.putClientProperty(com.formdev.flatlaf.FlatClientProperties.PLACEHOLDER_TEXT, "ID del Artículo");
        txtName = new JTextField();
        txtName.setPreferredSize(new Dimension(250, 40));
        txtName.putClientProperty(com.formdev.flatlaf.FlatClientProperties.PLACEHOLDER_TEXT, "Nombre");
        cmbBaseUnit = new JComboBox<>(UnitOfMeasure.values());
        cmbBaseUnit.setPreferredSize(new Dimension(250, 40));
        txtCurrentStock = new JTextField();
        txtCurrentStock.setPreferredSize(new Dimension(250, 40));
        txtCurrentStock.putClientProperty(com.formdev.flatlaf.FlatClientProperties.PLACEHOLDER_TEXT, "Stock Actual");
        txtMinThreshold = new JTextField();
        txtMinThreshold.setPreferredSize(new Dimension(250, 40));
        txtMinThreshold.putClientProperty(com.formdev.flatlaf.FlatClientProperties.PLACEHOLDER_TEXT, "Umbral Mínimo");
        formPanel.add(txtId);
        formPanel.add(txtName);
        formPanel.add(cmbBaseUnit);
        formPanel.add(txtCurrentStock);
        formPanel.add(txtMinThreshold);
        cardPanel.add(formPanel, BorderLayout.CENTER);
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        actionPanel.setBackground(ThemeManager.COLOR_WHITE);
        JButton btnSave = new JButton("Guardar");
        btnSave.setPreferredSize(new Dimension(120, 40));
        btnSave.setBackground(ThemeManager.COLOR_GOLD);
        btnSave.setForeground(Color.BLACK);
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSave.putClientProperty(com.formdev.flatlaf.FlatClientProperties.STYLE, "arc: 10");
        btnSave.addActionListener(e -> saveItem());
        JButton btnCancel = new JButton("Cancelar");
        btnCancel.setPreferredSize(new Dimension(120, 40));
        btnCancel.setBackground(Color.LIGHT_GRAY);
        btnCancel.setForeground(Color.BLACK);
        btnCancel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCancel.putClientProperty(com.formdev.flatlaf.FlatClientProperties.STYLE, "arc: 10");
        btnCancel.addActionListener(e -> dispose());
        actionPanel.add(btnSave);
        actionPanel.add(btnCancel);
        cardPanel.add(actionPanel, BorderLayout.SOUTH);
        add(cardPanel);
    }
    private void saveItem() {
        if (txtId.getText().trim().isEmpty() || txtName.getText().trim().isEmpty() || 
            txtCurrentStock.getText().trim().isEmpty() || txtMinThreshold.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor complete todos los campos obligatorios.", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            double currentStock = Double.parseDouble(txtCurrentStock.getText());
            double minThreshold = Double.parseDouble(txtMinThreshold.getText());
            InventoryItem newItem = new InventoryItem(
                    txtId.getText(),
                    txtName.getText(),
                    (UnitOfMeasure) cmbBaseUnit.getSelectedItem(),
                    currentStock,
                    minThreshold
            );
            controller.addInventoryItem(newItem);
            JOptionPane.showMessageDialog(this, "¡Éxito!");
            for (java.awt.Window w : java.awt.Window.getWindows()) {
                if (w instanceof ec.espe.edu.coffeeshop.view.InventoryItemView) {
                    ((ec.espe.edu.coffeeshop.view.InventoryItemView) w).loadTableData();
                }
            }
            dispose();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El Stock y el Umbral deben ser números válidos.", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Ocurrió un error.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
