package ec.espe.edu.coffeeshop.payment;

import java.math.BigDecimal;

/**
 * Implementation of the PaymentVisitor that processes different payment types.
 * Calculates change for cash and simulates network authorization for others.
 * 
 * @author Mateo Artieda, MKA Programmer, @ESPE
 */
public class PaymentProcessor implements PaymentVisitor {
    private BigDecimal change;
    private boolean authorized;
    private String statusMessage;

    public PaymentProcessor() {
        this.change = BigDecimal.ZERO;
        this.authorized = false;
        this.statusMessage = "";
    }

    @Override
    public void visit(CashPayment payment) {
        BigDecimal amount = payment.getAmount();
        BigDecimal tendered = payment.getAmountTendered();

        if (tendered.compareTo(amount) >= 0) {
            this.change = tendered.subtract(amount);
            this.authorized = true;
            this.statusMessage = "Cash payment processed. Change: $" + this.change;
        } else {
            this.change = BigDecimal.ZERO;
            this.authorized = false;
            this.statusMessage = "Insufficient cash tendered.";
        }
    }

    @Override
    public void visit(CardPayment payment) {
        // Simulation of network authorization
        if (payment.getPaymentToken() != null && !payment.getPaymentToken().isEmpty()) {
            this.authorized = true;
            this.statusMessage = "Card authorized successfully. Token: " + payment.getPaymentToken();
        } else {
            this.authorized = false;
            this.statusMessage = "Card authorization failed: Invalid token.";
        }
    }

    @Override
    public void visit(BankTransferPayment payment) {
        // Simulation of bank verification
        if (payment.getTransactionId() != null && !payment.getTransactionId().isEmpty()) {
            this.authorized = true;
            this.statusMessage = "Bank transfer verified. Trans ID: " + payment.getTransactionId();
        } else {
            this.authorized = false;
            this.statusMessage = "Bank transfer verification failed: Missing Transaction ID.";
        }
    }

    public BigDecimal getChange() {
        return change;
    }

    public boolean isAuthorized() {
        return authorized;
    }

    public String getStatusMessage() {
        return statusMessage;
    }
}
