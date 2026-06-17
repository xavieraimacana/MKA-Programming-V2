package ec.espe.edu.coffeeshop.controller;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import ec.espe.edu.coffeeshop.model.Supplier;
import ec.espe.edu.coffeeshop.utils.MongoDBConnection;
import org.bson.Document;
import java.util.ArrayList;
import java.util.List;
public class SupplierController {
    private final MongoCollection<Document> collection;
    public SupplierController() {
        MongoDatabase database = MongoDBConnection.getDatabase();
        this.collection = database.getCollection("Suppliers");
    }
    public boolean addSupplier(Supplier supplier) {
        try {
            Document doc = new Document("_id", supplier.getId())
                    .append("companyName", supplier.getCompanyName());
            collection.insertOne(doc);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public List<Supplier> getAllSuppliers() {
        List<Supplier> suppliers = new ArrayList<>();
        for (Document doc : collection.find()) {
            suppliers.add(new Supplier(
                    doc.getString("_id"),
                    doc.getString("companyName")
            ));
        }
        return suppliers;
    }
    public boolean deleteSupplier(String id) {
        try {
            DeleteResult result = collection.deleteOne(Filters.eq("_id", id));
            return result.getDeletedCount() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
