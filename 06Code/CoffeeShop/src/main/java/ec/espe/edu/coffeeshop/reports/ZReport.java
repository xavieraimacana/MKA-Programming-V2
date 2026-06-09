package ec.espe.edu.coffeeshop.reports;

import ec.espe.edu.coffeeshop.model.Invoice;
import ec.espe.edu.coffeeshop.repository.InvoiceRepository;
import ec.espe.edu.coffeeshop.repository.MongoInvoiceRepository;
import java.math.BigDecimal;
import java.util.List;

/**
 * Total financial report of sales (Z-Report).
 * 
 * @author Kevin Albán, MKA Programmer, @ESPE
 */
public class ZReport implements Report {
    private final InvoiceRepository invoiceRepository;

    public ZReport() {
        this.invoiceRepository = new MongoInvoiceRepository();
    }

    @Override
    public void generateReport() {
        List<Invoice> invoices = invoiceRepository.findAll();
        BigDecimal grandTotal = BigDecimal.ZERO;
        BigDecimal taxTotal = BigDecimal.ZERO;

        System.out.println("---------- Z-REPORT (TOTAL SALES) ----------");
        for (Invoice inv : invoices) {
            grandTotal = grandTotal.add(inv.getTotal());
            taxTotal = taxTotal.add(inv.getTax());
        }

        System.out.println("Total Invoices: " + invoices.size());
        System.out.println("Tax Total:      $" + taxTotal);
        System.out.println("GRAND TOTAL:    $" + grandTotal);
        System.out.println("--------------------------------------------");
    }
}
