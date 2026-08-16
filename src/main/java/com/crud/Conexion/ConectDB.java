package com.crud.Conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConectDB {

    private static final String url = "jdbc:mysql://localhost:3306/crud";
    private static final String user = "usuario1";
    private static final String password = "superpassword";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url,user,password);
    }
}
