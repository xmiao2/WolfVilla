package edu.ncsu.csc440.teamk.wolfvilla.dao;

import edu.ncsu.csc440.teamk.wolfvilla.model.Customer;
import edu.ncsu.csc440.teamk.wolfvilla.util.DBConnection;
import edu.ncsu.csc440.teamk.wolfvilla.util.SQLTypeTranslater;
import edu.ncsu.csc440.teamk.wolfvilla.util.TestEnvironmentSetter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adam on 10/31/2016.
 */
public class CustomerDAO {
    public static long createCustomer (Customer customer) throws SQLException, ClassNotFoundException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(
                     "INSERT INTO customers VALUES(customer_seq.nextval, ?, ?, ?, ?, ?)",  new int[]{1})) {
             stmt.setString(1, customer.getName());
             stmt.setString(2, SQLTypeTranslater.charToString(customer.getGender()));
             stmt.setString(3, customer.getPhoneNumber());
             stmt.setString(4, customer.getEmail());
             stmt.setString(5, customer.getAddress());

             stmt.executeUpdate();
             try (ResultSet ID = stmt.getGeneratedKeys()) {
                 ID.next();
                 return ID.getLong(1);
             }
        }
    }

    public static void deleteCustomer(long customerID) throws SQLException, ClassNotFoundException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement("DELETE FROM customers WHERE id = ?")) {
            stmt.setLong(1, customerID);
            stmt.executeUpdate();
        }
    }

    public static void updateCustomer (Customer customer) throws SQLException, ClassNotFoundException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(
                     "UPDATE customers SET name = ?, gender = ?, phone_number = ?, email = ?, address = ? WHERE id = ?")) {
            stmt.setString(1, customer.getName());
            stmt.setString(2, SQLTypeTranslater.charToString(customer.getGender()));
            stmt.setString(3, customer.getPhoneNumber());
            stmt.setString(4, customer.getEmail());
            stmt.setString(5, customer.getAddress());
            stmt.setLong(  6, customer.getId());
            stmt.executeUpdate();
        }
    }

    public static Customer getCustomer(long id) throws SQLException, ClassNotFoundException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement("SELECT * FROM customers WHERE id = ?");) {
            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                rs.next();
                return convertToCustomer(rs);
            }
        }
    }

    public static List<Customer> listCustomers(long id) throws SQLException, ClassNotFoundException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement("SELECT *\n" +
                                          "FROM CUSTOMERS");) {
            /*
                                          " +
                                          "WHERE id IN(SELECT DISTINCT customer_id\n" +
                                          "FROM checkin_information\n" +
                                          "WHERE hotel_id = ?)")
             */

            try (ResultSet rs = stmt.executeQuery()) {
                return convertCustomerList(rs);
            }
        }
    }

    private static List<Customer> convertCustomerList(ResultSet rs) throws SQLException {
        ArrayList<Customer> toReturn = new ArrayList<Customer>();
        while(rs.next()) {
            toReturn.add(convertToCustomer(rs));
        }
        return toReturn;
    }

    private static Customer convertToCustomer(ResultSet rs) throws SQLException {
        //Check to make sure it works
        return new Customer(rs.getLong(1), rs.getString(2), SQLTypeTranslater.stringToChar(rs.getString(3)), rs.getString(4), rs.getString(5), rs.getString(6));
    }

}
