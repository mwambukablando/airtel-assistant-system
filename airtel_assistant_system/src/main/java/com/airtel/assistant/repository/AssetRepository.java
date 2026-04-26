package com.airtel.assistant.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.airtel.assistant.config.DatabaseConfig;

public class AssetRepository {

    // ================= ADD ASSET =================
    public boolean addAsset(String name, String serial, String type) {
        String sql = "INSERT INTO assets(name, serial_number, type, status) VALUES(?,?,?,?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, serial);
            ps.setString(3, type);
            ps.setString(4, "AVAILABLE");

            boolean success = ps.executeUpdate() > 0;
            if (success) {
                AuditLogRepository.logAction(0, "Added new asset: " + name);
            }
            return success;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ================= SEARCH ASSETS =================
    public ResultSet searchAssets(String keyword) {
        // Improved SQL to search by Name, Serial, OR Type
        String sql = "SELECT id, name, serial_number, type, status FROM assets " +
                     "WHERE name LIKE ? OR serial_number LIKE ? OR type LIKE ?";
        try {
            Connection conn = DatabaseConfig.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            String pattern = "%" + keyword + "%";
            ps.setString(1, pattern);
            ps.setString(2, pattern);
            ps.setString(3, pattern);

            return ps.executeQuery();
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // ================= GET ALL ASSETS =================
    public ResultSet getAllAssets() {
        String sql = "SELECT id, name, serial_number, type, status FROM assets";
        try {
            Connection conn = DatabaseConfig.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            return ps.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // ================= UPDATE STATUS =================
    public boolean updateStatus(int id, String status) {
        String sql = "UPDATE assets SET status=? WHERE id=?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, id);
            boolean success = ps.executeUpdate() > 0;
            if (success) {
                AuditLogRepository.logAction(id, "Status updated to " + status);
            }
            return success;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ================= DASHBOARD STATS =================
    public int countTotalAssets() {
        String sql = "SELECT COUNT(*) FROM assets";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) { e.printStackTrace(); }
        return 0;
    }

    public int countAssignedAssets() {
        String sql = "SELECT COUNT(*) FROM assets WHERE status = 'ASSIGNED'";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) { e.printStackTrace(); }
        return 0;
    }

    public int countAvailableAssets() {
        String sql = "SELECT COUNT(*) FROM assets WHERE status = 'AVAILABLE'";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) { e.printStackTrace(); }
        return 0;
    }
}