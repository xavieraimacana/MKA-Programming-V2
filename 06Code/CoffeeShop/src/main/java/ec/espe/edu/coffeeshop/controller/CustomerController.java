package ec.espe.edu.coffeeshop.controller;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import ec.espe.edu.coffeeshop.model.Customer;
import ec.espe.edu.coffeeshop.utils.MongoDBConnection;
import org.bson.Document;
import java.util.ArrayList;
import java.util.List;
public class CustomerController {
    private final MongoCollection<Document> collection;
    public CustomerController() {
        MongoDatabase database = MongoDBConnection.getDatabase();
        this.collection = database.getCollection("Customers");
    }
    public boolean addCustomer(Customer customer) {
        try {
            Document doc = new Document("_id", customer.getId())
                    .append("name", customer.getName())
                    .append("taxId", customer.getTaxId())
                    .append("loyaltyPoints", customer.getLoyaltyPoints());
            collection.insertOne(doc);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        for (Document doc : collection.find()) {
            customers.add(new Customer(
                    doc.getString("_id"),
                    doc.getString("name"),
                    doc.getString("taxId"),
                    doc.getInteger("loyaltyPoints", 0)
            ));
        }
        return customers;
    }
    public boolean deleteCustomer(String id) {
        try {
            DeleteResult result = collection.deleteOne(Filters.eq("_id", id));
            return result.getDeletedCount() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
