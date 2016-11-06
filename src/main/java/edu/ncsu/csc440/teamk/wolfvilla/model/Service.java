package edu.ncsu.csc440.teamk.wolfvilla.model;

/**
 * Created by Edward on 10/24/16.
 */
public class Service {

    private long id;
    private String description;
    private double price;

    private long staffId;
    private long checkinID;

    public Service(long id, String description, double price, long staffId, long checkinID) {
        this.id = id;
        this.description = description;
        this.price = price;
        this.staffId = staffId;
        this.checkinID = checkinID;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public long getStaffId() {
        return staffId;
    }

    public void setStaffId(long staffId) {
        this.staffId = staffId;
    }

    public long getCheckinID() {
        return checkinID;
    }

    public void setCheckinID(long checkinID) {
        this.checkinID = checkinID;
    }
}
