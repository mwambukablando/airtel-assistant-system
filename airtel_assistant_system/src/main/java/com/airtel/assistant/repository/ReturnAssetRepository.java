package com.airtel.assistant.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.airtel.assistant.config.DatabaseConfig;

public class ReturnAssetRepository {

    public boolean returnAsset(int assetId, String dateString, String condition) {

        String formattedDate;
        try {
            // Convert "4/23/2026" to "2026-04-23"
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("M/d/yyyy");
            LocalDate date = LocalDate.parse(dateString, inputFormatter);
            formattedDate = date.toString();
        } catch (Exception e) {
            System.out.println("Date Error: " + e.getMessage());
            return false;
        }

        String sql = "INSERT INTO returns(asset_id, return_date, condition_status, status) VALUES(?,?,?,?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, assetId);
            ps.setString(2, formattedDate);
            ps.setString(3, condition);
            ps.setString(4, "RETURNED");

            return ps.executeUpdate() > 0;

        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}