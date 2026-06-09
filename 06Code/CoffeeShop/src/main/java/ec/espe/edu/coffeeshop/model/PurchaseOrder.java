package ec.espe.edu.coffeeshop.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Represents a purchase order sent to a supplier for replenishment.
 * 
 * @author Kevin Albán, MKA Programmer, @ESPE
 */
public class PurchaseOrder {
    private String id;
    private String supplierName;
    private Date date;
    private List<PurchaseOrderItem> itemsToBuy = new ArrayList<>();
    private BigDecimal totalCost;
    private PurchaseOrderStatus status;

    public PurchaseOrder() {}

    public PurchaseOrder(String id, String supplierName, Date date, BigDecimal totalCost, PurchaseOrderStatus status) {
        this.id = id;
        this.supplierName = supplierName;
        this.date = date;
        this.totalCost = totalCost;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<PurchaseOrderItem> getItemsToBuy() {
        return itemsToBuy;
    }

    public void setItemsToBuy(List<PurchaseOrderItem> itemsToBuy) {
        this.itemsToBuy = itemsToBuy;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public PurchaseOrderStatus getStatus() {
        return status;
    }

    public void setStatus(PurchaseOrderStatus status) {
        this.status = status;
    }
}
