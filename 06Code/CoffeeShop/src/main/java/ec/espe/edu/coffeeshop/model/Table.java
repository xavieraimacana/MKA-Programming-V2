package ec.espe.edu.coffeeshop.model;

/**
 * Represents a physical table inside the coffee shop.
 * 
 * @author MKA Programmers, ESPE
 */
public class Table {
    private String id;
    private int number;
    private int capacity;
    private TableStatus status;

    public Table() {}

    public Table(String id, int number, int capacity, TableStatus status) {
        this.id = id;
        this.number = number;
        this.capacity = capacity;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public TableStatus getStatus() {
        return status;
    }

    public void setStatus(TableStatus status) {
        this.status = status;
    }
}
