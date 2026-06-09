package ec.espe.edu.coffeeshop.discount;

import java.math.BigDecimal;

/**
 * Strategy interface for applying dynamic discount rates.
 * 
 * @author Anthony Aimacaña, MKA Programer, @ESPE
 */
public interface Discount {
    /**
     * Calculates the discounted total based on the strategy.
     * 
     * @param amount The original total amount.
     * @return The discounted total amount.
     */
    BigDecimal applyDiscount(BigDecimal amount);
}
