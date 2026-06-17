package ec.espe.edu.coffeeshop.controller;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import ec.espe.edu.coffeeshop.model.Order;
import ec.espe.edu.coffeeshop.utils.MongoDBConnection;
import org.bson.Document;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
public class KdsController {
    private final MongoCollection<Document> collection;
    public KdsController() {
        MongoDatabase database = MongoDBConnection.getDatabase();
        this.collection = database.getCollection("Orders");
    }
    public List<Order> getOrdersByStatus(String status) {
        List<Order> orders = new ArrayList<>();
        for (Document doc : collection.find(Filters.eq("status", status))) {
            orders.add(new Order(
                    doc.getString("_id"),
                    LocalDateTime.parse(doc.getString("orderDate")),
                    ec.espe.edu.coffeeshop.model.OrderStatus.valueOf(doc.getString("status")),
                    doc.getString("preparationNotes") != null ? doc.getString("preparationNotes") : ""
            ));
        }
        return orders;
    }
    public boolean updateOrderStatus(String orderId, String newStatus) {
        try {
            var result = collection.updateOne(
                    Filters.eq("_id", orderId),
                    Updates.set("status", newStatus)
            );
            return result.getModifiedCount() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
