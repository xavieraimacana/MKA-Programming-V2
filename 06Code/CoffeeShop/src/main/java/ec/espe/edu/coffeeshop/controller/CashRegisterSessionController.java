package ec.espe.edu.coffeeshop.controller;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import ec.espe.edu.coffeeshop.model.CashRegisterSession;
import ec.espe.edu.coffeeshop.utils.MongoDBConnection;
import org.bson.Document;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
public class CashRegisterSessionController {
    private final MongoCollection<Document> collection;
    public CashRegisterSessionController() {
        MongoDatabase database = MongoDBConnection.getDatabase();
        this.collection = database.getCollection("CashRegisterSessions");
    }
    public boolean addSession(CashRegisterSession session) {
        try {
            Document doc = new Document("_id", session.getId())
                    .append("openingTime", session.getOpeningTime() != null ? session.getOpeningTime().toString() : null)
                    .append("closingTime", session.getClosingTime() != null ? session.getClosingTime().toString() : null)
                    .append("startingFloat", session.getStartingFloat())
                    .append("expectedSystemCash", session.getExpectedSystemCash())
                    .append("declaredPhysicalCash", session.getDeclaredPhysicalCash())
                    .append("discrepancyAmount", session.getDiscrepancyAmount());
            collection.insertOne(doc);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public List<CashRegisterSession> getAllSessions() {
        List<CashRegisterSession> sessions = new ArrayList<>();
        for (Document doc : collection.find()) {
            LocalDateTime openingTime = doc.getString("openingTime") != null ? LocalDateTime.parse(doc.getString("openingTime")) : null;
            LocalDateTime closingTime = doc.getString("closingTime") != null ? LocalDateTime.parse(doc.getString("closingTime")) : null;
            sessions.add(new CashRegisterSession(
                    doc.getString("_id"),
                    openingTime,
                    closingTime,
                    doc.getDouble("startingFloat") != null ? doc.getDouble("startingFloat") : 0.0,
                    doc.getDouble("expectedSystemCash") != null ? doc.getDouble("expectedSystemCash") : 0.0,
                    doc.getDouble("declaredPhysicalCash") != null ? doc.getDouble("declaredPhysicalCash") : 0.0,
                    doc.getDouble("discrepancyAmount") != null ? doc.getDouble("discrepancyAmount") : 0.0
            ));
        }
        return sessions;
    }
}
