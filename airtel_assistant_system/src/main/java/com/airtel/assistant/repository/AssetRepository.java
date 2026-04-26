package com.airtel.assistant.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

@Repository 
public class AssetRepository {

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String dbUser;

    @Value("${spring.datasource.password}")
    private String dbPass;

    private Connection getConnection() throws Exception {
        return DriverManager.getConnection(dbUrl, dbUser, dbPass);
    }

    // ================= ADD ASSET (Updated to match Railway Columns) =================
    public boolean addAsset(String tag, String name, String serial, String brand, String model, String category) {
        // Matches Railway: asset_tag, device_name, serial_number, brand, model, category, condition_status
        String sql = "INSERT INTO assets(asset_tag, device_name, serial_number, brand, model, category, condition_status) VALUES(?,?,?,?,?,?,?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tag);
            ps.setString(2, name);
            ps.setString(3, serial);
            ps.setString(4, brand);
            ps.setString(5, model);
            ps.setString(6, category);
            ps.setString(7, "AVAILABLE");

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ================= SEARCH ASSETS (Updated Columns) =================
    public ResultSet searchAssets(String keyword) {
        String sql = "SELECT asset_id, asset_tag, device_name, serial_number, brand, model, category, condition_status FROM assets " +
                     "WHERE device_name LIKE ? OR serial_number LIKE ? OR asset_tag LIKE ?";
        try {
            Connection conn = getConnection();
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

    // ================= GET ALL ASSETS (Updated Columns) =================
    public ResultSet getAllAssets() {
        String sql = "SELECT asset_id, asset_tag, device_name, serial_number, brand, model, category, condition_status FROM assets";
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            return ps.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // ================= UPDATE STATUS =================
    public boolean updateStatus(int id, String status) {
        String sql = "UPDATE assets SET condition_status=? WHERE asset_id=?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ================= DASHBOARD STATS =================
    public int countTotalAssets() {
        String sql = "SELECT COUNT(*) FROM assets";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) { e.printStackTrace(); }
        return 0;
    }

    public int countAssignedAssets() {
        String sql = "SELECT COUNT(*) FROM assets WHERE condition_status = 'ASSIGNED'";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) { e.printStackTrace(); }
        return 0;
    }

    public int countAvailableAssets() {
        String sql = "SELECT COUNT(*) FROM assets WHERE condition_status = 'AVAILABLE'";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) { e.printStackTrace(); }
        return 0;
    }
}