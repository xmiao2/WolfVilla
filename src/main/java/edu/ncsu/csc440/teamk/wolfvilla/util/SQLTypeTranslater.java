package edu.ncsu.csc440.teamk.wolfvilla.util;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * Created by Joshua on 10/25/2016.
 */
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

    public static void safeIntSet(PreparedStatement ps, int index, Long value) throws SQLException {
        if (value == null) ps.setNull(index, Types.INTEGER);
        else ps.setLong(index, value);
    }

    public static Long safeGetLong(ResultSet rs, int index) throws SQLException {
        long l = rs.getLong(index);
        if (rs.wasNull()) {
            return null;
        }
        return l;
    }
}
