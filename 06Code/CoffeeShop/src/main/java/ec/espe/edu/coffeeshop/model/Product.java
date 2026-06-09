package ec.espe.edu.coffeeshop.model;

import java.math.BigDecimal;

/**
 * Abstract class representing a menu item in the Coffeeshop.
 * 
 * @author Anthony Aimacaña, MKA Programer, @ESPE
 */
public abstract class Product {
    private String id;
    private String name;
    private BigDecimal price;
    private ProductCategory category;
    private Recipe recipe;

    public Product() {}

    public Product(String id, String name, BigDecimal price, ProductCategory category, Recipe recipe) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
        this.recipe = recipe;
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public ProductCategory getCategory() {
        return category;
    }

    public void setCategory(ProductCategory category) {
        this.category = category;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }
}
