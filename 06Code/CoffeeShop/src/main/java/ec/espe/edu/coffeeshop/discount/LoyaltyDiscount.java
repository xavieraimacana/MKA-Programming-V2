package ec.espe.edu.coffeeshop.discount;

import ec.espe.edu.coffeeshop.model.Order;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Implementation of a discount based on customer loyalty points.
 * 
 * @author Mateo Artieda, MKA Programmer, @ESPE
 */
public class LoyaltyDiscount implements Discount {
    private static final BigDecimal POINTS_TO_CURRENCY_RATIO = new BigDecimal("100"); // 100 points = $1.00

    @Override
    public BigDecimal calculateDiscount(Order order) {
        if (order.getClient() == null || order.getSubtotal() == null) {
            return BigDecimal.ZERO;
        }

        int points = order.getClient().getLoyaltyPoints();
        BigDecimal discountAmount = new BigDecimal(points)
                .divide(POINTS_TO_CURRENCY_RATIO, 2, RoundingMode.FLOOR);

        // Ensure discount doesn't exceed subtotal
        return discountAmount.min(order.getSubtotal());
    }
}
