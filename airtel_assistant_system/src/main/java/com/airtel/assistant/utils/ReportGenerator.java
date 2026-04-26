package com.airtel.assistant.utils;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

import com.airtel.assistant.config.DatabaseConfig;

public class ReportGenerator {

    // 1. ASSET REPORT
    public static void generateAssetReport() {
        exportToCSV("Airtel_Asset_Report.csv", "SELECT * FROM assets");
    }

    // 2. ASSIGNMENT REPORT
    public static void generateAssignmentReport() {
        exportToCSV("Airtel_Assignment_Report.csv", "SELECT * FROM assignments");
    }

    // 3. RETURN REPORT
    public static void generateReturnReport() {
        exportToCSV("Airtel_Return_Report.csv", "SELECT * FROM returns");
    }

    // Generic helper method to save any table to CSV
    private static void exportToCSV(String fileName, String query) {
        try (Connection conn = DatabaseConfig.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(query);
             PrintWriter writer = new PrintWriter(fileName)) {

            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            // Write Headers
            for (int i = 1; i <= columnCount; i++) {
                writer.print(metaData.getColumnName(i) + (i < columnCount ? "," : ""));
            }
            writer.println();

            // Write Data
            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    writer.print(rs.getString(i) + (i < columnCount ? "," : ""));
                }
                writer.println();
            }

            System.out.println("Report successfully generated: " + fileName);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}