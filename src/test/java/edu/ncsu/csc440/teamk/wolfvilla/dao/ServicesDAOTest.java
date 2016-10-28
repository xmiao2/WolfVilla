package edu.ncsu.csc440.teamk.wolfvilla.dao;

import edu.ncsu.csc440.teamk.wolfvilla.model.*;
import org.junit.Test;

import java.sql.Date;

import static org.junit.Assert.*;

/**
 * Created by Joshua on 10/27/2016.
 */
public class ServicesDAOTest {
    @Test
    public void getCheckInServiceCost() throws Exception {
        CheckInInformation checkIn = new CheckInInformation(
                -1L, 1, new Date(2000,10,22), null,
                -1, 0, 1, 1,
                null, null
        );
        BillingInformation billing = new BillingInformation(-1, "address", "333224444",
                "credit", "9999888877776666", new Date(2017,11,22));
        long cid = CheckInDAO.addCheckIn(checkIn, billing);


        long sid = StaffDAO.addStaff(new Staff(-1L, "Tim", "caterer", "000112222", 12, 'F', "Place", "3336664444", 0));

        long vid = ServicesDAO.addService(new Service(-1, "Got punched by customer", 10.11, sid, cid));

        double price = ServicesDAO.getCheckInServiceCost(cid);
        assertEquals(10.11, price, 0.00001);

        long vid2 = ServicesDAO.addService(new Service(-1, "Fit my whole fist in my mouth.", 5.01, sid, cid));
        price = ServicesDAO.getCheckInServiceCost(cid);
        assertEquals(15.12, price, 0.00001);

        ServicesDAO.deleteService(vid);
        ServicesDAO.updateService(new Service(vid2, "But vommited.", 1.23, sid, cid));
        price = ServicesDAO.getCheckInServiceCost(cid);
        assertEquals(1.23, price, 0.00001);

        ServicesDAO.deleteService(vid2);
        StaffDAO.deleteStaff(sid);
        CheckInDAO.deleteCheckIn(cid);
    }

}