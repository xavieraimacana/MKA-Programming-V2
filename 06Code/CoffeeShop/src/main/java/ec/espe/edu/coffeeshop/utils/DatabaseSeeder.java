package ec.espe.edu.coffeeshop.utils;

import ec.espe.edu.coffeeshop.model.*;
import ec.espe.edu.coffeeshop.payment.CashPayment;
import ec.espe.edu.coffeeshop.repository.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Utility to seed all 11 classes/collections in MongoDB if they are empty.
 * Ensures the database is fully initialized with valid demo data.
 * 
 * @author MKA Programmers, ESPE
 */
public class DatabaseSeeder {

    public static void seed() {
        try {
            System.out.println("=== STARTING DATABASE SEEDING PROCESS ===");

            // 1. Seed Employees
            MongoEmployeeRepository employeeRepo = new MongoEmployeeRepository();
            if (employeeRepo.findAll().isEmpty()) {
                System.out.println("Seeding Employees...");
                employeeRepo.save(new Employee("1", "Anthony Aimacaña", EmployeeRole.MANAGER, "anthony", PasswordHasher.hashPassword("mka123"), true));
                employeeRepo.save(new Employee("2", "Mateo Artieda", EmployeeRole.CASHIER, "mateo", PasswordHasher.hashPassword("mateo123"), true));
                employeeRepo.save(new Employee("3", "Kevin Albán", EmployeeRole.BARISTA, "kevin", PasswordHasher.hashPassword("kevin123"), true));
                employeeRepo.save(new Employee("4", "Waiter User", EmployeeRole.WAITER, "waiter", PasswordHasher.hashPassword("waiter123"), true));
            }

            // Migración automática de contraseñas de empleados si hay alguna en texto plano
            for (Employee emp : employeeRepo.findAll()) {
                if (emp.getPassword() != null && !emp.getPassword().startsWith("$2a$")) {
                    emp.setPassword(PasswordHasher.hashPassword(emp.getPassword()));
                    employeeRepo.save(emp);
                }
            }
            System.out.println("-> Employees: OK.");

            // 2. Seed Ingredients
            MongoIngredientRepository ingRepo = new MongoIngredientRepository();
            if (ingRepo.findAll().isEmpty()) {
                System.out.println("Seeding Ingredients...");
                ingRepo.save(new Ingredient("1", "Coffee Beans", 10.0, 2.0));
                ingRepo.save(new Ingredient("2", "Whole Milk", 20.0, 5.0));
                ingRepo.save(new Ingredient("3", "Refined Sugar", 5.0, 1.0));
                ingRepo.save(new Ingredient("4", "Filtered Water", 100.0, 10.0));
                ingRepo.save(new Ingredient("5", "Chocolate Powder", 3.0, 0.5));
            }
            System.out.println("-> Ingredients: OK.");

            // 3. Seed Equipments
            MongoEquipmentRepository eqRepo = new MongoEquipmentRepository();
            if (eqRepo.findAll().isEmpty()) {
                System.out.println("Seeding Equipments...");
                eqRepo.save(new Equipment("1", "Espresso Machine", EquipmentStatus.OPERATIONAL, new Date()));
                eqRepo.save(new Equipment("2", "Coffee Grinder", EquipmentStatus.OPERATIONAL, new Date()));
                eqRepo.save(new Equipment("3", "Milk Frother", EquipmentStatus.OPERATIONAL, new Date()));
            }
            System.out.println("-> Equipments: OK.");

            // 4. Seed Products (with Recipes)
            MongoProductRepository prodRepo = new MongoProductRepository();
            if (prodRepo.findAll().isEmpty()) {
                System.out.println("Seeding Products & Recipes...");
                Ingredient coffeeBeans = ingRepo.findById("1");
                Ingredient milk = ingRepo.findById("2");
                Ingredient sugar = ingRepo.findById("3");
                Ingredient water = ingRepo.findById("4");
                Ingredient chocolate = ingRepo.findById("5");

                // Espresso
                Recipe rEsp = new Recipe();
                rEsp.setId("rec_esp");
                rEsp.getItems().add(new RecipeItem(coffeeBeans, 0.02));
                rEsp.getItems().add(new RecipeItem(water, 0.05));
                Product esp = new Product("1", "Espresso", new BigDecimal("1.50"), ProductCategory.BEVERAGE, rEsp) {};
                prodRepo.save(esp);

                // Americano
                Recipe rAm = new Recipe();
                rAm.setId("rec_am");
                rAm.getItems().add(new RecipeItem(coffeeBeans, 0.02));
                rAm.getItems().add(new RecipeItem(water, 0.15));
                Product am = new Product("2", "Americano", new BigDecimal("2.00"), ProductCategory.BEVERAGE, rAm) {};
                prodRepo.save(am);

                // Cappuccino
                Recipe rCap = new Recipe();
                rCap.setId("rec_cap");
                rCap.getItems().add(new RecipeItem(coffeeBeans, 0.02));
                rCap.getItems().add(new RecipeItem(milk, 0.10));
                rCap.getItems().add(new RecipeItem(water, 0.05));
                rCap.getItems().add(new RecipeItem(sugar, 0.01));
                Product cap = new Product("3", "Cappuccino", new BigDecimal("2.75"), ProductCategory.BEVERAGE, rCap) {};
                prodRepo.save(cap);

                // Chocolate Caliente
                Recipe rChoc = new Recipe();
                rChoc.setId("rec_choc");
                rChoc.getItems().add(new RecipeItem(chocolate, 0.02));
                rChoc.getItems().add(new RecipeItem(milk, 0.20));
                Product choc = new Product("4", "Hot Chocolate", new BigDecimal("2.50"), ProductCategory.BEVERAGE, rChoc) {};
                prodRepo.save(choc);
            }
            System.out.println("-> Products: OK.");

            // 5. Seed Customers (CRM)
            MongoCustomerRepository custRepo = new MongoCustomerRepository();
            if (custRepo.findAll().isEmpty()) {
                System.out.println("Seeding Customers...");
                custRepo.save(new Customer("1", "Consumidor Final", "9999999999", "consumidor@final.com"));
                custRepo.save(new Customer("2", "Anthony Aimacaña", "1712345678", "anthony@espe.edu.ec"));
                custRepo.save(new Customer("3", "Mateo Artieda", "1787654321", "mateo@espe.edu.ec"));
            }
            System.out.println("-> Customers: OK.");

            // 6. Seed Tables
            MongoTableRepository tableRepo = new MongoTableRepository();
            if (tableRepo.findAll().isEmpty()) {
                System.out.println("Seeding Tables...");
                tableRepo.save(new Table("1", 1, 2, TableStatus.FREE));
                tableRepo.save(new Table("2", 2, 4, TableStatus.FREE));
                tableRepo.save(new Table("3", 3, 4, TableStatus.FREE));
                tableRepo.save(new Table("4", 4, 6, TableStatus.FREE));
                tableRepo.save(new Table("5", 5, 2, TableStatus.FREE));
            }
            System.out.println("-> Tables: OK.");

            // 7. Seed Orders
            MongoOrderRepository orderRepo = new MongoOrderRepository();
            if (orderRepo.findAll().isEmpty()) {
                System.out.println("Seeding Orders...");
                Order dummyOrder = new Order("dummy_order_id", LocalDateTime.now(), OrderStatus.READY);
                dummyOrder.setClient(custRepo.findById("1"));
                
                Product cappuccino = prodRepo.findById("3");
                OrderItem dummyItem = new OrderItem(cappuccino, 1, new BigDecimal("2.75"));
                dummyOrder.getItems().add(dummyItem);
                
                dummyOrder.setSubtotal(new BigDecimal("2.46"));
                dummyOrder.setTax(new BigDecimal("0.29"));
                dummyOrder.setDiscount(BigDecimal.ZERO);
                dummyOrder.setTotal(new BigDecimal("2.75"));
                
                CashPayment cashPay = new CashPayment(new BigDecimal("2.75"), new BigDecimal("5.00"));
                dummyOrder.setPayment(cashPay);
                
                orderRepo.save(dummyOrder);
            }
            System.out.println("-> Orders: OK.");

            // 8. Seed Invoices
            MongoInvoiceRepository invoiceRepo = new MongoInvoiceRepository();
            if (invoiceRepo.findAll().isEmpty()) {
                System.out.println("Seeding Invoices...");
                Order order = orderRepo.findById("dummy_order_id");
                if (order != null) {
                    Invoice dummyInvoice = new Invoice("dummy_invoice_id", "INV-2026-00001", order);
                    invoiceRepo.save(dummyInvoice);
                }
            }
            System.out.println("-> Invoices: OK.");

            // 9. Seed Reservations
            MongoReservationRepository reservationRepo = new MongoReservationRepository();
            if (reservationRepo.findAll().isEmpty()) {
                System.out.println("Seeding Reservations...");
                Reservation dummyReservation = new Reservation("dummy_res_id", "1", "Anthony Aimacaña", LocalDateTime.now().plusHours(2), 2);
                reservationRepo.save(dummyReservation);
            }
            System.out.println("-> Reservations: OK.");

            // 10. Seed Shifts
            MongoShiftRepository shiftRepo = new MongoShiftRepository();
            if (shiftRepo.findAll().isEmpty()) {
                System.out.println("Seeding Shifts...");
                Shift dummyShift = new Shift("dummy_shift_id", "Mateo Artieda", new BigDecimal("50.00"));
                dummyShift.setEndTime(LocalDateTime.now());
                dummyShift.setDeclaredEndingCash(new BigDecimal("52.75"));
                dummyShift.setSystemEndingCash(new BigDecimal("52.75"));
                dummyShift.setDifference(BigDecimal.ZERO);
                dummyShift.setReconciliationStatus(ReconciliationStatus.BALANCED);
                shiftRepo.save(dummyShift);
            }
            System.out.println("-> Shifts: OK.");

            // 11. Seed Purchase Orders
            MongoPurchaseOrderRepository poRepo = new MongoPurchaseOrderRepository();
            if (poRepo.findAll().isEmpty()) {
                System.out.println("Seeding Purchase Orders...");
                PurchaseOrder dummyPO = new PurchaseOrder("dummy_po_id", "Coffee Supplier S.A.", new Date(), new BigDecimal("45.00"), PurchaseOrderStatus.DELIVERED);
                
                Ingredient coffeeBeans = ingRepo.findById("1");
                if (coffeeBeans != null) {
                    PurchaseOrderItem poItem = new PurchaseOrderItem(coffeeBeans, 5.0);
                    dummyPO.getItemsToBuy().add(poItem);
                }
                poRepo.save(dummyPO);
            }
            System.out.println("-> Purchase Orders: OK.");

            System.out.println("=== DATABASE SEEDING COMPLETED SUCCESSFULLY ===");
        } catch (Exception e) {
            System.err.println("Database seeding failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
