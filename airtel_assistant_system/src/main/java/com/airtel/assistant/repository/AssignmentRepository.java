package com.airtel.assistant.repository;

import java.sql.*;
import java.util.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class AssignmentRepository {

    @Value("${spring.datasource.url}")
    private String dbUrl;
    @Value("${spring.datasource.username}")
    private String dbUser;
    @Value("${spring.datasource.password}")
    private String dbPass;

    @Autowired
    private AuditLogRepository auditLogRepo;

    private Connection getConnection() throws Exception {
        return DriverManager.getConnection(dbUrl, dbUser, dbPass);
    }

    public boolean assignAsset(int assetId, String username, String department, String dateString) {
        String insertSql = "INSERT INTO assignments(asset_id, employee_name, department, assigned_date, status) VALUES(?,?,?,?,?)";
        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false); 
            try (PreparedStatement psInsert = conn.prepareStatement(insertSql)) {
                psInsert.setInt(1, assetId);
                psInsert.setString(2, username);
                psInsert.setString(3, department);
                psInsert.setString(4, dateString);
                psInsert.setString(5, "ACTIVE");
                int rowsInserted = psInsert.executeUpdate();
                if (rowsInserted > 0) {
                    conn.commit();
                    auditLogRepo.logAction(assetId, "Assigned to unique user: " + username);
                    return true;
                } else {
                    conn.rollback();
                    return false;
                }
            } catch (Exception e) {
                conn.rollback();
                e.printStackTrace();
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Integer findActiveIdByAssetId(int assetId) {
        String query = "SELECT assign_id FROM assignments WHERE asset_id = ? AND status = 'ACTIVE' LIMIT 1";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, assetId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("assign_id");
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    public void updateStatus(int assignId, String status) {
        String updateSql = "UPDATE assignments SET status = ? WHERE assign_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(updateSql)) {
            ps.setString(1, status);
            ps.setInt(2, assignId);
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    // --- ONLY ADDED THIS FOR REPORTS ---
    public List<Map<String, Object>> getAllAssignmentsList() {
        List<Map<String, Object>> list = new ArrayList<>();
        String sql = "SELECT asset_id, employee_name, department, assigned_date FROM assignments ORDER BY assigned_date DESC";
        try (Connection conn = getConnection(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("asset_id", rs.getInt("asset_id"));
                row.put("employee_name", rs.getString("employee_name"));
                row.put("department", rs.getString("department"));
                row.put("assignment_date", rs.getTimestamp("assigned_date"));
                list.add(row);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }
}