package com.airtel.assistant.repository;

import java.sql.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {

    @Value("${spring.datasource.url}")
    private String dbUrl;
    @Value("${spring.datasource.username}")
    private String dbUser;
    @Value("${spring.datasource.password}")
    private String dbPass;

    private Connection getConnection() throws Exception {
        return DriverManager.getConnection(dbUrl, dbUser, dbPass);
    }

    // --- Create User with Defaults: Password '123' and Role 'USER' ---
    public boolean createUser(String firstName, String lastName, String username) {
        String fullName = firstName + " " + lastName;
        String sql = "INSERT INTO users (username, password, role, full_name) VALUES (?, '123', 'USER', ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, fullName);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // --- Get All Users for the Management Table ---
    public ResultSet getAllUsers() {
        String sql = "SELECT username, full_name, role FROM users";
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            return ps.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}