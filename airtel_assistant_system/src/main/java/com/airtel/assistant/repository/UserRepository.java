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

    // --- Create User (Default: 123 / USER) ---
    public boolean createUser(String firstName, String lastName, String username) {
        String fullName = firstName + " " + lastName;
        String sql = "INSERT INTO users (username, password, role, full_name) VALUES (?, '123', 'USER', ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, fullName);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    // --- FIX: Login Method (Clears Error in UserService line 14) ---
    public boolean login(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    // --- FIX: GetRole Method (Clears Error in UserService line 18) ---
    public String getRole(String username) {
        String sql = "SELECT role FROM users WHERE username = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getString("role");
        } catch (Exception e) { e.printStackTrace(); }
        return "USER";
    }

    // --- NEW: Delete User ---
    public boolean deleteUser(String username) {
        String sql = "DELETE FROM users WHERE username = ? AND role != 'ADMIN'";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    public ResultSet getAllUsers() {
        String sql = "SELECT username, full_name, role FROM users";
        try {
            Connection conn = getConnection();
            return conn.prepareStatement(sql).executeQuery();
        } catch (Exception e) { return null; }
    }
}