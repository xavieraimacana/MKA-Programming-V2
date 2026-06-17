package ec.espe.edu.coffeeshop.model;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
public class Order {
    private String orderId;
    private LocalDateTime orderDate;
    private OrderStatus status;
    private String preparationNotes;
    private List<OrderItem> items;
    public Order(String orderId, LocalDateTime orderDate, OrderStatus status, String preparationNotes) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.status = status;
        this.preparationNotes = preparationNotes;
        this.items = new ArrayList<>();
    }
    public void addItem(Product product, int qty) {
        OrderItem item = new OrderItem(product, qty);
        items.add(item);
    }
    public void removeItem(String productId) {
        items.removeIf(item -> item.getProduct().getProductId().equals(productId));
    }
    public void updateStatus(OrderStatus newStatus) {
        this.status = newStatus;
    }
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public LocalDateTime getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDateTime orderDate) { this.orderDate = orderDate; }
    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }
    public String getPreparationNotes() { return preparationNotes; }
    public void setPreparationNotes(String preparationNotes) { this.preparationNotes = preparationNotes; }
    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }
}
