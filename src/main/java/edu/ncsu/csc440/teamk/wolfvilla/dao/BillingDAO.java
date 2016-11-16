package edu.ncsu.csc440.teamk.wolfvilla.dao;

import edu.ncsu.csc440.teamk.wolfvilla.model.BillingInformation;
import edu.ncsu.csc440.teamk.wolfvilla.util.DBConnection;

import java.sql.*;

/**
 * Created by Joshua on 10/30/2016.
 *
 *
 */
public class BillingDAO {

    /**
     * Updates the billing information associated with the checkin associated with the checkinId.
     * @param checkinId the id of the checkin associated with the billingInformation to update.
     * @param billInfo the new information to update the billingInformation to.
     * @throws SQLException If the query throws an exception
     * @throws ClassNotFoundException If DBConnection is cannot load connection.
     */
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

    /**
     * Retrieves the billing information associated with a given checkinId
     * @param checkinId the id of the checkin associated with this billing information.
     * @return the billing information associated with a given checkinId
     * @throws SQLException If the query throws an exception
     * @throws ClassNotFoundException If DBConnection is cannot load connection.
     */
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
