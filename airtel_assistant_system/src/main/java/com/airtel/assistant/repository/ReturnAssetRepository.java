package com.airtel.assistant.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import org.springframework.stereotype.Repository;
import com.airtel.assistant.config.DatabaseConfig;

@Repository
public class ReturnAssetRepository {

    public boolean returnAsset(int assetId, String dateString, String condition) {
        // SQL 1: Log the return record
        String logReturnSql = "INSERT INTO returns(asset_id, return_date, condition_status, status) VALUES(?,?,?,?)";
        // SQL 2: Mark the physical asset as available
        String updateAssetSql = "UPDATE assets SET status = 'AVAILABLE' WHERE asset_id = ?";
        // SQL 3: MARK THE ASSIGNMENT AS INACTIVE (Fixes your screenshot)
        String updateAssignmentSql = "UPDATE assignments SET status = 'RETURNED' WHERE asset_id = ? AND status = 'ACTIVE'";

        try (Connection conn = DatabaseConfig.getConnection()) {
            conn.setAutoCommit(false); // Start transaction

            try (PreparedStatement psLog = conn.prepareStatement(logReturnSql);
                 PreparedStatement psUpdate = conn.prepareStatement(updateAssetSql);
                 PreparedStatement psAssign = conn.prepareStatement(updateAssignmentSql)) {

                // 1. Log return
                psLog.setInt(1, assetId);
                psLog.setString(2, dateString);
                psLog.setString(3, condition);
                psLog.setString(4, "RETURNED");
                psLog.executeUpdate();

                // 2. Update asset status
                psUpdate.setInt(1, assetId);
                psUpdate.executeUpdate();

                // 3. Close the assignment record
                psAssign.setInt(1, assetId);
                psAssign.executeUpdate();

                conn.commit(); 
                return true;

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