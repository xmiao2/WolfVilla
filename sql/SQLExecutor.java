import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xm on 10/18/16.
 */
public class SQLExecutor {

    private static final String JDBC_URL = "jdbc:oracle:thin:@//orca.csc.ncsu.edu:1521/orcl.csc.ncsu.edu"; // Using SERVICE_NAME

    public static void main(String[] args) throws Exception {
        if (args.length != 3) {
            System.out.println("Usage: java SQLExecutor <sql file path> <username> <password>");
            return;
        }
        String filename = args[0];
        String username = args[1];
        String password = args[2];

        SQLExecutor sqlExecutor = new SQLExecutor();
        List<String> queries = sqlExecutor.parseSQLFile(filename);

        Class.forName("oracle.jdbc.driver.OracleDriver");
        Connection conn = DriverManager.getConnection(JDBC_URL, username, password);

        sqlExecutor.executeSQL(conn, queries);
    }

    public void executeSQL(Connection conn, List<String> queries) throws SQLException {
        long start = System.currentTimeMillis();
        for (String sql : queries) {
            Statement stmt = conn.createStatement();
            try {
                stmt.execute(sql);
            } catch (SQLException e) {
                throw new SQLException(e.getMessage() + " from executing: " + sql, e.getSQLState(), e.getErrorCode());
            } finally {
                stmt.close();
            }
        }
        conn.close();
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
