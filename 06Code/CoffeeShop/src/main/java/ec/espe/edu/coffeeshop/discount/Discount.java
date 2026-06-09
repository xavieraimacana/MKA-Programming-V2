package ec.espe.edu.coffeeshop.discount;

import ec.espe.edu.coffeeshop.model.Order;
import java.math.BigDecimal;

/**
 * Strategy interface for calculating dynamic discounts on an order.
 * 
 * @author Mateo Artieda, MKA Programmer, @ESPE
 */
public interface Discount {
    /**
     * Calculates the discount amount for a given order.
     * 
     * @param order The order to apply the discount to.
     * @return The calculated discount amount.
     */
    BigDecimal calculateDiscount(Order order);
}
