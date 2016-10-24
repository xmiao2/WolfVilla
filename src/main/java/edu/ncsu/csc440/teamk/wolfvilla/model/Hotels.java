package edu.ncsu.csc440.teamk.wolfvilla.model;

/**
 * Created by Edward on 10/24/16.
 */
public class Hotels {

    private long primaryKey;
    private String address;
    private String name;
    private String phoneNumber;

    public Hotels(long primaryKey, String address, String name, String phoneNumber) {
        this.primaryKey = primaryKey;
        this.address = address;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public long getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(long primaryKey) {
        this.primaryKey = primaryKey;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
