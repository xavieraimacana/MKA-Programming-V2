package ec.espe.edu.coffeeshop.model;

import java.math.BigDecimal;

/**
 * Represents a modifier for a product (e.g., extra sugar, milk type).
 * 
 * @author Mateo Artieda, MKA Programmer, @ESPE
 */
public class Modifier {
    private String id;
    private String name;
    private BigDecimal extraPrice;
    private Recipe recipe;

    public Modifier() {}

    public Modifier(String id, String name, BigDecimal extraPrice, Recipe recipe) {
        this.id = id;
        this.name = name;
        this.extraPrice = extraPrice;
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

    public BigDecimal getExtraPrice() {
        return extraPrice;
    }

    public void setExtraPrice(BigDecimal extraPrice) {
        this.extraPrice = extraPrice;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }
}
