package edu.ncsu.csc440.teamk.wolfvilla.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Class that instantiates connections to the database either for testing or production
 */
public class DBConnection {
    private static final String JDBC_URL = "jdbc:oracle:thin:@//orca.csc.ncsu.edu:1521/orcl.csc.ncsu.edu";

    //username and password for production
    private static final String PROD_USERNAME = "jacook7";
    private static final String PROD_PASSWORD = "200077272";

    //username and password for testing
    private static final String TEST_USERNAME = "xmiao2";
    private static final String TEST_PASSWORD = "200037219";

    private static boolean isTesting = false;

    /**
     * Creates a connection to the Oracle Database in the proper mode
     * @return Connection the connection object to the database
     * @throws SQLException if the database does not exist
     * @throws ClassNotFoundException if the SQL classes cannot be found
     */
    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("oracle.jdbc.driver.OracleDriver");
        return isTesting ? DriverManager.getConnection(JDBC_URL, PROD_USERNAME, PROD_PASSWORD) :
                DriverManager.getConnection(JDBC_URL, TEST_USERNAME, TEST_PASSWORD);
    }

    /**
     * Sets test mode to the boolean value, switching connection credentials in getConnection
     * from test credentials to production credentials.
     * @param shouldEnable
     */
    public static void enableTestMode(boolean shouldEnable) {
        isTesting = shouldEnable;
    }

    /**
     * Enables test mode, calls enableTestMode(true);
     */
    public static void enableTestMode() {
        enableTestMode(true);
    }
}
