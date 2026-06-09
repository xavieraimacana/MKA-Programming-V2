package ec.espe.edu.coffeeshop.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Represents a cashier's shift for tracking and reconciliation purposes.
 * 
 * @author Mateo Artieda, MKA Programmer, @ESPE
 */
public class Shift {
    private String id;
    private String cashierName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private BigDecimal startingCash;
    private BigDecimal declaredEndingCash;
    private BigDecimal systemEndingCash;
    private BigDecimal difference;
    private ReconciliationStatus reconciliationStatus;

    public Shift() {
        this.startTime = LocalDateTime.now();
        this.startingCash = BigDecimal.ZERO;
        this.declaredEndingCash = BigDecimal.ZERO;
        this.systemEndingCash = BigDecimal.ZERO;
        this.difference = BigDecimal.ZERO;
    }

    public Shift(String id, String cashierName, BigDecimal startingCash) {
        this.id = id;
        this.cashierName = cashierName;
        this.startingCash = startingCash;
        this.startTime = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCashierName() {
        return cashierName;
    }

    public void setCashierName(String cashierName) {
        this.cashierName = cashierName;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public BigDecimal getStartingCash() {
        return startingCash;
    }

    public void setStartingCash(BigDecimal startingCash) {
        this.startingCash = startingCash;
    }

    public BigDecimal getDeclaredEndingCash() {
        return declaredEndingCash;
    }

    public void setDeclaredEndingCash(BigDecimal declaredEndingCash) {
        this.declaredEndingCash = declaredEndingCash;
    }

    public BigDecimal getSystemEndingCash() {
        return systemEndingCash;
    }

    public void setSystemEndingCash(BigDecimal systemEndingCash) {
        this.systemEndingCash = systemEndingCash;
    }

    public BigDecimal getDifference() {
        return difference;
    }

    public void setDifference(BigDecimal difference) {
        this.difference = difference;
    }

    public ReconciliationStatus getReconciliationStatus() {
        return reconciliationStatus;
    }

    public void setReconciliationStatus(ReconciliationStatus reconciliationStatus) {
        this.reconciliationStatus = reconciliationStatus;
    }
}
