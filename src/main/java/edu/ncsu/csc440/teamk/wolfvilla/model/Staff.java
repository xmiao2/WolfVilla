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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Staff staff = (Staff) o;

        if (hotelId != staff.hotelId) return false;
        if (name != null ? !name.equals(staff.name) : staff.name != null) return false;
        if (title != null ? !title.equals(staff.title) : staff.title != null) return false;
        if (ssn != null ? !ssn.equals(staff.ssn) : staff.ssn != null) return false;
        if (age != null ? !age.equals(staff.age) : staff.age != null) return false;
        if (gender != null ? !gender.equals(staff.gender) : staff.gender != null) return false;
        if (address != null ? !address.equals(staff.address) : staff.address != null) return false;
        return phoneNumber != null ? phoneNumber.equals(staff.phoneNumber) : staff.phoneNumber == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (ssn != null ? ssn.hashCode() : 0);
        result = 31 * result + (age != null ? age.hashCode() : 0);
        result = 31 * result + (gender != null ? gender.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (phoneNumber != null ? phoneNumber.hashCode() : 0);
        result = 31 * result + (int) (hotelId ^ (hotelId >>> 32));
        return result;
    }
}
