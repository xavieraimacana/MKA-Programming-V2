package ec.espe.edu.coffeeshop.repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import ec.espe.edu.coffeeshop.model.Customer;
import org.bson.Document;
import java.util.ArrayList;
import java.util.List;

/**
 * MongoDB implementation of the CustomerRepository.
 * 
 * @author MKA Programmers, ESPE
 */
public class MongoCustomerRepository implements CustomerRepository {
    private final MongoCollection<Document> collection;

    public MongoCustomerRepository() {
        MongoDatabase db = MongoDBConnection.getInstance().getDatabase();
        this.collection = db.getCollection("customers");
    }

    @Override
    public void save(Customer customer) {
        Document doc = new Document("_id", customer.getId())
                .append("name", customer.getName())
                .append("taxId", customer.getTaxId())
                .append("email", customer.getEmail())
                .append("loyaltyPoints", customer.getLoyaltyPoints());
        
        collection.replaceOne(Filters.eq("_id", customer.getId()), doc, new ReplaceOptions().upsert(true));
    }

    @Override
    public Customer findById(String id) {
        Document doc = collection.find(Filters.eq("_id", id)).first();
        return (doc != null) ? documentToCustomer(doc) : null;
    }

    @Override
    public List<Customer> findAll() {
        List<Customer> list = new ArrayList<>();
        for (Document doc : collection.find()) {
            list.add(documentToCustomer(doc));
        }
        return list;
    }

    @Override
    public void delete(String id) {
        collection.deleteOne(Filters.eq("_id", id));
    }

    private Customer documentToCustomer(Document doc) {
        Customer customer = new Customer();
        customer.setId(doc.getString("_id"));
        customer.setName(doc.getString("name"));
        customer.setTaxId(doc.getString("taxId"));
        customer.setEmail(doc.getString("email"));
        customer.setLoyaltyPoints(doc.getInteger("loyaltyPoints", 0));
        return customer;
    }
}
