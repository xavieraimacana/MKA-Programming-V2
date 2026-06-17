package ec.espe.edu.coffeeshop.view;
import ec.espe.edu.coffeeshop.controller.StockEntryController;
import ec.espe.edu.coffeeshop.model.InventoryItem;
import ec.espe.edu.coffeeshop.model.StockEntry;
import ec.espe.edu.coffeeshop.model.UnitOfMeasure;
import ec.espe.edu.coffeeshop.utils.ThemeManager;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
public class StockEntryAddView extends JDialog {
    private final StockEntryController controller;
    private JTextField txtId;
    private JTextField txtQty;
    private JComboBox<UnitOfMeasure> cmbUnit;
    private JTextField txtTotalCost;
    public StockEntryAddView(JFrame parent, StockEntryController controller) {
        super(parent, "Añadir Nueva Entrada de Stock", true);
        this.controller = controller;
        setSize(600, 650);
        setLocationRelativeTo(parent);
        getContentPane().setBackground(ThemeManager.COLOR_BACKGROUND);
        setLayout(new GridBagLayout());
        JPanel cardPanel = new JPanel(new BorderLayout(20, 20));
        cardPanel.setBackground(ThemeManager.COLOR_WHITE);
        cardPanel.putClientProperty(com.formdev.flatlaf.FlatClientProperties.STYLE, "arc: 20");
        cardPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        JLabel titleLabel = new JLabel("Registrar Entrada de Stock");
        titleLabel.setFont(ThemeManager.TITLE_FONT);
        titleLabel.setForeground(ThemeManager.COLOR_GOLD);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        cardPanel.add(titleLabel, BorderLayout.NORTH);
        JPanel formPanel = new JPanel(new GridLayout(4, 1, 10, 15));
        formPanel.setBackground(ThemeManager.COLOR_WHITE);
        txtId = new JTextField();
        txtId.setPreferredSize(new Dimension(250, 40));
        txtId.putClientProperty(com.formdev.flatlaf.FlatClientProperties.PLACEHOLDER_TEXT, "ID de Entrada");
        txtQty = new JTextField();
        txtQty.setPreferredSize(new Dimension(250, 40));
        txtQty.putClientProperty(com.formdev.flatlaf.FlatClientProperties.PLACEHOLDER_TEXT, "Cantidad Recibida");
        cmbUnit = new JComboBox<>(UnitOfMeasure.values());
        cmbUnit.setPreferredSize(new Dimension(250, 40));
        txtTotalCost = new JTextField();
        txtTotalCost.setPreferredSize(new Dimension(250, 40));
        txtTotalCost.putClientProperty(com.formdev.flatlaf.FlatClientProperties.PLACEHOLDER_TEXT, "Costo Total ($)");
        formPanel.add(txtId);
        formPanel.add(txtQty);
        formPanel.add(cmbUnit);
        formPanel.add(txtTotalCost);
        cardPanel.add(formPanel, BorderLayout.CENTER);
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        actionPanel.setBackground(ThemeManager.COLOR_WHITE);
        JButton btnSave = new JButton("Guardar");
        btnSave.setPreferredSize(new Dimension(120, 40));
        btnSave.setBackground(ThemeManager.COLOR_GOLD);
        btnSave.setForeground(Color.BLACK);
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSave.putClientProperty(com.formdev.flatlaf.FlatClientProperties.STYLE, "arc: 10");
        btnSave.addActionListener(e -> saveEntry());
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
    private void saveEntry() {
        if (txtId.getText().trim().isEmpty() || txtQty.getText().trim().isEmpty() || txtTotalCost.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor complete todos los campos obligatorios.", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            double qty = Double.parseDouble(txtQty.getText());
            double cost = Double.parseDouble(txtTotalCost.getText());
            InventoryItem dummyItem = new InventoryItem("ITEM02", "Milk", ec.espe.edu.coffeeshop.model.UnitOfMeasure.LITERS, 20, 5);
            StockEntry entry = new StockEntry(
                    txtId.getText(),
                    LocalDateTime.now(),
                    qty,
                    (UnitOfMeasure) cmbUnit.getSelectedItem(),
                    cost,
                    dummyItem
            );
            controller.addStockEntry(entry);
            JOptionPane.showMessageDialog(this, "¡Éxito!");
            for (java.awt.Window w : java.awt.Window.getWindows()) {
                if (w instanceof ec.espe.edu.coffeeshop.view.StockEntryView) {
                    ((ec.espe.edu.coffeeshop.view.StockEntryView) w).loadTableData();
                }
            }
            dispose();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "La cantidad y el costo deben ser números válidos.", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Ocurrió un error.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
