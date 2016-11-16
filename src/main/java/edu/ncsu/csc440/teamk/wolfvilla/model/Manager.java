package edu.ncsu.csc440.teamk.wolfvilla.model;

/**
 * Created by Joshua on 10/24/2016.
 *
 * An object to hold all the information about a Manager held in the database.
 */
public class Manager {
    private long id;
    private long hotelId;

    public Manager(long id, long hotelId) {
        this.id = id;
        this.hotelId = hotelId;
    }

    public long getId() {
        return id;
    }

    public long getHotelId() {
        return hotelId;
    }

    public void setHotelId(long hotelId) {
        this.hotelId = hotelId;
    }
}
