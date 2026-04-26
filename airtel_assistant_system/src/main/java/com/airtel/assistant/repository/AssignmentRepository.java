package com.airtel.assistant.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class AssignmentRepository {

    // 1. Database Credentials from Render/Properties
    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String dbUser;

    @Value("${spring.datasource.password}")
    private String dbPass;

    // 2. Inject the Audit Repository (Fixes line 65)
    @Autowired
    private AuditLogRepository auditLogRepo;

    private Connection getConnection() throws Exception {
        return DriverManager.getConnection(dbUrl, dbUser, dbPass);
    }

    public boolean assignAsset(int assetId, String employee, String department, String dateString) {
        String formattedDate;
        try {
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("M/d/yyyy");
            LocalDate date = LocalDate.parse(dateString, inputFormatter);
            formattedDate = date.toString();
        } catch (Exception e) {
            System.out.println("Date format error: " + e.getMessage());
            return false;
        }

        String checkSql = "SELECT status FROM assets WHERE id = ?";
        String insertSql = "INSERT INTO assignments(asset_id, employee_name, department, assign_date, status, assign_id) VALUES(?,?,?,?,?,0)";
        String updateAssetSql = "UPDATE assets SET status = 'ASSIGNED' WHERE id = ?";

        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false); 

            // --- A. Check if Asset is Available ---
            try (PreparedStatement psCheck = conn.prepareStatement(checkSql)) {
                psCheck.setInt(1, assetId);
                ResultSet rs = psCheck.executeQuery();
                if (rs.next()) {
                    String currentStatus = rs.getString("status");
                    if (currentStatus == null || !currentStatus.trim().equalsIgnoreCase("Available")) {
                        System.out.println("Blocked: Asset " + assetId + " is currently " + currentStatus);
                        return false;
                    }
                } else {
                    return false; 
                }
            }

            // --- B. Execute Assignment ---
            try (PreparedStatement psInsert = conn.prepareStatement(insertSql);
                 PreparedStatement psUpdate = conn.prepareStatement(updateAssetSql)) {

                psInsert.setInt(1, assetId);
                psInsert.setString(2, employee);
                psInsert.setString(3, department);
                psInsert.setString(4, formattedDate);
                psInsert.setString(5, "ACTIVE");
                
                int rowsInserted = psInsert.executeUpdate();

                psUpdate.setInt(1, assetId);
                int rowsUpdated = psUpdate.executeUpdate();

                if (rowsInserted > 0 && rowsUpdated > 0) {
                    conn.commit();
                    
                    // FIX: Using the injected repo instead of the static call
                    auditLogRepo.logAction(assetId, "Assigned to " + employee);
                    
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
}