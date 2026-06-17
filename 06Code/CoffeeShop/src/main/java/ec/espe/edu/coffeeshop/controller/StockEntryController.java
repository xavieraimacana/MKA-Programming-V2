package ec.espe.edu.coffeeshop.controller;
import ec.espe.edu.coffeeshop.model.StockEntry;
import java.util.ArrayList;
import java.util.List;
public class StockEntryController {
    private List<StockEntry> stockEntries;
    public StockEntryController() {
        this.stockEntries = new ArrayList<>();
    }
    public void addStockEntry(StockEntry entry) {
        stockEntries.add(entry);
        entry.processEntry();
    }
    public void removeStockEntry(String entryId) {
        stockEntries.removeIf(entry -> entry.getEntryId().equals(entryId));
    }
    public StockEntry getStockEntry(String entryId) {
        return stockEntries.stream()
                .filter(entry -> entry.getEntryId().equals(entryId))
                .findFirst()
                .orElse(null);
    }
    public List<StockEntry> getAllStockEntries() {
        return new ArrayList<>(stockEntries);
    }
}
