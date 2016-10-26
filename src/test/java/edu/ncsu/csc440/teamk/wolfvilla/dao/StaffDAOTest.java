package edu.ncsu.csc440.teamk.wolfvilla.dao;

import edu.ncsu.csc440.teamk.wolfvilla.model.Staff;
import org.junit.Assert;

import static org.junit.Assert.*;

public class StaffDAOTest {
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
    }

    private boolean testEqual(Staff s1, Staff s2) {
        if (s1 == null) {
            return s2 == null;
        }
        return s1.equals(s2);
    }

}