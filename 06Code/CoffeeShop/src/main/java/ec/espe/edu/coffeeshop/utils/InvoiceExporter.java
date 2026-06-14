package ec.espe.edu.coffeeshop.utils;

import ec.espe.edu.coffeeshop.model.Invoice;
import ec.espe.edu.coffeeshop.model.OrderItem;
import ec.espe.edu.coffeeshop.model.Modifier;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

/**
 * Utility class to export invoices to plain text files.
 * 
 * @author Mateo Artieda, MKA Programmer, @ESPE
 */
public class InvoiceExporter {
    private static final String EXPORT_PATH = "07Other/Invoices/";

    public static void exportToText(Invoice invoice) throws IOException {
        File directory = new File(EXPORT_PATH);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String fileName = EXPORT_PATH + "invoice_" + invoice.getInvoiceNumber() + ".txt";
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            writer.println("==========================================");
            writer.println("           COFFEE SHOP MKA                ");
            writer.println("==========================================");
            writer.println("Invoice #: " + invoice.getInvoiceNumber());
            writer.println("Date:      " + invoice.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            writer.println("------------------------------------------");
            writer.println("CLIENT DATA:");
            writer.println("Name:      " + (invoice.getClientName() != null ? invoice.getClientName() : "Final Consumer"));
            writer.println("Tax ID:    " + (invoice.getClientTaxId() != null ? invoice.getClientTaxId() : "9999999999"));
            writer.println("------------------------------------------");
            writer.println(String.format("%-20s %3s %8s %8s", "Product", "Qty", "Price", "Total"));
            writer.println("------------------------------------------");

            if (invoice.getOrder() != null && invoice.getOrder().getItems() != null) {
                for (OrderItem item : invoice.getOrder().getItems()) {
                    String prodName = item.getProduct() != null ? item.getProduct().getName() : "Unknown Product";
                    if (prodName.length() > 20) prodName = prodName.substring(0, 17) + "...";
                    
                    BigDecimal itemTotal = item.getPricePaidSnapshot().multiply(new java.math.BigDecimal(item.getQuantity()));
                    
                    writer.println(String.format("%-20s %3d %8.2f %8.2f", 
                        prodName, item.getQuantity(), item.getPricePaidSnapshot(), itemTotal));
                    
                    for (Modifier mod : item.getModifiers()) {
                        writer.println(String.format("  + %-18s %8.2f", mod.getName(), mod.getExtraPrice()));
                    }
                }
            }

            writer.println("------------------------------------------");
            writer.println(String.format("%-33s %8.2f", "SUBTOTAL:", invoice.getSubtotal()));
            writer.println(String.format("%-33s %8.2f", "TAX (15%):", invoice.getTax()));
            writer.println(String.format("%-33s %8.2f", "TOTAL:", invoice.getTotal()));
            writer.println("==========================================");
            writer.println("        THANKS FOR YOUR PURCHASE!         ");
            writer.println("==========================================");
        }
    }
}
