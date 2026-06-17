package ec.espe.edu.coffeeshop.controller;
import ec.espe.edu.coffeeshop.model.InventoryItem;
import java.util.ArrayList;
import java.util.List;
public class InventoryController {
    private List<InventoryItem> inventoryItems;
    public InventoryController() {
        this.inventoryItems = new ArrayList<>();
    }
    public void addInventoryItem(InventoryItem item) {
        inventoryItems.add(item);
    }
    public void removeInventoryItem(String itemId) {
        inventoryItems.removeIf(item -> item.getItemId().equals(itemId));
    }
    public void updateInventoryItem(InventoryItem updatedItem) {
        for (int i = 0; i < inventoryItems.size(); i++) {
            if (inventoryItems.get(i).getItemId().equals(updatedItem.getItemId())) {
                inventoryItems.set(i, updatedItem);
                return;
            }
        }
    }
    public InventoryItem getInventoryItem(String itemId) {
        return inventoryItems.stream()
                .filter(item -> item.getItemId().equals(itemId))
                .findFirst()
                .orElse(null);
    }
    public List<InventoryItem> getAllInventoryItems() {
        return new ArrayList<>(inventoryItems);
    }
    public List<InventoryItem> getLowStockItems() {
        List<InventoryItem> lowStockItems = new ArrayList<>();
        for (InventoryItem item : inventoryItems) {
            if (item.checkLowStock()) {
                lowStockItems.add(item);
            }
        }
        return lowStockItems;
    }
}
