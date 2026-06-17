package ec.espe.edu.coffeeshop.view;
import ec.espe.edu.coffeeshop.controller.SupplierController;
import ec.espe.edu.coffeeshop.model.Supplier;
import ec.espe.edu.coffeeshop.utils.ThemeManager;
import javax.swing.*;
import java.awt.*;
public class SupplierAddView extends JDialog {
    private final SupplierController controller;
    private JTextField txtId;
    private JTextField txtCompanyName;
    public SupplierAddView(JFrame parent, SupplierController controller) {
        super(parent, "Registrar Nuevo Proveedor", true);
        this.controller = controller;
        setSize(600, 500);
        setLocationRelativeTo(parent);
        getContentPane().setBackground(ThemeManager.COLOR_BACKGROUND);
        setLayout(new GridBagLayout());
        JPanel cardPanel = new JPanel();
        cardPanel.setBackground(ThemeManager.COLOR_WHITE);
        cardPanel.putClientProperty(com.formdev.flatlaf.FlatClientProperties.STYLE, "arc: 20");
        cardPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        cardPanel.setLayout(new BorderLayout(10, 20));
        JLabel titleLabel = new JLabel("Registrar Nuevo Proveedor");
        titleLabel.setFont(ThemeManager.TITLE_FONT);
        titleLabel.setForeground(ThemeManager.COLOR_GOLD);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        cardPanel.add(titleLabel, BorderLayout.NORTH);
        JPanel formPanel = new JPanel(new GridLayout(2, 1, 10, 15));
        formPanel.setBackground(ThemeManager.COLOR_WHITE);
        txtId = new JTextField();
        setupTextField(txtId, "ID del Proveedor");
        txtCompanyName = new JTextField();
        setupTextField(txtCompanyName, "Nombre de la Empresa");
        formPanel.add(txtId);
        formPanel.add(txtCompanyName);
        cardPanel.add(formPanel, BorderLayout.CENTER);
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        actionPanel.setBackground(ThemeManager.COLOR_WHITE);
        JButton btnSave = new JButton("Registrar Proveedor");
        btnSave.setBackground(ThemeManager.COLOR_GOLD);
        btnSave.setForeground(Color.BLACK);
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnSave.setPreferredSize(new Dimension(150, 40));
        btnSave.addActionListener(e -> addSupplier());
        JButton btnCancel = new JButton("Cancelar");
        btnCancel.setBackground(Color.LIGHT_GRAY);
        btnCancel.setForeground(Color.BLACK);
        btnCancel.setPreferredSize(new Dimension(120, 40));
        btnCancel.addActionListener(e -> dispose());
        actionPanel.add(btnSave);
        actionPanel.add(btnCancel);
        cardPanel.add(actionPanel, BorderLayout.SOUTH);
        add(cardPanel);
    }
    private void setupTextField(JTextField textField, String placeholder) {
        textField.setPreferredSize(new Dimension(250, 40));
        textField.putClientProperty(com.formdev.flatlaf.FlatClientProperties.PLACEHOLDER_TEXT, placeholder);
        textField.putClientProperty(com.formdev.flatlaf.FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);
    }
    private void addSupplier() {
        String idStr = txtId.getText().trim();
        String companyStr = txtCompanyName.getText().trim();
        if (idStr.isEmpty() || companyStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (idStr.length() < 3) {
            JOptionPane.showMessageDialog(this, "El ID del proveedor debe tener al menos 3 caracteres.", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Supplier newSup = new Supplier(idStr, companyStr);
        if (controller.addSupplier(newSup)) {
            JOptionPane.showMessageDialog(this, "¡Éxito!");
            for (java.awt.Window w : java.awt.Window.getWindows()) {
                if (w instanceof ec.espe.edu.coffeeshop.view.SupplierView) {
                    ((ec.espe.edu.coffeeshop.view.SupplierView) w).loadTableData();
                }
            }
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Error al registrar el proveedor.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
