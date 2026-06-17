package ec.espe.edu.coffeeshop.controller;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import ec.espe.edu.coffeeshop.model.Recipe;
import ec.espe.edu.coffeeshop.model.RecipeIngredient;
import ec.espe.edu.coffeeshop.model.UnitOfMeasure;
import ec.espe.edu.coffeeshop.utils.MongoDBConnection;
import org.bson.Document;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
public class RecipeController {
    private final MongoCollection<Document> collection;
    public RecipeController() {
        MongoDatabase database = MongoDBConnection.getDatabase();
        this.collection = database.getCollection("Recipes");
    }
    public boolean addRecipe(Recipe recipe) {
        try {
            List<Document> ingredientsDocs = recipe.getIngredients().stream().map(ing ->
                    new Document("itemId", ing.getItemId())
                            .append("requiredQuantity", ing.getRequiredQuantity())
                            .append("unit", ing.getUnit().name())
            ).collect(Collectors.toList());
            Document doc = new Document("_id", recipe.getRecipeId())
                    .append("instructions", recipe.getInstructions())
                    .append("ingredients", ingredientsDocs);
            collection.insertOne(doc);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean updateRecipe(Recipe recipe) {
        try {
            List<Document> ingredientsDocs = recipe.getIngredients().stream().map(ing ->
                    new Document("itemId", ing.getItemId())
                            .append("requiredQuantity", ing.getRequiredQuantity())
                            .append("unit", ing.getUnit().name())
            ).collect(Collectors.toList());
            Document doc = new Document("instructions", recipe.getInstructions())
                    .append("ingredients", ingredientsDocs);
            collection.updateOne(Filters.eq("_id", recipe.getRecipeId()), new Document("$set", doc));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean deleteRecipe(String id) {
        try {
            return collection.deleteOne(Filters.eq("_id", id)).getDeletedCount() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public List<Recipe> getAllRecipes() {
        List<Recipe> recipes = new ArrayList<>();
        for (Document doc : collection.find()) {
            Recipe r = new Recipe(
                    doc.getString("_id"),
                    doc.getString("instructions")
            );
            List<Document> ingDocs = (List<Document>) doc.get("ingredients");
            if (ingDocs != null) {
                for (Document iDoc : ingDocs) {
                    r.addIngredient(
                            iDoc.getString("itemId"),
                            iDoc.getDouble("requiredQuantity"),
                            UnitOfMeasure.valueOf(iDoc.getString("unit"))
                    );
                }
            }
            recipes.add(r);
        }
        return recipes;
    }
}
