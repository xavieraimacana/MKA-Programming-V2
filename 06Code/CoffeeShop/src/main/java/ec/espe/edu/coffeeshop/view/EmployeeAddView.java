package ec.espe.edu.coffeeshop.view;
import com.formdev.flatlaf.FlatClientProperties;
import ec.espe.edu.coffeeshop.controller.EmployeeController;
import ec.espe.edu.coffeeshop.model.Employee;
import ec.espe.edu.coffeeshop.utils.ThemeManager;
import javax.swing.*;
import java.awt.*;
public class EmployeeAddView extends JDialog {
    private final EmployeeController controller;
    private JTextField txtId;
    private JTextField txtName;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JComboBox<String> cmbRole;
    public EmployeeAddView(JFrame parent, EmployeeController controller) {
        super(parent, "Registrar Nuevo Empleado", true);
        this.controller = controller;
        setSize(600, 650);
        setLocationRelativeTo(parent);
        getContentPane().setBackground(ThemeManager.COLOR_BACKGROUND);
        setLayout(new GridBagLayout());
        JPanel cardPanel = new JPanel(new BorderLayout(10, 10));
        cardPanel.setBackground(ThemeManager.COLOR_WHITE);
        cardPanel.putClientProperty(FlatClientProperties.STYLE, "arc: 20");
        cardPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        JLabel titleLabel = new JLabel("Registrar Nuevo Empleado");
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
        txtId.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "ID Emp");
        txtId.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);
        txtName = new JTextField();
        txtName.setPreferredSize(new Dimension(250, 40));
        txtName.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nombre Completo");
        txtName.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);
        txtUsername = new JTextField();
        txtUsername.setPreferredSize(new Dimension(250, 40));
        txtUsername.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nombre de Usuario");
        txtUsername.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);
        txtPassword = new JPasswordField();
        txtPassword.setPreferredSize(new Dimension(250, 40));
        txtPassword.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Contraseña");
        txtPassword.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);
        cmbRole = new JComboBox<>(new String[]{"BARISTA", "CASHIER", "MANAGER"});
        cmbRole.setBackground(ThemeManager.COLOR_WHITE);
        cmbRole.setPreferredSize(new Dimension(250, 40));
        gbc.gridy = 0; formPanel.add(txtId, gbc);
        gbc.gridy = 1; formPanel.add(txtName, gbc);
        gbc.gridy = 2; formPanel.add(txtUsername, gbc);
        gbc.gridy = 3; formPanel.add(txtPassword, gbc);
        gbc.gridy = 4;
        JPanel rolePanel = new JPanel(new BorderLayout(5, 5));
        rolePanel.setBackground(ThemeManager.COLOR_WHITE);
        JLabel lblRole = new JLabel("Rol:");
        lblRole.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        rolePanel.add(lblRole, BorderLayout.NORTH);
        rolePanel.add(cmbRole, BorderLayout.CENTER);
        formPanel.add(rolePanel, gbc);
        cardPanel.add(formPanel, BorderLayout.CENTER);
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        actionPanel.setBackground(ThemeManager.COLOR_WHITE);
        actionPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        JButton btnSave = new JButton("Registrar");
        btnSave.setPreferredSize(new Dimension(120, 40));
        btnSave.setBackground(ThemeManager.COLOR_GOLD);
        btnSave.setForeground(Color.BLACK);
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnSave.addActionListener(e -> addEmployee());
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
    private void addEmployee() {
        String pass = new String(txtPassword.getPassword());
        String idStr = txtId.getText().trim();
        String nameStr = txtName.getText().trim();
        String userStr = txtUsername.getText().trim();
        if (idStr.isEmpty() || nameStr.isEmpty() || userStr.isEmpty() || pass.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (idStr.length() < 3) {
            JOptionPane.showMessageDialog(this, "El ID del empleado debe tener al menos 3 caracteres.", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (nameStr.matches(".*\\d.*")) {
            JOptionPane.showMessageDialog(this, "El nombre no puede contener números.", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (pass.length() < 4) {
            JOptionPane.showMessageDialog(this, "La contraseña debe tener al menos 4 caracteres.", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Employee newEmp = new Employee(
                idStr,
                nameStr,
                userStr,
                pass,
                cmbRole.getSelectedItem().toString()
        );
        if (controller.addEmployee(newEmp)) {
            JOptionPane.showMessageDialog(this, "¡Éxito!");
            for (java.awt.Window w : java.awt.Window.getWindows()) {
                if (w instanceof ec.espe.edu.coffeeshop.view.EmployeeView) {
                    ((ec.espe.edu.coffeeshop.view.EmployeeView) w).loadTableData();
                }
            }
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Error al registrar empleado.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
