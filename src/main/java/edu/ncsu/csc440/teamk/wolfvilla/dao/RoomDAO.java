package edu.ncsu.csc440.teamk.wolfvilla.dao;

import edu.ncsu.csc440.teamk.wolfvilla.model.Room;
import edu.ncsu.csc440.teamk.wolfvilla.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Edward on 11/6/16.
 */
public class RoomDAO {
    public static Room getRoom(Long hotel_id, Integer roomNumber) throws SQLException, ClassNotFoundException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(
                     "SELECT * FROM rooms WHERE hotel_id = ? AND room_number = ?")) {

            stmt.setLong(1, hotel_id);
            stmt.setInt(2, roomNumber);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? convertToRoom(rs) : null;
            }
        }
    }

    public static List<Room> listRooms(Long hotel_id) throws SQLException, ClassNotFoundException {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(
                     "SELECT * FROM rooms WHERE hotel_id = ?")) {

            stmt.setLong(1, hotel_id);

            try (ResultSet rs = stmt.executeQuery()) {
                return convertRoomList(rs);
            }
        }
    }

    public static void createRoom(Room room) throws SQLException, ClassNotFoundException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(
                     "INSERT INTO rooms VALUES(?, ?, ?, ?)")) {
            stmt.setLong(1, room.getHotelId());
            stmt.setInt(2, room.getRoomNumber());
            stmt.setString(3, room.getCategoryName());
            stmt.setLong(4, room.getMaxOccupancy());
            stmt.executeUpdate();
        }
    }

    public static void updateRoom(Room room) throws SQLException, ClassNotFoundException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(
                     "UPDATE rooms SET category_name = ?, max_occupancy = ? WHERE room_number = ? AND hotel_id = ?")) {

            stmt.setString(1, room.getCategoryName());
            stmt.setLong(2, room.getMaxOccupancy());
            stmt.setInt(3, room.getRoomNumber());
            stmt.setLong(4, room.getHotelId());

            stmt.executeUpdate();
        }
    }

    public static void deleteRoom(Long hotelID, Integer roomNumber) throws SQLException, ClassNotFoundException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(
                     "DELETE FROM rooms WHERE hotel_id = ? AND room_number = ?")) {
            stmt.setLong(1, hotelID);
            stmt.setInt(2, roomNumber);
            stmt.executeUpdate();
        }
    }

    private static List<Room> convertRoomList(ResultSet rs) throws SQLException {
        ArrayList<Room> toReturn = new ArrayList<Room>();
        while(rs.next()) {
            toReturn.add(convertToRoom(rs));
        }
        return toReturn;
    }

    private static Room convertToRoom(ResultSet rs) throws SQLException {
        return new Room(rs.getLong(1), rs.getInt(2), rs.getString(3), rs.getInt(4));
    }
}
