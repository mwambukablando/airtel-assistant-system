package com.airtel.assistant.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig {

    // It now looks for the variables you set in application.properties/Railway
    private static final String URL = System.getenv("DB_URL") != null 
            ? System.getenv("DB_URL") 
            : "jdbc:mysql://localhost:3306/airtel_assets";

    private static final String USERNAME = System.getenv("DB_USER") != null 
            ? System.getenv("DB_USER") 
            : "root";

    private static final String PASSWORD = System.getenv("DB_PASS") != null 
            ? System.getenv("DB_PASS") 
            : "";

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (Exception e) {
            System.out.println("Connection Failed: " + e.getMessage());
            return null;
        }
    }
}