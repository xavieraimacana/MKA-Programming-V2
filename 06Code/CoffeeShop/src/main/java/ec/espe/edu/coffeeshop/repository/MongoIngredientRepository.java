package ec.espe.edu.coffeeshop.repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import ec.espe.edu.coffeeshop.model.Ingredient;
import org.bson.Document;
import java.util.ArrayList;
import java.util.List;

/**
 * MongoDB implementation of the IngredientRepository.
 * 
 * @author Kevin Albán, MKA Programmer, @ESPE
 */
public class MongoIngredientRepository implements IngredientRepository {
    private final MongoCollection<Document> collection;

    public MongoIngredientRepository() {
        MongoDatabase db = MongoDBConnection.getInstance().getDatabase();
        this.collection = db.getCollection("ingredients");
    }

    @Override
    public void save(Ingredient ingredient) {
        Document doc = new Document("_id", ingredient.getId())
                .append("name", ingredient.getName())
                .append("stock", ingredient.getStock())
                .append("minimumAlertQuantity", ingredient.getMinimumAlertQuantity());
        
        collection.replaceOne(Filters.eq("_id", ingredient.getId()), doc, new ReplaceOptions().upsert(true));
    }

    @Override
    public Ingredient findById(String id) {
        Document doc = collection.find(Filters.eq("_id", id)).first();
        return (doc != null) ? documentToIngredient(doc) : null;
    }

    @Override
    public List<Ingredient> findAll() {
        List<Ingredient> list = new ArrayList<>();
        for (Document doc : collection.find()) {
            list.add(documentToIngredient(doc));
        }
        return list;
    }

    @Override
    public void delete(String id) {
        collection.deleteOne(Filters.eq("_id", id));
    }

    private Ingredient documentToIngredient(Document doc) {
        Ingredient ingredient = new Ingredient();
        ingredient.setId(doc.getString("_id"));
        ingredient.setName(doc.getString("name"));
        ingredient.setStock(doc.getDouble("stock"));
        ingredient.setMinimumAlertQuantity(doc.getDouble("minimumAlertQuantity"));
        return ingredient;
    }
}
