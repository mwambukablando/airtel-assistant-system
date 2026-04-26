package com.airtel.assistant.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.springframework.stereotype.Repository;
import com.airtel.assistant.config.DatabaseConfig;

@Repository
public class ReturnAssetRepository {

    public boolean returnAsset(int assetId, String dateString, String condition) {
        String formattedDate;
        try {
            // Handle date conversion
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            // If your HTML date input sends yyyy-MM-dd, use that. 
            // If it sends M/d/yyyy (like your Swing form), use the previous formatter.
            LocalDate date = LocalDate.parse(dateString); 
            formattedDate = date.toString();
        } catch (Exception e) {
            formattedDate = LocalDate.now().toString(); // Fallback to today
        }

        String logReturnSql = "INSERT INTO returns(asset_id, return_date, condition_status, status) VALUES(?,?,?,?)";
        String updateAssetSql = "UPDATE assets SET status = 'AVAILABLE' WHERE asset_id = ?";

        try (Connection conn = DatabaseConfig.getConnection()) {
            conn.setAutoCommit(false); // Start Transaction

            try (PreparedStatement psLog = conn.prepareStatement(logReturnSql);
                 PreparedStatement psUpdate = conn.prepareStatement(updateAssetSql)) {

                // 1. Log the return
                psLog.setInt(1, assetId);
                psLog.setString(2, formattedDate);
                psLog.setString(3, condition);
                psLog.setString(4, "RETURNED");
                psLog.executeUpdate();

                // 2. Update the Asset status to AVAILABLE
                psUpdate.setInt(1, assetId);
                psUpdate.executeUpdate();

                conn.commit(); // Save both changes!
                return true;

            } catch (Exception e) {
                conn.rollback(); // Undo if anything goes wrong
                e.printStackTrace();
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}