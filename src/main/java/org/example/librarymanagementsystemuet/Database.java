package org.example.librarymanagementsystemuet;

import java.sql.*;

public class Database {

    protected static Connection connect;
    protected static PreparedStatement prepare;
    protected static ResultSet result;
    protected static Statement statement;

    protected static Connection connectDB() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager
                    .getConnection("jdbc:mysql://localhost:3307/library_management_system_uet",
                            "root", "");
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
