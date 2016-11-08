package edu.ncsu.csc440.teamk.wolfvilla.util;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class SQLTypeTranslater {
    public static Character stringToChar(String s) {
        if (s == null || s.length() < 1) {
            return null;
        }
        return s.charAt(0);
    }
    public static String charToString(Character c) {
        if (c == null) {
            return null;
        }
        return c.toString();
    }

    public static void setIntOrNull(PreparedStatement ps, int index, Integer value) throws SQLException {
        if (value == null) {
            ps.setNull(index, Types.INTEGER);
        }
        else ps.setInt(index, value);
    }

    public static void setLongOrNull(PreparedStatement ps, int index, Long value) throws SQLException {
        if (value == null) {
            ps.setNull(index, Types.INTEGER);
        }
        else ps.setLong(index, value);
    }

    public static Long getLongOrNull(ResultSet rs, int index) throws SQLException {
        Long l = rs.getLong(index);
        return rs.wasNull() ? null : l;
    }

    public static Integer getIntOrNull(ResultSet rs, int index) throws SQLException {
        Integer i = rs.getInt(index);
        return rs.wasNull() ? null : i;
    }
}
