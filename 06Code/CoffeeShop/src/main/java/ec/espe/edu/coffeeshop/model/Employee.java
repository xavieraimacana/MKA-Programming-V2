package ec.espe.edu.coffeeshop.model;
public class Employee {
    private String employeeId;
    private String name;
    private String role;
    private String username;
    private String password;
    public Employee(String employeeId, String name, String username, String password, String role) {
        this.employeeId = employeeId;
        this.name = name;
        this.username = username;
        this.password = password;
        this.role = role;
    }
    public boolean login(String pin) {
        return this.password != null && this.password.equals(pin);
    }
    public void logout() {
        System.out.println("Employee logged out: " + name);
    }
    public void updateRole(String newRole) {
        this.role = newRole;
    }
    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }
    public String getId() { return employeeId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
