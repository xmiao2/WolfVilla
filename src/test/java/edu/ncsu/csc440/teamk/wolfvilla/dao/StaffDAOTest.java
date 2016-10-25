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
    }

    private boolean testEqual(Staff s1, Staff s2) {
        if (s1 == null) {
            return s2 == null;
        }
        return s1.equals(s2);
    }

}