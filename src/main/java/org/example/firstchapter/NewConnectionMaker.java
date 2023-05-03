package org.example.firstchapter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class NewConnectionMaker implements ConnectionMaker {

    public Connection makeConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        Connection c = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/spring", "root", "nf6yxxzc"
        );
        return c;
    }

}
