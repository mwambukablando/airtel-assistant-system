package com.airtel.assistant.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import org.springframework.stereotype.Repository;
import com.airtel.assistant.config.DatabaseConfig;

@Repository
public class ReturnAssetRepository {

    public boolean returnAsset(int assignId, String dateString, String condition) {
        // FIXED: Using 'returns_table' and 'condition_on_return' to match your Railway screenshot
        String logReturnSql = "INSERT INTO returns_table(assign_id, return_date, condition_on_return) VALUES(?,?,?)";

        try (Connection conn = DatabaseConfig.getConnection()) {
            try (PreparedStatement psLog = conn.prepareStatement(logReturnSql)) {
                psLog.setInt(1, assignId);
                psLog.setString(2, dateString);
                psLog.setString(3, condition);
                
                return psLog.executeUpdate() > 0;
            }
        } catch (Exception e) { 
            System.out.println("DB ERROR: " + e.getMessage());
            e.printStackTrace(); 
            return false; 
        }
    }
}