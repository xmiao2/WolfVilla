package edu.ncsu.csc440.teamk.wolfvilla.model;

/**
 * Created by Edward on 10/24/16.
 */
public class Hotel {

    private long id;
    private Long manager;
    private String address;
    private String name;
    private String phoneNumber;

    public Hotel() {
        this("", "", "");
    }

    public Hotel(String address, String name, String phoneNumber) {
        this(-1L,  null, address, name, phoneNumber);
    }

    public Hotel(long id, Long manager, String address, String name, String phoneNumber) {
        this.id = id;
        this.manager = manager;
        this.address = address;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public Long getManager() {
        return manager;
    }

    public void setManager(Long manager) {
        this.manager = manager;
    }
}
