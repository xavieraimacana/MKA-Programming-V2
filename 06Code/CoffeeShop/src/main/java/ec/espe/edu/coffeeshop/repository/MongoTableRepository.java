package ec.espe.edu.coffeeshop.repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import ec.espe.edu.coffeeshop.model.Table;
import ec.espe.edu.coffeeshop.model.TableStatus;
import org.bson.Document;
import java.util.ArrayList;
import java.util.List;

/**
 * MongoDB implementation of the TableRepository.
 * 
 * @author MKA Programmers, ESPE
 */
public class MongoTableRepository implements TableRepository {
    private final MongoCollection<Document> collection;

    public MongoTableRepository() {
        MongoDatabase db = MongoDBConnection.getInstance().getDatabase();
        this.collection = db.getCollection("tables");
    }

    @Override
    public void save(Table table) {
        Document doc = new Document("_id", table.getId())
                .append("number", table.getNumber())
                .append("capacity", table.getCapacity())
                .append("status", table.getStatus().name());
        
        collection.replaceOne(Filters.eq("_id", table.getId()), doc, new ReplaceOptions().upsert(true));
    }

    @Override
    public Table findById(String id) {
        Document doc = collection.find(Filters.eq("_id", id)).first();
        return (doc != null) ? documentToTable(doc) : null;
    }

    @Override
    public List<Table> findAll() {
        List<Table> list = new ArrayList<>();
        for (Document doc : collection.find()) {
            list.add(documentToTable(doc));
        }
        return list;
    }

    @Override
    public void delete(String id) {
        collection.deleteOne(Filters.eq("_id", id));
    }

    private Table documentToTable(Document doc) {
        Table table = new Table();
        table.setId(doc.getString("_id"));
        table.setNumber(doc.getInteger("number"));
        table.setCapacity(doc.getInteger("capacity"));
        table.setStatus(TableStatus.valueOf(doc.getString("status")));
        return table;
    }
}
