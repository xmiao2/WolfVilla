package edu.ncsu.csc440.teamk.wolfvilla.dao;

import edu.ncsu.csc440.teamk.wolfvilla.model.BillingInformation;
import edu.ncsu.csc440.teamk.wolfvilla.model.CheckInInformation;
import edu.ncsu.csc440.teamk.wolfvilla.model.Customer;
import edu.ncsu.csc440.teamk.wolfvilla.util.DBConnection;
import edu.ncsu.csc440.teamk.wolfvilla.util.SQLTypeTranslater;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joshua on 10/27/2016.
 */
public class CheckInDAO {

    public static List<Customer> listOccupants(Date startDate, Date endDate, long hotelID) throws SQLException, ClassNotFoundException {
        try (Connection connection = DBConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement(
                "SELECT customer_id " +
                "FROM checkin_information " +
                "WHERE (date_end IS NULL OR ? < date_end) AND " +
                "date_start < ? AND hotel_id = ?")) {
            stmt.setDate(1, startDate);
            stmt.setDate(2, endDate);
            stmt.setLong(3, hotelID);
            try (ResultSet rs = stmt.executeQuery()) {
                rs.next();
                return convertCustomerList(rs);
            }
        }
    }

    public static void deleteCheckIn(long checkInID) throws SQLException, ClassNotFoundException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(
                     "DELETE from checkin_information WHERE id = ?")) {
            stmt.setLong(1, checkInID);
            stmt.executeUpdate();
        }
    }

    public static long addCheckIn(CheckInInformation checkIn, BillingInformation billing) throws SQLException, ClassNotFoundException {
        try (Connection connection = DBConnection.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement stmt1 = connection.prepareStatement(
                     "INSERT INTO billing_information \n" +
                             "VALUES(billing_information_seq.nextval, ?, ?, ?, ?, ?)",
                     new int[]{1});
             PreparedStatement stmt2 = connection.prepareStatement(
                     "INSERT INTO checkin_information VALUES(checkin_information_seq.nextval , ?, " +
                             "?, ?, ?, ?, ?, ?, ?, ?)",  new int[]{1})) {
                stmt1.setString(1, billing.getBillingAddress());
                stmt1.setString(2, billing.getSsn());
                stmt1.setString(3, billing.getPaymentMethod());
                stmt1.setString(4, billing.getCardNumber());
                stmt1.setDate(5, billing.getExpirationDate());
                stmt1.executeUpdate();

                long billingId = 0L;
                try (ResultSet ID = stmt1.getGeneratedKeys()) {
                    ID.next();
                    billingId = ID.getLong(1);
                }

                stmt2.setInt(1, checkIn.getCurrentOcupancy());
                stmt2.setDate(2, checkIn.getCheckinTime());
                stmt2.setDate(3, checkIn.getCheckoutTime());
                stmt2.setLong(4, billingId);
                stmt2.setLong(5, checkIn.getHotelId());
                stmt2.setLong(6, checkIn.getRoomNumber());
                stmt2.setLong(7, checkIn.getCustomerId());
                SQLTypeTranslater.safeIntSet(stmt2, 8, checkIn.getCateringStaffId());
                SQLTypeTranslater.safeIntSet(stmt2, 9, checkIn.getRoomServiceStaffId());

                stmt2.executeUpdate();
                try (ResultSet ID = stmt2.getGeneratedKeys()) {
                    ID.next();
                    long newId = ID.getLong(1);

                    connection.commit();
                    return newId;
                }
            } catch (Exception e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
        }
    }

    public static void updateCheckIn(CheckInInformation checkIn) throws SQLException, ClassNotFoundException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(
                     "UPDATE checkin_information \n" +
                             "SET hotel_id = ?,  room_number=?, current_occupancy=?, " +
                             "checkin_time = ?, checkout_time=?, catering_staff_id=?, " +
                             "room_service_staff_id=? \n" +
                             "WHERE id=?",  new int[]{1})) {

            stmt.setLong(1, checkIn.getHotelId());
            stmt.setLong(2, checkIn.getRoomNumber());
            stmt.setInt(3, checkIn.getCurrentOcupancy());
            stmt.setDate(4, checkIn.getCheckinTime());
            stmt.setDate(5, checkIn.getCheckoutTime());
            SQLTypeTranslater.safeIntSet(stmt, 6, checkIn.getCateringStaffId());
            SQLTypeTranslater.safeIntSet(stmt, 7, checkIn.getRoomServiceStaffId());

            stmt.setLong(8, checkIn.getId());

            stmt.executeUpdate();
        }
    }

    public static CheckInInformation getCheckIn(long checkInID) throws SQLException, ClassNotFoundException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(
                     "SELECT * FROM checkin_information WHERE id = ?")) {
            stmt.setLong(1, checkInID);
            try (ResultSet rs = stmt.executeQuery()) {
                rs.next();
                return convertToCheckIn(rs);
            }
        }
    }

    public static List<CheckInInformation> listCheckIn(long hotelId) throws SQLException, ClassNotFoundException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(
                     "SELECT * FROM checkin_information WHERE hotel_id = ?")) {
            stmt.setLong(1, hotelId);
            stmt.executeQuery();
            return convertStaffList(stmt.executeQuery());
        }
    }

    private static List<CheckInInformation> convertStaffList(ResultSet rs) throws SQLException {
        ArrayList<CheckInInformation> toReturn = new ArrayList<CheckInInformation>();
        while(rs.next()) {
            toReturn.add(convertToCheckIn(rs));
        }
        return toReturn;
    }

    private static List<Customer> convertCustomerList(ResultSet rs) throws SQLException {
        ArrayList<Customer> toReturn = new ArrayList<Customer>();
        while(rs.next()) {
            toReturn.add(new Customer(rs.getLong(1), rs.getString(2),
                    SQLTypeTranslater.stringToChar(rs.getString(3)), rs.getString(4),
                    rs.getString(5), rs.getString(6)));
        }
        return toReturn;
    }

    private static CheckInInformation convertToCheckIn(ResultSet rs) throws SQLException {
        return new CheckInInformation(rs.getLong(1), rs.getInt(2), rs.getDate(3), rs.getDate(4),
        rs.getLong(5), rs.getLong(6), rs.getLong(7), rs.getLong(8),
        SQLTypeTranslater.safeGetLong(rs, 9), SQLTypeTranslater.safeGetLong(rs, 10));
    }
}
