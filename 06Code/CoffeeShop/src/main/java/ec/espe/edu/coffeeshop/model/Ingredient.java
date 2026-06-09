package ec.espe.edu.coffeeshop.model;

/**
 * Represents an ingredient tracked perpetually in the inventory.
 * 
 * @author Anthony Aimacaña, MKA Programer, @ESPE
 */
public class Ingredient {
    private String id;
    private String name;
    private Double stock;
    private Double minimumAlertQuantity;

    public Ingredient() {}

    public Ingredient(String id, String name, Double stock, Double minimumAlertQuantity) {
        this.id = id;
        this.name = name;
        this.stock = stock;
        this.minimumAlertQuantity = minimumAlertQuantity;
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

    public Double getStock() {
        return stock;
    }

    public void setStock(Double stock) {
        this.stock = stock;
    }

    public Double getMinimumAlertQuantity() {
        return minimumAlertQuantity;
    }

    public void setMinimumAlertQuantity(Double minimumAlertQuantity) {
        this.minimumAlertQuantity = minimumAlertQuantity;
    }
}
