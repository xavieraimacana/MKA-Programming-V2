package ec.espe.edu.coffeeshop.utils;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import java.util.Arrays;
import java.time.LocalDateTime;
public class DatabaseSeeder {
    public static void seed() {
        MongoDatabase db = MongoDBConnection.getDatabase();
        System.out.println("Starting Database Seeding...");
        MongoCollection<Document> employees = db.getCollection("Employees");
        if (employees.countDocuments() == 0) {
            employees.insertMany(Arrays.asList(
                new Document("_id", "EMP-001")
                        .append("employeeId", "EMP-001")
                        .append("name", "System Admin")
                        .append("username", "admin") 
                        .append("password", "admin123") 
                        .append("role", "MANAGER"),
                new Document("_id", "EMP-002")
                        .append("employeeId", "EMP-002")
                        .append("name", "John Barista")
                        .append("username", "john")
                        .append("password", "1234")
                        .append("role", "BARISTA")
            ));
            System.out.println("-> Seeded Employees collection.");
        }
        MongoCollection<Document> customers = db.getCollection("Customers");
        if (customers.countDocuments() == 0) {
            customers.insertMany(Arrays.asList(
                new Document("_id", "CUST-001")
                        .append("customerId", "CUST-001")
                        .append("name", "Alice Smith")
                        .append("taxId", "1720000001")
                        .append("loyaltyPoints", 150)
            ));
            System.out.println("-> Seeded Customers collection.");
        }
        MongoCollection<Document> suppliers = db.getCollection("Suppliers");
        if (suppliers.countDocuments() == 0) {
            suppliers.insertMany(Arrays.asList(
                new Document("_id", "SUP-001")
                        .append("supplierId", "SUP-001")
                        .append("companyName", "Global Coffee Beans Ltd.")
            ));
            System.out.println("-> Seeded Suppliers collection.");
        }
        MongoCollection<Document> cashSessions = db.getCollection("CashRegisterSessions");
        if (cashSessions.countDocuments() == 0) {
            cashSessions.insertOne(
                new Document("_id", "CRS-001")
                        .append("sessionId", "CRS-001")
                        .append("openingTime", LocalDateTime.now().minusHours(4).toString())
                        .append("closingTime", "")
                        .append("startingFloat", 150.00)
                        .append("expectedSystemCash", 150.00)
                        .append("declaredPhysicalCash", 0.0)
                        .append("discrepancyAmount", 0.0)
            );
            System.out.println("-> Seeded CashRegisterSessions collection.");
        }
        MongoCollection<Document> products = db.getCollection("Products");
        if (products.countDocuments() == 0) {
            products.insertMany(Arrays.asList(
                new Document("_id", "PROD-001")
                        .append("productId", "PROD-001")
                        .append("name", "Caramel Macchiato")
                        .append("basePrice", 4.50)
                        .append("available", true)
            ));
            System.out.println("-> Seeded Products collection.");
        }
        MongoCollection<Document> prodMods = db.getCollection("ProductModifiers");
        if (prodMods.countDocuments() == 0) {
            prodMods.insertOne(
                new Document("_id", "PM-001")
                        .append("modifierId", "PM-001")
                        .append("name", "Extra Vanilla Syrup")
                        .append("additionalPrice", 0.50)
            );
            System.out.println("-> Seeded ProductModifiers collection.");
        }
        MongoCollection<Document> recipes = db.getCollection("Recipes");
        if (recipes.countDocuments() == 0) {
            recipes.insertOne(
                new Document("_id", "REC-001")
                        .append("recipeId", "REC-001")
                        .append("instructions", "1. Brew espresso. 2. Steam milk. 3. Add syrup.")
            );
            System.out.println("-> Seeded Recipes collection.");
        }
        MongoCollection<Document> recipeIng = db.getCollection("RecipeIngredients");
        if (recipeIng.countDocuments() == 0) {
            recipeIng.insertOne(
                new Document("_id", "RI-001")
                        .append("requiredQuantity", 200.0)
                        .append("unit", "MILLILITERS")
            );
            System.out.println("-> Seeded RecipeIngredients collection.");
        }
        MongoCollection<Document> inventory = db.getCollection("InventoryItems");
        if (inventory.countDocuments() == 0) {
            inventory.insertOne(
                new Document("_id", "INV-001")
                        .append("itemId", "INV-001")
                        .append("name", "Whole Milk")
                        .append("baseUnit", "LITERS")
                        .append("currentStock", 20.0)
                        .append("minThreshold", 5.0)
            );
            System.out.println("-> Seeded InventoryItems collection.");
        }
        MongoCollection<Document> invTrans = db.getCollection("InventoryTransactions");
        if (invTrans.countDocuments() == 0) {
            invTrans.insertOne(
                new Document("_id", "IT-001")
                        .append("transactionId", "IT-001")
                        .append("date", LocalDateTime.now().toString())
                        .append("type", "STOCK_IN")
                        .append("quantityChange", 10.0)
                        .append("referenceDocumentId", "SE-001")
            );
            System.out.println("-> Seeded InventoryTransactions collection.");
        }
        MongoCollection<Document> stockEntries = db.getCollection("StockEntries");
        if (stockEntries.countDocuments() == 0) {
            stockEntries.insertOne(
                new Document("_id", "SE-001")
                        .append("entryId", "SE-001")
                        .append("dateReceived", LocalDateTime.now().toString())
                        .append("quantityReceived", 50.0)
                        .append("unitReceived", "LITERS")
                        .append("totalCost", 100.0)
            );
            System.out.println("-> Seeded StockEntries collection.");
        }
        MongoCollection<Document> orders = db.getCollection("Orders");
        if (orders.countDocuments() == 0) {
            orders.insertOne(
                new Document("_id", "ORD-1001")
                        .append("orderId", "ORD-1001")
                        .append("orderDate", LocalDateTime.now().toString())
                        .append("status", "COMPLETED")
                        .append("preparationNotes", "To Go")
            );
            System.out.println("-> Seeded Orders collection.");
        }
        MongoCollection<Document> orderItems = db.getCollection("OrderItems");
        if (orderItems.countDocuments() == 0) {
            orderItems.insertOne(
                new Document("_id", "OI-001")
                        .append("quantity", 2)
                        .append("subtotal", 9.00)
            );
            System.out.println("-> Seeded OrderItems collection.");
        }
        MongoCollection<Document> orderItemMods = db.getCollection("OrderItemModifiers");
        if (orderItemMods.countDocuments() == 0) {
            orderItemMods.insertOne(
                new Document("_id", "OIM-001")
                        .append("priceAtTimeOfOrder", 0.50)
            );
            System.out.println("-> Seeded OrderItemModifiers collection.");
        }
        MongoCollection<Document> invoices = db.getCollection("Invoices");
        if (invoices.countDocuments() == 0) {
            invoices.insertOne(
                new Document("_id", "INV-1001")
                        .append("invoiceId", "INV-1001")
                        .append("issueDate", LocalDateTime.now().toString())
                        .append("subtotal", 9.50)
                        .append("taxAmount", 1.42)
                        .append("totalAmount", 10.92)
            );
            System.out.println("-> Seeded Invoices collection.");
        }
        MongoCollection<Document> payments = db.getCollection("Payments");
        if (payments.countDocuments() == 0) {
            payments.insertOne(
                new Document("_id", "PAY-001")
                        .append("paymentId", "PAY-001")
                        .append("amount", 10.92)
                        .append("method", "CREDIT_CARD")
                        .append("isSuccessful", true)
            );
            System.out.println("-> Seeded Payments collection.");
        }
        System.out.println("Database Seeding Completed Successfully.");
    }
}
