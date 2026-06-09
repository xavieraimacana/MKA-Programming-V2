package ec.espe.edu.coffeeshop.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents an item within an order.
 * 
 * @author Mateo Artieda, MKA Programmer, @ESPE
 */
public class OrderItem {
    private Product product;
    private int quantity;
    private BigDecimal pricePaidSnapshot;
    private List<Modifier> modifiers = new ArrayList<>();

    public OrderItem() {}

    public OrderItem(Product product, int quantity, BigDecimal pricePaidSnapshot) {
        this.product = product;
        this.quantity = quantity;
        this.pricePaidSnapshot = pricePaidSnapshot;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPricePaidSnapshot() {
        return pricePaidSnapshot;
    }

    public void setPricePaidSnapshot(BigDecimal pricePaidSnapshot) {
        this.pricePaidSnapshot = pricePaidSnapshot;
    }

    public List<Modifier> getModifiers() {
        return modifiers;
    }

    public void setModifiers(List<Modifier> modifiers) {
        this.modifiers = modifiers;
    }
}
