package edu.ncsu.csc440.teamk.wolfvilla.util;

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
}
