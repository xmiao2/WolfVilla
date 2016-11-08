package edu.ncsu.csc440.teamk.wolfvilla.dao;

import edu.ncsu.csc440.teamk.wolfvilla.model.Service;
import edu.ncsu.csc440.teamk.wolfvilla.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
            stmt.setLong(5, service.getId());

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

    public static Service getService (long id) throws SQLException, ClassNotFoundException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(
                     "SELECT * FROM services WHERE id = ?")) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return covertToService(rs);
                } else {
                    return null;
                }
            }
        }
    }

    public static List<Service> getAllServices() throws SQLException, ClassNotFoundException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(
                     "SELECT * FROM services")) {
            return covertToServiceList(stmt.executeQuery());
        }
    }

    private static List<Service> covertToServiceList(ResultSet rs) throws SQLException {
        List<Service> toReturn = new ArrayList<Service>();
        while (rs.next()) {
            toReturn.add(covertToService(rs));
        }
        return toReturn;
    }

    private static Service covertToService(ResultSet rs) throws SQLException {
        return new Service(rs.getLong(1), rs.getString(2), rs.getDouble(3), rs.getLong(4), rs.getLong(5));
    }
}
