package edu.ncsu.csc440.teamk.wolfvilla.dao;

import edu.ncsu.csc440.teamk.wolfvilla.model.Room;
import edu.ncsu.csc440.teamk.wolfvilla.model.Customer;
import edu.ncsu.csc440.teamk.wolfvilla.model.Staff;
import edu.ncsu.csc440.teamk.wolfvilla.util.DBConnection;
import edu.ncsu.csc440.teamk.wolfvilla.util.SQLTypeTranslater;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Date;

import static edu.ncsu.csc440.teamk.wolfvilla.dao.StaffDAO.convertToStaff;

/**
 * Created by Adam on 10/31/2016.
 */
public class ReportDAO {
    /**
     * @param hotelId hotel id
     * @return report all available hotels based on hotel id
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public static List<Room> reportAllAvailable(long hotelId) throws SQLException, ClassNotFoundException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(
                     "SELECT *\n" +
                     "FROM rooms \n" +
                     "WHERE hotel_id = ? AND room_number NOT IN (SELECT room_number \n" +
                     "FROM checkin_information \n" +
                     "WHERE checkin_information.hotel_id = hotel_id AND checkout_time IS NULL)")){

            stmt.setLong(1, hotelId);
            ResultSet rs = stmt.executeQuery();
            return convertRoomList(rs);
        }
    }

    /**
     * @param category room category
     * @param startDate checkin start date
     * @param endDate checkin end date
     * @param hotelId hotel id
     * @param occupants number of occupants
     * @return all available rooms based on the given parameters
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public static List<Room> reportAvailable(String category, Date startDate, Date endDate, long hotelId, int occupants) throws SQLException, ClassNotFoundException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement("SELECT *\n" +
                     "FROM rooms \n" +
                     "WHERE hotel_id = ? AND max_occupancy >= ? AND category_name = ? AND room_number NOT IN (SELECT room_number \n" +
                     "FROM checkin_information \n" +
                     "WHERE checkin_information.hotel_id = hotel_id AND ( ? < checkin_time OR checkout_time IS NULL) AND checkin_time < ?)")){

            stmt.setLong(1, hotelId);
            stmt.setInt(2, occupants);
            stmt.setString(3, category);
            stmt.setDate(4, startDate);
            stmt.setDate(5, endDate);
            ResultSet rs = stmt.executeQuery();
            return convertRoomList(rs); //TODO: Return list of objects, see HotelMethod
        }
    }

    /**
     * @param startDate start date of checkin
     * @param endDate end date of checkin
     * @param hotelId hotel id
     * @return list of room numbers that are occupied
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public static List<Integer> reportOccupied(Date startDate, Date endDate, long hotelId) throws SQLException, ClassNotFoundException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement("SELECT room_number \n" +
                     "FROM checkin_information \n" +
                     "WHERE (? < checkout_time OR checkout_time IS NULL) AND TIMESTAMP '2017-01-03  00:00:00' > checkin_time AND hotel_id = ?")){
            stmt.setDate(1, startDate);
            //stmt.setDate(2, endDate);
            stmt.setLong(2, hotelId);
            ResultSet rs = stmt.executeQuery();

            ArrayList<Integer> toReturn = new ArrayList<Integer>();
            while(rs.next()) {
                toReturn.add(rs.getInt(1));
                //System.out.println(toReturn.get(toReturn.size() - 1));
            }
            return toReturn;
        }
    }

    /**
     * @param startDate start date of checkin
     * @param endDate end date of checkin
     * @param hotelId hotel id
     * @return list of rooms that are unoccupied
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public static List<Room> reportUnoccupanied(Date startDate, Date endDate, long hotelId) throws SQLException, ClassNotFoundException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement("SELECT *\n" +
                     "FROM rooms \n" +
                     "WHERE hotel_id = ? AND room_number NOT IN (SELECT room_number \n" +
                     "FROM checkin_information \n" +
                     "WHERE checkin_information.hotel_id = hotel_id AND ( ? < checkout_time OR checkout_time IS NULL) " +
                     "AND checkin_time <  ?)")){

            stmt.setLong(1, hotelId);
            stmt.setDate(2, startDate);
            stmt.setDate(3, endDate);
            ResultSet rs = stmt.executeQuery();
            return convertRoomList(rs); //TODO: Return list of objects, see HotelMethod
        }
    }

    /**
     * @param startDate start date of checkin
     * @param endDate end date of checkin
     * @param hotelId hotel id
     * @return list of customers occupying rooms based on give hotel and time range
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public static List<Customer> reportOccupants(Date startDate, Date endDate, long hotelId) throws SQLException, ClassNotFoundException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement("SELECT * \n" +
                     "FROM checkin_information \n" +
                     "WHERE (checkout_time IS NULL OR  ? < checkout_time) " +
                     "AND checkin_time <  ? AND hotel_id = ?")){

            stmt.setDate(1, startDate);
            stmt.setDate(2, endDate);
            stmt.setLong(3, hotelId);
            ResultSet rs = stmt.executeQuery();
            return convertCustomerList(rs); //TODO: Return list of objects, see HotelMethod
        }
    }

    /**
     * @param startDate start date of checkin
     * @param endDate end date of checkin
     * @param hotelId hotel id
     * @return percentage of rooms occupied in the hotel
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    //TODO: Make transaction
    public static double percentOccupied(Date startDate, Date endDate, long hotelId) throws SQLException, ClassNotFoundException {
        int occupiedRooms;
        int totalRooms = 1;

        try (Connection connection = DBConnection.getConnection();
            PreparedStatement stmt1 = connection.prepareStatement("(SELECT count( DISTINCT room_number) AS room_count \n" +
                            "FROM checkin_information \n" +
                            "WHERE (checkout_time IS NULL OR ? < checkout_time) AND " +
                            "checkin_time < ? AND checkin_information.hotel_id = ?)")){

            stmt1.setDate(1, startDate);
            stmt1.setDate(2, endDate);
            stmt1.setLong(3, hotelId);
            ResultSet rs = stmt1.executeQuery();

            rs.next();
            occupiedRooms = rs.getInt(1);
            //System.out.println(occupiedRooms);
        }

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt2 = connection.prepareStatement("SELECT count(DISTINCT room_number) AS room_count\n" +
                     "FROM rooms\n" +
                     "WHERE hotel_id = ?")){

            stmt2.setLong(1, hotelId);
            ResultSet rs = stmt2.executeQuery();

            rs.next();
            totalRooms = rs.getInt(1);
            //System.out.println(totalRooms);
        }

        return occupiedRooms / (totalRooms + 0.0);
    }

    /**
     * @param jobTitle job title of staff
     * @return list of staff with the given title
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public static List<Staff> getStaffByRole(String jobTitle) throws SQLException, ClassNotFoundException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement("SELECT *\n" +
                     "FROM staff, title_department\n" +
                             "WHERE title_department.title = ? AND staff.title = ?")){

            stmt.setString(1, jobTitle);
            stmt.setString(2, jobTitle);
            //stmt.setLong(3, hotelId);
            ResultSet rs = stmt.executeQuery();
            return convertStaffList(rs);
        }
    }

    /**
     * @param jobTitle job title of staff
     * @param hotelId hotel id
     * @return list of staff by job title and hotel id
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public static List<Staff> getHotelStaffByRole(String jobTitle, long hotelId) throws SQLException, ClassNotFoundException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement("SELECT *\n" +
                     "FROM staff, title_department\n" +
                     "WHERE title_department.title = ? AND staff.title = ? AND hotel_id = ?")){

            stmt.setString(1, jobTitle);
            stmt.setString(2, jobTitle);
            stmt.setLong(3, hotelId);
            ResultSet rs = stmt.executeQuery();
            return convertStaffList(rs);
        }
    }

    /**
     * @param staffId id of staff
     * @return list of unique customers served by the staff
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public static List<Customer> getCustomersOfStaff(long staffId) throws SQLException, ClassNotFoundException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement("SELECT *\n" +
                     "FROM CUSTOMERS\n" +
                     "WHERE id IN(SELECT DISTINCT customer_id\n" +
                     "FROM services, checkin_information \n" +
                     "WHERE staff_id = ? AND checkin_id = checkin_information.id)")){

            stmt.setLong(1, staffId);
            ResultSet rs = stmt.executeQuery();
            return convertCustomerList(rs); //TODO: Return list of objects, see HotelMethod
        }
    }

    /**
     * @param rs result set containing list of room queries
     * @return List of room objects
     * @throws SQLException
     */
    private static List<Room> convertRoomList(ResultSet rs) throws SQLException {
        ArrayList<Room> toReturn = new ArrayList<Room>();
        while(rs.next()) {
            toReturn.add(convertToRoom(rs));
        }
        return toReturn;
    }

