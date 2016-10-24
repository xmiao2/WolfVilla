package edu.ncsu.csc440.teamk.wolfvilla.util;

public class Connection {
    private Connection connection;

    private Connection() {
    }

    public Connection getInstance() {
        if (connection == null) {
            connection = new Connection();
        }
        return connection;
    }
}
