package ec.espe.edu.coffeeshop.reports;

import ec.espe.edu.coffeeshop.model.Ingredient;
import ec.espe.edu.coffeeshop.repository.IngredientRepository;
import ec.espe.edu.coffeeshop.repository.MongoIngredientRepository;
import java.util.List;

/**
 * Report of current stock levels highlighting low supplies.
 * 
 * @author Kevin Albán, MKA Programmer, @ESPE
 */
public class InventoryReport implements Report {
    private final IngredientRepository ingredientRepository;

    public InventoryReport() {
        this.ingredientRepository = new MongoIngredientRepository();
    }

    @Override
    public void generateReport() {
        List<Ingredient> ingredients = ingredientRepository.findAll();

        System.out.println("---------- INVENTORY STOCK REPORT ----------");
        System.out.printf("%-20s | %-10s | %-10s | %-10s%n", "Ingredient", "Stock", "Min Alert", "Status");
        System.out.println("------------------------------------------------------------");

        for (Ingredient ing : ingredients) {
            String status = (ing.getStock() < ing.getMinimumAlertQuantity()) ? "LOW STOCK" : "OK";
            System.out.printf("%-20s | %-10.3f | %-10.3f | %-10s%n", 
                              ing.getName(), ing.getStock(), ing.getMinimumAlertQuantity(), status);
        }
        System.out.println("------------------------------------------------------------");
    }
}
