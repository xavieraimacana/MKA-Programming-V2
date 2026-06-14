package ec.espe.edu.coffeeshop.model;

import java.time.LocalDateTime;

/**
 * Represents a customer's table reservation at a specific date and time.
 * 
 * @author MKA Programmers, ESPE
 */
public class Reservation {
    private String id;
    private String tableId;
    private String customerName;
    private LocalDateTime reservationTime;
    private int numberOfPeople;

    public Reservation() {}

    public Reservation(String id, String tableId, String customerName, LocalDateTime reservationTime, int numberOfPeople) {
        this.id = id;
        this.tableId = tableId;
        this.customerName = customerName;
        this.reservationTime = reservationTime;
        this.numberOfPeople = numberOfPeople;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public LocalDateTime getReservationTime() {
        return reservationTime;
    }

    public void setReservationTime(LocalDateTime reservationTime) {
        this.reservationTime = reservationTime;
    }

    public int getNumberOfPeople() {
        return numberOfPeople;
    }

    public void setNumberOfPeople(int numberOfPeople) {
        this.numberOfPeople = numberOfPeople;
    }
}
