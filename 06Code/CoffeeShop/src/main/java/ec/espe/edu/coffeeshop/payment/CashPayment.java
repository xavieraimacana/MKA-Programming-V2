package ec.espe.edu.coffeeshop.payment;

import java.math.BigDecimal;

/**
 * Cash payment method.
 * 
 * @author Anthony Aimacaña, MKA Programer, @ESPE
 */
public class CashPayment implements Payment {
    private BigDecimal amount;
    private BigDecimal amountTendered;

    public CashPayment() {}

    public CashPayment(BigDecimal amount, BigDecimal amountTendered) {
        this.amount = amount;
        this.amountTendered = amountTendered;
    }

    @Override
    public void accept(PaymentVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public BigDecimal getAmount() {
        return amount;
    }

    public BigDecimal getAmountTendered() {
        return amountTendered;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setAmountTendered(BigDecimal amountTendered) {
        this.amountTendered = amountTendered;
    }
}
