package ec.espe.edu.coffeeshop.discount;

import ec.espe.edu.coffeeshop.model.Order;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Implementation of a percentage-based discount.
 * 
 * @author Mateo Artieda, MKA Programmer, @ESPE
 */
public class PercentageDiscount implements Discount {
    private BigDecimal percentage;

    public PercentageDiscount(BigDecimal percentage) {
        this.percentage = percentage;
    }

    @Override
    public BigDecimal calculateDiscount(Order order) {
        if (order.getSubtotal() == null) return BigDecimal.ZERO;
        
        // discount = subtotal * (percentage / 100)
        return order.getSubtotal()
                .multiply(percentage)
                .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
    }

    public BigDecimal getPercentage() {
        return percentage;
    }

    public void setPercentage(BigDecimal percentage) {
        this.percentage = percentage;
    }
}
