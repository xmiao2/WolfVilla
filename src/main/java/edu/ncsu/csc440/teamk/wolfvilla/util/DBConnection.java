package edu.ncsu.csc440.teamk.wolfvilla.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String JDBC_URL = "jdbc:oracle:thin:@//orca.csc.ncsu.edu:1521/orcl.csc.ncsu.edu";

    private static final String PROD_USERNAME = "jacook7";
    private static final String PROD_PASSWORD = "200077272";

    private static final String TEST_USERNAME = "xmiao2";
    private static final String TEST_PASSWORD = "200037219";

    private static boolean isTesting = false;

    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("oracle.jdbc.driver.OracleDriver");
        return isTesting ? DriverManager.getConnection(JDBC_URL, PROD_USERNAME, PROD_PASSWORD) :
                DriverManager.getConnection(JDBC_URL, TEST_USERNAME, TEST_PASSWORD);
    }

    public static void enableTestMode(boolean shouldEnable) {
        isTesting = shouldEnable;
    }

    public static void enableTestMode() {
        enableTestMode(true);
    }
}
