package edu.ncsu.csc440.teamk.wolfvilla.dao;

import edu.ncsu.csc440.teamk.wolfvilla.model.Service;
import edu.ncsu.csc440.teamk.wolfvilla.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Joshua on 10/27/2016.
 */
public class ServicesDAO {

    public static void deleteService(long serviceId) throws SQLException, ClassNotFoundException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement("DELETE FROM services WHERE id = ?")) {
            stmt.setLong(1, serviceId);
            stmt.executeUpdate();
        }
    }


    public static void updateService(Service service) throws SQLException, ClassNotFoundException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(
                     "UPDATE services SET staff_id = ?, description = ?, " +
                             "price = ?, checkin_id  = ? WHERE id = ?")) {
            stmt.setLong(1, service.getStaffId());
            stmt.setString(2, service.getDescription());
            stmt.setDouble(3, service.getPrice());
            stmt.setLong(4, service.getCheckinID());
            stmt.setLong(5, service.getPrimaryKey());

            stmt.executeUpdate();
        }
    }

    public static double getCheckInServiceCost(long checkInID) throws SQLException, ClassNotFoundException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(
                     "SELECT SUM(price) FROM services WHERE checkin_id = ?")) {
            stmt.setLong(1, checkInID);

            try (ResultSet rs = stmt.executeQuery()) {
                rs.next();
                return rs.getDouble(1);
            }
        }
    }

    public static long addService(Service service) throws SQLException, ClassNotFoundException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(
                     "INSERT INTO services VALUES (services_seq.nextval, ?, ?, ?, ?)",  new int[]{1})) {
            stmt.setString(1, service.getDescription());
            stmt.setDouble(2, service.getPrice());
            stmt.setLong(3, service.getStaffId());
            stmt.setLong(4, service.getCheckinID());

            stmt.executeUpdate();
            try (ResultSet ID = stmt.getGeneratedKeys()) {
                ID.next();
                return ID.getLong(1);
            }
        }
    }
}
