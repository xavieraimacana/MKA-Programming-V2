package ec.espe.edu.coffeeshop;

import ec.espe.edu.coffeeshop.model.Employee;
import ec.espe.edu.coffeeshop.model.EmployeeRole;
import ec.espe.edu.coffeeshop.repository.MongoEmployeeRepository;
import ec.espe.edu.coffeeshop.view.LoginFrame;

import javax.swing.*;

/**
 * Main entry point for the Coffeeshop Management System.
 * Connects to MongoDB, seeds initial database records, and launches the GUI.
 * 
 * @author Anthony Aimacaña, MKA Programer, @ESPE
 */
public class CoffeeShop {

    public static void main(String[] args) {
        // 1. Conectar y sembrar base de datos remota si está vacía
        MongoEmployeeRepository employeeRepo = new MongoEmployeeRepository();
        seedDatabaseIfEmpty(employeeRepo);

        // 2. Iniciar la interfaz gráfica de usuario (GUI) en el Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            try {
                // Configurar Look & Feel del sistema operativo para una integración visual óptima
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                // Fallback al look and feel básico si falla
            }
            
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }

    /**
     * Seeds default employees into the MongoDB collection if it is currently empty.
     * Sets changePasswordRequired to true for all default users.
     */
    private static void seedDatabaseIfEmpty(MongoEmployeeRepository repo) {
        try {
            System.out.println("=== DIAGNOSTICO DE EMPLEADOS EN MONGODB ===");
            java.util.List<Employee> employees = repo.findAll();
            if (employees.isEmpty()) {
                System.out.println("La coleccion esta vacia. Sembrando empleados por defecto...");
            } else {
                System.out.println("Empleados registrados actualmente (" + employees.size() + "):");
                for (Employee emp : employees) {
                    System.out.println(" - ID: " + emp.getId() + " | Nombre: " + emp.getName() + " | Usuario: " + emp.getUsername() + " | Rol: " + emp.getRole() + " | Requiere Cambio: " + emp.isChangePasswordRequired());
                }
            }

            // Siembra robusta: si no existe cada ID individualmente o si el registro es obsoleto (username es null), lo reparamos/sembramos
            Employee emp1 = repo.findById("1");
            if (emp1 == null || emp1.getUsername() == null) {
                System.out.println("Sembrando/Reparando admin por defecto (Anthony)...");
                repo.save(new Employee("1", "Anthony Aimacaña", EmployeeRole.MANAGER, "anthony", "mka123", true));
            }
            Employee emp2 = repo.findById("2");
            if (emp2 == null || emp2.getUsername() == null) {
                System.out.println("Sembrando/Reparando cajero por defecto (Mateo)...");
                repo.save(new Employee("2", "Mateo Artieda", EmployeeRole.CASHIER, "mateo", "mateo123", true));
            }
            Employee emp3 = repo.findById("3");
            if (emp3 == null || emp3.getUsername() == null) {
                System.out.println("Sembrando/Reparando barista por defecto (Kevin)...");
                repo.save(new Employee("3", "Kevin Albán", EmployeeRole.BARISTA, "kevin", "kevin123", true));
            }
            Employee emp4 = repo.findById("4");
            if (emp4 == null || emp4.getUsername() == null) {
                System.out.println("Sembrando/Reparando mesero por defecto...");
                repo.save(new Employee("4", "Waiter User", EmployeeRole.WAITER, "waiter", "waiter123", true));
            }
            System.out.println("============================================");
        } catch (Exception e) {
            System.err.println("No se pudo conectar o sembrar la base de datos: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
