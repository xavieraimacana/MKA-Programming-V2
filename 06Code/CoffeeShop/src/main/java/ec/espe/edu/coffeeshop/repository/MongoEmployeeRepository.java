package ec.espe.edu.coffeeshop.repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import ec.espe.edu.coffeeshop.model.Employee;
import ec.espe.edu.coffeeshop.model.EmployeeRole;
import org.bson.Document;
import java.util.ArrayList;
import java.util.List;

/**
 * MongoDB implementation of the EmployeeRepository.
 * 
 * @author Anthony Aimacaña, MKA Programer, @ESPE
 */
public class MongoEmployeeRepository implements EmployeeRepository {
    private final MongoCollection<Document> collection;

    public MongoEmployeeRepository() {
        MongoDatabase db = MongoDBConnection.getInstance().getDatabase();
        this.collection = db.getCollection("employees");
    }

    @Override
    public void save(Employee employee) {
        Document doc = new Document("_id", employee.getId())
                .append("name", employee.getName())
                .append("role", employee.getRole().name())
                .append("username", employee.getUsername())
                .append("password", employee.getPassword())
                .append("changePasswordRequired", employee.isChangePasswordRequired());
        
        collection.replaceOne(Filters.eq("_id", employee.getId()), doc, new ReplaceOptions().upsert(true));
    }

    @Override
    public Employee findById(String id) {
        Document doc = collection.find(Filters.eq("_id", id)).first();
        if (doc == null) {
            return null;
        }
        return documentToEmployee(doc);
    }

    @Override
    public Employee findByCredentials(String username, String password) {
        Document doc = collection.find(Filters.and(
            Filters.eq("username", username),
            Filters.eq("password", password)
        )).first();
        if (doc == null) {
            return null;
        }
        return documentToEmployee(doc);
    }

    @Override
    public List<Employee> findAll() {
        List<Employee> list = new ArrayList<>();
        for (Document doc : collection.find()) {
            list.add(documentToEmployee(doc));
        }
        return list;
    }

    @Override
    public void delete(String id) {
        collection.deleteOne(Filters.eq("_id", id));
    }

    private Employee documentToEmployee(Document doc) {
        Employee employee = new Employee();
        employee.setId(doc.getString("_id"));
        employee.setName(doc.getString("name"));
        employee.setRole(EmployeeRole.valueOf(doc.getString("role")));
        employee.setUsername(doc.getString("username"));
        employee.setPassword(doc.getString("password"));
        
        // Asume false por defecto si el campo no existe en el documento
        employee.setChangePasswordRequired(doc.getBoolean("changePasswordRequired", false));
        return employee;
    }
}
