package edu.ncsu.csc440.teamk.wolfvilla.dao;

import edu.ncsu.csc440.teamk.wolfvilla.model.Room;
import edu.ncsu.csc440.teamk.wolfvilla.model.Customer;
import edu.ncsu.csc440.teamk.wolfvilla.model.Staff;
import edu.ncsu.csc440.teamk.wolfvilla.util.DBConnection;
import edu.ncsu.csc440.teamk.wolfvilla.util.TestEnvironmentSetter;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Date;

import static org.junit.Assert.*;

/**
 * Created by Adac on 11/6/2016.
 */
public class ReportDAOTest{

    @BeforeClass
    public static void setupClass() throws Exception {
        DBConnection.enableTestMode();
        TestEnvironmentSetter.setUp();
    }

    @Test
    public void createCustomer() throws Exception {
        List<Room> availRooms = ReportDAO.reportAvailable("Deluxe", new Date(2017-1900, 1-1, 2), new Date(2017-1900, 1-1, 3), 0, 2);
        assertEquals(availRooms.size(),4);

        //for(int i = 0; i < availRooms.size() ;i++){
        //    System.out.println(availRooms.get(i).getRoomNumber());
        //}

        List<Integer> occupiedRooms = ReportDAO.reportOccupied(new Date(2017-1900, 1-1, 2), new Date(2017-1900, 1-1, 3), 0);
        assertEquals(occupiedRooms.size(),2);

        List<Room> unoccupiedRooms = ReportDAO.reportUnoccupanied(new Date(2017-1900, 1-1, 2), new Date(2017-1900, 1-1, 3), 0);
        assertEquals(unoccupiedRooms.size(),30);

        List<Customer> occupants = ReportDAO.reportOccupants(new Date(2017-1900, 1-1, 2), new Date(2017-1900, 1-1, 3), 0);
        assertEquals(occupants.size(),2);

        double percentOcc = ReportDAO.percentOccupied(new Date(2017-1900, 1-1, 2), new Date(2017-1900, 1-1, 3), 0);
        assertTrue(Math.abs(percentOcc - 2/32.0) < 0.0001);

        List<Staff> cateringStaff = ReportDAO.getStaffByRole("caterer");
        assertEquals(cateringStaff.size(),4);

        List<Staff> cateringStaffHotel0 = ReportDAO.getHotelStaffByRole("caterer", 1);
        assertEquals(cateringStaffHotel0.size(),1);

        List<Customer> staffCustomers = ReportDAO.getCustomersOfStaff(4);
        assertEquals(staffCustomers.size(),1);
    }
}
