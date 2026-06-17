package ec.espe.edu.coffeeshop.model;
public class Customer {
    private String customerId;
    private String name;
    private String taxId;
    private int loyaltyPoints;
    public Customer(String customerId, String name, String taxId, int loyaltyPoints) {
        this.customerId = customerId;
        this.name = name;
        this.taxId = taxId;
        this.loyaltyPoints = loyaltyPoints;
    }
    public void registerCustomer() {
        System.out.println("Customer registered: " + name);
    }
    public void addLoyaltyPoints(int points) {
        this.loyaltyPoints += points;
    }
    public void redeemPoints(int points) {
        if (this.loyaltyPoints >= points) {
            this.loyaltyPoints -= points;
        }
    }
    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }
    public String getId() { return customerId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getTaxId() { return taxId; }
    public void setTaxId(String taxId) { this.taxId = taxId; }
    public int getLoyaltyPoints() { return loyaltyPoints; }
    public void setLoyaltyPoints(int loyaltyPoints) { this.loyaltyPoints = loyaltyPoints; }
}
