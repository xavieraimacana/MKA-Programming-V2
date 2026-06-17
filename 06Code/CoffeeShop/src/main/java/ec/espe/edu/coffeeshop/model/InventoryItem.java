package ec.espe.edu.coffeeshop.model;
public class InventoryItem {
    private String itemId;
    private String name;
    private UnitOfMeasure baseUnit;
    private double currentStock;
    private double minThreshold;
    public InventoryItem(String itemId, String name, UnitOfMeasure baseUnit, double currentStock, double minThreshold) {
        this.itemId = itemId;
        this.name = name;
        this.baseUnit = baseUnit;
        this.currentStock = currentStock;
        this.minThreshold = minThreshold;
    }
    public boolean checkLowStock() {
        return this.currentStock <= this.minThreshold;
    }
    public double getCurrentStock() {
        return currentStock;
    }
    public double convertUnits(double amount, UnitOfMeasure from) {
        if (from == this.baseUnit) {
            return amount;
        }
        if ((from == UnitOfMeasure.GRAMS && this.baseUnit == UnitOfMeasure.KILOGRAMS) ||
            (from == UnitOfMeasure.MILLILITERS && this.baseUnit == UnitOfMeasure.LITERS)) {
            return amount / 1000.0;
        }
        if ((from == UnitOfMeasure.KILOGRAMS && this.baseUnit == UnitOfMeasure.GRAMS) ||
            (from == UnitOfMeasure.LITERS && this.baseUnit == UnitOfMeasure.MILLILITERS)) {
            return amount * 1000.0;
        }
        return amount;
    }
    public String getItemId() { return itemId; }
    public void setItemId(String itemId) { this.itemId = itemId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public UnitOfMeasure getBaseUnit() { return baseUnit; }
    public void setBaseUnit(UnitOfMeasure baseUnit) { this.baseUnit = baseUnit; }
    public void setCurrentStock(double currentStock) { this.currentStock = currentStock; }
    public double getMinThreshold() { return minThreshold; }
    public void setMinThreshold(double minThreshold) { this.minThreshold = minThreshold; }
    @Override
    public String toString() {
        return name;
    }
}
