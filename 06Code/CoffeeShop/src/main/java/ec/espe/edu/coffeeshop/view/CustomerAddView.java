package ec.espe.edu.coffeeshop.view;
import com.formdev.flatlaf.FlatClientProperties;
import ec.espe.edu.coffeeshop.controller.CustomerController;
import ec.espe.edu.coffeeshop.model.Customer;
import ec.espe.edu.coffeeshop.utils.ThemeManager;
import javax.swing.*;
import java.awt.*;
public class CustomerAddView extends JDialog {
    private final CustomerController controller;
    private JTextField txtId;
    private JTextField txtName;
    private JTextField txtTaxId;
    private JTextField txtLoyaltyPoints;
    public CustomerAddView(JFrame parent, CustomerController controller) {
        super(parent, "Registrar Nuevo Cliente", true);
        this.controller = controller;
        setSize(600, 650);
        setLocationRelativeTo(parent);
        getContentPane().setBackground(ThemeManager.COLOR_BACKGROUND);
        setLayout(new GridBagLayout());
        JPanel cardPanel = new JPanel(new BorderLayout(10, 10));
        cardPanel.setBackground(ThemeManager.COLOR_WHITE);
        cardPanel.putClientProperty(FlatClientProperties.STYLE, "arc: 20");
        cardPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        JLabel titleLabel = new JLabel("Registrar Nuevo Cliente");
        titleLabel.setFont(ThemeManager.TITLE_FONT);
        titleLabel.setForeground(ThemeManager.COLOR_GOLD);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        cardPanel.add(titleLabel, BorderLayout.NORTH);
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(ThemeManager.COLOR_WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.weightx = 1.0;
        txtId = new JTextField();
        txtId.setPreferredSize(new Dimension(250, 40));
        txtId.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "ID del Cliente");
        txtId.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);
        txtName = new JTextField();
        txtName.setPreferredSize(new Dimension(250, 40));
        txtName.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nombre Completo");
        txtName.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);
        txtTaxId = new JTextField();
        txtTaxId.setPreferredSize(new Dimension(250, 40));
        txtTaxId.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "ID Fiscal");
        txtTaxId.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);
        txtLoyaltyPoints = new JTextField();
        txtLoyaltyPoints.setPreferredSize(new Dimension(250, 40));
        txtLoyaltyPoints.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Puntos de Lealtad");
        txtLoyaltyPoints.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);
        gbc.gridy = 0; formPanel.add(txtId, gbc);
        gbc.gridy = 1; formPanel.add(txtName, gbc);
        gbc.gridy = 2; formPanel.add(txtTaxId, gbc);
        gbc.gridy = 3; formPanel.add(txtLoyaltyPoints, gbc);
        cardPanel.add(formPanel, BorderLayout.CENTER);
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        actionPanel.setBackground(ThemeManager.COLOR_WHITE);
        actionPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        JButton btnSave = new JButton("Registrar");
        btnSave.setPreferredSize(new Dimension(120, 40));
        btnSave.setBackground(ThemeManager.COLOR_GOLD);
        btnSave.setForeground(Color.BLACK);
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnSave.addActionListener(e -> addCustomer());
        JButton btnCancel = new JButton("Cancelar");
        btnCancel.setPreferredSize(new Dimension(120, 40));
        btnCancel.setBackground(Color.LIGHT_GRAY);
        btnCancel.setForeground(Color.BLACK);
        btnCancel.addActionListener(e -> dispose());
        actionPanel.add(btnSave);
        actionPanel.add(btnCancel);
        cardPanel.add(actionPanel, BorderLayout.SOUTH);
        add(cardPanel);
    }
    private void addCustomer() {
        String idStr = txtId.getText().trim();
        String nameStr = txtName.getText().trim();
        String taxIdStr = txtTaxId.getText().trim();
        String loyaltyPointsStr = txtLoyaltyPoints.getText().trim();
        if (idStr.isEmpty() || nameStr.isEmpty() || taxIdStr.isEmpty() || loyaltyPointsStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (idStr.length() < 3) {
            JOptionPane.showMessageDialog(this, "El ID del cliente debe tener al menos 3 caracteres.", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (nameStr.matches(".*\\\\d.*")) {
            JOptionPane.showMessageDialog(this, "El nombre no puede contener números.", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int loyaltyPoints;
        try {
            loyaltyPoints = Integer.parseInt(loyaltyPointsStr);
            if (loyaltyPoints < 0) {
                JOptionPane.showMessageDialog(this, "Los puntos de lealtad no pueden ser negativos.", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Los puntos de lealtad deben ser un número entero válido.", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Customer newCust = new Customer(idStr, nameStr, taxIdStr, loyaltyPoints);
        if (controller.addCustomer(newCust)) {
            JOptionPane.showMessageDialog(this, "¡Éxito!");
            for (java.awt.Window w : java.awt.Window.getWindows()) {
                if (w instanceof ec.espe.edu.coffeeshop.view.CustomerView) {
                    ((ec.espe.edu.coffeeshop.view.CustomerView) w).loadTableData();
                }
            }
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Error al registrar cliente.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
