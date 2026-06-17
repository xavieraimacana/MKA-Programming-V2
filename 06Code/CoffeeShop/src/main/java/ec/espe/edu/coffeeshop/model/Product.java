package ec.espe.edu.coffeeshop.model;
public class Product {
    private String productId;
    private String name;
    private double basePrice;
    private boolean available;
    public Product() {}
    public Product(String productId, String name, double basePrice, boolean available) {
        this.productId = productId;
        this.name = name;
        this.basePrice = basePrice;
        this.available = available;
    }
    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public double getBasePrice() { return basePrice; }
    public void setBasePrice(double basePrice) { this.basePrice = basePrice; }
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
    public void updatePrice(double newPrice) {
        if (newPrice < 0) throw new IllegalArgumentException("Price cannot be negative");
        this.basePrice = newPrice;
    }
    public void toggleAvailability() {
        this.available = !this.available;
    }
    @Override
    public String toString() {
        return name + " - $" + basePrice;
    }
}
