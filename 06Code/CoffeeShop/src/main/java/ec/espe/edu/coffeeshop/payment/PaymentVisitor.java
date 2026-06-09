package ec.espe.edu.coffeeshop.payment;

/**
 * Visitor interface for polymorphically resolving different payment methods.
 * 
 * @author Anthony Aimacaña, MKA Programer, @ESPE
 */
public interface PaymentVisitor {
    void visit(CashPayment payment);
    void visit(CardPayment payment);
    void visit(BankTransferPayment payment);
}
