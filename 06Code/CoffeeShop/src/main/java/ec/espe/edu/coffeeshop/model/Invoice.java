package ec.espe.edu.coffeeshop.model;
import java.time.LocalDateTime;
public class Invoice {
    private String invoiceId;
    private Order order;
    private LocalDateTime issueDate;
    private double subtotal;
    private double taxAmount;
    private double totalAmount;
    private static final double TAX_RATE = 0.15; 
    public Invoice(String invoiceId, Order order, LocalDateTime issueDate) {
        this.invoiceId = invoiceId;
        this.order = order;
        this.issueDate = issueDate;
        calculateSubtotal();
        calculateTaxes();
    }
    private void calculateSubtotal() {
        this.subtotal = 0;
        if (order != null && order.getItems() != null) {
            for (OrderItem item : order.getItems()) {
                this.subtotal += item.getSubtotal();
            }
        }
    }
    public void calculateTaxes() {
        this.taxAmount = this.subtotal * TAX_RATE;
        this.totalAmount = this.subtotal + this.taxAmount;
    }
    public String generateFiscalReceipt() {
        StringBuilder receipt = new StringBuilder();
        receipt.append("======================\n");
        receipt.append("       INVOICE        \n");
        receipt.append("======================\n");
        receipt.append("Invoice ID: ").append(invoiceId).append("\n");
        receipt.append("Date: ").append(issueDate.toString()).append("\n");
        receipt.append("----------------------\n");
        if (order != null && order.getItems() != null) {
            for (OrderItem item : order.getItems()) {
                receipt.append(item.getProduct().getName())
                       .append(" x").append(item.getQuantity())
                       .append(" - $").append(String.format("%.2f", item.getSubtotal())).append("\n");
            }
        }
        receipt.append("----------------------\n");
        receipt.append("Subtotal: $").append(String.format("%.2f", subtotal)).append("\n");
        receipt.append("Tax (15%): $").append(String.format("%.2f", taxAmount)).append("\n");
        receipt.append("Total: $").append(String.format("%.2f", totalAmount)).append("\n");
        receipt.append("======================\n");
        return receipt.toString();
    }
    public void triggerInventoryDeduction() {
        System.out.println("Triggering inventory deduction for Invoice: " + invoiceId);
    }
    public String getInvoiceId() { return invoiceId; }
    public void setInvoiceId(String invoiceId) { this.invoiceId = invoiceId; }
    public Order getOrder() { return order; }
    public void setOrder(Order order) { 
        this.order = order; 
        calculateSubtotal();
        calculateTaxes();
    }
    public LocalDateTime getIssueDate() { return issueDate; }
    public void setIssueDate(LocalDateTime issueDate) { this.issueDate = issueDate; }
    public double getSubtotal() { return subtotal; }
    public double getTaxAmount() { return taxAmount; }
    public double getTotalAmount() { return totalAmount; }
}
