package edu.ncsu.csc440.teamk.wolfvilla.dao;

import edu.ncsu.csc440.teamk.wolfvilla.model.RoomCategory;
import edu.ncsu.csc440.teamk.wolfvilla.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by Edward on 11/6/16.
 */
public class RoomCategoryDAO {

    public static void createRoomCategory(RoomCategory roomCategory) throws SQLException, ClassNotFoundException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(
                     "INSERT INTO room_categories\n" +
                             "VALUES (?, ?, ?)")) {

            stmt.setDouble(1, roomCategory.getNightlyRate());
            stmt.setString(2, roomCategory.getCategoryName());
            stmt.setInt(3, roomCategory.getMaxOccupancy());

            stmt.executeUpdate();
        }
    }

    public static void updateRoomCategory(RoomCategory roomCategory) throws SQLException, ClassNotFoundException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(
                     "UPDATE room_categories\n" +
                             "SET nightly_rate = ?\n" +
                             "WHERE category_name = ? AND max_occupancy = ?")) {

             stmt.setDouble(1, roomCategory.getNightlyRate());
             stmt.setString(2, roomCategory.getCategoryName());
             stmt.setInt(3, roomCategory.getMaxOccupancy());

            stmt.executeUpdate();
        }
    }

    public static void deleteRoomCategory(RoomCategory roomCategory) throws SQLException, ClassNotFoundException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(
                     "DELETE FROM room_categories\n" +
                             "WHERE category_name = ? AND max_occupancy = ?")) {

            stmt.setString(1, roomCategory.getCategoryName());
            stmt.setInt(2, roomCategory.getMaxOccupancy());

            stmt.executeUpdate();
        }
    }
}
