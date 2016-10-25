package edu.ncsu.csc440.teamk.wolfvilla.dao;

import edu.ncsu.csc440.teamk.wolfvilla.model.Staff;
import edu.ncsu.csc440.teamk.wolfvilla.util.DBConnection;

import java.sql.*;

/**
 * Created by xm on 10/24/16.
 */
public class StaffDAO {
    private static Character stringToChar(String s) {
        if (s == null || s.length() < 1) {
            return null;
        }
        return s.charAt(0);
    }
    private static String charToString(Character c) {
        if (c == null) {
            return null;
        }
        return c.toString();
    }

    public static long addStaff(Staff staff) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getConnection();


        PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO staff \n" +
                        "VALUES (staff_seq.nextval, ?, ?, ?, ?, ?, ?, ?, ?)",  new int[]{1});
        stmt.setString(1,staff.getName());
        stmt.setString(2,staff.getTitle());
        stmt.setString(3,staff.getSsn());
        stmt.setInt(4,staff.getAge());
        stmt.setString(5, charToString(staff.getGender()));
        stmt.setString(6, staff.getAddress());
        stmt.setString(7, staff.getPhoneNumber());
        stmt.setLong(8, staff.getHotelId());

        stmt.executeUpdate();
        ResultSet ID = stmt.getGeneratedKeys();
        ID.next();
        return ID.getLong(1);
    }

    public static Staff retrieveStaff(long staffID) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getConnection();


        PreparedStatement stmt = connection.prepareStatement("Select * From staff WHERE id = ?");
        stmt.setLong(1,staffID);
        ResultSet rs = stmt.executeQuery();
        rs.next();
        return convertToStaff(rs);
    }

    private static Staff convertToStaff(ResultSet rs) throws SQLException {
        return new Staff(rs.getLong(1), rs.getString(2), rs.getString(3),
                rs.getString(4), rs.getInt(5), stringToChar(rs.getString(6)),
                rs.getString(7), rs.getString(8), rs.getLong(9));
    }
}
