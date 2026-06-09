package ec.espe.edu.coffeeshop.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a recipe containing a list of recipe items.
 * 
 * @author Anthony Aimacaña, MKA Programer, @ESPE
 */
public class Recipe {
    private String id;
    private List<RecipeItem> items = new ArrayList<>();

    public Recipe() {}

    public Recipe(String id, List<RecipeItem> items) {
        this.id = id;
        this.items = items;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<RecipeItem> getItems() {
        return items;
    }

    public void setItems(List<RecipeItem> items) {
        this.items = items;
    }
}
