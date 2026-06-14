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
        System.out.println(getReportText());
    }

    @Override
    public String getReportText() {
        List<Invoice> invoices = invoiceRepository.findAll();
        BigDecimal grandTotal = BigDecimal.ZERO;
        BigDecimal taxTotal = BigDecimal.ZERO;

        StringBuilder sb = new StringBuilder();
        sb.append("---------- Z-REPORT (TOTAL SALES) ----------\n");
        for (Invoice inv : invoices) {
            grandTotal = grandTotal.add(inv.getTotal());
            taxTotal = taxTotal.add(inv.getTax());
        }

        sb.append("Total Invoices: ").append(invoices.size()).append("\n");
        sb.append("Tax Total:      $").append(taxTotal).append("\n");
        sb.append("GRAND TOTAL:    $").append(grandTotal).append("\n");
        sb.append("--------------------------------------------\n");
        return sb.toString();
    }
}
