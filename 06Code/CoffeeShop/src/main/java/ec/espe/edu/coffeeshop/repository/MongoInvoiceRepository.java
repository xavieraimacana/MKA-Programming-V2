package ec.espe.edu.coffeeshop.repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.model.Sorts;
import ec.espe.edu.coffeeshop.model.Invoice;
import ec.espe.edu.coffeeshop.model.Order;
import org.bson.Document;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * MongoDB implementation of the InvoiceRepository.
 * 
 * @author Mateo Artieda, MKA Programmer, @ESPE
 */
public class MongoInvoiceRepository implements InvoiceRepository {
    private final MongoCollection<Document> collection;
    private final OrderRepository orderRepository;

    public MongoInvoiceRepository() {
        MongoDatabase db = MongoDBConnection.getInstance().getDatabase();
        this.collection = db.getCollection("invoices");
        this.orderRepository = new MongoOrderRepository();
    }

    @Override
    public void save(Invoice invoice) {
        Document doc = invoiceToDocument(invoice);
        collection.replaceOne(Filters.eq("_id", invoice.getId()), doc, new ReplaceOptions().upsert(true));
    }

    @Override
    public Invoice findById(String id) {
        Document doc = collection.find(Filters.eq("_id", id)).first();
        return (doc != null) ? documentToInvoice(doc) : null;
    }

    @Override
    public List<Invoice> findAll() {
        List<Invoice> list = new ArrayList<>();
        for (Document doc : collection.find()) {
            list.add(documentToInvoice(doc));
        }
        return list;
    }

    @Override
    public void delete(String id) {
        collection.deleteOne(Filters.eq("_id", id));
    }

    @Override
    public String getNextInvoiceNumber() {
        Document lastInvoice = collection.find().sort(Sorts.descending("invoiceNumber")).first();
        if (lastInvoice == null) {
            return "000001";
        }
        int lastNum = Integer.parseInt(lastInvoice.getString("invoiceNumber"));
        return String.format("%06d", lastNum + 1);
    }

    private Document invoiceToDocument(Invoice invoice) {
        return new Document("_id", invoice.getId())
                .append("invoiceNumber", invoice.getInvoiceNumber())
                .append("date", Date.from(invoice.getDate().atZone(ZoneId.systemDefault()).toInstant()))
                .append("clientName", invoice.getClientName())
                .append("clientTaxId", invoice.getClientTaxId())
                .append("order_id", invoice.getOrder() != null ? invoice.getOrder().getId() : null)
                .append("subtotal", invoice.getSubtotal().toString())
                .append("tax", invoice.getTax().toString())
                .append("total", invoice.getTotal().toString());
    }

    private Invoice documentToInvoice(Document doc) {
        Invoice invoice = new Invoice();
        invoice.setId(doc.getString("_id"));
        invoice.setInvoiceNumber(doc.getString("invoiceNumber"));
        invoice.setDate(doc.getDate("date").toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        invoice.setClientName(doc.getString("clientName"));
        invoice.setClientTaxId(doc.getString("clientTaxId"));
        invoice.setSubtotal(new BigDecimal(doc.getString("subtotal")));
        invoice.setTax(new BigDecimal(doc.getString("tax")));
        invoice.setTotal(new BigDecimal(doc.getString("total")));

        String orderId = doc.getString("order_id");
        if (orderId != null) {
            invoice.setOrder(orderRepository.findById(orderId));
        }

        return invoice;
    }
}
