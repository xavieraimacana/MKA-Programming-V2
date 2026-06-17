package ec.espe.edu.coffeeshop.view;
import com.formdev.flatlaf.FlatClientProperties;
import ec.espe.edu.coffeeshop.controller.CashRegisterSessionController;
import ec.espe.edu.coffeeshop.model.CashRegisterSession;
import ec.espe.edu.coffeeshop.utils.ThemeManager;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
public class CashRegisterSessionAddView extends JDialog {
    private final CashRegisterSessionController controller;
    private JTextField txtSessionId;
    private JTextField txtStartingFloat;
    private JTextField txtExpectedSystemCash;
    private JTextField txtDeclaredPhysicalCash;
    public CashRegisterSessionAddView(JFrame parent, CashRegisterSessionController controller) {
        super(parent, "Abrir Nueva Sesión de Caja", true);
        this.controller = controller;
        setSize(600, 550);
        setLocationRelativeTo(parent);
        getContentPane().setBackground(ThemeManager.COLOR_BACKGROUND);
        setLayout(new GridBagLayout());
        JPanel cardPanel = new JPanel();
        cardPanel.setBackground(ThemeManager.COLOR_WHITE);
        cardPanel.putClientProperty(FlatClientProperties.STYLE, "arc: 20");
        cardPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        cardPanel.setLayout(new BorderLayout(10, 20));
        JLabel titleLabel = new JLabel("Abrir Sesión de Caja");
        titleLabel.setFont(ThemeManager.TITLE_FONT);
        titleLabel.setForeground(ThemeManager.COLOR_GOLD);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        cardPanel.add(titleLabel, BorderLayout.NORTH);
        JPanel formPanel = new JPanel(new GridLayout(4, 1, 10, 15));
        formPanel.setBackground(ThemeManager.COLOR_WHITE);
        txtSessionId = new JTextField();
        setupTextField(txtSessionId, "ID de Sesión");
        txtStartingFloat = new JTextField();
        setupTextField(txtStartingFloat, "Monto Inicial en Caja");
        txtExpectedSystemCash = new JTextField();
        setupTextField(txtExpectedSystemCash, "Efectivo Esperado en Sistema");
        txtDeclaredPhysicalCash = new JTextField();
        setupTextField(txtDeclaredPhysicalCash, "Efectivo Físico Declarado");
        formPanel.add(txtSessionId);
        formPanel.add(txtStartingFloat);
        formPanel.add(txtExpectedSystemCash);
        formPanel.add(txtDeclaredPhysicalCash);
        cardPanel.add(formPanel, BorderLayout.CENTER);
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        actionPanel.setBackground(ThemeManager.COLOR_WHITE);
        JButton btnSave = new JButton("Abrir Sesión");
        btnSave.setBackground(ThemeManager.COLOR_GOLD);
        btnSave.setForeground(Color.BLACK);
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnSave.setPreferredSize(new Dimension(150, 40));
        btnSave.addActionListener(e -> addSession());
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
        textField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, placeholder);
        textField.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);
    }
    private void addSession() {
        String idStr = txtSessionId.getText().trim();
        String startingFloatStr = txtStartingFloat.getText().trim();
        String expectedCashStr = txtExpectedSystemCash.getText().trim();
        String declaredCashStr = txtDeclaredPhysicalCash.getText().trim();
        if (idStr.isEmpty() || startingFloatStr.isEmpty() || expectedCashStr.isEmpty() || declaredCashStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
            return;
        }
        double startingFloat, expectedCash, declaredCash;
        try {
            startingFloat = Double.parseDouble(startingFloatStr);
            expectedCash = Double.parseDouble(expectedCashStr);
            declaredCash = Double.parseDouble(declaredCashStr);
            if (startingFloat < 0 || expectedCash < 0 || declaredCash < 0) {
                JOptionPane.showMessageDialog(this, "Los montos no pueden ser negativos.", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese montos numéricos válidos.", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
            return;
        }
        CashRegisterSession newSession = new CashRegisterSession(
            idStr, 
            LocalDateTime.now(), 
            LocalDateTime.now(), 
            startingFloat, 
            expectedCash, 
            declaredCash, 
            declaredCash - expectedCash
        );
        if (controller.addSession(newSession)) {
            JOptionPane.showMessageDialog(this, "¡Sesión abierta con éxito!");
            for (java.awt.Window w : java.awt.Window.getWindows()) {
                if (w instanceof ec.espe.edu.coffeeshop.view.CashRegisterSessionView) {
                    ((ec.espe.edu.coffeeshop.view.CashRegisterSessionView) w).loadTableData();
                }
            }
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Error al abrir la sesión.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
