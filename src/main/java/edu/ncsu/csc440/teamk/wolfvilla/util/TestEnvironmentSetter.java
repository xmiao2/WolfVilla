package edu.ncsu.csc440.teamk.wolfvilla.util;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xm on 10/18/16.
 */
public class TestEnvironmentSetter {
    public static void main(String[] args) throws Exception {
        setUp();
    }
    public static void setUp() throws Exception {

        TestEnvironmentSetter sqlExecutor = new TestEnvironmentSetter();
        List<String> queries = sqlExecutor.parseSQLFile("sql/createTables.sql");

        Connection conn = DBConnection.getConnection();

        sqlExecutor.executeSQL(conn, queries);
    }

    public void executeSQL(Connection conn, List<String> queries) throws SQLException {
        long start = System.currentTimeMillis();
        Statement stmt = conn.createStatement();
        for (String sql : queries) {
            try {
                stmt.execute(sql);
            } catch (SQLException e) {
                System.out.println(e.getMessage() + " from executing: " + sql);
            }
        }
        stmt.close();
    }

    private List<String> parseSQLFile(String filepath) throws IOException {
        List<String> queries = new ArrayList<String>();

        BufferedReader reader = null;
        FileReader fileReader = null;
        try{

            fileReader = new FileReader(new File(filepath));
            reader =new BufferedReader(fileReader);
            String line = "";
            String currentQuery = "";
            while ((line = reader.readLine()) != null) {
                for (int i = 0; i < line.length(); i++) {
                    if (line.charAt(i) == ';') {
                        queries.add(currentQuery);
                        currentQuery = "";
                    } else
                        currentQuery += line.charAt(i);
                }
            }
        }
        finally{
            try{
                if(!(fileReader == null)) fileReader.close();
            }
            finally{
                if(!(reader==null)) reader.close();
            }
        }
        return queries;
    }

}
