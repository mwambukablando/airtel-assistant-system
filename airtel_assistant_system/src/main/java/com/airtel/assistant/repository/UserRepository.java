package com.airtel.assistant.repository;

import java.sql.*;
import java.util.*;
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

    public boolean createUser(String firstName, String lastName, String username) {
        String fullName = firstName + " " + lastName;
        // FIXED: Changed full_name to fullname to match your Railway screenshot
        String sql = "INSERT INTO users (username, password, role, fullname) VALUES (?, '123', 'USER', ?)";
        
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

    public List<Map<String, String>> getAllUsersList() {
        List<Map<String, String>> list = new ArrayList<>();
        // FIXED: Changed full_name to fullname
        String sql = "SELECT username, fullname, role FROM users";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Map<String, String> user = new HashMap<>();
                user.put("username", rs.getString("username"));
                
                // FIXED: Reading from 'fullname'
                String name = rs.getString("fullname");
                user.put("fullName", (name != null && !name.isEmpty()) ? name : "Administrator");
                
                user.put("role", rs.getString("role"));
                list.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Keep your login, getRole, and deleteUser methods below...
    public boolean login(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) { return false; }
    }

    public String getRole(String username) {
        String sql = "SELECT role FROM users WHERE username = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getString("role");
        } catch (Exception e) { }
        return "USER";
    }

    public boolean deleteUser(String username) {
        String sql = "DELETE FROM users WHERE username = ? AND role != 'ADMIN'";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { return false; }
    }
}