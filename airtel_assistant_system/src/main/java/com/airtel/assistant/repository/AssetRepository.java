package com.airtel.assistant.repository;

import java.sql.*;
import java.util.*;
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

    // --- ADD ASSET ---
    public boolean addAsset(String tag, String name, String serial, String brand, String model, String category) {
        String sql = "INSERT INTO assets(asset_tag, device_name, serial_number, brand, model, category) VALUES(?,?,?,?,?,?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tag);
            ps.setString(2, name);
            ps.setString(3, serial);
            ps.setString(4, brand);
            ps.setString(5, model);
            ps.setString(6, category);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    // --- NEW: This method clears the error in ReturnService.java ---
    public boolean updateStatus(int id, String status) {
        // Note: Check if your column is 'status' or 'condition_status' in Railway
        String sql = "UPDATE assets SET status=? WHERE asset_id=?";
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

    // --- UPDATED: Used by the Dashboard to list assets safely ---
    public List<Map<String, String>> getAllAssets() {
        return searchAssetsList(""); // Passing empty string returns everyone
    }

    // --- Search Method (Returns List for connection safety) ---
    public List<Map<String, String>> searchAssetsList(String keyword) {
        List<Map<String, String>> results = new ArrayList<>();
        String sql = "SELECT a.asset_id, a.asset_tag, a.device_name, a.serial_number, a.brand, a.model, " +
                     "CASE WHEN ass.asset_id IS NOT NULL THEN 'ASSIGNED' ELSE 'AVAILABLE' END AS calculated_status " +
                     "FROM assets a " +
                     "LEFT JOIN assignments ass ON a.asset_id = ass.asset_id AND ass.status = 'ACTIVE' " +
                     "WHERE a.device_name LIKE ? OR a.serial_number LIKE ? OR a.asset_tag LIKE ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            String pattern = "%" + keyword + "%";
            ps.setString(1, pattern);
            ps.setString(2, pattern);
            ps.setString(3, pattern);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, String> row = new HashMap<>();
                    row.put("id", String.valueOf(rs.getInt("asset_id")));
                    row.put("tag", rs.getString("asset_tag"));
                    row.put("name", rs.getString("device_name"));
                    row.put("serial", rs.getString("serial_number"));
                    row.put("brand", rs.getString("brand"));
                    row.put("status", rs.getString("calculated_status"));
                    results.add(row);
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return results;
    }

    // --- Dashboard Stats ---
    public int countTotalAssets() {
        String sql = "SELECT COUNT(*) FROM assets";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) { e.printStackTrace(); }
        return 0;
    }

    public int countAssignedAssets() {
        String sql = "SELECT COUNT(*) FROM assignments WHERE status = 'ACTIVE'";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) { e.printStackTrace(); }
        return 0;
    }

    public int countAvailableAssets() {
        String sql = "SELECT (SELECT COUNT(*) FROM assets) - (SELECT COUNT(*) FROM assignments WHERE status = 'ACTIVE')";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) { e.printStackTrace(); }
        return 0;
    }
}