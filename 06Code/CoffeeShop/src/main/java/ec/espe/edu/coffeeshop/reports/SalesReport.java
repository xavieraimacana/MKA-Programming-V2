package ec.espe.edu.coffeeshop.reports;

import ec.espe.edu.coffeeshop.model.Invoice;
import ec.espe.edu.coffeeshop.model.OrderItem;
import ec.espe.edu.coffeeshop.repository.InvoiceRepository;
import ec.espe.edu.coffeeshop.repository.MongoInvoiceRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Report of best-selling products.
 * 
 * @author Kevin Albán, MKA Programmer, @ESPE
 */
public class SalesReport implements Report {
    private final InvoiceRepository invoiceRepository;

    public SalesReport() {
        this.invoiceRepository = new MongoInvoiceRepository();
    }

    @Override
    public void generateReport() {
        System.out.println(getReportText());
    }

    @Override
    public String getReportText() {
        List<Invoice> invoices = invoiceRepository.findAll();
        Map<String, Integer> productSales = new HashMap<>();

        for (Invoice inv : invoices) {
            if (inv.getOrder() != null && inv.getOrder().getItems() != null) {
                for (OrderItem item : inv.getOrder().getItems()) {
                    String productName = item.getProduct().getName();
                    productSales.put(productName, productSales.getOrDefault(productName, 0) + item.getQuantity());
                }
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append("---------- BEST-SELLING PRODUCTS ----------\n");
        sb.append(String.format("%-30s | %-10s%n", "Product", "Quantity Sold"));
        sb.append("---------------------------------------------\n");

        productSales.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .forEach(entry -> sb.append(String.format("%-30s | %-10d%n", entry.getKey(), entry.getValue())));

        sb.append("---------------------------------------------\n");
        return sb.toString();
    }
}
