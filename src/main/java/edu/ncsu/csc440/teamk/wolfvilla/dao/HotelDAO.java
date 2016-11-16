package edu.ncsu.csc440.teamk.wolfvilla.dao;

import edu.ncsu.csc440.teamk.wolfvilla.model.Hotel;
import edu.ncsu.csc440.teamk.wolfvilla.util.DBConnection;
import edu.ncsu.csc440.teamk.wolfvilla.util.SQLTypeTranslater;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joshua on 10/25/2016.
 */
public class HotelDAO {

    /**
     * Assign a manager to hotel.
     *
     * @param hotelId id of hotel
     * @param managerId manager id to assign the hotel
     * @throws SQLException If an error occurs setting this hotels manager to managerId
     * @throws ClassNotFoundException If the JDBC oracle jar cannot be loaded by DBConnection
     */
    public static void assignHotelManager(long hotelId, long managerId) throws SQLException, ClassNotFoundException {
        try (Connection connection = DBConnection.getConnection()) {
            connection.setAutoCommit(false);
            //Prepare relevant SQL insert statements
            try (PreparedStatement removeFromOldHotel = connection.prepareStatement(
                    "UPDATE hotels SET manager = NULL WHERE manager = ?");
                 PreparedStatement addToNewHotel = connection.prepareStatement(
                         "UPDATE hotels SET manager = ? WHERE id = ?")) {
                //remove the manager from his last hotel, if he had one.
                removeFromOldHotel.setLong(1, managerId);
                removeFromOldHotel.executeUpdate();
                //Add the manager to his new hotel.
                addToNewHotel.setLong(1, managerId);
                addToNewHotel.setLong(2, hotelId);
                addToNewHotel.executeUpdate();
                //If we got here, everything worked, commit.
                connection.commit();
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
     * Add a hotel tuple in the database.
     *
     * @param hotel hotel to add
     * @return generated id of the hotel
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public static long createHotel (Hotel hotel) throws SQLException, ClassNotFoundException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(
                     "INSERT INTO hotels VALUES(hotel_seq.nextval, null, ?, ?, ?)",  new int[]{1})) {
            stmt.setString(1, hotel.getAddress());
            stmt.setString(2, hotel.getName());
            stmt.setString(3, hotel.getPhoneNumber());

            stmt.executeUpdate();
            try (ResultSet ID = stmt.getGeneratedKeys()) {
                ID.next();
                return ID.getLong(1);
            }
        }
    }

    /**
     * Delete a hotel based on id.
     *
     * @param hotelID hotel id
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public static void deleteHotel(long hotelID) throws SQLException, ClassNotFoundException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement("DELETE FROM hotels WHERE id = ?")) {
            stmt.setLong(1, hotelID);
            stmt.executeUpdate();
        }
    }

    /**
     * Update a hotel based on given object in database.
     *
     * @param hotel hotel to modify
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public static void updateHotel (Hotel hotel) throws SQLException, ClassNotFoundException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(
                     "UPDATE hotels SET address = ?, name = ?, phone_number = ? WHERE id = ?")) {
            stmt.setString(1, hotel.getAddress());
            stmt.setString(2, hotel.getName());
            stmt.setString(3, hotel.getPhoneNumber());
            stmt.setLong(4, hotel.getId());
            stmt.executeUpdate();
        }
    }

    /**
     * @return list of hotel in the database
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public static List<Hotel> getHotels() throws SQLException, ClassNotFoundException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement("SELECT * FROM hotels");
             ResultSet rs = stmt.executeQuery()) {
            return convertHotelList(rs);
        }
    }

    /**
     * @param id id of hotel
     * @return hotel based on given id
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public static Hotel getHotelById(long id) throws SQLException, ClassNotFoundException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement("SELECT * FROM hotels WHERE id = ?");) {
            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                rs.next();
                return convertToHotel(rs);
            }
        }
    }

    /**
     * @param rs result set containing queries of Hotels
     * @return List of hotels based on result set
     * @throws SQLException
     */
    private static List<Hotel> convertHotelList(ResultSet rs) throws SQLException {
        ArrayList<Hotel> toReturn = new ArrayList<Hotel>();
        while(rs.next()) {
            toReturn.add(convertToHotel(rs));
        }
        return toReturn;
    }

    /**
     * @param rs result set containing queries of hotels
     * @return Hotel based on result set
     * @throws SQLException
     */
    private static Hotel convertToHotel(ResultSet rs) throws SQLException {
        return new Hotel(rs.getLong(1), SQLTypeTranslater.getLongOrNull(rs, 2), rs.getString(3), rs.getString(4), rs.getString(5));
    }
}
