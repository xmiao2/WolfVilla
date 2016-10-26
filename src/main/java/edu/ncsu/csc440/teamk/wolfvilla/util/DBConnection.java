package edu.ncsu.csc440.teamk.wolfvilla.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBConnection {
    private static final String JDBC_URL = "jdbc:oracle:thin:@//orca.csc.ncsu.edu:1521/orcl.csc.ncsu.edu";
    private static final String USERNAME = "jacook7";//"xmiao2";
    private static final String PASSWORD = "200077272";//"200037219";

    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("oracle.jdbc.driver.OracleDriver");
        return DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
    }
}
