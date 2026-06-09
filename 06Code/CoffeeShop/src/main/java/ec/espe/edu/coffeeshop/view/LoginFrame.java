package ec.espe.edu.coffeeshop.view;

import ec.espe.edu.coffeeshop.model.Employee;
import ec.espe.edu.coffeeshop.repository.MongoDBConnection;
import ec.espe.edu.coffeeshop.repository.MongoEmployeeRepository;
import ec.espe.edu.coffeeshop.utils.I18nHelper;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Locale;

/**
 * Modern, custom-styled Swing Login GUI for the Coffeeshop system.
 * Uses a warm dark theme aligned with coffeeshop aesthetics.
 * Supports credentials login (username and password) and flat i18n toggles.
 * Prompts for credentials update on first-time login.
 * 
 * @author Anthony Aimacaña, MKA Programer, @ESPE
 */
public class LoginFrame extends JFrame {
    private final MongoEmployeeRepository employeeRepo;
    
    // UI Components
    private JPanel mainPanel;
    private JLabel titleLabel;
    private JLabel subtitleLabel;
    
    private JLabel usernameLabel;
    private JTextField usernameField;
    
    private JLabel passwordLabel;
    private JPasswordField passwordField;
    
    private JButton loginButton;
    private JLabel errorLabel;
    
    private JButton btnEn;
    private JButton btnEs;

    public LoginFrame() {
        this.employeeRepo = new MongoEmployeeRepository();
        
        // Window Configuration
        setTitle("Coffeeshop System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 520);
        setLocationRelativeTo(null); // Center on screen
        setResizable(false);
        
        // Main Container Setup
        mainPanel = new JPanel();
        mainPanel.setBackground(new Color(28, 25, 23)); // Stone 900 (Dark background)
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBorder(new EmptyBorder(30, 40, 30, 40));
        setContentPane(mainPanel);

        initializeComponents();
        
        // Default to English locale first (matches I18nHelper default)
        updateLanguage(0);
    }

    private void initializeComponents() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 0, 8, 0);

