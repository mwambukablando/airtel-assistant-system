package com.airtel.assistant.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.airtel.assistant.config.DatabaseConfig;

public class DepartmentRepository {

    public boolean saveDepartment(String departmentName) {

        try {
            Connection conn = DatabaseConfig.getConnection();

            String sql = "INSERT INTO departments(department_name) VALUES(?)";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, departmentName);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<String> getAllDepartments() {

        List<String> list = new ArrayList<>();

        try {
            Connection conn = DatabaseConfig.getConnection();

            String sql = "SELECT department_name FROM departments";

            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(rs.getString("department_name"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}