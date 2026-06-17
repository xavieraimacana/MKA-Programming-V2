package ec.espe.edu.coffeeshop.model;
import java.time.LocalDateTime;
public class StockEntry {
    private String entryId;
    private LocalDateTime dateReceived;
    private double quantityReceived;
    private UnitOfMeasure unitReceived;
    private double totalCost;
    private InventoryItem item;
    public StockEntry(String entryId, LocalDateTime dateReceived, double quantityReceived, UnitOfMeasure unitReceived, double totalCost, InventoryItem item) {
        this.entryId = entryId;
        this.dateReceived = dateReceived;
        this.quantityReceived = quantityReceived;
        this.unitReceived = unitReceived;
        this.totalCost = totalCost;
        this.item = item;
    }
    public void processEntry() {
        if (item != null) {
            double convertedQuantity = item.convertUnits(quantityReceived, unitReceived);
            item.setCurrentStock(item.getCurrentStock() + convertedQuantity);
        }
    }
    public double calculateUnitCost() {
        if (quantityReceived == 0) return 0;
        return totalCost / quantityReceived;
    }
    public String getEntryId() { return entryId; }
    public void setEntryId(String entryId) { this.entryId = entryId; }
    public LocalDateTime getDateReceived() { return dateReceived; }
    public void setDateReceived(LocalDateTime dateReceived) { this.dateReceived = dateReceived; }
    public double getQuantityReceived() { return quantityReceived; }
    public void setQuantityReceived(double quantityReceived) { this.quantityReceived = quantityReceived; }
    public UnitOfMeasure getUnitReceived() { return unitReceived; }
    public void setUnitReceived(UnitOfMeasure unitReceived) { this.unitReceived = unitReceived; }
    public double getTotalCost() { return totalCost; }
    public void setTotalCost(double totalCost) { this.totalCost = totalCost; }
    public InventoryItem getItem() { return item; }
    public void setItem(InventoryItem item) { this.item = item; }
}
