package ec.espe.edu.coffeeshop.controller;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import ec.espe.edu.coffeeshop.model.ProductModifier;
import ec.espe.edu.coffeeshop.utils.MongoDBConnection;
import org.bson.Document;
import java.util.ArrayList;
import java.util.List;
public class ProductModifierController {
    private final MongoCollection<Document> collection;
    public ProductModifierController() {
        MongoDatabase database = MongoDBConnection.getDatabase();
        this.collection = database.getCollection("ProductModifiers");
    }
    public boolean addModifier(ProductModifier modifier) {
        try {
            Document doc = new Document("_id", modifier.getModifierId())
                    .append("name", modifier.getName())
                    .append("additionalPrice", modifier.getAdditionalPrice());
            collection.insertOne(doc);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public List<ProductModifier> getAllModifiers() {
        List<ProductModifier> modifiers = new ArrayList<>();
        for (Document doc : collection.find()) {
            modifiers.add(new ProductModifier(
                    doc.getString("_id"),
                    doc.getString("name"),
                    doc.getDouble("additionalPrice")
            ));
        }
        return modifiers;
    }
    public boolean updateModifier(ProductModifier modifier) {
        try {
            Document doc = new Document("name", modifier.getName())
                    .append("additionalPrice", modifier.getAdditionalPrice());
            collection.updateOne(Filters.eq("_id", modifier.getModifierId()), new Document("$set", doc));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean deleteModifier(String id) {
        try {
            return collection.deleteOne(Filters.eq("_id", id)).getDeletedCount() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
