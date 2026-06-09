package ec.espe.edu.coffeeshop.payment;

import java.math.BigDecimal;

/**
 * Bank transfer payment method.
 * 
 * @author Anthony Aimacaña, MKA Programer, @ESPE
 */
public class BankTransferPayment implements Payment {
    private BigDecimal amount;
    private String bankName;
    private String transactionId;

    public BankTransferPayment() {}

    public BankTransferPayment(BigDecimal amount, String bankName, String transactionId) {
        this.amount = amount;
        this.bankName = bankName;
        this.transactionId = transactionId;
    }

    @Override
    public void accept(PaymentVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public BigDecimal getAmount() {
        return amount;
    }

    public String getBankName() {
        return bankName;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
}
