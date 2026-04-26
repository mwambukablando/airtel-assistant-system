package com.airtel.assistant.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import org.springframework.stereotype.Repository;
import com.airtel.assistant.config.DatabaseConfig;

@Repository
public class ReturnAssetRepository {

    public boolean returnAsset(int assetId, String dateString, String condition) {
        String logReturnSql = "INSERT INTO returns(asset_id, return_date, condition_status, status) VALUES(?,?,?,?)";
        String updateAssetSql = "UPDATE assets SET status = 'AVAILABLE' WHERE asset_id = ?";
        String updateAssignmentSql = "UPDATE assignments SET status = 'RETURNED' WHERE asset_id = ? AND status = 'ACTIVE'";

        try (Connection conn = DatabaseConfig.getConnection()) {
            conn.setAutoCommit(false); 
            try (PreparedStatement psLog = conn.prepareStatement(logReturnSql);
                 PreparedStatement psUpdate = conn.prepareStatement(updateAssetSql);
                 PreparedStatement psAssign = conn.prepareStatement(updateAssignmentSql)) {

                psLog.setInt(1, assetId);
                psLog.setString(2, dateString);
                psLog.setString(3, condition);
                psLog.setString(4, "RETURNED");
                psLog.executeUpdate();

                psUpdate.setInt(1, assetId);
                psUpdate.executeUpdate();

                psAssign.setInt(1, assetId);
                psAssign.executeUpdate();

                conn.commit(); 
                return true;
            } catch (Exception e) {
                conn.rollback();
                e.printStackTrace();
                return false;
            }
        } catch (Exception e) { return false; }
    }
}