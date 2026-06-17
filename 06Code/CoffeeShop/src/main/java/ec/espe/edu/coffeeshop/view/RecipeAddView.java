package ec.espe.edu.coffeeshop.view;
import ec.espe.edu.coffeeshop.controller.RecipeController;
import ec.espe.edu.coffeeshop.model.Recipe;
import ec.espe.edu.coffeeshop.model.UnitOfMeasure;
import ec.espe.edu.coffeeshop.utils.ThemeManager;
import javax.swing.*;
import java.awt.*;
public class RecipeAddView extends JPanel {
    private JTextField txtId, txtInstructions, txtItemId, txtQty;
    private JComboBox<UnitOfMeasure> cmbUnit;
    private RecipeController controller;
    private RecipeView parent;
    public RecipeAddView(RecipeView parent, RecipeController controller) {
        this.parent = parent;
        this.controller = controller;
        setLayout(new BorderLayout(10, 10));
        setBackground(ThemeManager.COLOR_BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JLabel titleLabel = new JLabel("AGREGAR NUEVA RECETA", SwingConstants.CENTER);
        titleLabel.setFont(ThemeManager.TITLE_FONT);
        add(titleLabel, BorderLayout.NORTH);
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBackground(ThemeManager.COLOR_BACKGROUND);
        formPanel.add(new JLabel("ID de Receta:"));
        txtId = new JTextField();
        formPanel.add(txtId);
        formPanel.add(new JLabel("Instrucciones:"));
        txtInstructions = new JTextField();
        formPanel.add(txtInstructions);
        formPanel.add(new JLabel("ID del Primer Ingrediente:"));
        txtItemId = new JTextField();
        formPanel.add(txtItemId);
        formPanel.add(new JLabel("Cantidad:"));
        txtQty = new JTextField();
        formPanel.add(txtQty);
        formPanel.add(new JLabel("Unidad de Medida:"));
        cmbUnit = new JComboBox<>(UnitOfMeasure.values());
        formPanel.add(cmbUnit);
        add(formPanel, BorderLayout.CENTER);
        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(ThemeManager.COLOR_BACKGROUND);
        JButton btnSave = new JButton("Guardar");
        btnSave.addActionListener(e -> saveRecipe());
        JButton btnCancel = new JButton("Cancelar");
        btnCancel.addActionListener(e -> {
            clearFields();
            parent.showCard("List");
        });
        btnPanel.add(btnSave);
        btnPanel.add(btnCancel);
        add(btnPanel, BorderLayout.SOUTH);
    }
    private void saveRecipe() {
        String id = txtId.getText().trim();
        String instructions = txtInstructions.getText().trim();
        String itemId = txtItemId.getText().trim();
        String qtyStr = txtQty.getText().trim();
        if (id.isEmpty() || instructions.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El ID de la Receta y las Instrucciones son obligatorios");
            return;
        }
        Recipe r = new Recipe(id, instructions);
        if (!itemId.isEmpty() && !qtyStr.isEmpty()) {
            try {
                double qty = Double.parseDouble(qtyStr);
                if (qty <= 0) {
                    JOptionPane.showMessageDialog(this, "La cantidad debe ser positiva");
                    return;
                }
                r.addIngredient(itemId, qty, (UnitOfMeasure) cmbUnit.getSelectedItem());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Formato de cantidad inválido");
                return;
            }
        }
        if (controller.addRecipe(r)) {
            JOptionPane.showMessageDialog(this, "Receta guardada exitosamente");
            clearFields();
            parent.loadTableData();
            parent.showCard("List");
        } else {
            JOptionPane.showMessageDialog(this, "Error al guardar la receta");
        }
    }
    private void clearFields() {
        txtId.setText("");
        txtInstructions.setText("");
        txtItemId.setText("");
        txtQty.setText("");
        cmbUnit.setSelectedIndex(0);
    }
}
