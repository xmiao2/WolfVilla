package edu.ncsu.csc440.teamk.wolfvilla.dao;

import edu.ncsu.csc440.teamk.wolfvilla.model.Hotel;
import edu.ncsu.csc440.teamk.wolfvilla.util.DBConnection;
import edu.ncsu.csc440.teamk.wolfvilla.util.TestEnvironmentSetter;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Joshua on 10/25/2016.
 */
public class HotelDAOTest {
    @BeforeClass
    public static void setupClass() throws Exception {
        DBConnection.enableTestMode();
        TestEnvironmentSetter.setUp();
    }

    @Test
    public void createHotel() throws Exception {
        Hotel h1 = new Hotel(-1L, "Test room", "test", "3336668888");
        long id = HotelDAO.createHotel(h1);

        Hotel h2 = HotelDAO.getHotelById(id);

        assertEquals(h1, h2);

        Hotel h3 = new Hotel(id, "Test room", "proof", "3336668888");
        HotelDAO.updateHotel(h3);

        Hotel h4 = HotelDAO.getHotelById(id);
        assertEquals(h3, h4);
        assertNotEquals(h2, h3);

        HotelDAO.deleteHotel(id);
    }

}