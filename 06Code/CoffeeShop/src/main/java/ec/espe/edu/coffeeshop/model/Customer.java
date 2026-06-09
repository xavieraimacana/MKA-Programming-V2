package ec.espe.edu.coffeeshop.model;

/**
 * Represents a customer of the coffee shop.
 * 
 * @author Mateo Artieda, MKA Programmer, @ESPE
 */
public class Customer {
    private String id;
    private String name;
    private String taxId;
    private String email;
    private int loyaltyPoints;

    public Customer() {}

    public Customer(String id, String name, String taxId, String email) {
        this.id = id;
        this.name = name;
        this.taxId = taxId;
        this.email = email;
        this.loyaltyPoints = 0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTaxId() {
        return taxId;
    }

    public void setTaxId(String taxId) {
        this.taxId = taxId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getLoyaltyPoints() {
        return loyaltyPoints;
    }

    public void setLoyaltyPoints(int loyaltyPoints) {
        this.loyaltyPoints = loyaltyPoints;
    }
}
