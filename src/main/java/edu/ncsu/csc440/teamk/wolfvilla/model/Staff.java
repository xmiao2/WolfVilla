package edu.ncsu.csc440.teamk.wolfvilla.model;

/**
 * Created by xm on 10/24/16.
 *
 * An object to hold all the information about a Staff held in the database.
 */
public class Staff {
    private long id;
    private String name;
    private TitleDepartment titleDepartment;
    private String ssn;
    private Integer age;
    private Character gender;
    private String address;
    private String phoneNumber;
    private long hotelId;

    public Staff() {
        this.titleDepartment = new TitleDepartment("");
    }

    public Staff(String name, String title,
                 String ssn, Integer age, Character gender, String address,
                 String phoneNumber, long hotelId) {
        this(-1L, name, title, ssn, age, gender, address, phoneNumber, hotelId);
    }

    public Staff(long id, String name, String title,
                 String ssn, Integer age, Character gender, String address,
                 String phoneNumber, long hotelId) {
        this(id, name, new TitleDepartment(title), ssn, age, gender, address, phoneNumber, hotelId);
    }

    public Staff(long id, String name, TitleDepartment titleDepartment,
                 String ssn, Integer age, Character gender, String address,
                 String phoneNumber, long hotelId) {
        this.id = id;
        this.name = name;
        this.titleDepartment = titleDepartment;
        this.ssn = ssn;
        this.age = age;
        this.gender = gender;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.hotelId = hotelId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return titleDepartment.getTitle();
    }

    public String getDepartment() {
        return titleDepartment.getDepartment();
    }

    public TitleDepartment getTitleDepartment() {
        return titleDepartment;
    }

    public void setTitleDepartment(TitleDepartment titleDepartment) {
        this.titleDepartment = titleDepartment;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Character getGender() {
        return gender;
    }

    public void setGender(Character gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public long getHotelId() {
        return hotelId;
    }

    public void setHotelId(long hotelId) {
        this.hotelId = hotelId;
    }
}
