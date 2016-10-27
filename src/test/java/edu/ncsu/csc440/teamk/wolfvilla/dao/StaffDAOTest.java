package edu.ncsu.csc440.teamk.wolfvilla.dao;

import edu.ncsu.csc440.teamk.wolfvilla.model.Hotel;
import edu.ncsu.csc440.teamk.wolfvilla.model.Staff;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class StaffDAOTest {
    @Test
    public void retrieveStaff() throws Exception {
        long id = HotelDAO.createHotel( new Hotel(-1L, "Test room", "test", "3336668888"));

        Staff s1 = new Staff(-1L, "Tim", "caterer", "000112222", 12, 'F', "Place", "3336664444", id);
        Staff s2 = new Staff(-1L, "Kim", "caterer", "000112222", 12, 'M', "Place", "3336664444", id);
        Staff s3 = new Staff(-1L, "Jim", "service staff", "000112222", 12, 'M', "Place", "3336664444", id);
        long id1 = StaffDAO.addStaff(s1);
        long id2 = StaffDAO.addStaff(s2);
        long id3 = StaffDAO.addStaff(s3);
        {
            boolean has1 = false;
            boolean has2 = false;
            boolean has3 = false;

            List<Staff> staffList = StaffDAO.retrieveStaffByHotel(id);

            assertEquals(3, staffList.size());
            for (Staff s : staffList) {
                has1 = has1 || (s.getName().equals("Tim"));
                has2 = has2 || (s.getName().equals("Jim"));
                has3 = has3 || (s.getName().equals("Kim"));
            }
            assertTrue(has1 && has2 && has3);
        }
        {
            boolean has1 = false;
            boolean has2 = false;

            List<Staff> staffList = StaffDAO.retrieveStaffByTitle(id, "caterer");

            assertEquals(2, staffList.size());
            for (Staff s : staffList) {
                has1 = has1 || (s.getName().equals("Tim"));
                has2 = has2 || (s.getName().equals("Kim"));
            }
            assertTrue(has1 && has2);
        }

        StaffDAO.deleteStaff(id1);
        StaffDAO.deleteStaff(id2);
        StaffDAO.deleteStaff(id3);
        HotelDAO.deleteHotel(id);
    }

    @org.junit.Test
    public void addStaff() throws Exception {
        Staff s1 = new Staff(-1L, "Tim", "caterer", "000112222", 12, 'F', "Place", "3336664444", 0L);
        long id = StaffDAO.addStaff(s1);
        Staff s2 = StaffDAO.retrieveStaff(id);
        assertEquals(s1,s2);
        Staff s3 = new Staff(id, "Tim", "caterer", "000112222", 12, 'M', "Place", "3336664444", 0L);
        assertNotEquals(s1, s3);
        StaffDAO.updateStaff(s3);

        s2 = StaffDAO.retrieveStaff(id);
        assertNotEquals(s1, s2);
        assertEquals(s2, s3);

        StaffDAO.deleteStaff(id);
    }

    private boolean testEqual(Staff s1, Staff s2) {
        if (s1 == null) {
            return s2 == null;
        }
        return s1.equals(s2);
    }

}