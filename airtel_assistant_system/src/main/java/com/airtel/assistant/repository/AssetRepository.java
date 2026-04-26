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
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // --- SMART LOGIC: Checks for ACTIVE assignments to determine status ---
    public ResultSet getAllAssets() {
        String sql = "SELECT a.asset_id, a.asset_tag, a.device_name, a.serial_number, a.brand, a.model, a.category, " +
                     "CASE WHEN ass.asset_id IS NOT NULL THEN 'ASSIGNED' ELSE 'AVAILABLE' END AS calculated_status " +
                     "FROM assets a " +
                     "LEFT JOIN assignments ass ON a.asset_id = ass.asset_id AND ass.status = 'ACTIVE'";
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            return ps.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ResultSet searchAssets(String keyword) {
        String sql = "SELECT a.asset_id, a.asset_tag, a.device_name, a.serial_number, a.brand, a.model, a.category, " +
                     "CASE WHEN ass.asset_id IS NOT NULL THEN 'ASSIGNED' ELSE 'AVAILABLE' END AS calculated_status " +
                     "FROM assets a " +
                     "LEFT JOIN assignments ass ON a.asset_id = ass.asset_id AND ass.status = 'ACTIVE' " +
                     "WHERE a.device_name LIKE ? OR a.serial_number LIKE ? OR a.asset_tag LIKE ?";
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

    // --- SMART DASHBOARD STATS ---
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
        String sql = "SELECT COUNT(*) FROM assignments WHERE status = 'ACTIVE'";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) { e.printStackTrace(); }
        return 0;
    }

    public int countAvailableAssets() {
        String sql = "SELECT (SELECT COUNT(*) FROM assets) - (SELECT COUNT(*) FROM assignments WHERE status = 'ACTIVE')";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) { e.printStackTrace(); }
        return 0;
    }
}