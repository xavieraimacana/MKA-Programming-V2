package ec.espe.edu.coffeeshop.model;

import java.util.Date;

/**
 * Represents a piece of equipment in the coffee shop, such as coffee makers or grinders.
 * 
 * @author Kevin Albán, MKA Programmer, @ESPE
 */
public class Equipment {
    private String id;
    private String name;
    private EquipmentStatus status;
    private Date lastMaintenanceDate;

    public Equipment() {}

    public Equipment(String id, String name, EquipmentStatus status, Date lastMaintenanceDate) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.lastMaintenanceDate = lastMaintenanceDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EquipmentStatus getStatus() {
        return status;
    }

    public void setStatus(EquipmentStatus status) {
        this.status = status;
    }

    public Date getLastMaintenanceDate() {
        return lastMaintenanceDate;
    }

    public void setLastMaintenanceDate(Date lastMaintenanceDate) {
        this.lastMaintenanceDate = lastMaintenanceDate;
    }
}
