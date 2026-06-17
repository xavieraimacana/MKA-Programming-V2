package ec.espe.edu.coffeeshop.controller;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import ec.espe.edu.coffeeshop.utils.MongoDBConnection;
import org.bson.Document;
import java.util.ArrayList;
import java.util.List;
public class ReportController {
    private final MongoCollection<Document> collection;
    public ReportController() {
        MongoDatabase database = MongoDBConnection.getDatabase();
        this.collection = database.getCollection("Orders");
    }
    public List<Document> getSalesReport() {
        List<Document> orders = new ArrayList<>();
        try {
            for (Document doc : collection.find()) {
                orders.add(doc);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orders;
    }
}
