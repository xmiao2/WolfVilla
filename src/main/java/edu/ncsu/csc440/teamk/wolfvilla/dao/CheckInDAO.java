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
    /**
     * Reports all customers in the given hotel between startDate and endDate. That is, if
     * there is any overlap between startDate and EndDate with a given checkin, that customer
     * is returned.
     * @param startDate The beginning date in the interval.
     * @param endDate The ending date in the interval.
     * @param hotelID The id of the hotel to look for customers in
     * @returna list of occupants in the given hotel in the given interval.
     * @throws SQLException If the query throws an exception
     * @throws ClassNotFoundException If DBConnection is cannot load connection.
     */
    public static List<Customer> listOccupants(Date startDate, Date endDate, long hotelID)
            throws SQLException, ClassNotFoundException {
        try (Connection connection = DBConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement(
                "SELECT customers.id, customers.name, customers.gender, " +
                        "customers.phone_number, customers.email, customers.address " +
                "FROM checkin_information, customers " +
                "WHERE (checkout_time IS NULL OR ? < checkout_time) AND " +
                "checkin_time < ? AND hotel_id = ? AND customers.id = checkin_information.customer_id")) {
            stmt.setDate(1, startDate);
            stmt.setDate(2, endDate);
            stmt.setLong(3, hotelID);
            try (ResultSet rs = stmt.executeQuery()) {
                rs.next();
                return convertCustomerList(rs);
            }
        }
    }

    /**
     * Deletes the checkin with checkInID.
     * @param checkInID the id of the check in to delete.
     * @throws SQLException If the query throws an exception
     * @throws ClassNotFoundException If DBConnection is cannot load connection.
     */
    public static void deleteCheckIn(long checkInID) throws SQLException, ClassNotFoundException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(
                     "DELETE from checkin_information WHERE id = ?")) {
            stmt.setLong(1, checkInID);
            stmt.executeUpdate();
        }
    }

    /**
     * Generate a new checkin_information entry in the database
     * @param checkIn CheckInInformation model that holds the checkin data
     * @param billing BillingInformation model that holds the billing data
     * @return newID the generated primary key of the checkin entry if the transaction was completed successfully
     * @throws SQLException if improper values are passed
     * @throws ClassNotFoundException if the compiler cannot locate the relevant JDBC files
     */
    public static long addCheckIn(CheckInInformation checkIn, BillingInformation billing) throws SQLException, ClassNotFoundException {
        //Attempt to instantiate a connection and disable autocommit functionalities
        try (Connection connection = DBConnection.getConnection()) {
            connection.setAutoCommit(false);
            //Prepare relevant SQL insert statements
            try (PreparedStatement stmt1 = connection.prepareStatement(
                     "INSERT INTO billing_information \n" +
                             "VALUES(billing_information_seq.nextval, ?, ?, ?, ?, ?)",
                     new int[]{1});
             PreparedStatement stmt2 = connection.prepareStatement(
                     "INSERT INTO checkin_information VALUES(checkin_information_seq.nextval , ?, " +
                             "?, ?, ?, ?, ?, ?, ?, ?)",  new int[]{1});
            PreparedStatement getOccupants = connection.prepareStatement("SELECT * " +
                    "FROM checkin_information " +
                    "WHERE (checkout_time IS NULL OR ? < checkout_time) AND " +
                    "(? IS NULL OR checkin_time < ?) AND hotel_id = ? AND room_number = ?")) {
                getOccupants.setDate(1, checkIn.getCheckinTime());
                getOccupants.setDate(2, checkIn.getCheckoutTime());
                getOccupants.setDate(3, checkIn.getCheckoutTime());
                getOccupants.setLong(4, checkIn.getHotelId());
                getOccupants.setLong(5, checkIn.getRoomNumber());

                try (ResultSet rs = getOccupants.executeQuery()) {
                    if (rs.next()) {
                        throw new SQLException("Found a conflicting check-in");
                    }
                }

                //Populate the first prepared statement's values and then execute it
                stmt1.setString(1, billing.getBillingAddress());
                stmt1.setString(2, billing.getSsn());
                stmt1.setString(3, billing.getPaymentMethod());
                stmt1.setString(4, billing.getCardNumber());
                stmt1.setDate(5, billing.getExpirationDate());
                stmt1.executeUpdate();

                //Store primary key SQL generated for the new billing information
                long billingId = 0L;
                try (ResultSet ID = stmt1.getGeneratedKeys()) {
                    ID.next();
                    billingId = ID.getLong(1);
                }

                //Populate the second prepared statement and then execute it
                stmt2.setInt(1, checkIn.getCurrentOcupancy());
                stmt2.setDate(2, checkIn.getCheckinTime());
                stmt2.setDate(3, checkIn.getCheckoutTime());
                stmt2.setLong(4, billingId);
                stmt2.setLong(5, checkIn.getHotelId());
                stmt2.setLong(6, checkIn.getRoomNumber());
                stmt2.setLong(7, checkIn.getCustomerId());
                SQLTypeTranslater.setLongOrNull(stmt2, 8, checkIn.getCateringStaffId());
                SQLTypeTranslater.setLongOrNull(stmt2, 9, checkIn.getRoomServiceStaffId());

                stmt2.executeUpdate();

                //Check to see if the second statement went through properly. If yes, commit
                // and return the newID of the generate check in event
                try (ResultSet ID = stmt2.getGeneratedKeys()) {
                    ID.next();
                    long newId = ID.getLong(1);

                    connection.commit();
                    return newId;
                }
            // If anything fails along the line, rollback the changes to the database and throw an exception
            } catch (Exception e) {
                connection.rollback();
                throw e;
            // Always revert the status of the connection back to always auto-committing
            } finally {
                connection.setAutoCommit(true);
            }
        }
    }

    /**
     * Updates the checkin in the database with the same id as checkin to the
     * values in checkin.
     * @param checkIn the new value of the checkin, including the id of the checkin to update.
     * @throws SQLException If the query throws an exception
     * @throws ClassNotFoundException If DBConnection is cannot load connection.
     */
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
            SQLTypeTranslater.setLongOrNull(stmt, 6, checkIn.getCateringStaffId());
            SQLTypeTranslater.setLongOrNull(stmt, 7, checkIn.getRoomServiceStaffId());

            stmt.setLong(8, checkIn.getId());

            stmt.executeUpdate();
        }
    }

    /**
     * Checks out from a checkin, putting check out date to a date.
     * @param id the id of the check in to check out.
     * @param date the date of the check out.
     * @return the cost of the stay being checked out
     * @throws SQLException If the query throws an exception
     * @throws ClassNotFoundException If DBConnection is cannot load connection.
     */
    public static double checkOut(long id, Date date) throws SQLException, ClassNotFoundException {
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
                         "SELECT extract(day from (checkout_time - checkin_time)) FROM checkin_information WHERE id = ?");
                 PreparedStatement setCheckOutDate = connection.prepareStatement(
                         "UPDATE checkin_information SET checkout_time=? WHERE id=?")) {
                if (date == null) {
                    setCheckOutDate.setDate(1, new Date(new java.util.Date().getTime()));
                } else {
                    setCheckOutDate.setDate(1, date);
                }
                setCheckOutDate.setLong(2, id);
                setCheckOutDate.executeUpdate();

                getServicePrice.setLong(1, id);
                getRate.setLong(1, id);
                getTime.setLong(1, id);
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
            } finally{
                connection.setAutoCommit(true);
            }
        }
    }

    /**
     *
     * @param checkInID the checkin id to get the checkin associated with it.
     * @return the checkin associated with this check in id.
     * @throws SQLException If the query throws an exception
     * @throws ClassNotFoundException If DBConnection is cannot load connection.
     */
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

    /**
     * @return A list of all checkins.
     * @throws SQLException If the query throws an exception
     * @throws ClassNotFoundException If DBConnection is cannot load connection.
     */
    public static List<CheckInInformation> listCheckIn() throws SQLException, ClassNotFoundException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(
                     "SELECT * FROM checkin_information")) {
            stmt.executeQuery();
            return convertCheckinList(stmt.executeQuery());
        }
    }

    /**
     * @param hotelId the hotel to get the check in ins
     * @return Returns all checkins for a given hotel
     * @throws SQLException If the query throws an exception
     * @throws ClassNotFoundException If DBConnection is cannot load connection.
     */
    public static List<CheckInInformation> listCheckInByHotel(long hotelId) throws SQLException, ClassNotFoundException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(
                     "SELECT * FROM checkin_information WHERE hotel_id = ?")) {
            stmt.setLong(1, hotelId);
            stmt.executeQuery();
            return convertCheckinList(stmt.executeQuery());
        }
    }

    /**
     * @param rs the result set containing a list of check ins.
     * @return A list of checkins contained in rs
     * @throws SQLException if an exception is thrown retrieving customers from rs.
     */
    private static List<CheckInInformation> convertCheckinList(ResultSet rs) throws SQLException {
        ArrayList<CheckInInformation> toReturn = new ArrayList<CheckInInformation>();
        while(rs.next()) {
            toReturn.add(convertToCheckIn(rs));
        }
        return toReturn;
    }

    /**
     * Converts all remaining entries in rs into a list of customers.
     * @param rs the rs to get cusotmers of
     * @return a list of customers
     * @throws SQLException If an SQLException is called retrieving customers from rs.
     */
    private static List<Customer> convertCustomerList(ResultSet rs) throws SQLException {
        ArrayList<Customer> toReturn = new ArrayList<Customer>();
        while(rs.next()) {
            toReturn.add(new Customer(rs.getLong(1), rs.getString(2),
                    SQLTypeTranslater.stringToChar(rs.getString(3)), rs.getString(4),
                    rs.getString(5), rs.getString(6)));
        }
        return toReturn;
    }

    /**
     * This turns the current row in the result set to a checkin.
     * @param rs the resultset.
     * @return a checkinInformation from rs
     * @throws SQLException If rs throws an exception querying this row.
     */
    private static CheckInInformation convertToCheckIn(ResultSet rs) throws SQLException {
        return new CheckInInformation(rs.getLong(1), rs.getInt(2), rs.getDate(3), rs.getDate(4),
        rs.getLong(5), rs.getLong(6), rs.getLong(7), rs.getLong(8),
        SQLTypeTranslater.getLongOrNull(rs, 9), SQLTypeTranslater.getLongOrNull(rs, 10));
    }
}
