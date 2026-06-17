package ec.espe.edu.coffeeshop.utils;
import ec.espe.edu.coffeeshop.model.Order;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.UUID;
public class BusinessLogic {
    public static final double TAX_RATE = 0.15; 
    public static double computeSubtotal(List<Double> itemPrices) {
        return itemPrices.stream().mapToDouble(Double::doubleValue).sum();
    }
    public static double computeTax(double subtotal) {
        return subtotal * TAX_RATE;
    }
    public static double computeTotalWithDiscount(double subtotal, double discountPercentage) {
        double discountAmount = subtotal * (discountPercentage / 100.0);
        double total = subtotal - discountAmount;
        return total + computeTax(total);
    }
    public static double calculateChange(double totalDue, double amountPaid) {
        if (amountPaid < totalDue) {
            throw new IllegalArgumentException("Amount paid is less than total due.");
        }
        return amountPaid - totalDue;
    }
    public static boolean isStockLow(int currentStock, int minimumThreshold) {
        return currentStock <= minimumThreshold;
    }
    public static double calculateHoursWorked(String timeIn, String timeOut) {
        try {
            LocalTime in = LocalTime.parse(timeIn);
            LocalTime out = LocalTime.parse(timeOut);
            Duration duration = Duration.between(in, out);
            return duration.toMinutes() / 60.0;
        } catch (DateTimeParseException e) {
            return 0.0;
        }
    }
    public static boolean validatePasswordStrength(String password) {
        return password != null && password.length() >= 8 && password.matches(".*\\d.*");
    }
    public static String generateInvoiceNumber() {
        return "INV-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
    public static double calculateDailyRevenue(List<Order> todayOrders) {
        return todayOrders.stream()
                .filter(o -> ec.espe.edu.coffeeshop.model.OrderStatus.COMPLETED.equals(o.getStatus()))
                .mapToDouble(o -> o.getItems().stream().mapToDouble(ec.espe.edu.coffeeshop.model.OrderItem::getSubtotal).sum())
                .sum();
    }
    public static int calculateLoyaltyPoints(double orderTotal) {
        return (int) (orderTotal / 10.0);
    }
}
