package ec.espe.edu.coffeeshop.model;
import java.util.List;
public class Supplier {
    private String supplierId;
    private String companyName;
    public Supplier() {
    }
    public Supplier(String supplierId, String companyName) {
        this.supplierId = supplierId;
        this.companyName = companyName;
    }
    public void placePurchaseOrder(List<String> items) {
        System.out.println("Order placed with " + companyName + " for items: " + items);
    }
    public void updateContactInfo(String info) {
        System.out.println("Contact info updated: " + info);
    }
    public String getSupplierId() { return supplierId; }
    public void setSupplierId(String supplierId) { this.supplierId = supplierId; }
    public String getId() { return supplierId; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
}
