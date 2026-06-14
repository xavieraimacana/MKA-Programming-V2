package ec.espe.edu.coffeeshop.view;

import ec.espe.edu.coffeeshop.model.Ingredient;
import ec.espe.edu.coffeeshop.repository.MongoIngredientRepository;
import ec.espe.edu.coffeeshop.utils.I18nHelper;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Administrative GUI for managing coffee shop raw ingredients (CRUD operations - Light & Gold Theme).
 * 
 * @author MKA Programmers, ESPE
 */
public class ManageInventoryDialog extends JDialog {
    private final MongoIngredientRepository ingredientRepo;
    private JTable inventoryTable;
    private DefaultTableModel tableModel;

    // Form Fields
    private JTextField txtId;
    private JTextField txtName;
    private JTextField txtStock;
    private JTextField txtUnit;
    private JTextField txtMinAlert;

    // Colors
    private final Color bgColor = new Color(250, 248, 245); // Warm White
    private final Color inputColor = new Color(255, 255, 255); // Pure White
    private final Color textColor = new Color(41, 37, 36); // Dark Gray
    private final Color goldPrimary = new Color(197, 160, 89); // Gold
    private final Color goldSecondary = new Color(163, 128, 62); // Darker Gold
    private final Color borderColor = new Color(224, 220, 214); // Sand Border

