package ec.espe.edu.coffeeshop.discount;

import ec.espe.edu.coffeeshop.model.Order;
import java.math.BigDecimal;

/**
 * Implementation of a fixed value discount.
 * 
 * @author Mateo Artieda, MKA Programmer, @ESPE
 */
public class FixedDiscount implements Discount {
    private BigDecimal fixedAmount;

    public FixedDiscount(BigDecimal fixedAmount) {
        this.fixedAmount = fixedAmount;
    }

    @Override
    public BigDecimal calculateDiscount(Order order) {
        // Return fixed amount, ensuring it doesn't exceed the subtotal
        if (order.getSubtotal() == null) return BigDecimal.ZERO;
        return fixedAmount.min(order.getSubtotal());
    }

    public BigDecimal getFixedAmount() {
        return fixedAmount;
    }

    public void setFixedAmount(BigDecimal fixedAmount) {
        this.fixedAmount = fixedAmount;
    }
}
