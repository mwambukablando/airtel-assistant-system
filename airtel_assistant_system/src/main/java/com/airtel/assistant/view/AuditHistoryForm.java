package com.airtel.assistant.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import com.airtel.assistant.config.DatabaseConfig;
import com.airtel.assistant.utils.AppStyle;

public class AuditHistoryForm extends JFrame {

    DefaultTableModel model;
    JTable table;
    JButton btnBack;

    public AuditHistoryForm() {
        setTitle("Airtel | System Audit Logs");
        setSize(900, 600); // Larger for easier reading of timestamps
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // --- MAIN PANEL ---
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(AppStyle.DARK_BG);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        setContentPane(mainPanel);

        // --- HEADER ---
        JLabel lblHeader = new JLabel("SYSTEM TRANSACTION LOGS", SwingConstants.LEFT);
        lblHeader.setForeground(AppStyle.AIRTEL_RED);
        lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblHeader.setBorder(new EmptyBorder(0, 0, 10, 0));
        mainPanel.add(lblHeader, BorderLayout.NORTH);

        // ================= TABLE (THE CSS TREATMENT) =================
        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        table = new JTable(model);
        model.addColumn("ID");
        model.addColumn("Asset ID");
        model.addColumn("Action");
        model.addColumn("Performed By");
        model.addColumn("Timestamp");

        // --- STYLING THE TABLE ---
        table.setBackground(AppStyle.PANEL_BG);
        table.setForeground(Color.WHITE);
        table.setGridColor(new Color(60, 60, 60));
        table.setRowHeight(35); // Tall rows for a modern feel
        table.setSelectionBackground(AppStyle.AIRTEL_RED);
        table.setSelectionForeground(Color.WHITE);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setShowVerticalLines(false); // Clean modern look

        // --- STYLING THE HEADER ---
        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(20, 20, 20));
        header.setForeground(AppStyle.AIRTEL_RED);
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setPreferredSize(new Dimension(100, 40));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(AppStyle.DARK_BG);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 60), 1));

        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // ================= BOTTOM PANEL =================
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.setOpaque(false);

        btnBack = new JButton("CLOSE LOGS");
        AppStyle.styleDarkButton(btnBack);
        btnBack.setBackground(new Color(50, 50, 50)); // Grey for secondary action
        btnBack.setPreferredSize(new Dimension(180, 40));

        bottom.add(btnBack);
        mainPanel.add(bottom, BorderLayout.SOUTH);

        // ================= ACTIONS =================
        btnBack.addActionListener(e -> this.dispose());

        loadLogs();
        setVisible(true);
    }

    private void loadLogs() {
        String sql = "SELECT * FROM audit_logs ORDER BY id DESC";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            model.setRowCount(0);
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getInt("asset_id"),
                    rs.getString("action").toUpperCase(), // Capitalize for better look
                    rs.getString("user"),
                    rs.getTimestamp("action_date")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            UIManager.put("OptionPane.background", AppStyle.DARK_BG);
            UIManager.put("Panel.background", AppStyle.DARK_BG);
            UIManager.put("OptionPane.messageForeground", Color.WHITE);
            JOptionPane.showMessageDialog(this, "Error loading logs: " + e.getMessage());
        }
    }
}