package ec.espe.edu.coffeeshop.model;

/**
 * Represents an ingredient and the quantity to be purchased.
 * 
 * @author Kevin Albán, MKA Programmer, @ESPE
 */
public class PurchaseOrderItem {
    private Ingredient ingredient;
    private Double quantity;

    public PurchaseOrderItem() {}

    public PurchaseOrderItem(Ingredient ingredient, Double quantity) {
        this.ingredient = ingredient;
        this.quantity = quantity;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }
}
