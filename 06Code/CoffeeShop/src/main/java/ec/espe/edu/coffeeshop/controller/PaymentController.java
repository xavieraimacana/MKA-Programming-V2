package ec.espe.edu.coffeeshop.controller;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import ec.espe.edu.coffeeshop.model.Order;
import ec.espe.edu.coffeeshop.model.OrderStatus;
import ec.espe.edu.coffeeshop.model.Payment;
import ec.espe.edu.coffeeshop.utils.MongoDBConnection;
import org.bson.Document;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
public class PaymentController {
    private final MongoCollection<Document> collection;
    public PaymentController() {
        MongoDatabase database = MongoDBConnection.getDatabase();
        this.collection = database.getCollection("Payments");
    }
    public boolean addPayment(Payment payment) {
        try {
            Document doc = new Document("_id", payment.getPaymentId())
                    .append("orderId", payment.getOrder() != null ? payment.getOrder().getOrderId() : null)
                    .append("amount", payment.getAmount())
                    .append("method", payment.getMethod())
                    .append("isSuccessful", payment.isSuccessful());
            collection.insertOne(doc);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public List<Payment> getAllPayments() {
        List<Payment> payments = new ArrayList<>();
        try (MongoCursor<Document> cursor = collection.find().iterator()) {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                String paymentId = doc.getString("_id");
                String orderId = doc.getString("orderId");
                double amount = doc.getDouble("amount");
                String method = doc.getString("method");
                boolean isSuccessful = doc.getBoolean("isSuccessful");
                Order order = new Order(orderId, LocalDateTime.now(), OrderStatus.COMPLETED, "");
                Payment payment = new Payment(paymentId, order, amount, method);
                payment.setSuccessful(isSuccessful);
                payments.add(payment);
            }
        }
        return payments;
    }
}
