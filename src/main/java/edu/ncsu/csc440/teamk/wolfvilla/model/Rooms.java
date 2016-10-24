package edu.ncsu.csc440.teamk.wolfvilla.model;

/**
 * Created by Adac on 10/24/2016.
 */
public class Rooms {
    private Long id;
    private Integer roomNumber;
    private String categoryName;
    private int maxOccupancy;

    public Long getId() {
        return id;
    }

    public Integer getRoomNumber() {
        return roomNumber;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public int getMaxOccupancy() {
        return maxOccupancy;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setRoomNumber(Integer roomNumber) {
        this.roomNumber = roomNumber;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setMaxOccupancy(int maxOccupancy) {
        this.maxOccupancy = maxOccupancy;
    }

    public Rooms(Long id, Integer roomNumber, String categoryName, int maxOccupancy) {
        this.id = id;
        this.roomNumber = roomNumber;
        this.categoryName = categoryName;
        this.maxOccupancy = maxOccupancy;
    }
}
