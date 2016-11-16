package edu.ncsu.csc440.teamk.wolfvilla.dao;

import edu.ncsu.csc440.teamk.wolfvilla.model.Room;
import edu.ncsu.csc440.teamk.wolfvilla.model.RoomCategory;
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
    /**
     * @param hotel_id the id of the hotel containing the room to retrieve.
     * @param roomNumber the number of the room to retrieve.
     * @return The room with the given roomNumber in the hotel corresponding to the given hotel_id.
     * @throws SQLException If the query throws an exception
     * @throws ClassNotFoundException If DBConnection is cannot load connection.
     */
    public static Room getRoom(Long hotel_id, Integer roomNumber) throws SQLException, ClassNotFoundException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(
                     "SELECT * FROM rooms r, room_categories c WHERE r.max_occupancy = c.max_occupancy AND " +
                             "r.category_name = c.category_name AND hotel_id = ? AND room_number = ?")) {

            stmt.setLong(1, hotel_id);
            stmt.setInt(2, roomNumber);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? convertToRoom(rs) : null;
            }
        }
    }

    /**
     * Lists all rooms in the given hotel.
     * @param hotel_id the id of the hotel to get rooms from.
     * @return the list of all rooms in the hotel corresponding to the given id.
     * @throws SQLException If the query throws an exception
     * @throws ClassNotFoundException If DBConnection is cannot load connection.
     */
    public static List<Room> listRooms(Long hotel_id) throws SQLException, ClassNotFoundException {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(
                     "SELECT * FROM rooms r, room_categories c WHERE r.max_occupancy = c.max_occupancy AND " +
                             "r.category_name = c.category_name AND hotel_id = ?")) {

            stmt.setLong(1, hotel_id);

            try (ResultSet rs = stmt.executeQuery()) {
                return convertRoomList(rs);
            }
        }
    }

    /**
     * Stores the given room in the database, in the given hotel with the given room number.
     * @param room The room to store, including hotel and room number.
     * @throws SQLException If the query throws an exception
     * @throws ClassNotFoundException If DBConnection is cannot load connection.
     */
    public static void createRoom(Room room) throws SQLException, ClassNotFoundException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(
                     "INSERT INTO rooms VALUES(?, ?, ?, ?)")) {
            stmt.setLong(1, room.getHotelId());
            stmt.setInt(2, room.getRoomNumber());
            stmt.setString(3, room.getCategory().getCategoryName());
            stmt.setLong(4, room.getCategory().getMaxOccupancy());
            stmt.executeUpdate();
        }
    }

    /**
     * Updates the room with the same hotel and room number as the given room to the
     * same category as the given room.
     * @param room The room containing the new category, and the room number and hotelid to update.
     * @throws SQLException If the query throws an exception
     * @throws ClassNotFoundException If DBConnection is cannot load connection.
     */
    public static void updateRoom(Room room) throws SQLException, ClassNotFoundException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(
                     "UPDATE rooms SET category_name = ?, max_occupancy = ? WHERE room_number = ? AND hotel_id = ?")) {

            stmt.setString(1, room.getCategory().getCategoryName());
            stmt.setLong(2, room.getCategory().getMaxOccupancy());
            stmt.setInt(3, room.getRoomNumber());
            stmt.setLong(4, room.getHotelId());

            stmt.executeUpdate();
        }
    }

    /**
     * Deletes the room in the given hotel with the given roomNumber
     * @param hotelID the id of the hotel containing the room.
     * @param roomNumber The number of the room to delete.
     * @throws SQLException If the query throws an exception
     * @throws ClassNotFoundException If DBConnection is cannot load connection.
     */
    public static void deleteRoom(Long hotelID, Integer roomNumber) throws SQLException, ClassNotFoundException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(
                     "DELETE FROM rooms WHERE hotel_id = ? AND room_number = ?")) {
            stmt.setLong(1, hotelID);
            stmt.setInt(2, roomNumber);
            stmt.executeUpdate();
        }
    }

    /**
     * @param rs the result set to get rooms from.
     * @return the list of rooms in this result set.
     * @throws SQLException If the query throws an exception
     */
    public static List<Room> convertRoomList(ResultSet rs) throws SQLException {
        ArrayList<Room> toReturn = new ArrayList<Room>();
        while(rs.next()) {
            toReturn.add(convertToRoom(rs));
        }
        return toReturn;
    }

    /**
     * Converts the current row of a result set to a room.
     * @param rs the resultset to get a room from.
     * @return a Room from the current row in a result set.
     * @throws SQLException If the query throws an exception
     */
    private static Room convertToRoom(ResultSet rs) throws SQLException {
        RoomCategory cat = new RoomCategory(rs.getString(5), rs.getInt(6), rs.getDouble(7));
        return new Room(rs.getLong(1), rs.getInt(2), cat);
    }
}
