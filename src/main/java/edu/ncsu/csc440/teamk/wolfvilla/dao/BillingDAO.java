package edu.ncsu.csc440.teamk.wolfvilla.dao;

import edu.ncsu.csc440.teamk.wolfvilla.model.BillingInformation;
import edu.ncsu.csc440.teamk.wolfvilla.util.DBConnection;

import java.sql.*;

/**
 * Created by Joshua on 10/30/2016.
 */
public class BillingDAO {
    public static double getBillTotal(long checkInID) throws SQLException, ClassNotFoundException {
        try (Connection connection = DBConnection.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement getServicePrice = connection.prepareStatement(
                     "SELECT SUM(price) FROM services WHERE checkin_id = ?");
                     PreparedStatement getRate = connection.prepareStatement(
                             "SELECT room_categories.nightly_rate " +
                                     "FROM room_categories, rooms, checkin_information " +
                                     "WHERE id = ? AND rooms.hotel_id = checkin_information.hotel_id AND " +
                                     "checkin_information.room_number = rooms.room_number AND " +
                                     "room_categories.category_name = rooms.category_name AND " +
                                     "room_categories.max_occupancy = rooms.max_occupancy");
                     PreparedStatement getTime = connection.prepareStatement(
                             "SELECT extract(day from (checkout_time - checkin_time)) FROM checkin_information WHERE id = ?")) {
                getServicePrice.setLong(1, checkInID);
                getRate.setLong(1, checkInID);
                getTime.setLong(1, checkInID);
                double services = 0;
                double roomRate = 0;
                double time = 0;
                try (ResultSet rs = getServicePrice.executeQuery()) {
                    rs.next();
                    services = rs.getDouble(1);
                }
                try (ResultSet rs = getRate.executeQuery()) {
                    rs.next();
                    roomRate = rs.getDouble(1);
                }
                try (ResultSet rs = getTime.executeQuery()) {
                    rs.next();
                    time = rs.getInt(1);
                }
                connection.commit();
                return services + roomRate * time;
            } catch (Exception e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
        }
    }

    public static void updateBillingInformation(long checkinId, BillingInformation billInfo)
            throws SQLException, ClassNotFoundException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(
                     "UPDATE billing_information " +
                             "SET billing_address=?, ssn=?, payment_method=?, card_number=?, expiration_date = ? " +
                             "WHERE id = (SELECT billing_information_id FROM checkin_information WHERE id = ?)")) {
            stmt.setString(1, billInfo.getBillingAddress());
            stmt.setString(2, billInfo.getSsn());
            stmt.setString(3, billInfo.getPaymentMethod());
            stmt.setString(4, billInfo.getCardNumber());
            stmt.setDate(5, billInfo.getExpirationDate());

            stmt.setLong(6, checkinId);
            stmt.executeUpdate();
        }
    }


    public static BillingInformation retrieveBillingInformation(long checkinId)
            throws SQLException, ClassNotFoundException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(
                     "SELECT * FROM billing_information " +
                             "WHERE id = (SELECT billing_information_id FROM checkin_information WHERE id = ?)")) {
            stmt.setLong(1, checkinId);

            try (ResultSet rs = stmt.executeQuery()) {
                rs.next();
                return new BillingInformation(rs.getLong(1), rs.getString(2), rs.getString(3),
                        rs.getString(4), rs.getString(5), rs.getDate(6));
            }
        }
    }
}
