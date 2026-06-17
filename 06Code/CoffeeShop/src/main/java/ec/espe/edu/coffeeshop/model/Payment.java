package ec.espe.edu.coffeeshop.model;
public class Payment {
    private String paymentId;
    private Order order;
    private double amount;
    private String method; 
    private boolean isSuccessful;
    public Payment(String paymentId, Order order, double amount, String method) {
        this.paymentId = paymentId;
        this.order = order;
        this.amount = amount;
        this.method = method;
        this.isSuccessful = false; 
    }
    public void processPayment() {
        if (amount > 0 && method != null && !method.isEmpty()) {
            this.isSuccessful = true;
            System.out.println("Payment " + paymentId + " processed successfully via " + method);
        } else {
            this.isSuccessful = false;
            System.out.println("Payment " + paymentId + " failed.");
        }
    }
    public void refundPayment() {
        if (isSuccessful) {
            this.isSuccessful = false;
            System.out.println("Payment " + paymentId + " refunded.");
        } else {
            System.out.println("Cannot refund payment " + paymentId + " as it was not successful.");
        }
    }
    public String getPaymentId() { return paymentId; }
    public void setPaymentId(String paymentId) { this.paymentId = paymentId; }
    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }
    public boolean isSuccessful() { return isSuccessful; }
    public void setSuccessful(boolean successful) { isSuccessful = successful; }
}