    /**
     * @param rs result set containing list of room queries
     * @return room object
     * @throws SQLException
     */
    private static Room convertToRoom(ResultSet rs) throws SQLException {
        return new Room(rs.getLong(1), (Integer)rs.getInt(2), rs.getString(3), rs.getInt(4));
    }

    /**
     * @param rs Result set containing list of customer queries
     * @return list of customer objects
     * @throws SQLException
     */
    private static List<Customer> convertCustomerList(ResultSet rs) throws SQLException {
        ArrayList<Customer> toReturn = new ArrayList<Customer>();
        while(rs.next()) {
            toReturn.add(convertToCustomer(rs));
        }
        return toReturn;
    }

    /**
     * @param rs result set containing list of customer queries
     * @return customer object
     * @throws SQLException
     */
    private static Customer convertToCustomer(ResultSet rs) throws SQLException {
        return new Customer(rs.getLong(1), rs.getString(2), SQLTypeTranslater.stringToChar(rs.getString(3)), rs.getString(4),
                rs.getString(5), rs.getString(6));
    }

    /**
     * @param rs result set containing list of staff queries
     * @return list of staff objects
     * @throws SQLException
     */
    private static List<Staff> convertStaffList(ResultSet rs) throws SQLException {
        ArrayList<Staff> toReturn = new ArrayList<Staff>();
        while(rs.next()) {
            toReturn.add(convertToStaff(rs));
        }
        return toReturn;
    }
}
