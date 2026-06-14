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
 * Premium Light & Gold theme Swing Login GUI for the Coffeeshop system.
 * Uses a warm light theme aligned with elegant coffee aesthetics.
 * Supports credentials login (username and password) and dynamic i18n toggles.
 * Prompts for credentials update on first-time login.
 * 
 * @author MKA Programer, @ESPE
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

    // Light & Gold Theme Colors
    private final Color bgColor = new Color(250, 248, 245); // Warm White
    private final Color inputColor = new Color(255, 255, 255); // Pure White
    private final Color textColor = new Color(41, 37, 36); // Stone 800 (Dark Gray)
    private final Color textMutedColor = new Color(120, 113, 108); // Stone 500 (Muted Gray)
    private final Color goldPrimary = new Color(197, 160, 89); // Warm Gold
    private final Color goldSecondary = new Color(163, 128, 62); // Darker Gold
    private final Color borderColor = new Color(224, 220, 214); // Light Sand Border

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
        mainPanel.setBackground(bgColor);
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
        langPanel.setBackground(bgColor);
        
        btnEn = new JButton("EN");
        btnEs = new JButton("ES");
        
        btnEn.addActionListener(e -> updateLanguage(0));
        btnEs.addActionListener(e -> updateLanguage(1));
        
        langPanel.add(btnEn);
        JLabel divider = new JLabel("|");
        divider.setForeground(borderColor);
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
        titleLabel.setForeground(goldSecondary);
        gbc.gridy = 2;
        mainPanel.add(titleLabel, gbc);

        // 3. Subtitle
        subtitleLabel = new JLabel("University Project", JLabel.CENTER);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(textMutedColor);
        gbc.gridy = 3;
        mainPanel.add(subtitleLabel, gbc);

        // Spacer
        gbc.gridy = 4;
        mainPanel.add(Box.createVerticalStrut(20), gbc);

        // 4. Username Label
        usernameLabel = new JLabel("Username");
        usernameLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        usernameLabel.setForeground(textColor);
        gbc.gridy = 5;
        mainPanel.add(usernameLabel, gbc);

        // 5. Username Text Field
        usernameField = new JTextField();
        usernameField.setBackground(inputColor);
        usernameField.setForeground(textColor);
        usernameField.setCaretColor(textColor);
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        usernameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(borderColor, 1),
            BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        gbc.gridy = 6;
        mainPanel.add(usernameField, gbc);

        // 6. Password Label
        passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        passwordLabel.setForeground(textColor);
        gbc.gridy = 7;
        mainPanel.add(passwordLabel, gbc);

        // 7. Password Field
        passwordField = new JPasswordField();
        passwordField.setBackground(inputColor);
        passwordField.setForeground(textColor);
        passwordField.setCaretColor(textColor);
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(borderColor, 1),
            BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        gbc.gridy = 8;
        mainPanel.add(passwordField, gbc);

        // 8. Login Button
        loginButton = new JButton("Login");
        loginButton.setBackground(goldPrimary);
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        loginButton.setFocusPainted(false);
        loginButton.setBorderPainted(false);
        loginButton.setOpaque(true);
        loginButton.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.addActionListener(this::onLoginClicked);
        
        // Hover effect on Login Button
        loginButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                loginButton.setBackground(goldSecondary);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                loginButton.setBackground(goldPrimary);
            }
        });

        gbc.gridy = 9;
        gbc.insets = new Insets(15, 0, 8, 0);
        mainPanel.add(loginButton, gbc);

        // 9. Error/Status Label
        errorLabel = new JLabel("", JLabel.CENTER);
        errorLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        errorLabel.setForeground(new Color(220, 38, 38)); // Red 600
        gbc.gridy = 10;
        gbc.insets = new Insets(8, 0, 8, 0);
        mainPanel.add(errorLabel, gbc);
    }

    private void styleLangButton(JButton btn, boolean active) {
        btn.setFont(new Font("Segoe UI", active ? Font.BOLD : Font.PLAIN, 12));
        btn.setForeground(active ? goldSecondary : textMutedColor);
        btn.setBackground(bgColor);
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
            subtitleLabel.setText("Proyecto Universitario");
            styleLangButton(btnEn, false);
            styleLangButton(btnEs, true);
        } else { // English
            I18nHelper.setLocale(Locale.ENGLISH);
            usernameLabel.setText(I18nHelper.getMessage("login.username"));
            passwordLabel.setText(I18nHelper.getMessage("login.password"));
            loginButton.setText(I18nHelper.getMessage("login.prompt"));
            subtitleLabel.setText("University Project");
            styleLangButton(btnEn, true);
            styleLangButton(btnEs, false);
        }
        
        // Update error text if it was active
        if (!errorLabel.getText().isEmpty()) {
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

        Employee employee = employeeRepo.findByCredentials(username, password);
        if (employee == null) {
            errorLabel.setText(I18nHelper.getMessage("error.employee_not_found"));
        } else {
            if (employee.isChangePasswordRequired()) {
                boolean changed = showChangeCredentialsDialog(employee);
                if (changed) {
                    usernameField.setText("");
                    passwordField.setText("");
                    errorLabel.setForeground(new Color(5, 150, 105)); // Green 600 for success
                    errorLabel.setText(I18nHelper.getMessage("login.change_success"));
                }
                return;
            }

            JOptionPane.showMessageDialog(this,
                I18nHelper.getMessage("login.success") + " " + employee.getName() + "\nRole: " + employee.getRole(),
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
            
            ec.espe.edu.coffeeshop.utils.SystemContext.getInstance().setCurrentUser(employee);
            
            this.dispose();
            
            System.out.println("LOGGED IN: " + employee.getName() + " [" + employee.getRole() + "]");
            
            SwingUtilities.invokeLater(() -> {
                switch (employee.getRole()) {
                    case MANAGER:
                    case CASHIER:
                        new PosFrame(employee).setVisible(true);
                        break;
                    case BARISTA:
                        new KdsFrame(ec.espe.edu.coffeeshop.utils.SystemContext.getInstance().getKitchenManager()).setVisible(true);
                        break;
                    case WAITER:
                        new TableManagementFrame().setVisible(true);
                        break;
                }
            });
        }
    }

    private boolean showChangeCredentialsDialog(Employee employee) {
        ChangeCredentialsDialog dialog = new ChangeCredentialsDialog(this, employee, this.employeeRepo);
        dialog.setVisible(true);
        return dialog.isSucceeded();
    }
}
