package com.airtel.assistant.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.airtel.assistant.config.DatabaseConfig;

public class UserRepository {

    public boolean login(String username, String password) {

        try {
            Connection conn = DatabaseConfig.getConnection();

            String sql = "SELECT * FROM users WHERE username=? AND password=?";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            return rs.next(); // true if user exists

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getRole(String username) {

        try {
            Connection conn = DatabaseConfig.getConnection();

            String sql = "SELECT role FROM users WHERE username=?";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getString("role");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}