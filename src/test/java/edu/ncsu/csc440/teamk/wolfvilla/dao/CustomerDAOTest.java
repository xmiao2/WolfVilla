package edu.ncsu.csc440.teamk.wolfvilla.dao;

import edu.ncsu.csc440.teamk.wolfvilla.model.Customer;
import edu.ncsu.csc440.teamk.wolfvilla.util.DBConnection;
import edu.ncsu.csc440.teamk.wolfvilla.util.TestEnvironmentSetter;
import org.junit.Test;
import org.junit.BeforeClass;

import static org.junit.Assert.*;

/**
 * Created by Adac on 11/6/2016.
 */
public class CustomerDAOTest {
    @BeforeClass
    public static void setupClass() throws Exception {
        DBConnection.enableTestMode();
        TestEnvironmentSetter.setUp();
    }

    @Test
    public void createCustomer() throws Exception {
        //long id, String name, Character gender, String phoneNumber, String email, String address
        Customer c1 = new Customer(666, "Harry Tompkins", 'M', "9191234567", "htompkin@gmail.com",
                "920 Hedriks Ln, Raleigh, NC 23456");
        long id1 = CustomerDAO.createCustomer(c1);

        Customer c2 = new Customer(777, "Mary Pompkins", 'F', "9191204567", "mpompkin@gmail.com",
                "902 Mystery Ln, Raleigh, NC 23456");
        long id2 = CustomerDAO.createCustomer(c2);
        Customer c3 = CustomerDAO.getCustomer(1);
        assertEquals(c3.getName(), "Alan Turing");

        CustomerDAO.updateCustomer(new Customer(5, "M.P.", 'F', "9191204567", "mp@gmail.com",
                "902 Mystery P, Raleigh, NC 23456"));

        Customer c4 = CustomerDAO.getCustomer(5);

        assertEquals(c4.getName(), "M.P.");

        CustomerDAO.deleteCustomer(4);
    }
}
