package ec.espe.edu.coffeeshop.repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import ec.espe.edu.coffeeshop.model.Reservation;
import org.bson.Document;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * MongoDB implementation of the ReservationRepository.
 * 
 * @author MKA Programmers, ESPE
 */
public class MongoReservationRepository implements ReservationRepository {
    private final MongoCollection<Document> collection;

    public MongoReservationRepository() {
        MongoDatabase db = MongoDBConnection.getInstance().getDatabase();
        this.collection = db.getCollection("reservations");
    }

    @Override
    public void save(Reservation reservation) {
        Document doc = new Document("_id", reservation.getId())
                .append("tableId", reservation.getTableId())
                .append("customerName", reservation.getCustomerName())
                .append("reservationTime", Date.from(reservation.getReservationTime().atZone(ZoneId.systemDefault()).toInstant()))
                .append("numberOfPeople", reservation.getNumberOfPeople());
        
        collection.replaceOne(Filters.eq("_id", reservation.getId()), doc, new ReplaceOptions().upsert(true));
    }

    @Override
    public Reservation findById(String id) {
        Document doc = collection.find(Filters.eq("_id", id)).first();
        return (doc != null) ? documentToReservation(doc) : null;
    }

    @Override
    public List<Reservation> findAll() {
        List<Reservation> list = new ArrayList<>();
        for (Document doc : collection.find()) {
            list.add(documentToReservation(doc));
        }
        return list;
    }

    @Override
    public void delete(String id) {
        collection.deleteOne(Filters.eq("_id", id));
    }

    private Reservation documentToReservation(Document doc) {
        Reservation reservation = new Reservation();
        reservation.setId(doc.getString("_id"));
        reservation.setTableId(doc.getString("tableId"));
        reservation.setCustomerName(doc.getString("customerName"));
        
        Date date = doc.getDate("reservationTime");
        if (date != null) {
            reservation.setReservationTime(LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()));
        }
        
        reservation.setNumberOfPeople(doc.getInteger("numberOfPeople"));
        return reservation;
    }
}
