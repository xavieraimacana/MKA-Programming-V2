package ec.espe.edu.coffeeshop.controller;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import ec.espe.edu.coffeeshop.model.Employee;
import ec.espe.edu.coffeeshop.utils.MongoDBConnection;
import org.bson.Document;
public class EmployeeController {
    private final MongoCollection<Document> collection;
    public EmployeeController() {
        MongoDatabase database = MongoDBConnection.getDatabase();
        this.collection = database.getCollection("Employees");
    }
    public boolean addEmployee(Employee employee) {
        try {
            Document doc = new Document("_id", employee.getId())
                    .append("name", employee.getName())
                    .append("username", employee.getUsername())
                    .append("password", org.mindrot.jbcrypt.BCrypt.hashpw(employee.getPassword(), org.mindrot.jbcrypt.BCrypt.gensalt()))
                    .append("role", employee.getRole());
            collection.insertOne(doc);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public Employee authenticate(String username, String rawPassword) {
        Document doc = collection.find(Filters.eq("username", username)).first();
        if (doc != null) {
            String storedHash = doc.getString("password");
            boolean passwordMatches = false;
            if (storedHash != null && storedHash.startsWith("$2a$")) {
                passwordMatches = org.mindrot.jbcrypt.BCrypt.checkpw(rawPassword, storedHash);
            } else {
                passwordMatches = rawPassword.equals(storedHash);
            }
            if (passwordMatches) {
                return new Employee(
                        doc.getString("_id"),
                        doc.getString("name"),
                        doc.getString("username"),
                        doc.getString("password"),
                        doc.getString("role")
                );
            }
        }
        return null;
    }
    public java.util.List<Employee> getAllEmployees() {
        java.util.List<Employee> employees = new java.util.ArrayList<>();
        for (Document doc : collection.find()) {
            employees.add(new Employee(
                    doc.getString("_id"),
                    doc.getString("name"),
                    doc.getString("username"),
                    doc.getString("password"),
                    doc.getString("role")
            ));
        }
        return employees;
    }
    public boolean deleteEmployee(String id) {
        try {
            DeleteResult result = collection.deleteOne(Filters.eq("_id", id));
            return result.getDeletedCount() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
