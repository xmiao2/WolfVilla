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
    /**
     * Deletes the service with the given serviceID.
     * @param serviceId the id of the service to delete.
     * @throws SQLException If the query throws an exception
     * @throws ClassNotFoundException If DBConnection is cannot load connection.
     */
    public static void deleteService(long serviceId) throws SQLException, ClassNotFoundException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement("DELETE FROM services WHERE id = ?")) {
            stmt.setLong(1, serviceId);
            stmt.executeUpdate();
        }
    }

    /**
     * Updates the service with the same id as service to the information in service.
     * @param service the information to update the service with the same id in the database to.
     * @throws SQLException If the query throws an exception
     * @throws ClassNotFoundException If DBConnection is cannot load connection.
     */
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

    /**
     * Returns the sum of all service costs associated with a checkin.
     * @param checkInID the if of the checkin to sum services for.
     * @return The sum of all services costs associated with a checkInID.
     * @throws SQLException If the query throws an exception
     * @throws ClassNotFoundException If DBConnection is cannot load connection.
     */
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

    /**
     * Adds a new service with the information in service.
     * @param service the information of the service to add.
     * @return the id of the services added.
     * @throws SQLException If the query throws an exception
     * @throws ClassNotFoundException If DBConnection is cannot load connection.
     */
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

    /**
     * @param id the id of the services to retrieve
     * @return the service associated with the id.
     * @throws SQLException If the query throws an exception
     * @throws ClassNotFoundException If DBConnection is cannot load connection.
     */
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

    /**
     * @return the list of all services.
     * @throws SQLException If the query throws an exception
     * @throws ClassNotFoundException If DBConnection is cannot load connection.
     */
    public static List<Service> getAllServices() throws SQLException, ClassNotFoundException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(
                     "SELECT * FROM services")) {
            return convertToServiceList(stmt.executeQuery());
        }
    }

    /**
     * @param rs the result set to get services from.
     * @return a list of rervices from rs.
     * @throws SQLException If the query throws an exception
     */
    private static List<Service> convertToServiceList(ResultSet rs) throws SQLException {
        List<Service> toReturn = new ArrayList<Service>();
        while (rs.next()) {
            toReturn.add(covertToService(rs));
        }
        return toReturn;
    }

    /**
     *
     * @param rs the result set to get a service from.
     * @return a service from the current row in rs.
     * @throws SQLException If the query throws an exception
     */
    private static Service covertToService(ResultSet rs) throws SQLException {
        return new Service(rs.getLong(1), rs.getString(2), rs.getDouble(3), rs.getLong(4), rs.getLong(5));
    }
}
