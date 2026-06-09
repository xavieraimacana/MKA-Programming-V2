package ec.espe.edu.coffeeshop.model;

/**
 * Represents an employee in the coffeeshop with credentials.
 * 
 * @author Anthony Aimacaña, MKA Programer, @ESPE
 */
public class Employee {
    private String id;
    private String name;
    private EmployeeRole role;
    private String username;
    private String password;

    public Employee() {}

    public Employee(String id, String name, EmployeeRole role, String username, String password) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.username = username;
        this.password = password;
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

    public EmployeeRole getRole() {
        return role;
    }

    public void setRole(EmployeeRole role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
