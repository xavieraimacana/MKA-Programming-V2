package ec.espe.edu.coffeeshop.model;

/**
 * Associates an ingredient with the specific quantity needed for a recipe.
 * 
 * @author Anthony Aimacaña, MKA Programer, @ESPE
 */
public class RecipeItem {
    private Ingredient ingredient;
    private Double quantityNeeded;

    public RecipeItem() {}

    public RecipeItem(Ingredient ingredient, Double quantityNeeded) {
        this.ingredient = ingredient;
        this.quantityNeeded = quantityNeeded;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    public Double getQuantityNeeded() {
        return quantityNeeded;
    }

    public void setQuantityNeeded(Double quantityNeeded) {
        this.quantityNeeded = quantityNeeded;
    }
}
