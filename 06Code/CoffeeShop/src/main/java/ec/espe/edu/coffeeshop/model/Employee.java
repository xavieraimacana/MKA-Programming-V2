package ec.espe.edu.coffeeshop.model;

/**
 * Represents an employee with credentials and security configuration.
 * 
 * @author Anthony Aimacaña, MKA Programer, @ESPE
 */
public class Employee {
    private String id;
    private String name;
    private EmployeeRole role;
    private String username;
    private String password;
    private boolean changePasswordRequired;

    public Employee() {}

    public Employee(String id, String name, EmployeeRole role, String username, String password, boolean changePasswordRequired) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.username = username;
        this.password = password;
        this.changePasswordRequired = changePasswordRequired;
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

    public boolean isChangePasswordRequired() {
        return changePasswordRequired;
    }

    public void setChangePasswordRequired(boolean changePasswordRequired) {
        this.changePasswordRequired = changePasswordRequired;
    }
}
