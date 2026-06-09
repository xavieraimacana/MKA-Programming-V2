package ec.espe.edu.coffeeshop.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Represents a fiscal invoice for a completed order.
 * 
 * @author Mateo Artieda, MKA Programmer, @ESPE
 */
public class Invoice {
    private String id;
    private String invoiceNumber;
    private LocalDateTime date;
    private String clientName;
    private String clientTaxId;
    private Order order;
    private BigDecimal subtotal;
    private BigDecimal tax;
    private BigDecimal total;

    public Invoice() {
        this.date = LocalDateTime.now();
    }

    public Invoice(String id, String invoiceNumber, Order order) {
        this.id = id;
        this.invoiceNumber = invoiceNumber;
        this.order = order;
        this.date = LocalDateTime.now();
        if (order.getClient() != null) {
            this.clientName = order.getClient().getName();
            this.clientTaxId = order.getClient().getTaxId();
        }
        this.subtotal = order.getSubtotal();
        this.tax = order.getTax();
        this.total = order.getTotal();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientTaxId() {
        return clientTaxId;
    }

    public void setClientTaxId(String clientTaxId) {
        this.clientTaxId = clientTaxId;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}
