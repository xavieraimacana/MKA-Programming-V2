package ec.espe.edu.coffeeshop.controller;
import ec.espe.edu.coffeeshop.model.InventoryTransaction;
import java.util.ArrayList;
import java.util.List;
public class InventoryTransactionController {
    private List<InventoryTransaction> transactions;
    public InventoryTransactionController() {
        this.transactions = new ArrayList<>();
    }
    public void addTransaction(InventoryTransaction transaction) {
        transactions.add(transaction);
        transaction.executeTransaction();
    }
    public void removeTransaction(String transactionId) {
        InventoryTransaction transaction = getTransaction(transactionId);
        if (transaction != null) {
            transaction.revertTransaction();
            transactions.remove(transaction);
        }
    }
    public InventoryTransaction getTransaction(String transactionId) {
        return transactions.stream()
                .filter(t -> t.getTransactionId().equals(transactionId))
                .findFirst()
                .orElse(null);
    }
    public List<InventoryTransaction> getAllTransactions() {
        return new ArrayList<>(transactions);
    }
}
