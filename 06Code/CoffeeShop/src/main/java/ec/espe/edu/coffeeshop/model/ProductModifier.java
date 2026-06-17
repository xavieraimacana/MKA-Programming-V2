package ec.espe.edu.coffeeshop.model;
public class ProductModifier {
    private String modifierId;
    private String name;
    private double additionalPrice;
    public ProductModifier() {}
    public ProductModifier(String modifierId, String name, double additionalPrice) {
        this.modifierId = modifierId;
        this.name = name;
        this.additionalPrice = additionalPrice;
    }
    public String getModifierId() { return modifierId; }
    public void setModifierId(String modifierId) { this.modifierId = modifierId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public double getAdditionalPrice() { return additionalPrice; }
    public void setAdditionalPrice(double additionalPrice) { this.additionalPrice = additionalPrice; }
    public void updatePrice(double newPrice) {
        if (newPrice < 0) throw new IllegalArgumentException("Price cannot be negative");
        this.additionalPrice = newPrice;
    }
    @Override
    public String toString() {
        return name + " (+$" + additionalPrice + ")";
    }
}
