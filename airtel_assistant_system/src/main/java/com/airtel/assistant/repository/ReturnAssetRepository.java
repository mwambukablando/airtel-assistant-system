package com.airtel.assistant.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import org.springframework.stereotype.Repository;
import com.airtel.assistant.config.DatabaseConfig;

@Repository
public class ReturnAssetRepository {

    public boolean returnAsset(int assignId, String dateString, String condition) {
        // We link to assign_id to match your database schema
        String logReturnSql = "INSERT INTO returns(assign_id, return_date, condition_on_return, status) VALUES(?,?,?,?)";

        try (Connection conn = DatabaseConfig.getConnection()) {
            try (PreparedStatement psLog = conn.prepareStatement(logReturnSql)) {
                psLog.setInt(1, assignId);
                psLog.setString(2, dateString);
                psLog.setString(3, condition);
                psLog.setString(4, "RETURNED");
                
                return psLog.executeUpdate() > 0;
            }
        } catch (Exception e) { 
            e.printStackTrace();
            return false; 
        }
    }
}