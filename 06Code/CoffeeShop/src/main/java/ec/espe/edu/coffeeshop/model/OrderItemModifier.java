package ec.espe.edu.coffeeshop.model;
public class OrderItemModifier {
    private ProductModifier modifier;
    private double priceAtTimeOfOrder;
    public OrderItemModifier(ProductModifier modifier, double priceAtTimeOfOrder) {
        this.modifier = modifier;
        this.priceAtTimeOfOrder = priceAtTimeOfOrder;
    }
    public double getAppliedPrice() {
        return priceAtTimeOfOrder;
    }
    public ProductModifier getModifier() { return modifier; }
    public void setModifier(ProductModifier modifier) { this.modifier = modifier; }
    public double getPriceAtTimeOfOrder() { return priceAtTimeOfOrder; }
    public void setPriceAtTimeOfOrder(double priceAtTimeOfOrder) { this.priceAtTimeOfOrder = priceAtTimeOfOrder; }
}
