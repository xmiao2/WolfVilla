package edu.ncsu.csc440.teamk.wolfvilla.model;

/**
 * Created by Adac on 10/24/2016.
 *
 * An object to hold all the information about a Room held in the database.
 */
public class Room {
    private Long hotelId;
    private Integer roomNumber;
    private RoomCategory category;

    public Room() {
        category = new RoomCategory();
    }

    public Room(Long hotelId, Integer roomNumber, RoomCategory category) {
        this.hotelId = hotelId;
        this.roomNumber = roomNumber;
        this.category = category;
    }

    public Integer getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(Integer roomNumber) {
        this.roomNumber = roomNumber;
    }

    public Long getHotelId() {
        return hotelId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    }

    public RoomCategory getCategory() {
        return category;
    }

    public void setCategory(RoomCategory category) {
        this.category = category;
    }

    public String getCategoryName() {
        return category.getCategoryName();
    }

    public void setCategoryName(String name) {
        category.setCategoryName(name);
    }

    public Integer getMaxOccupancy() {
        return category.getMaxOccupancy();
    }

    public void setMaxOccupancy(Integer occupancy) {
        category.setMaxOccupancy(occupancy);
    }
}
