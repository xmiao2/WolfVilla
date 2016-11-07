package edu.ncsu.csc440.teamk.wolfvilla.dao;

import edu.ncsu.csc440.teamk.wolfvilla.util.DBConnection;
import edu.ncsu.csc440.teamk.wolfvilla.util.TestEnvironmentSetter;
import org.junit.BeforeClass;
import org.junit.Test;

public class RoomDAOTest {

    @BeforeClass
    public void setupClass() throws Exception {
        DBConnection.enableTestMode();
        TestEnvironmentSetter.setUp();
    }


}