        // 1. Language selector panel (Flat Buttons)
        JPanel langPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 2, 0));
        langPanel.setBackground(new Color(28, 25, 23));
        
        btnEn = new JButton("EN");
        btnEs = new JButton("ES");
        
        btnEn.addActionListener(e -> updateLanguage(0));
        btnEs.addActionListener(e -> updateLanguage(1));
        
        langPanel.add(btnEn);
        JLabel divider = new JLabel("|");
        divider.setForeground(new Color(68, 64, 60));
        langPanel.add(divider);
        langPanel.add(btnEs);
        
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        mainPanel.add(langPanel, gbc);

        // Spacer
        gbc.gridy = 1;
        mainPanel.add(Box.createVerticalStrut(10), gbc);

        // 2. Title Label (Coffeeshop)
        titleLabel = new JLabel("Coffeeshop", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(new Color(245, 158, 11)); // Amber 500
        gbc.gridy = 2;
        mainPanel.add(titleLabel, gbc);

        // 3. Subtitle
        subtitleLabel = new JLabel("Enterprise Portal", JLabel.CENTER);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(168, 162, 158)); // Stone 400
        gbc.gridy = 3;
        mainPanel.add(subtitleLabel, gbc);

        // Spacer
        gbc.gridy = 4;
        mainPanel.add(Box.createVerticalStrut(20), gbc);

        // 4. Username Label
        usernameLabel = new JLabel("Username");
        usernameLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        usernameLabel.setForeground(new Color(231, 229, 228)); // Stone 200
        gbc.gridy = 5;
        mainPanel.add(usernameLabel, gbc);

        // 5. Username Text Field
        usernameField = new JTextField();
        usernameField.setBackground(new Color(41, 37, 36)); // Stone 800
        usernameField.setForeground(Color.WHITE);
        usernameField.setCaretColor(Color.WHITE);
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        usernameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(68, 64, 60), 1),
            BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        gbc.gridy = 6;
        mainPanel.add(usernameField, gbc);

        // 6. Password Label
        passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        passwordLabel.setForeground(new Color(231, 229, 228)); // Stone 200
        gbc.gridy = 7;
        mainPanel.add(passwordLabel, gbc);

        // 7. Password Field
        passwordField = new JPasswordField();
        passwordField.setBackground(new Color(41, 37, 36)); // Stone 800
        passwordField.setForeground(Color.WHITE);
        passwordField.setCaretColor(Color.WHITE);
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(68, 64, 60), 1),
            BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        gbc.gridy = 8;
        mainPanel.add(passwordField, gbc);

        // 8. Login Button
        loginButton = new JButton("Login");
        loginButton.setBackground(new Color(217, 119, 6)); // Amber 600
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        loginButton.setFocusPainted(false);
        loginButton.setBorderPainted(false);
        loginButton.setOpaque(true);
        loginButton.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.addActionListener(this::onLoginClicked);
        gbc.gridy = 9;
        gbc.insets = new Insets(15, 0, 8, 0);
        mainPanel.add(loginButton, gbc);

        // 9. Error/Status Label
        errorLabel = new JLabel("", JLabel.CENTER);
        errorLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        errorLabel.setForeground(new Color(239, 68, 68)); // Red 500
        gbc.gridy = 10;
        gbc.insets = new Insets(8, 0, 8, 0);
        mainPanel.add(errorLabel, gbc);
    }

    private void styleLangButton(JButton btn, boolean active) {
        btn.setFont(new Font("Segoe UI", active ? Font.BOLD : Font.PLAIN, 12));
        btn.setForeground(active ? new Color(245, 158, 11) : new Color(168, 162, 158));
        btn.setBackground(new Color(28, 25, 23));
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void updateLanguage(int index) {
        if (index == 1) { // Spanish
            I18nHelper.setLocale(new Locale("es"));
            usernameLabel.setText(I18nHelper.getMessage("login.username"));
            passwordLabel.setText(I18nHelper.getMessage("login.password"));
            loginButton.setText(I18nHelper.getMessage("login.prompt"));
            subtitleLabel.setText("Portal Empresarial");
            styleLangButton(btnEn, false);
            styleLangButton(btnEs, true);
        } else { // English
            I18nHelper.setLocale(Locale.ENGLISH);
            usernameLabel.setText(I18nHelper.getMessage("login.username"));
            passwordLabel.setText(I18nHelper.getMessage("login.password"));
            loginButton.setText(I18nHelper.getMessage("login.prompt"));
            subtitleLabel.setText("Enterprise Portal");
            styleLangButton(btnEn, true);
            styleLangButton(btnEs, false);
        }
        
        // Update error text if it was active
        if (!errorLabel.getText().isEmpty()) {
            // Check if error is password mismatch or employee not found to localized properly
            if (errorLabel.getText().equals("Passwords do not match!") || errorLabel.getText().equals("¡Las contraseñas no coinciden!")) {
                errorLabel.setText(I18nHelper.getMessage("error.password_mismatch"));
            } else if (errorLabel.getText().equals("Credentials updated successfully! Please log in again.") || errorLabel.getText().equals("¡Credenciales actualizadas con éxito! Inicie sesión nuevamente.")) {
                errorLabel.setText(I18nHelper.getMessage("login.change_success"));
            } else {
                errorLabel.setText(I18nHelper.getMessage("error.employee_not_found"));
            }
        }
    }

    private void onLoginClicked(ActionEvent e) {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        errorLabel.setText("");

        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText(I18nHelper.getMessage("error.invalid_option"));
            return;
        }

        // Buscar el empleado mediante sus credenciales en la base de datos remota de MongoDB
        Employee employee = employeeRepo.findByCredentials(username, password);
        if (employee == null) {
            errorLabel.setText(I18nHelper.getMessage("error.employee_not_found"));
        } else {
            // Verificar si requiere cambiar credenciales
            if (employee.isChangePasswordRequired()) {
                boolean changed = showChangeCredentialsDialog(employee);
                if (changed) {
                    // Limpiar campos y forzar login con las nuevas credenciales
                    usernameField.setText("");
                    passwordField.setText("");
                    errorLabel.setForeground(new Color(34, 197, 94)); // Green 500 for success
                    errorLabel.setText(I18nHelper.getMessage("login.change_success"));
                }
                return;
            }

            // Login Exitoso normal
            JOptionPane.showMessageDialog(this,
                I18nHelper.getMessage("login.success") + " " + employee.getName() + "\nRole: " + employee.getRole(),
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
            
            // Cerrar ventana de login
            this.dispose();
            
            // Notificamos por consola y cerramos conexiones de prueba
            System.out.println("LOGGED IN: " + employee.getName() + " [" + employee.getRole() + "]");
            MongoDBConnection.getInstance().close();
            System.exit(0);
        }
    }

    /**
     * Shows a customized modal dialog prompting the user to update default credentials.
     */
    private boolean showChangeCredentialsDialog(Employee employee) {
        ChangeCredentialsDialog dialog = new ChangeCredentialsDialog(this, employee, this.employeeRepo);
        dialog.setVisible(true);
        return dialog.isSucceeded();
    }
}
