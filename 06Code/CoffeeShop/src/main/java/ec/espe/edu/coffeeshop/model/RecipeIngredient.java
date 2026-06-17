package ec.espe.edu.coffeeshop.model;
public class RecipeIngredient {
    private String itemId;
    private double requiredQuantity;
    private UnitOfMeasure unit;
    public RecipeIngredient() {}
    public RecipeIngredient(String itemId, double requiredQuantity, UnitOfMeasure unit) {
        this.itemId = itemId;
        this.requiredQuantity = requiredQuantity;
        this.unit = unit;
    }
    public String getItemId() { return itemId; }
    public void setItemId(String itemId) { this.itemId = itemId; }
    public double getRequiredQuantity() { return requiredQuantity; }
    public void setRequiredQuantity(double requiredQuantity) { this.requiredQuantity = requiredQuantity; }
    public UnitOfMeasure getUnit() { return unit; }
    public void setUnit(UnitOfMeasure unit) { this.unit = unit; }
    public double calculateConversion() {
        if (unit == null) return requiredQuantity;
        switch (unit) {
            case MILLILITERS:
            case GRAMS:
                return requiredQuantity / 1000.0;
            default:
                return requiredQuantity;
        }
    }
}
