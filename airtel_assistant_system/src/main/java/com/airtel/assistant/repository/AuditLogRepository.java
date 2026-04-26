package com.airtel.assistant.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import com.airtel.assistant.security.SessionManager;

@Repository
public class AuditLogRepository {

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String dbUser;

    @Value("${spring.datasource.password}")
    private String dbPass;

    private Connection getConnection() throws Exception {
        return DriverManager.getConnection(dbUrl, dbUser, dbPass);
    }

    public void logAction(int assetId, String action) {
        String sql = "INSERT INTO audit_logs (asset_id, action, user, action_date) VALUES (?, ?, ?, NOW())";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, assetId);
            ps.setString(2, action);
            ps.setString(3, SessionManager.getUsername());

            ps.executeUpdate();
            System.out.println("Audit Logged: " + action);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}