package edu.ncsu.csc440.teamk.wolfvilla.dao;

import edu.ncsu.csc440.teamk.wolfvilla.model.Hotel;
import edu.ncsu.csc440.teamk.wolfvilla.util.DBConnection;

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
    public static long createHotel (Hotel hotel) throws SQLException, ClassNotFoundException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(
                     "INSERT INTO hotels VALUES(hotel_seq.nextval, ?, ?, ?)",  new int[]{1})) {
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

    public static void deleteHotel(long hotelID) throws SQLException, ClassNotFoundException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement("DELETE FROM hotels WHERE id = ?")) {
            stmt.setLong(1, hotelID);
            stmt.executeUpdate();
        }
    }

    public static void updateHotel (Hotel hotel) throws SQLException, ClassNotFoundException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(
                     "UPDATE hotels SET address = ?, name = ?, phone_number = ? WHERE id = ?")) {
            stmt.setString(1, hotel.getAddress());
            stmt.setString(2, hotel.getName());
            stmt.setString(3, hotel.getPhoneNumber());
            stmt.setLong(4, hotel.getPrimaryKey());
            stmt.executeUpdate();
        }
    }

    public static List<Hotel> getHotels() throws SQLException, ClassNotFoundException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement("SELECT * FROM hotels");
             ResultSet rs = stmt.executeQuery()) {
            return convertHotelList(rs);
        }
    }

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

    private static List<Hotel> convertHotelList(ResultSet rs) throws SQLException {
        ArrayList<Hotel> toReturn = new ArrayList<Hotel>();
        while(rs.next()) {
            toReturn.add(convertToHotel(rs));
        }
        return toReturn;
    }

    private static Hotel convertToHotel(ResultSet rs) throws SQLException {
        return new Hotel(rs.getLong(1), rs.getString(2), rs.getString(3), rs.getString(4));
    }
}
