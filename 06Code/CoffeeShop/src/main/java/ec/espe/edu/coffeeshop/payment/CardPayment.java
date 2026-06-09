package ec.espe.edu.coffeeshop.payment;

import java.math.BigDecimal;

/**
 * Credit/Debit Card payment method.
 * 
 * @author Anthony Aimacaña, MKA Programer, @ESPE
 */
public class CardPayment implements Payment {
    private BigDecimal amount;
    private String paymentToken;

    public CardPayment() {}

    public CardPayment(BigDecimal amount, String paymentToken) {
        this.amount = amount;
        this.paymentToken = paymentToken;
    }

    @Override
    public void accept(PaymentVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public BigDecimal getAmount() {
        return amount;
    }

    public String getPaymentToken() {
        return paymentToken;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setPaymentToken(String paymentToken) {
        this.paymentToken = paymentToken;
    }
}
