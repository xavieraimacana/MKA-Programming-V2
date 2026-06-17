package ec.espe.edu.coffeeshop.view;
import com.formdev.flatlaf.FlatClientProperties;
import ec.espe.edu.coffeeshop.controller.EmployeeController;
import ec.espe.edu.coffeeshop.model.Employee;
import ec.espe.edu.coffeeshop.utils.ThemeManager;
import javax.swing.*;
import java.awt.*;
public class LoginView extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private EmployeeController employeeController;
    public LoginView() {
        this.employeeController = new EmployeeController();
        setTitle("Coffee Shop - Autenticación");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 
        setResizable(false);
        getContentPane().setBackground(ThemeManager.COLOR_BACKGROUND);
        setLayout(new GridBagLayout()); 
        JPanel cardPanel = new JPanel(new GridBagLayout());
        cardPanel.setBackground(ThemeManager.COLOR_WHITE);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
                BorderFactory.createEmptyBorder(40, 50, 40, 50) 
        ));
        cardPanel.putClientProperty(FlatClientProperties.STYLE, "arc: 20");
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0); 
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        JLabel lblTitle = new JLabel("COFFEE SHOP");
        lblTitle.setFont(ThemeManager.TITLE_FONT);
        lblTitle.setForeground(ThemeManager.COLOR_GOLD);
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        JLabel lblSubtitle = new JLabel("Ingresa a tu cuenta");
        lblSubtitle.setFont(ThemeManager.MAIN_FONT);
        lblSubtitle.setForeground(Color.GRAY);
        lblSubtitle.setHorizontalAlignment(SwingConstants.CENTER);
        txtUsername = new JTextField(20);
        txtUsername.setPreferredSize(new Dimension(300, 45));
        txtUsername.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Usuario");
        txtUsername.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);
        txtPassword = new JPasswordField(20);
        txtPassword.setPreferredSize(new Dimension(300, 45));
        txtPassword.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Contraseña");
        txtPassword.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);
        JButton btnLogin = new JButton("Ingresar");
        btnLogin.setPreferredSize(new Dimension(300, 45));
        btnLogin.setBackground(ThemeManager.COLOR_GOLD);
        btnLogin.setForeground(Color.BLACK);
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.addActionListener(e -> attemptLogin());
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 5, 0);
        cardPanel.add(lblTitle, gbc);
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 30, 0);
        cardPanel.add(lblSubtitle, gbc);
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 15, 0);
        cardPanel.add(txtUsername, gbc);
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, 30, 0);
        cardPanel.add(txtPassword, gbc);
        gbc.gridy = 4;
        gbc.insets = new Insets(0, 0, 0, 0);
        cardPanel.add(btnLogin, gbc);
        add(cardPanel);
    }
    private void attemptLogin() {
        String username = txtUsername.getText();
        String password = new String(txtPassword.getPassword());
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor ingresa usuario y contraseña.", "Error de Ingreso", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Employee emp = employeeController.authenticate(username, password);        
        if (emp != null) {
            JOptionPane.showMessageDialog(this, "Bienvenido de nuevo, " + emp.getName() + "!", "Acceso Exitoso", JOptionPane.INFORMATION_MESSAGE);
            this.dispose(); 
            SwingUtilities.invokeLater(() -> {
                MainMenuView mainView = new MainMenuView(emp);
                mainView.setVisible(true);
            });
        } else {
            JOptionPane.showMessageDialog(this, "Credenciales inválidas. Verifica en MongoDB.", "Acceso Denegado", JOptionPane.ERROR_MESSAGE);
        }
    }
}
