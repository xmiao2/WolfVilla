package edu.ncsu.csc440.teamk.wolfvilla.model;

/**
 * Created by Edward on 10/24/16.
 */
public class Customer {

    private long primaryKey;
    private String name;
    private Character gender;
    private String phoneNumber;
    private String email;
    private String address;

    public Customer(long primaryKey, String name, Character gender, String phoneNumber, String email, String address) {
        this.primaryKey = primaryKey;
        this.name = name;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
    }

    public long getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(long primaryKey) {
        this.primaryKey = primaryKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Character getGender() {
        return gender;
    }

    public void setGender(Character gender) {
        this.gender = gender;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
