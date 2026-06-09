package ec.espe.edu.coffeeshop.repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import ec.espe.edu.coffeeshop.model.*;
import ec.espe.edu.coffeeshop.payment.*;
import org.bson.Document;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * MongoDB implementation of the OrderRepository.
 * 
 * @author Mateo Artieda, MKA Programmer, @ESPE
 */
public class MongoOrderRepository implements OrderRepository {
    private final MongoCollection<Document> collection;

    public MongoOrderRepository() {
        MongoDatabase db = MongoDBConnection.getInstance().getDatabase();
        this.collection = db.getCollection("orders");
    }

    @Override
    public void save(Order order) {
        Document doc = orderToDocument(order);
        collection.replaceOne(Filters.eq("_id", order.getId()), doc, new ReplaceOptions().upsert(true));
    }

    @Override
    public Order findById(String id) {
        Document doc = collection.find(Filters.eq("_id", id)).first();
        return (doc != null) ? documentToOrder(doc) : null;
    }

    @Override
    public List<Order> findAll() {
        List<Order> list = new ArrayList<>();
        for (Document doc : collection.find()) {
            list.add(documentToOrder(doc));
        }
        return list;
    }

    @Override
    public void delete(String id) {
        collection.deleteOne(Filters.eq("_id", id));
    }

    private Document orderToDocument(Order order) {
        List<Document> itemsDocs = order.getItems().stream()
                .map(this::orderItemToDocument)
                .collect(Collectors.toList());

        Document doc = new Document("_id", order.getId())
                .append("date", Date.from(order.getDate().atZone(ZoneId.systemDefault()).toInstant()))
                .append("status", order.getStatus().name())
                .append("items", itemsDocs)
                .append("subtotal", order.getSubtotal().toString())
                .append("tax", order.getTax().toString())
                .append("discount", order.getDiscount().toString())
                .append("total", order.getTotal().toString());

        if (order.getClient() != null) {
            doc.append("client", customerToDocument(order.getClient()));
        }

        if (order.getPayment() != null) {
            doc.append("payment", paymentToDocument(order.getPayment()));
        }

        return doc;
    }

    private Document orderItemToDocument(OrderItem item) {
        List<Document> modifiersDocs = item.getModifiers().stream()
                .map(this::modifierToDocument)
                .collect(Collectors.toList());

        Document doc = new Document("quantity", item.getQuantity())
                .append("pricePaidSnapshot", item.getPricePaidSnapshot().toString())
                .append("modifiers", modifiersDocs);
        
        if (item.getProduct() != null) {
            doc.append("product_id", item.getProduct().getId());
            doc.append("product_name", item.getProduct().getName());
        }
        
        return doc;
    }

    private Document modifierToDocument(Modifier modifier) {
        return new Document("id", modifier.getId())
                .append("name", modifier.getName())
                .append("extraPrice", modifier.getExtraPrice().toString());
    }

    private Document customerToDocument(Customer customer) {
        return new Document("id", customer.getId())
                .append("name", customer.getName())
                .append("taxId", customer.getTaxId())
                .append("email", customer.getEmail());
    }

    private Document paymentToDocument(Payment payment) {
        Document doc = new Document("type", payment.getClass().getSimpleName())
                .append("amount", payment.getAmount().toString());
        
        if (payment instanceof CashPayment) {
            doc.append("amountTendered", ((CashPayment) payment).getAmountTendered().toString());
        } else if (payment instanceof CardPayment) {
            doc.append("paymentToken", ((CardPayment) payment).getPaymentToken());
        } else if (payment instanceof BankTransferPayment) {
            BankTransferPayment btp = (BankTransferPayment) payment;
            doc.append("bankName", btp.getBankName())
               .append("transactionId", btp.getTransactionId());
        }
        
        return doc;
    }

    private Order documentToOrder(Document doc) {
        Order order = new Order();
        order.setId(doc.getString("_id"));
        order.setDate(doc.getDate("date").toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        order.setStatus(OrderStatus.valueOf(doc.getString("status")));
        order.setSubtotal(new BigDecimal(doc.getString("subtotal")));
        order.setTax(new BigDecimal(doc.getString("tax")));
        order.setDiscount(new BigDecimal(doc.getString("discount")));
        order.setTotal(new BigDecimal(doc.getString("total")));

        List<Document> itemsDocs = doc.getList("items", Document.class);
        if (itemsDocs != null) {
            order.setItems(itemsDocs.stream().map(this::documentToOrderItem).collect(Collectors.toList()));
        }

        Document clientDoc = doc.get("client", Document.class);
        if (clientDoc != null) {
            order.setClient(documentToCustomer(clientDoc));
        }

        Document paymentDoc = doc.get("payment", Document.class);
        if (paymentDoc != null) {
            order.setPayment(documentToPayment(paymentDoc));
        }

        return order;
    }

    private OrderItem documentToOrderItem(Document doc) {
        OrderItem item = new OrderItem();
        item.setQuantity(doc.getInteger("quantity"));
        item.setPricePaidSnapshot(new BigDecimal(doc.getString("pricePaidSnapshot")));
        
        // Product cannot be reconstructed here because it's abstract and we don't have concrete classes yet.
        // It should be fetched from a ProductRepository in a full implementation.
        
        List<Document> modsDocs = doc.getList("modifiers", Document.class);
        if (modsDocs != null) {
            item.setModifiers(modsDocs.stream().map(this::documentToModifier).collect(Collectors.toList()));
        }
        
        return item;
    }

    private Modifier documentToModifier(Document doc) {
        Modifier mod = new Modifier();
        mod.setId(doc.getString("id"));
        mod.setName(doc.getString("name"));
        mod.setExtraPrice(new BigDecimal(doc.getString("extraPrice")));
        return mod;
    }

    private Customer documentToCustomer(Document doc) {
        Customer c = new Customer();
        c.setId(doc.getString("id"));
        c.setName(doc.getString("name"));
        c.setTaxId(doc.getString("taxId"));
        c.setEmail(doc.getString("email"));
        return c;
    }

    private Payment documentToPayment(Document doc) {
        String type = doc.getString("type");
        BigDecimal amount = new BigDecimal(doc.getString("amount"));
        
        if ("CashPayment".equals(type)) {
            return new CashPayment(amount, new BigDecimal(doc.getString("amountTendered")));
        } else if ("CardPayment".equals(type)) {
            return new CardPayment(amount, doc.getString("paymentToken"));
        } else if ("BankTransferPayment".equals(type)) {
            return new BankTransferPayment(amount, doc.getString("bankName"), doc.getString("transactionId"));
        }
        return null;
    }
}
