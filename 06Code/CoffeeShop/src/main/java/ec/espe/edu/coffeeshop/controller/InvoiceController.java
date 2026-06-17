package ec.espe.edu.coffeeshop.controller;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import ec.espe.edu.coffeeshop.model.Invoice;
import ec.espe.edu.coffeeshop.model.Order;
import ec.espe.edu.coffeeshop.model.OrderStatus;
import ec.espe.edu.coffeeshop.utils.MongoDBConnection;
import org.bson.Document;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
public class InvoiceController {
    private final MongoCollection<Document> collection;
    public InvoiceController() {
        MongoDatabase database = MongoDBConnection.getDatabase();
        this.collection = database.getCollection("Invoices");
    }
    public boolean addInvoice(Invoice invoice) {
        try {
            Document doc = new Document("_id", invoice.getInvoiceId())
                    .append("orderId", invoice.getOrder() != null ? invoice.getOrder().getOrderId() : null)
                    .append("issueDate", invoice.getIssueDate().toString())
                    .append("subtotal", invoice.getSubtotal())
                    .append("taxAmount", invoice.getTaxAmount())
                    .append("totalAmount", invoice.getTotalAmount());
            collection.insertOne(doc);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public List<Invoice> getAllInvoices() {
        List<Invoice> invoices = new ArrayList<>();
        try (MongoCursor<Document> cursor = collection.find().iterator()) {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                String invoiceId = doc.getString("_id");
                String orderId = doc.getString("orderId");
                LocalDateTime date = LocalDateTime.parse(doc.getString("issueDate"));
                Order order = new Order(orderId, LocalDateTime.now(), OrderStatus.COMPLETED, "");
                Invoice invoice = new Invoice(invoiceId, order, date);
                invoices.add(invoice);
            }
        }
        return invoices;
    }
}
