package edu.ncsu.csc440.teamk.wolfvilla.dao;

import edu.ncsu.csc440.teamk.wolfvilla.model.BillingInformation;
import edu.ncsu.csc440.teamk.wolfvilla.model.CheckInInformation;
import org.junit.Test;

import java.sql.Date;

import static org.junit.Assert.*;

/**
 * Created by Joshua on 10/27/2016.
 */
public class CheckInDAOTest {
    @Test
    public void addCheckIn() throws Exception {
        CheckInInformation checkIn = new CheckInInformation(
        -1L, 1, new Date(2000,10,22), null,
        -1, 0, 1, 1,
        null, null
        );
        BillingInformation billing = new BillingInformation(-1, "address", "333224444",
                "credit", "9999888877776666", new Date(2017,11,22));
        long cid = CheckInDAO.addCheckIn(checkIn, billing);

        assertEquals(new Date(2000,10,22), CheckInDAO.getCheckIn(cid).getCheckinTime());

        CheckInInformation checkIn2 = new CheckInInformation(
                cid, 1, new Date(2016,10,22), null,
                -1, 0, 1, 1,
                null, null
        );
        CheckInDAO.updateCheckIn(checkIn2);
        assertEquals(new Date(2016,10,22), CheckInDAO.getCheckIn(cid).getCheckinTime());

        CheckInDAO.deleteCheckIn(cid);
    }

}