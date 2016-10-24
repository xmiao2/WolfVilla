package edu.ncsu.csc440.teamk.wolfvilla.model;

/**
 * Created by Edward on 10/24/16.
 */
public class Services {

    private long primaryKey;
    private String description;
    private double price;

    private long staffId;
    private long checkinID;

    public Services(long primaryKey, String description, double price, long staffId, long checkinID) {
        this.primaryKey = primaryKey;
        this.description = description;
        this.price = price;
        this.staffId = staffId;
        this.checkinID = checkinID;
    }

    public long getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(long primaryKey) {
        this.primaryKey = primaryKey;
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
