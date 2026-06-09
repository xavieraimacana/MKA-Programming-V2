package ec.espe.edu.coffeeshop.repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import ec.espe.edu.coffeeshop.model.Ingredient;
import ec.espe.edu.coffeeshop.model.PurchaseOrder;
import ec.espe.edu.coffeeshop.model.PurchaseOrderItem;
import ec.espe.edu.coffeeshop.model.PurchaseOrderStatus;
import org.bson.Document;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * MongoDB implementation of the PurchaseOrderRepository.
 * 
 * @author Kevin Albán, MKA Programmer, @ESPE
 */
public class MongoPurchaseOrderRepository implements PurchaseOrderRepository {
    private final MongoCollection<Document> collection;

    public MongoPurchaseOrderRepository() {
        MongoDatabase db = MongoDBConnection.getInstance().getDatabase();
        this.collection = db.getCollection("purchase_orders");
    }

    @Override
    public void save(PurchaseOrder order) {
        List<Document> itemsDocs = order.getItemsToBuy().stream()
                .map(item -> new Document("ingredientId", item.getIngredient().getId())
                        .append("ingredientName", item.getIngredient().getName())
                        .append("quantity", item.getQuantity()))
                .collect(Collectors.toList());

        Document doc = new Document("_id", order.getId())
                .append("supplierName", order.getSupplierName())
                .append("date", order.getDate())
                .append("totalCost", order.getTotalCost().toString())
                .append("status", order.getStatus().name())
                .append("itemsToBuy", itemsDocs);

        collection.replaceOne(Filters.eq("_id", order.getId()), doc, new ReplaceOptions().upsert(true));
    }

    @Override
    public PurchaseOrder findById(String id) {
        Document doc = collection.find(Filters.eq("_id", id)).first();
        return (doc != null) ? documentToPurchaseOrder(doc) : null;
    }

    @Override
    public List<PurchaseOrder> findAll() {
        List<PurchaseOrder> list = new ArrayList<>();
        for (Document doc : collection.find()) {
            list.add(documentToPurchaseOrder(doc));
        }
        return list;
    }

    @Override
    public void delete(String id) {
        collection.deleteOne(Filters.eq("_id", id));
    }

    private PurchaseOrder documentToPurchaseOrder(Document doc) {
        PurchaseOrder order = new PurchaseOrder();
        order.setId(doc.getString("_id"));
        order.setSupplierName(doc.getString("supplierName"));
        order.setDate(doc.getDate("date"));
        order.setTotalCost(new BigDecimal(doc.getString("totalCost")));
        order.setStatus(PurchaseOrderStatus.valueOf(doc.getString("status")));

        List<Document> itemsDocs = (List<Document>) doc.get("itemsToBuy");
        if (itemsDocs != null) {
            List<PurchaseOrderItem> items = itemsDocs.stream()
                    .map(itemDoc -> {
                        Ingredient ing = new Ingredient();
                        ing.setId(itemDoc.getString("ingredientId"));
                        ing.setName(itemDoc.getString("ingredientName"));
                        return new PurchaseOrderItem(ing, itemDoc.getDouble("quantity"));
                    })
                    .collect(Collectors.toList());
            order.setItemsToBuy(items);
        }

        return order;
    }
}
