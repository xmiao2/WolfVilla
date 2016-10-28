package edu.ncsu.csc440.teamk.wolfvilla.dao;

import edu.ncsu.csc440.teamk.wolfvilla.model.Hotel;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Joshua on 10/25/2016.
 */
public class HotelDAOTest {
    @Test
    public void createHotel() throws Exception {
        Hotel h1 = new Hotel(-1L, "Test room", "test", "3336668888");
        long id = HotelDAO.createHotel(h1);

        Hotel h2 = new Hotel(id, "Test room", "proof", "3336668888");
        //Just make sure the function does not crash anything.
        HotelDAO.getHotels();
        HotelDAO.updateHotel(h2);
        HotelDAO.deleteHotel(id);
    }

}