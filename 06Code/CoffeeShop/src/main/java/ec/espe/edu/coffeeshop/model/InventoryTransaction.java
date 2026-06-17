package ec.espe.edu.coffeeshop.model;
import java.time.LocalDateTime;
public class InventoryTransaction {
    private String transactionId;
    private LocalDateTime date;
    private TransactionType type;
    private double quantityChange;
    private String referenceDocumentId;
    private InventoryItem item;
    public InventoryTransaction(String transactionId, LocalDateTime date, TransactionType type, double quantityChange, String referenceDocumentId, InventoryItem item) {
        this.transactionId = transactionId;
        this.date = date;
        this.type = type;
        this.quantityChange = quantityChange;
        this.referenceDocumentId = referenceDocumentId;
        this.item = item;
    }
    public void executeTransaction() {
        if (item != null) {
            double newStock = item.getCurrentStock() + quantityChange;
            item.setCurrentStock(newStock);
        }
    }
    public void revertTransaction() {
        if (item != null) {
            double newStock = item.getCurrentStock() - quantityChange;
            item.setCurrentStock(newStock);
        }
    }
    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }
    public TransactionType getType() { return type; }
    public void setType(TransactionType type) { this.type = type; }
    public double getQuantityChange() { return quantityChange; }
    public void setQuantityChange(double quantityChange) { this.quantityChange = quantityChange; }
    public String getReferenceDocumentId() { return referenceDocumentId; }
    public void setReferenceDocumentId(String referenceDocumentId) { this.referenceDocumentId = referenceDocumentId; }
    public InventoryItem getItem() { return item; }
    public void setItem(InventoryItem item) { this.item = item; }
}
