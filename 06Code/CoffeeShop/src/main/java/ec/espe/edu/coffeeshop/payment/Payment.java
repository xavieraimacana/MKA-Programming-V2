package ec.espe.edu.coffeeshop.payment;

import java.math.BigDecimal;

/**
 * Base interface for all payment methods (Visitor pattern).
 * 
 * @author Anthony Aimacaña, MKA Programer, @ESPE
 */
public interface Payment {
    /**
     * Accepts a payment visitor to process the payment polimorphically.
     */
    void accept(PaymentVisitor visitor);

    /**
     * Gets the total payment amount.
     */
    BigDecimal getAmount();
}
