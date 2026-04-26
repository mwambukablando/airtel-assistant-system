package com.airtel.assistant.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;

import com.airtel.assistant.config.DatabaseConfig;
import com.airtel.assistant.security.SessionManager;

public class AuditLogRepository {

    public static void logAction(int assetId, String action) {
        String sql = "INSERT INTO audit_logs (asset_id, action, user, action_date) VALUES (?, ?, ?, NOW())";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, assetId);
            ps.setString(2, action);
            ps.setString(3, SessionManager.getUsername()); // Tracks WHO did it

            ps.executeUpdate();
            System.out.println("Audit Logged: " + action);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}