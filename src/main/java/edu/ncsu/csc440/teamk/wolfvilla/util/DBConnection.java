package edu.ncsu.csc440.teamk.wolfvilla.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBConnection {
    private DBConnection dbConnection;
    private Connection connection;

    private static final String JDBC_URL = "jdbc:oracle:thin:@//orca.csc.ncsu.edu:1521/orcl.csc.ncsu.edu";
    private static final String USERNAME = "xmiao2";
    private static final String PASSWORD = "200037219";

    private DBConnection() throws SQLException, ClassNotFoundException {
        Class.forName("oracle.jdbc.driver.OracleDriver");
        connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
    }

    public DBConnection getInstance() throws SQLException, ClassNotFoundException {
        if (dbConnection == null) {
            dbConnection = new DBConnection();
        }
        return dbConnection;
    }

    public Connection getConnection() {
        return connection;
    }
}
