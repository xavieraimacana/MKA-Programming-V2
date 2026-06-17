package ec.espe.edu.coffeeshop.controller;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import ec.espe.edu.coffeeshop.model.Product;
import ec.espe.edu.coffeeshop.utils.MongoDBConnection;
import org.bson.Document;
import java.util.ArrayList;
import java.util.List;
public class ProductController {
    private final MongoCollection<Document> collection;
    public ProductController() {
        MongoDatabase database = MongoDBConnection.getDatabase();
        this.collection = database.getCollection("Products");
    }
    public boolean addProduct(Product product) {
        try {
            Document doc = new Document("_id", product.getProductId())
                    .append("name", product.getName())
                    .append("basePrice", product.getBasePrice())
                    .append("available", product.isAvailable());
            collection.insertOne(doc);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        for (Document doc : collection.find()) {
            products.add(new Product(
                    doc.getString("_id"),
                    doc.getString("name"),
                    doc.getDouble("basePrice"),
                    doc.getBoolean("available", false)
            ));
        }
        return products;
    }
    public boolean updateProduct(Product product) {
        try {
            Document doc = new Document("name", product.getName())
                    .append("basePrice", product.getBasePrice())
                    .append("available", product.isAvailable());
            collection.updateOne(Filters.eq("_id", product.getProductId()), new Document("$set", doc));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean deleteProduct(String id) {
        try {
            return collection.deleteOne(Filters.eq("_id", id)).getDeletedCount() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
