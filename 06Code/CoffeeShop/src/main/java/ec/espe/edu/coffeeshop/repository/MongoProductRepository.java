package ec.espe.edu.coffeeshop.repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import ec.espe.edu.coffeeshop.model.Product;
import ec.espe.edu.coffeeshop.model.ProductCategory;
import ec.espe.edu.coffeeshop.model.Recipe;
import ec.espe.edu.coffeeshop.model.RecipeItem;
import ec.espe.edu.coffeeshop.model.Ingredient;
import org.bson.Document;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * MongoDB implementation of the ProductRepository.
 * Handles the abstract Product class by instantiating a concrete internal implementation.
 * 
 * @author Mateo Artieda, MKA Programmer, @ESPE
 */
public class MongoProductRepository implements ProductRepository {
    private final MongoCollection<Document> collection;

    public MongoProductRepository() {
        MongoDatabase db = MongoDBConnection.getInstance().getDatabase();
        this.collection = db.getCollection("products");
    }

    @Override
    public void save(Product product) {
        List<Document> recipeItemsDocs = new ArrayList<>();
        if (product.getRecipe() != null && product.getRecipe().getItems() != null) {
            for (RecipeItem item : product.getRecipe().getItems()) {
                recipeItemsDocs.add(new Document("ingredientId", item.getIngredient().getId())
                        .append("quantityNeeded", item.getQuantityNeeded()));
            }
        }
        
        Document doc = new Document("_id", product.getId())
                .append("name", product.getName())
                .append("price", product.getPrice().toString())
                .append("category", product.getCategory().name())
                .append("recipe", new Document("items", recipeItemsDocs));
        
        collection.replaceOne(Filters.eq("_id", product.getId()), doc, new ReplaceOptions().upsert(true));
    }

    @Override
    public Product findById(String id) {
        Document doc = collection.find(Filters.eq("_id", id)).first();
        return (doc != null) ? documentToProduct(doc) : null;
    }

    @Override
    public List<Product> findAll() {
        List<Product> list = new ArrayList<>();
        for (Document doc : collection.find()) {
            list.add(documentToProduct(doc));
        }
        return list;
    }

    @Override
    public void delete(String id) {
        collection.deleteOne(Filters.eq("_id", id));
    }

    private Product documentToProduct(Document doc) {
        // Concrete implementation for the abstract Product class
        Product product = new Product() {}; 
        product.setId(doc.getString("_id"));
        product.setName(doc.getString("name"));
        product.setPrice(new BigDecimal(doc.getString("price")));
        product.setCategory(ProductCategory.valueOf(doc.getString("category")));
        
        Document recipeDoc = doc.get("recipe", Document.class);
        if (recipeDoc != null) {
            Recipe recipe = new Recipe();
            List<Document> itemsDocs = recipeDoc.getList("items", Document.class);
            if (itemsDocs != null) {
                // Instanciamos el repositorio de ingredientes localmente para resolver referencias
                MongoIngredientRepository ingRepo = new MongoIngredientRepository();
                for (Document itemDoc : itemsDocs) {
                    String ingId = itemDoc.getString("ingredientId");
                    Double qty = itemDoc.getDouble("quantityNeeded");
                    ec.espe.edu.coffeeshop.model.Ingredient ing = ingRepo.findById(ingId);
                    if (ing != null) {
                        recipe.getItems().add(new RecipeItem(ing, qty));
                    }
                }
            }
            product.setRecipe(recipe);
        }
        return product;
    }
}
