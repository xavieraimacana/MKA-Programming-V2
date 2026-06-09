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
     * Incorporates usernames and passwords for testing credentials.
     */
    private static void seedDatabaseIfEmpty(MongoEmployeeRepository repo) {
        try {
            if (repo.findAll().isEmpty()) {
                System.out.println("Sembrando base de datos con empleados por defecto...");
                repo.save(new Employee("1", "Anthony Aimacaña", EmployeeRole.MANAGER, "anthony", "mka123"));
                repo.save(new Employee("2", "Mateo Artieda", EmployeeRole.CASHIER, "mateo", "mateo123"));
                repo.save(new Employee("3", "Kevin Albán", EmployeeRole.BARISTA, "kevin", "kevin123"));
                repo.save(new Employee("4", "Waiter User", EmployeeRole.WAITER, "waiter", "waiter123"));
                System.out.println("Sembrado completado.");
            }
        } catch (Exception e) {
            System.err.println("No se pudo sembrar la base de datos de manera automática: " + e.getMessage());
            System.out.println("Por favor, asegúrate de que la dirección de MongoDB remota sea accesible.");
        }
    }
}
