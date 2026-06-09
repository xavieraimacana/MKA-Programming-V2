package ec.espe.edu.coffeeshop.repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import ec.espe.edu.coffeeshop.model.Equipment;
import ec.espe.edu.coffeeshop.model.EquipmentStatus;
import org.bson.Document;
import java.util.ArrayList;
import java.util.List;

/**
 * MongoDB implementation of the EquipmentRepository.
 * 
 * @author Kevin Albán, MKA Programmer, @ESPE
 */
public class MongoEquipmentRepository implements EquipmentRepository {
    private final MongoCollection<Document> collection;

    public MongoEquipmentRepository() {
        MongoDatabase db = MongoDBConnection.getInstance().getDatabase();
        this.collection = db.getCollection("equipments");
    }

    @Override
    public void save(Equipment equipment) {
        Document doc = new Document("_id", equipment.getId())
                .append("name", equipment.getName())
                .append("status", equipment.getStatus().name())
                .append("lastMaintenanceDate", equipment.getLastMaintenanceDate());
        
        collection.replaceOne(Filters.eq("_id", equipment.getId()), doc, new ReplaceOptions().upsert(true));
    }

    @Override
    public Equipment findById(String id) {
        Document doc = collection.find(Filters.eq("_id", id)).first();
        return (doc != null) ? documentToEquipment(doc) : null;
    }

    @Override
    public List<Equipment> findAll() {
        List<Equipment> list = new ArrayList<>();
        for (Document doc : collection.find()) {
            list.add(documentToEquipment(doc));
        }
        return list;
    }

    @Override
    public void delete(String id) {
        collection.deleteOne(Filters.eq("_id", id));
    }

    private Equipment documentToEquipment(Document doc) {
        Equipment equipment = new Equipment();
        equipment.setId(doc.getString("_id"));
        equipment.setName(doc.getString("name"));
        equipment.setStatus(EquipmentStatus.valueOf(doc.getString("status")));
        equipment.setLastMaintenanceDate(doc.getDate("lastMaintenanceDate"));
        return equipment;
    }
}
