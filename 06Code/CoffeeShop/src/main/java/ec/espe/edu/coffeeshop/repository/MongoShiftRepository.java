package ec.espe.edu.coffeeshop.repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import ec.espe.edu.coffeeshop.model.ReconciliationStatus;
import ec.espe.edu.coffeeshop.model.Shift;
import org.bson.Document;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * MongoDB implementation of the ShiftRepository.
 * 
 * @author Mateo Artieda, MKA Programmer, @ESPE
 */
public class MongoShiftRepository implements ShiftRepository {
    private final MongoCollection<Document> collection;

    public MongoShiftRepository() {
        MongoDatabase db = MongoDBConnection.getInstance().getDatabase();
        this.collection = db.getCollection("shifts");
    }

    @Override
    public void save(Shift shift) {
        Document doc = shiftToDocument(shift);
        collection.replaceOne(Filters.eq("_id", shift.getId()), doc, new ReplaceOptions().upsert(true));
    }

    @Override
    public Shift findById(String id) {
        Document doc = collection.find(Filters.eq("_id", id)).first();
        return (doc != null) ? documentToShift(doc) : null;
    }

    @Override
    public List<Shift> findAll() {
        List<Shift> list = new ArrayList<>();
        for (Document doc : collection.find()) {
            list.add(documentToShift(doc));
        }
        return list;
    }

    @Override
    public void delete(String id) {
        collection.deleteOne(Filters.eq("_id", id));
    }

    private Document shiftToDocument(Shift shift) {
        Document doc = new Document("_id", shift.getId())
                .append("cashierName", shift.getCashierName())
                .append("startTime", Date.from(shift.getStartTime().atZone(ZoneId.systemDefault()).toInstant()))
                .append("startingCash", shift.getStartingCash().toString())
                .append("declaredEndingCash", shift.getDeclaredEndingCash().toString())
                .append("systemEndingCash", shift.getSystemEndingCash().toString())
                .append("difference", shift.getDifference().toString());

        if (shift.getEndTime() != null) {
            doc.append("endTime", Date.from(shift.getEndTime().atZone(ZoneId.systemDefault()).toInstant()));
        }

        if (shift.getReconciliationStatus() != null) {
            doc.append("reconciliationStatus", shift.getReconciliationStatus().name());
        }

        return doc;
    }

    private Shift documentToShift(Document doc) {
        Shift shift = new Shift();
        shift.setId(doc.getString("_id"));
        shift.setCashierName(doc.getString("cashierName"));
        shift.setStartTime(doc.getDate("startTime").toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        shift.setStartingCash(new BigDecimal(doc.getString("startingCash")));
        shift.setDeclaredEndingCash(new BigDecimal(doc.getString("declaredEndingCash")));
        shift.setSystemEndingCash(new BigDecimal(doc.getString("systemEndingCash")));
        shift.setDifference(new BigDecimal(doc.getString("difference")));

        Date endTime = doc.getDate("endTime");
        if (endTime != null) {
            shift.setEndTime(endTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        }

        String status = doc.getString("reconciliationStatus");
        if (status != null) {
            shift.setReconciliationStatus(ReconciliationStatus.valueOf(status));
        }

        return shift;
    }
}
