package ec.espe.edu.coffeeshop;

import ec.espe.edu.coffeeshop.model.Employee;
import ec.espe.edu.coffeeshop.model.EmployeeRole;
import ec.espe.edu.coffeeshop.repository.MongoDBConnection;
import ec.espe.edu.coffeeshop.repository.MongoEmployeeRepository;
import ec.espe.edu.coffeeshop.utils.I18nHelper;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.Scanner;

/**
 * Main entry point for the Coffeeshop Management System.
 * Handles language selection, database seeding, and role-based login menus.
 * 
 * @author Anthony Aimacaña, MKA Programer, @ESPE
 */
public class CoffeeShop {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        // 1. Selección de Idioma Inicial (i18n)
        System.out.println("========================================");
        System.out.println("Choose Language / Seleccione Idioma:");
        System.out.println("1. English");
        System.out.println("2. Español");
        System.out.print("Option / Opción: ");
        int langOption = 1;
        try {
            langOption = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            // Default to English
        }
        
        if (langOption == 2) {
            I18nHelper.setLocale(new Locale("es"));
        } else {
            I18nHelper.setLocale(Locale.ENGLISH);
        }
        
        System.out.println("\n" + I18nHelper.getMessage("menu.welcome"));
        System.out.println("========================================");

        // 2. Conectar y Sembrar base de datos si está vacía
        MongoEmployeeRepository employeeRepo = new MongoEmployeeRepository();
        seedDatabaseIfEmpty(employeeRepo);

        // 3. Bucle Principal de Login
        Employee loggedInUser = null;
        while (loggedInUser == null) {
            System.out.print("\n" + I18nHelper.getMessage("login.prompt") + " (1: Manager, 2: Cashier, 3: Barista, 4: Waiter): ");
            String id = scanner.nextLine().trim();
            
            loggedInUser = employeeRepo.findById(id);
            if (loggedInUser == null) {
                System.out.println(I18nHelper.getMessage("error.employee_not_found"));
            }
        }

        // 4. Mostrar Menú según el Rol del Empleado Logueado
        System.out.println("\n----------------------------------------");
        System.out.println(I18nHelper.getMessage("login.success") + " " + loggedInUser.getName());
        System.out.println("Role / Rol: " + loggedInUser.getRole());
        System.out.println("----------------------------------------");

        displayMenuForRole(loggedInUser, scanner);
        
        // Cerrar conexiones al salir
        MongoDBConnection.getInstance().close();
        System.out.println("\nGoodbye! / ¡Adiós!");
    }

    /**
     * Seeds default employees into the MongoDB collection if it is currently empty.
     */
    private static void seedDatabaseIfEmpty(MongoEmployeeRepository repo) {
        try {
            if (repo.findAll().isEmpty()) {
                System.out.println("Sembrando base de datos con empleados por defecto...");
                repo.save(new Employee("1", "Anthony Aimacaña", EmployeeRole.MANAGER));
                repo.save(new Employee("2", "Mateo Artieda", EmployeeRole.CASHIER));
                repo.save(new Employee("3", "Kevin Albán", EmployeeRole.BARISTA));
                repo.save(new Employee("4", "Waiter User", EmployeeRole.WAITER));
                System.out.println("Sembrado completado.");
            }
        } catch (Exception e) {
            System.err.println("No se pudo sembrar la base de datos de manera automática: " + e.getMessage());
            System.out.println("Asegúrate de que la dirección de MongoDB en MongoDBConnection.java sea accesible.");
        }
    }

    /**
     * Renders a skeleton menu based on the user's role.
     */
    private static void displayMenuForRole(Employee employee, Scanner scanner) {
        boolean exit = false;
        while (!exit) {
            System.out.println("\n=== " + I18nHelper.getMessage("app.title") + " ===");
            
            switch (employee.getRole()) {
                case MANAGER:
                    System.out.println("1. " + I18nHelper.getMessage("menu.inventory") + " [CRUD]");
                    System.out.println("2. " + I18nHelper.getMessage("menu.backoffice") + " [Z-Report]");
                    System.out.println("3. " + I18nHelper.getMessage("menu.pos") + " [POS Menu]");
                    System.out.println("4. Exit / Salir");
                    break;
                case CASHIER:
                    System.out.println("1. " + I18nHelper.getMessage("menu.pos") + " [Create Order & Checkout]");
                    System.out.println("2. " + I18nHelper.getMessage("menu.shift") + " [Open/Close Shift]");
                    System.out.println("3. Exit / Salir");
                    break;
                case BARISTA:
                    System.out.println("1. " + I18nHelper.getMessage("menu.kds") + " [View Pending Orders]");
                    System.out.println("2. Exit / Salir");
                    break;
                case WAITER:
                    System.out.println("1. " + I18nHelper.getMessage("menu.tables") + " [Assign Waiter / Reserve Table]");
                    System.out.println("2. Exit / Salir");
                    break;
            }

            System.out.print(I18nHelper.getMessage("menu.choose"));
            String input = scanner.nextLine().trim();
            
            // Simulación básica de salida. El resto del equipo implementará las opciones.
            if (input.equals("4") && employee.getRole() == EmployeeRole.MANAGER) {
                exit = true;
            } else if (input.equals("3") && employee.getRole() == EmployeeRole.CASHIER) {
                exit = true;
            } else if (input.equals("2") && (employee.getRole() == EmployeeRole.BARISTA || employee.getRole() == EmployeeRole.WAITER)) {
                exit = true;
            } else {
                System.out.println("\n[SKELETON] Has seleccionado: " + input + ". Lógica pendiente de implementación por tu grupo.");
            }
        }
    }
}
