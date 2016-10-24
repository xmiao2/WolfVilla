package edu.ncsu.csc440.teamk.wolfvilla.model;

/**
 * Created by xm on 10/24/16.
 */
public class Staff {
    private long id;
    private String name;
    private String title;
    private String ssn;
    private Integer age;
    private Character gender;
    private String address;
    private String phoneNumber;
    private long hotelId;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public String getSsn() {
        return ssn;
    }

    public Integer getAge() {
        return age;
    }

    public Character getGender() {
        return gender;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public long getHotelId() {
        return hotelId;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public void setGender(Character gender) {
        this.gender = gender;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setHotelId(long hotelId) {
        this.hotelId = hotelId;
    }

    public Staff(Long id, String name, String title, String ssn, Integer age, Character gender, String address, String phoneNumber, long hotelId){
        this.id = id;
        this.name = name;
        this.title = title;
        this.ssn = ssn;
        this.age = age;
        this.gender = gender;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.hotelId = hotelId;
    }
}
