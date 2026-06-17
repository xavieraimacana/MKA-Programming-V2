package ec.espe.edu.coffeeshop.model;
import java.util.ArrayList;
import java.util.List;
public class OrderItem {
    private Product product;
    private int quantity;
    private double subtotal;
    private List<OrderItemModifier> modifiers;
    public OrderItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
        this.modifiers = new ArrayList<>();
        calculateSubtotal();
    }
    public void addModifier(OrderItemModifier modifier) {
        this.modifiers.add(modifier);
        calculateSubtotal();
    }
    public void calculateSubtotal() {
        double modifiersTotal = 0;
        if (modifiers != null) {
            for (OrderItemModifier modifier : modifiers) {
                modifiersTotal += modifier.getAppliedPrice();
            }
        }
        this.subtotal = (product.getBasePrice() + modifiersTotal) * quantity;
    }
    public Product getProduct() { return product; }
    public void setProduct(Product product) { 
        this.product = product; 
        calculateSubtotal();
    }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { 
        this.quantity = quantity; 
        calculateSubtotal();
    }
    public double getSubtotal() { return subtotal; }
    public List<OrderItemModifier> getModifiers() { return modifiers; }
    public void setModifiers(List<OrderItemModifier> modifiers) { 
        this.modifiers = modifiers; 
        calculateSubtotal();
    }
}
