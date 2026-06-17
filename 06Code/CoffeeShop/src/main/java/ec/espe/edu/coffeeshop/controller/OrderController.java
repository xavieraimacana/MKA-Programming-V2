package ec.espe.edu.coffeeshop.controller;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import ec.espe.edu.coffeeshop.model.Order;
import ec.espe.edu.coffeeshop.model.OrderItem;
import ec.espe.edu.coffeeshop.model.OrderItemModifier;
import ec.espe.edu.coffeeshop.model.OrderStatus;
import ec.espe.edu.coffeeshop.model.Product;
import ec.espe.edu.coffeeshop.model.ProductModifier;
import ec.espe.edu.coffeeshop.utils.MongoDBConnection;
import org.bson.Document;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
public class OrderController {
    private final MongoCollection<Document> collection;
    public OrderController() {
        MongoDatabase database = MongoDBConnection.getDatabase();
        this.collection = database.getCollection("Orders");
    }
    public boolean addOrder(Order order) {
        try {
            List<Document> itemDocs = new ArrayList<>();
            for (OrderItem item : order.getItems()) {
                List<Document> modDocs = new ArrayList<>();
                for (OrderItemModifier mod : item.getModifiers()) {
                    modDocs.add(new Document("modifierId", mod.getModifier().getModifierId()) 
                                .append("priceAtTimeOfOrder", mod.getPriceAtTimeOfOrder()));
                }
                Document itemDoc = new Document("productId", item.getProduct().getProductId())
                                     .append("productName", item.getProduct().getName())
                                     .append("quantity", item.getQuantity())
                                     .append("subtotal", item.getSubtotal())
                                     .append("modifiers", modDocs);
                itemDocs.add(itemDoc);
            }
            Document doc = new Document("_id", order.getOrderId())
                    .append("orderDate", order.getOrderDate().toString())
                    .append("status", order.getStatus().name())
                    .append("preparationNotes", order.getPreparationNotes())
                    .append("items", itemDocs);
            collection.insertOne(doc);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        try (MongoCursor<Document> cursor = collection.find().iterator()) {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                String orderId = doc.getString("_id");
                LocalDateTime date = LocalDateTime.parse(doc.getString("orderDate"));
                OrderStatus status = OrderStatus.valueOf(doc.getString("status"));
                String notes = doc.getString("preparationNotes");
                Order order = new Order(orderId, date, status, notes);
                List<Document> itemDocs = (List<Document>) doc.get("items");
                if (itemDocs != null) {
                    for (Document itemDoc : itemDocs) {
                        String prodId = itemDoc.getString("productId");
                        String prodName = itemDoc.getString("productName");
                        int qty = itemDoc.getInteger("quantity");
                        Product p = new Product(prodId, prodName, 0, true);
                        OrderItem oi = new OrderItem(p, qty);
                        order.getItems().add(oi);
                    }
                }
                orders.add(order);
            }
        }
        return orders;
    }
}
