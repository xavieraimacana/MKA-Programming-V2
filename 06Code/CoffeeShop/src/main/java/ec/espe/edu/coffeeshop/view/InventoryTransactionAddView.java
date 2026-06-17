package ec.espe.edu.coffeeshop.view;
import ec.espe.edu.coffeeshop.controller.InventoryTransactionController;
import ec.espe.edu.coffeeshop.model.InventoryItem;
import ec.espe.edu.coffeeshop.model.InventoryTransaction;
import ec.espe.edu.coffeeshop.model.TransactionType;
import ec.espe.edu.coffeeshop.utils.ThemeManager;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
public class InventoryTransactionAddView extends JDialog {
    private final InventoryTransactionController controller;
    private JTextField txtId;
    private JComboBox<TransactionType> cmbType;
    private JTextField txtQtyChange;
    private JTextField txtRefDoc;
    public InventoryTransactionAddView(JFrame parent, InventoryTransactionController controller) {
        super(parent, "Añadir Nueva Transacción", true);
        this.controller = controller;
        setSize(600, 650);
        setLocationRelativeTo(parent);
        getContentPane().setBackground(ThemeManager.COLOR_BACKGROUND);
        setLayout(new GridBagLayout());
        JPanel cardPanel = new JPanel(new BorderLayout(20, 20));
        cardPanel.setBackground(ThemeManager.COLOR_WHITE);
        cardPanel.putClientProperty(com.formdev.flatlaf.FlatClientProperties.STYLE, "arc: 20");
        cardPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        JLabel titleLabel = new JLabel("Registrar Transacción");
        titleLabel.setFont(ThemeManager.TITLE_FONT);
        titleLabel.setForeground(ThemeManager.COLOR_GOLD);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        cardPanel.add(titleLabel, BorderLayout.NORTH);
        JPanel formPanel = new JPanel(new GridLayout(4, 1, 10, 15));
        formPanel.setBackground(ThemeManager.COLOR_WHITE);
        txtId = new JTextField();
        txtId.setPreferredSize(new Dimension(250, 40));
        txtId.putClientProperty(com.formdev.flatlaf.FlatClientProperties.PLACEHOLDER_TEXT, "ID de Transacción");
        cmbType = new JComboBox<>(TransactionType.values());
        cmbType.setPreferredSize(new Dimension(250, 40));
        txtQtyChange = new JTextField();
        txtQtyChange.setPreferredSize(new Dimension(250, 40));
        txtQtyChange.putClientProperty(com.formdev.flatlaf.FlatClientProperties.PLACEHOLDER_TEXT, "Cambio de Cantidad (puede ser negativo)");
        txtRefDoc = new JTextField();
        txtRefDoc.setPreferredSize(new Dimension(250, 40));
        txtRefDoc.putClientProperty(com.formdev.flatlaf.FlatClientProperties.PLACEHOLDER_TEXT, "ID de Documento de Ref.");
        formPanel.add(txtId);
        formPanel.add(cmbType);
        formPanel.add(txtQtyChange);
        formPanel.add(txtRefDoc);
        cardPanel.add(formPanel, BorderLayout.CENTER);
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        actionPanel.setBackground(ThemeManager.COLOR_WHITE);
        JButton btnSave = new JButton("Guardar");
        btnSave.setPreferredSize(new Dimension(120, 40));
        btnSave.setBackground(ThemeManager.COLOR_GOLD);
        btnSave.setForeground(Color.BLACK);
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSave.putClientProperty(com.formdev.flatlaf.FlatClientProperties.STYLE, "arc: 10");
        btnSave.addActionListener(e -> saveTransaction());
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
    private void saveTransaction() {
        if (txtId.getText().trim().isEmpty() || txtQtyChange.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor complete todos los campos obligatorios.", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            double qty = Double.parseDouble(txtQtyChange.getText());
            InventoryItem dummyItem = new InventoryItem("ITEM01", "Coffee Beans", ec.espe.edu.coffeeshop.model.UnitOfMeasure.KILOGRAMS, 10, 2);
            InventoryTransaction tx = new InventoryTransaction(
                    txtId.getText(),
                    LocalDateTime.now(),
                    (TransactionType) cmbType.getSelectedItem(),
                    qty,
                    txtRefDoc.getText(),
                    dummyItem
            );
            controller.addTransaction(tx);
            JOptionPane.showMessageDialog(this, "¡Éxito!");
            for (java.awt.Window w : java.awt.Window.getWindows()) {
                if (w instanceof ec.espe.edu.coffeeshop.view.InventoryTransactionView) {
                    ((ec.espe.edu.coffeeshop.view.InventoryTransactionView) w).loadTableData();
                }
            }
            dispose();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "La cantidad debe ser un número válido.", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Ocurrió un error.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
