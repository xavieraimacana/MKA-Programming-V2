package ec.espe.edu.coffeeshop.repository;

import ec.espe.edu.coffeeshop.model.Invoice;

/**
 * Repository interface for Invoice entities.
 * 
 * @author Mateo Artieda, MKA Programmer, @ESPE
 */
public interface InvoiceRepository extends Repository<Invoice> {
    /**
     * Gets the next sequential invoice number.
     */
    String getNextInvoiceNumber();
}
