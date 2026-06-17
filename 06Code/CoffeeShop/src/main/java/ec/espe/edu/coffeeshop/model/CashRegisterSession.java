package ec.espe.edu.coffeeshop.model;
import java.time.LocalDateTime;
public class CashRegisterSession {
    private String sessionId;
    private LocalDateTime openingTime;
    private LocalDateTime closingTime;
    private double startingFloat;
    private double expectedSystemCash;
    private double declaredPhysicalCash;
    private double discrepancyAmount;
    public CashRegisterSession() {}
    public CashRegisterSession(String sessionId, LocalDateTime openingTime, LocalDateTime closingTime, double startingFloat, double expectedSystemCash, double declaredPhysicalCash, double discrepancyAmount) {
        this.sessionId = sessionId;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
        this.startingFloat = startingFloat;
        this.expectedSystemCash = expectedSystemCash;
        this.declaredPhysicalCash = declaredPhysicalCash;
        this.discrepancyAmount = discrepancyAmount;
    }
    public void openRegister(double amount) {
        this.openingTime = LocalDateTime.now();
        this.startingFloat = amount;
    }
    public void closeRegister(double declaredAmount) {
        this.closingTime = LocalDateTime.now();
        this.declaredPhysicalCash = declaredAmount;
        this.discrepancyAmount = calculateDiscrepancy();
    }
    public double calculateDiscrepancy() {
        return this.declaredPhysicalCash - this.expectedSystemCash;
    }
    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }
    public String getId() { return sessionId; }
    public LocalDateTime getOpeningTime() { return openingTime; }
    public void setOpeningTime(LocalDateTime openingTime) { this.openingTime = openingTime; }
    public LocalDateTime getClosingTime() { return closingTime; }
    public void setClosingTime(LocalDateTime closingTime) { this.closingTime = closingTime; }
    public double getStartingFloat() { return startingFloat; }
    public void setStartingFloat(double startingFloat) { this.startingFloat = startingFloat; }
    public double getExpectedSystemCash() { return expectedSystemCash; }
    public void setExpectedSystemCash(double expectedSystemCash) { this.expectedSystemCash = expectedSystemCash; }
    public double getDeclaredPhysicalCash() { return declaredPhysicalCash; }
    public void setDeclaredPhysicalCash(double declaredPhysicalCash) { this.declaredPhysicalCash = declaredPhysicalCash; }
    public double getDiscrepancyAmount() { return discrepancyAmount; }
    public void setDiscrepancyAmount(double discrepancyAmount) { this.discrepancyAmount = discrepancyAmount; }
}