    public ManageInventoryDialog(Frame parent) {
        super(parent, I18nHelper.getMessage("menu.manage_inv"), true);
        this.ingredientRepo = new MongoIngredientRepository();
        
        setSize(750, 480);
        setLocationRelativeTo(parent);
        
        JPanel mainPanel = new JPanel(new BorderLayout(15, 0));
        mainPanel.setBackground(bgColor);
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        setContentPane(mainPanel);

        // Header
        JLabel titleLbl = new JLabel(I18nHelper.getMessage("inv.panel"), JLabel.CENTER);
        titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 15));
        titleLbl.setForeground(goldSecondary);
        titleLbl.setBorder(new EmptyBorder(0, 0, 10, 0));
        mainPanel.add(titleLbl, BorderLayout.NORTH);

        // Left Side: Inventory Table View
        tableModel = new DefaultTableModel(new Object[]{
                I18nHelper.getMessage("general.id"), 
                I18nHelper.getMessage("general.name"), 
                I18nHelper.getMessage("inv.quantity"), 
                I18nHelper.getMessage("inv.min_threshold"), 
                I18nHelper.getMessage("inv.status")
            }, 0);
        inventoryTable = new JTable(tableModel);
        inventoryTable.setBackground(inputColor);
        inventoryTable.setForeground(textColor);
        inventoryTable.setGridColor(borderColor);
        inventoryTable.getTableHeader().setBackground(new Color(245, 242, 237));
        inventoryTable.getTableHeader().setForeground(textColor);
        
        // Listener to load selected item to form
        inventoryTable.getSelectionModel().addListSelectionListener(e -> {
            int row = inventoryTable.getSelectedRow();
            if (row >= 0) {
                txtId.setText(tableModel.getValueAt(row, 0).toString());
                txtName.setText(tableModel.getValueAt(row, 1).toString());
                txtStock.setText(tableModel.getValueAt(row, 2).toString());
                txtUnit.setText(tableModel.getValueAt(row, 3).toString());
                txtMinAlert.setText(tableModel.getValueAt(row, 4).toString());
                txtId.setEditable(false);
            }
        });

        JScrollPane scrollPane = new JScrollPane(inventoryTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(borderColor));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Right Side: Editor Form
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(bgColor);
        formPanel.setPreferredSize(new Dimension(280, 0));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.weightx = 1.0;
        gbc.gridx = 0;

        // Form fields setup
        boolean es = I18nHelper.getLocale().getLanguage().equals("es");
        txtId = createField(es ? "ID de Ingrediente:" : "Ingredient ID:", 0, formPanel, gbc);
        txtName = createField(es ? "Nombre:" : "Name:", 2, formPanel, gbc);
        txtStock = createField(es ? "Cantidad en Stock:" : "Stock Quantity:", 4, formPanel, gbc);
        txtUnit = createField(es ? "Unidad (ej. kg, L):" : "Unit (e.g. kg, L):", 6, formPanel, gbc);
        txtMinAlert = createField(es ? "Alerta Mínima:" : "Minimum Alert Threshold:", 8, formPanel, gbc);

        // Button Group (Save, Delete, Clear)
        JPanel actionsPanel = new JPanel(new GridLayout(2, 2, 8, 8));
        actionsPanel.setBackground(bgColor);
        actionsPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

        JButton btnSave = createStyledButton(es ? "GUARDAR / ACT." : "SAVE / UPDATE", goldPrimary);
        btnSave.addActionListener(e -> saveIngredient());
        actionsPanel.add(btnSave);

        JButton btnDelete = createStyledButton(es ? "ELIMINAR" : "DELETE", new Color(220, 38, 38));
        btnDelete.addActionListener(e -> deleteIngredient());
        actionsPanel.add(btnDelete);

        JButton btnClear = createStyledButton(es ? "LIMPIAR FORM." : "CLEAR FORM", new Color(120, 113, 108));
        btnClear.addActionListener(e -> clearForm());
        actionsPanel.add(btnClear);

        JButton btnAdd = createStyledButton(I18nHelper.getMessage("btn.add_stock"), new Color(5, 150, 105));
        btnAdd.addActionListener(e -> addStock());
        actionsPanel.add(btnAdd);

        JButton btnUse = createStyledButton(I18nHelper.getMessage("btn.record_usage"), new Color(217, 119, 6));
        btnUse.addActionListener(e -> recordUsage());
        actionsPanel.add(btnUse);

        gbc.gridy = 10;
        gbc.gridwidth = 2;
        formPanel.add(actionsPanel, gbc);

        mainPanel.add(formPanel, BorderLayout.EAST);

        refreshTableData();
    }

    private JTextField createField(String labelText, int y, JPanel panel, GridBagConstraints gbc) {
        JLabel label = new JLabel(labelText);
        label.setForeground(textColor);
        label.setFont(new Font("Segoe UI", Font.BOLD, 11));
        gbc.gridy = y;
        panel.add(label, gbc);

        JTextField field = new JTextField();
        field.setBackground(inputColor);
        field.setForeground(textColor);
        field.setCaretColor(textColor);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(borderColor),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        gbc.gridy = y + 1;
        panel.add(field, gbc);
        return field;
    }

    private JButton createStyledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(bg.darker());
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(bg);
            }
        });
        return btn;
    }

    private void refreshTableData() {
        tableModel.setRowCount(0);
        List<Ingredient> ingredients = ingredientRepo.findAll();
        for (Ingredient ing : ingredients) {
            tableModel.addRow(new Object[]{
                ing.getId(),
                ing.getName(),
                ing.getStock(),
                "und",
                ing.getMinimumAlertQuantity()
            });
        }
    }

    private void clearForm() {
        txtId.setText("");
        txtId.setEditable(true);
        txtName.setText("");
        txtStock.setText("");
        txtUnit.setText("");
        txtMinAlert.setText("");
        inventoryTable.clearSelection();
    }

    private void saveIngredient() {
        String id = txtId.getText().trim();
        String name = txtName.getText().trim();
        String stockStr = txtStock.getText().trim();
        String unit = txtUnit.getText().trim();
        String minStr = txtMinAlert.getText().trim();

        boolean es = I18nHelper.getLocale().getLanguage().equals("es");
        if (id.isEmpty() || name.isEmpty() || stockStr.isEmpty() || minStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, es ? "Por favor llene todos los campos." : "Please fill in all form fields.", es ? "Advertencia" : "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            double stock = Double.parseDouble(stockStr);
            double min = Double.parseDouble(minStr);
            
            Ingredient ingredient = new Ingredient(id, name, stock, min);
            ingredientRepo.save(ingredient);
            
            refreshTableData();
            clearForm();
            JOptionPane.showMessageDialog(this, es ? "¡Ingrediente guardado con éxito!" : "Ingredient saved successfully!", es ? "Éxito" : "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, es ? "Stock y Alerta deben ser números válidos." : "Stock and Alert quantity must be valid numbers.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteIngredient() {
        String id = txtId.getText().trim();
        boolean es = I18nHelper.getLocale().getLanguage().equals("es");
        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this, es ? "Por favor, seleccione un ingrediente para eliminar." : "Please select an ingredient to delete.", es ? "Advertencia" : "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int choice = JOptionPane.showConfirmDialog(this, es ? "¿Estás seguro de que quieres eliminar este ingrediente?" : "Are you sure you want to delete this ingredient?", es ? "Confirmar" : "Confirm", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            ingredientRepo.delete(id);
            refreshTableData();
            clearForm();
            JOptionPane.showMessageDialog(this, es ? "Ingrediente eliminado exitosamente." : "Ingredient deleted successfully.", es ? "Éxito" : "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void addStock() {
        int row = inventoryTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, I18nHelper.getMessage("inv.warn_select"), I18nHelper.getMessage("eq.warn_title"), JOptionPane.WARNING_MESSAGE);
            return;
        }

        String id = tableModel.getValueAt(row, 0).toString();
        Ingredient ing = ingredientRepo.findById(id);
        if (ing != null) {
            String input = JOptionPane.showInputDialog(this, java.text.MessageFormat.format(I18nHelper.getMessage("inv.prompt_add"), ing.getName()), I18nHelper.getMessage("btn.add_stock"), JOptionPane.QUESTION_MESSAGE);
            if (input != null && !input.trim().isEmpty()) {
                try {
                    double amount = Double.parseDouble(input);
                    if (amount > 0) {
                        ing.setStock(ing.getStock() + amount);
                        ingredientRepo.save(ing);
                        refreshTableData();
                    } else {
                        JOptionPane.showMessageDialog(this, I18nHelper.getMessage("inv.invalid_qty"), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, I18nHelper.getMessage("inv.invalid_qty"), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void recordUsage() {
        int row = inventoryTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, I18nHelper.getMessage("inv.warn_select"), I18nHelper.getMessage("eq.warn_title"), JOptionPane.WARNING_MESSAGE);
            return;
        }

        String id = tableModel.getValueAt(row, 0).toString();
        Ingredient ing = ingredientRepo.findById(id);
        if (ing != null) {
            String input = JOptionPane.showInputDialog(this, java.text.MessageFormat.format(I18nHelper.getMessage("inv.prompt_use"), ing.getName()), I18nHelper.getMessage("btn.record_usage"), JOptionPane.QUESTION_MESSAGE);
            if (input != null && !input.trim().isEmpty()) {
                try {
                    double amount = Double.parseDouble(input);
                    if (amount > 0 && amount <= ing.getStock()) {
                        ing.setStock(ing.getStock() - amount);
                        ingredientRepo.save(ing);
                        refreshTableData();
                    } else {
                        JOptionPane.showMessageDialog(this, I18nHelper.getMessage("inv.invalid_qty"), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, I18nHelper.getMessage("inv.invalid_qty"), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
}
