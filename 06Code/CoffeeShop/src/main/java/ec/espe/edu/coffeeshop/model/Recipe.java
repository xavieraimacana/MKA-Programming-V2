package ec.espe.edu.coffeeshop.model;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
public class Recipe {
    private String recipeId;
    private String instructions;
    private List<RecipeIngredient> ingredients;
    public Recipe() {
        this.ingredients = new ArrayList<>();
    }
    public Recipe(String recipeId, String instructions) {
        this.recipeId = recipeId;
        this.instructions = instructions;
        this.ingredients = new ArrayList<>();
    }
    public String getRecipeId() { return recipeId; }
    public void setRecipeId(String recipeId) { this.recipeId = recipeId; }
    public String getInstructions() { return instructions; }
    public void setInstructions(String instructions) { this.instructions = instructions; }
    public List<RecipeIngredient> getIngredients() { return ingredients; }
    public void setIngredients(List<RecipeIngredient> ingredients) { this.ingredients = ingredients; }
    public void addIngredient(String itemId, double qty, UnitOfMeasure unit) {
        this.ingredients.add(new RecipeIngredient(itemId, qty, unit));
    }
    public void removeIngredient(String itemId) {
        this.ingredients = this.ingredients.stream()
                .filter(i -> !i.getItemId().equals(itemId))
                .collect(Collectors.toList());
    }
}
