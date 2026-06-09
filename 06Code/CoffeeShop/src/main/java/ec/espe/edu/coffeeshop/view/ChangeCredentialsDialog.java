package ec.espe.edu.coffeeshop.view;

import ec.espe.edu.coffeeshop.model.Employee;
import ec.espe.edu.coffeeshop.repository.MongoEmployeeRepository;
import ec.espe.edu.coffeeshop.utils.I18nHelper;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Custom, premium styled JDialog for updating employee credentials.
 * Matches the dark stone and amber color palette of the LoginFrame.
 * 
 * @author Anthony Aimacaña, MKA Programer, @ESPE
 */
public class ChangeCredentialsDialog extends JDialog {
    private final Employee employee;
    private final MongoEmployeeRepository employeeRepo;
    private boolean succeeded = false;

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JLabel errorLabel;
    
    private JLabel titleLabel;
    private JLabel userLabel;
    private JLabel passLabel;
    private JLabel confirmLabel;
    private JButton saveButton;
    private JButton cancelButton;

    public ChangeCredentialsDialog(Frame parent, Employee employee, MongoEmployeeRepository employeeRepo) {
        super(parent, I18nHelper.getMessage("login.change_pwd_title"), true);
        this.employee = employee;
        this.employeeRepo = employeeRepo;

        setSize(380, 420);
        setLocationRelativeTo(parent);
        setResizable(false);
        
        // Dark theme layout
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(new Color(28, 25, 23)); // Stone 900
        contentPanel.setBorder(new EmptyBorder(25, 30, 25, 30));
        setContentPane(contentPanel);

        initializeComponents(contentPanel);
    }

    private void initializeComponents(JPanel panel) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 0, 6, 0);
        gbc.weightx = 1.0;

        // Title
        titleLabel = new JLabel(I18nHelper.getMessage("login.change_pwd_title"), JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(245, 158, 11)); // Amber 500
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        // Spacer
        gbc.gridy = 1;
        panel.add(Box.createVerticalStrut(10), gbc);

        // Reset gridwidth to 1
        gbc.gridwidth = 1;

        // Username Label
        userLabel = new JLabel(I18nHelper.getMessage("login.new_username"));
        userLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        userLabel.setForeground(new Color(231, 229, 228)); // Stone 200
        gbc.gridy = 2;
        panel.add(userLabel, gbc);

        // Username Field
        usernameField = new JTextField(employee.getUsername());
        styleTextField(usernameField);
        gbc.gridy = 3;
        panel.add(usernameField, gbc);

        // Password Label
        passLabel = new JLabel(I18nHelper.getMessage("login.new_password"));
        passLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        passLabel.setForeground(new Color(231, 229, 228)); // Stone 200
        gbc.gridy = 4;
        panel.add(passLabel, gbc);

        // Password Field
        passwordField = new JPasswordField();
        styleTextField(passwordField);
        gbc.gridy = 5;
        panel.add(passwordField, gbc);

        // Confirm Password Label
        confirmLabel = new JLabel(I18nHelper.getMessage("login.confirm_password"));
        confirmLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        confirmLabel.setForeground(new Color(231, 229, 228)); // Stone 200
        gbc.gridy = 6;
        panel.add(confirmLabel, gbc);

        // Confirm Password Field
        confirmPasswordField = new JPasswordField();
        styleTextField(confirmPasswordField);
        gbc.gridy = 7;
        panel.add(confirmPasswordField, gbc);

        // Error Label
        errorLabel = new JLabel("", JLabel.CENTER);
        errorLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        errorLabel.setForeground(new Color(239, 68, 68)); // Red 500
        gbc.gridy = 8;
        panel.add(errorLabel, gbc);

        // Spacer
        gbc.gridy = 9;
        panel.add(Box.createVerticalStrut(5), gbc);

        // Buttons Panel (Horizontal layout for Save and Cancel)
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        buttonPanel.setBackground(new Color(28, 25, 23));

        String saveText = I18nHelper.getLocale().getLanguage().equals("es") ? "Guardar" : "Save";
        String cancelText = I18nHelper.getLocale().getLanguage().equals("es") ? "Cancelar" : "Cancel";

        saveButton = new JButton(saveText);
        saveButton.setBackground(new Color(217, 119, 6)); // Amber 600
        saveButton.setForeground(Color.WHITE);
        saveButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        saveButton.setFocusPainted(false);
        saveButton.setBorderPainted(false);
        saveButton.setOpaque(true);
        saveButton.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
        saveButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        saveButton.addActionListener(this::onSaveClicked);

        cancelButton = new JButton(cancelText);
        cancelButton.setBackground(new Color(68, 64, 60)); // Stone 600
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        cancelButton.setFocusPainted(false);
        cancelButton.setBorderPainted(false);
        cancelButton.setOpaque(true);
        cancelButton.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
        cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        gbc.gridy = 10;
        gbc.insets = new Insets(10, 0, 0, 0);
        panel.add(buttonPanel, gbc);
    }

    private void styleTextField(JTextField field) {
        field.setBackground(new Color(41, 37, 36)); // Stone 800
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(68, 64, 60), 1),
            BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
    }

    private void onSaveClicked(ActionEvent e) {
        String newUsername = usernameField.getText().trim();
        String newPassword = new String(passwordField.getPassword()).trim();
        String confirmPassword = new String(confirmPasswordField.getPassword()).trim();

        if (newUsername.isEmpty() || newPassword.isEmpty()) {
            errorLabel.setText(I18nHelper.getMessage("error.invalid_option"));
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            errorLabel.setText(I18nHelper.getMessage("error.password_mismatch"));
            return;
        }

        // Prohibir credenciales por defecto
        if ((newUsername.equals("anthony") && newPassword.equals("mka123")) ||
            (newUsername.equals("mateo") && newPassword.equals("mateo123")) ||
            (newUsername.equals("kevin") && newPassword.equals("kevin123")) ||
            (newUsername.equals("waiter") && newPassword.equals("waiter123"))) {
            
            String errorMsg = I18nHelper.getLocale().getLanguage().equals("es") 
                ? "¡No puedes usar las credenciales por defecto!" 
                : "Cannot use default credentials!";
            errorLabel.setText(errorMsg);
            return;
        }

        // Actualizar datos en MongoDB
        employee.setUsername(newUsername);
        employee.setPassword(newPassword);
        employee.setChangePasswordRequired(false);
        
        try {
            employeeRepo.save(employee);
            succeeded = true;
            dispose();
        } catch (Exception ex) {
            String errorMsg = I18nHelper.getLocale().getLanguage().equals("es") 
                ? "Error de conexión con MongoDB" 
                : "MongoDB connection error";
            errorLabel.setText(errorMsg);
        }
    }

    public boolean isSucceeded() {
        return succeeded;
    }
}
