package com.airtel.assistant.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import com.airtel.assistant.config.DatabaseConfig;

public class AssignmentRepository {

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

        // --- THE CORRECTION IS HERE ---
        // We added 'assign_id' to the columns and '0' to the values to stop the error.
        String checkSql = "SELECT status FROM assets WHERE id = ?";
        String insertSql = "INSERT INTO assignments(asset_id, employee_name, department, assign_date, status, assign_id) VALUES(?,?,?,?,?,0)";
        String updateAssetSql = "UPDATE assets SET status = 'ASSIGNED' WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection()) {
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
                    // Log the action
                    AuditLogRepository.logAction(assetId, "Assigned to " + employee);
                    return true;
                } else {
                    conn.rollback();
                    return false;
                }
            } catch (Exception e) {
                if (conn != null) conn.rollback();
                e.printStackTrace();
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}