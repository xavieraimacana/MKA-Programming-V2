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
        System.out.println(getReportText());
    }

    @Override
    public String getReportText() {
        List<Ingredient> ingredients = ingredientRepository.findAll();
        StringBuilder sb = new StringBuilder();

        sb.append("---------- INVENTORY STOCK REPORT ----------\n");
        sb.append(String.format("%-20s | %-10s | %-10s | %-10s%n", "Ingredient", "Stock", "Min Alert", "Status"));
        sb.append("------------------------------------------------------------\n");

        for (Ingredient ing : ingredients) {
            String status = (ing.getStock() < ing.getMinimumAlertQuantity()) ? "LOW STOCK" : "OK";
            sb.append(String.format("%-20s | %-10.3f | %-10.3f | %-10s%n", 
                              ing.getName(), ing.getStock(), ing.getMinimumAlertQuantity(), status));
        }
        sb.append("------------------------------------------------------------\n");
        return sb.toString();
    }
}
