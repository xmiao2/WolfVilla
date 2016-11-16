package edu.ncsu.csc440.teamk.wolfvilla.util;

import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
/**
 * This class contains a consolidated set of methods to handle
 * translating the results taken from the database into something more easily handled by Java.
 */
public class SQLTypeTranslater {
    /**
     * Since JDBC does not have a function for getting individual chars, this safeky converts
     * a string from SQL to its first char, or null if string is null.
     * @param s the string to convert char
     * @return the first character of s, or null
     */
    public static Character stringToChar(String s) {
        if (s == null || s.length() < 1) {
            return null;
        }
        return s.charAt(0);
    }

    /**
     * Converts a single char to a string so we can save it to the database
     * @param c the character to return as a string
     * @return a string containing the single character, c, or null if c is null.
     */
    public static String charToString(Character c) {
        if (c == null) {
            return null;
        }
        return c.toString();
    }

    /**
     * safely sets an int in a prepared stament, meaning it sets it to value if value != null, and to null otherwise.
     * @param ps the prepared statement to insert value into.
     * @param index the index of the prepared statement to set.
     * @param value the value to insert.
     * @throws SQLException If an exception is thrown inserting the value into ps.
     */
    public static void setIntOrNull(PreparedStatement ps, int index, Integer value) throws SQLException {
        if (value == null) {
            ps.setNull(index, Types.INTEGER);
        }
        else ps.setInt(index, value);
    }

    /**
     * safely sets a long in a prepared stament, meaning it sets it to value if value != null, and to null otherwise.
     * @param ps the prepared statement to insert value into.
     * @param index the index of the prepared statement to set.
     * @param value the value to insert.
     * @throws SQLException If an exception is thrown inserting the value into ps.
     */
    public static void setLongOrNull(PreparedStatement ps, int index, Long value) throws SQLException {
        if (value == null) {
            ps.setNull(index, Types.INTEGER);
        }
        else ps.setLong(index, value);
    }

    /**
     * safely sets a date in a prepared stament, meaning it sets it to value if value != null, and to null otherwise.
     * @param ps the prepared statement to insert value into.
     * @param index the index of the prepared statement to set.
     * @param value the date to insert.
     * @throws SQLException If an exception is thrown inserting the value into ps.
     */
    public static void setDateOrNull(PreparedStatement ps, int index, Date value) throws SQLException {
        if (value == null) {
            ps.setNull(index, Types.INTEGER);
        }
        else ps.setDate(index, value);
    }

    /**
     * Safely retrieves a long from a resultset, meaning it returns a null if the value is null in
     * the result set, and returns the long otherwise.
     * @param rs the result set to get data from.
     * @param index the index of the data to retrieve.
     * @return the long in the resultset, or null if none.
     * @throws SQLException
     */
    public static Long getLongOrNull(ResultSet rs, int index) throws SQLException {
        Long l = rs.getLong(index);
        return rs.wasNull() ? null : l;
    }
    /**
     * Safely retrieves an int from a resultset, meaning it returns a null if the value is null in
     * the result set, and returns the int otherwise.
     * @param rs the result set to get data from.
     * @param index the index of the data to retrieve.
     * @return the long in the resultset, or null if none.
     * @throws SQLException
     */
    public static Integer getIntOrNull(ResultSet rs, int index) throws SQLException {
        Integer i = rs.getInt(index);
        return rs.wasNull() ? null : i;
    }

    /**
     * THis one is actually not for SQL, but did not deserve its own file.
     *
     * This takes a string as an input from a web form, and converts it to a date.
     * This is necessary because thymeleaf cannot handle null dates from a form.
     * @param input the date string to convert to a date object.
     * @return a date with the value represented by input string.
     * @throws ParseException if the string is not null, but not in YEAR-MM-DD format.
     */
    public static Date stringToDate(String input) throws ParseException {
        if (input == null || input.equals("")) {
            return null;
        }
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return new Date(df.parse(input).getTime());
    }
}
