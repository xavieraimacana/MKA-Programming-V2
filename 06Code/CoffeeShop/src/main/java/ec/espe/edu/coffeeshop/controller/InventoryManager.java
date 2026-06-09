package ec.espe.edu.coffeeshop.controller;

import ec.espe.edu.coffeeshop.exception.OutOfStockException;
import ec.espe.edu.coffeeshop.model.Ingredient;
import ec.espe.edu.coffeeshop.model.Product;
import ec.espe.edu.coffeeshop.model.Recipe;
import ec.espe.edu.coffeeshop.model.RecipeItem;
import ec.espe.edu.coffeeshop.repository.IngredientRepository;
import ec.espe.edu.coffeeshop.repository.MongoIngredientRepository;

/**
 * Manages inventory operations, such as stock deduction based on recipes.
 * 
 * @author Kevin Albán, MKA Programmer, @ESPE
 */
public class InventoryManager {
    private final IngredientRepository ingredientRepository;

    public InventoryManager() {
        this.ingredientRepository = new MongoIngredientRepository();
    }

    /**
     * Deducts the required ingredients for a product from the inventory.
     * 
     * @param product The product being prepared.
     * @throws OutOfStockException If any ingredient is insufficient.
     */
    public void deductStock(Product product) throws OutOfStockException {
        Recipe recipe = product.getRecipe();
        if (recipe == null || recipe.getItems() == null || recipe.getItems().isEmpty()) {
            return;
        }

        // 1. Validate all ingredients have enough stock
        for (RecipeItem item : recipe.getItems()) {
            Ingredient ingredientInDb = ingredientRepository.findById(item.getIngredient().getId());
            if (ingredientInDb == null) {
                throw new OutOfStockException("Ingredient not found in inventory: " + item.getIngredient().getName());
            }
            if (ingredientInDb.getStock() < item.getQuantityNeeded()) {
                throw new OutOfStockException("Insufficient stock for: " + ingredientInDb.getName());
            }
        }

        // 2. Deduct stock and check for alerts
        for (RecipeItem item : recipe.getItems()) {
            Ingredient ingredientInDb = ingredientRepository.findById(item.getIngredient().getId());
            double newStock = ingredientInDb.getStock() - item.getQuantityNeeded();
            ingredientInDb.setStock(newStock);
            
            ingredientRepository.save(ingredientInDb);

            if (newStock < ingredientInDb.getMinimumAlertQuantity()) {
                sendStockAlert(ingredientInDb);
            }
        }
    }

    private void sendStockAlert(Ingredient ingredient) {
        System.out.println("ALERT: Low stock for " + ingredient.getName() + 
                           ". Current: " + ingredient.getStock() + 
                           ", Minimum: " + ingredient.getMinimumAlertQuantity());
        // In a real application, this could trigger an email, notification, etc.
    }
}
