package com.airtel.assistant.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class AssignmentRepository {

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String dbUser;

    @Value("${spring.datasource.password}")
    private String dbPass;

    @Autowired
    private AuditLogRepository auditLogRepo;

    private Connection getConnection() throws Exception {
        return DriverManager.getConnection(dbUrl, dbUser, dbPass);
    }

    public boolean assignAsset(int assetId, String employee, String department, String dateString) {
        // We use your exact Railway column names here
        String insertSql = "INSERT INTO assignments(asset_id, employee_name, department, assigned_date, status) VALUES(?,?,?,?,?)";

        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false); 

            try (PreparedStatement psInsert = conn.prepareStatement(insertSql)) {
                psInsert.setInt(1, assetId);
                psInsert.setString(2, employee);
                psInsert.setString(3, department);
                psInsert.setString(4, dateString);
                psInsert.setString(5, "ACTIVE");
                
                int rowsInserted = psInsert.executeUpdate();

                if (rowsInserted > 0) {
                    conn.commit();
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