package edu.ncsu.csc440.teamk.wolfvilla.model;

/**
 * Created by Adac on 10/24/2016.
 *
 * An object to hold all the information about a Room held in the database.
 */
public class Room {
    private Long hotelId;
    private Integer roomNumber;
    private String categoryName;
    private int maxOccupancy;

    public Room() {
    }

    public Room(Long hotelId, Integer roomNumber, String categoryName, int maxOccupancy) {
        this.hotelId = hotelId;
        this.roomNumber = roomNumber;
        this.categoryName = categoryName;
        this.maxOccupancy = maxOccupancy;
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

    public void setRoomNumber(Integer roomNumber) {
        this.roomNumber = roomNumber;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setMaxOccupancy(int maxOccupancy) {
        this.maxOccupancy = maxOccupancy;
    }

    public Long getHotelId() {
        return hotelId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    }
}
