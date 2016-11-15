package edu.ncsu.csc440.teamk.wolfvilla.dao;

import edu.ncsu.csc440.teamk.wolfvilla.model.Staff;
import edu.ncsu.csc440.teamk.wolfvilla.model.TitleDepartment;
import edu.ncsu.csc440.teamk.wolfvilla.util.DBConnection;
import edu.ncsu.csc440.teamk.wolfvilla.util.SQLTypeTranslater;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static edu.ncsu.csc440.teamk.wolfvilla.util.SQLTypeTranslater.getIntOrNull;

/**
 * Created by xm on 10/24/16.
 */
public class StaffDAO {

    /**
     * Updates the staff based on the given object.
     * @param staff staff object to update
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public static void updateStaff(Staff staff) throws SQLException, ClassNotFoundException {
        try (Connection connection = DBConnection.getConnection();
                PreparedStatement stmt = connection.prepareStatement(
                "UPDATE staff SET name=?, title=?, ssn=?, age=?, gender=?, address=?, phone_number=?, hotel_id=? " +
                        "WHERE id = ?")) {
            stmt.setString(1, staff.getName());
            stmt.setString(2, staff.getTitleDepartment().getTitle());
            stmt.setString(3, staff.getSsn());
            SQLTypeTranslater.setIntOrNull(stmt, 4, staff.getAge());
            stmt.setString(5, SQLTypeTranslater.charToString(staff.getGender()));
            stmt.setString(6, staff.getAddress());
            stmt.setString(7, staff.getPhoneNumber());
            stmt.setLong(8, staff.getHotelId());
            stmt.setLong(9, staff.getId());

            stmt.executeUpdate();
        }
    }

    /**
     * Adds the given staff to the database.
     * @param staff staff to add
     * @return generated id of the staff
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public static long addStaff(Staff staff) throws SQLException, ClassNotFoundException {
        try (Connection connection = DBConnection.getConnection();
                PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO staff VALUES (staff_seq.nextval, ?, ?, ?, ?, ?, ?, ?, ?)",  new int[]{1})) {
            stmt.setString(1, staff.getName());
            stmt.setString(2, staff.getTitleDepartment().getTitle());
            stmt.setString(3, staff.getSsn());
            SQLTypeTranslater.setIntOrNull(stmt, 4, staff.getAge());
            stmt.setString(5, SQLTypeTranslater.charToString(staff.getGender()));
            stmt.setString(6, staff.getAddress());
            stmt.setString(7, staff.getPhoneNumber());
            stmt.setLong(8, staff.getHotelId());

            stmt.executeUpdate();
            try (ResultSet ID = stmt.getGeneratedKeys()) {
                ID.next();
                return ID.getLong(1);
            }
        }
    }

    /**
     * Gets the staff based on given id from database.
     * @param staffID id of the staff to retrieve
     * @return Staff object
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public static Staff retrieveStaff(long staffID) throws SQLException, ClassNotFoundException {
        try (Connection connection = DBConnection.getConnection();
                PreparedStatement stmt = connection.prepareStatement("Select * From staff s, title_department t WHERE id = ? AND s.title = t.title") ) {
            stmt.setLong(1, staffID);

            try (ResultSet rs = stmt.executeQuery()) {
                rs.next();
                return convertToStaff(rs);
            }
        }
    }

    /**
     * Deletes the staff of the given ID from database.
     * @param staffID id of staff
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public static void deleteStaff(long staffID) throws SQLException, ClassNotFoundException {
        try (Connection connection = DBConnection.getConnection();
                 PreparedStatement stmt = connection.prepareStatement("DELETE FROM staff WHERE id = ?")) {
            stmt.setLong(1, staffID);
            stmt.executeUpdate();
        }
    }

    /**
     * Gets all staff from the database.
     * @return a list of Staff objects
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public static List<Staff> retrieveAllStaff() throws SQLException, ClassNotFoundException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement("SELECT * FROM staff s, title_department t WHERE s.title = t.title")) {
            try (ResultSet rs = stmt.executeQuery()) {
                return convertStaffList(rs);
            }
        }
    }

    /**
     * Gets all staff from the database based on given hotel id.
     * @param hotelID a list of Staff objects belonging to a hotel
     * @return hotel id
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public static List<Staff> retrieveStaffByHotel(long hotelID) throws SQLException, ClassNotFoundException {
        try (Connection connection = DBConnection.getConnection();
                PreparedStatement stmt = connection.prepareStatement("SELECT * FROM staff s, title_department t WHERE hotel_id = ? AND s.title = t.title")) {
            stmt.setLong(1, hotelID);
            try (ResultSet rs = stmt.executeQuery()) {
                return convertStaffList(rs);
            }
        }
    }

    /**
     * @param hotelID hotel id
     * @param title title of staff
     * @return all the staff from database based on given hotel id and title name
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public static List<Staff> retrieveStaffByTitle(long hotelID, String title) throws SQLException, ClassNotFoundException {
        try (Connection connection = DBConnection.getConnection();
                PreparedStatement stmt = connection.prepareStatement("SELECT * FROM staff s, title_department t WHERE hotel_id = ? AND title = ? AND s.title = t.title")) {
            stmt.setLong(1, hotelID);
            stmt.setString(2, title);
            try (ResultSet rs = stmt.executeQuery()) {
                return convertStaffList(rs);
            }
        }
    }

    /**
     * Converts result set to Staff object.
     * @param rs result set containing staff queries
     * @return list of staff by result set
     * @throws SQLException
     */
    private static List<Staff> convertStaffList(ResultSet rs) throws SQLException {
        ArrayList<Staff> toReturn = new ArrayList<Staff>();
        while(rs.next()) {
            toReturn.add(convertToStaff(rs));
        }
        return toReturn;
    }

    /**
     * @param rs result set containing staff queries
     * @return staff object based on result set
     * @throws SQLException
     */
    public static Staff convertToStaff(ResultSet rs) throws SQLException {
        TitleDepartment titleDepartment = new TitleDepartment(rs.getString(3), rs.getString(11));
        return new Staff(rs.getLong(1), rs.getString(2), titleDepartment,
                rs.getString(4), getIntOrNull(rs, 5), SQLTypeTranslater.stringToChar(rs.getString(6)),
                rs.getString(7), rs.getString(8), rs.getLong(9));
    }
}
