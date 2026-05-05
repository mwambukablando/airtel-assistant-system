package com.airtel.assistant.repository;

import java.sql.*;
import java.util.*;
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

    // LOGGING ACTION (Matching your Railway columns)
    public void logAction(int assetId, String action) {
        String sql = "INSERT INTO audit_logs (asset_id, action, done_by, action_date) VALUES (?, ?, ?, NOW())";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, assetId);
            ps.setString(2, action);
            ps.setString(3, SessionManager.getUsername());
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    // NEW: FETCHING ACTIONS for the HTML Table
    public List<Map<String, Object>> getAllLogs() {
        List<Map<String, Object>> logs = new ArrayList<>();
        String sql = "SELECT * FROM audit_logs ORDER BY action_date DESC";
        try (Connection conn = getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Map<String, Object> log = new HashMap<>();
                log.put("date", rs.getTimestamp("action_date"));
                log.put("user", rs.getString("done_by"));
                log.put("action", rs.getString("action"));
                log.put("assetId", rs.getInt("asset_id"));
                logs.add(log);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return logs;
    }
}