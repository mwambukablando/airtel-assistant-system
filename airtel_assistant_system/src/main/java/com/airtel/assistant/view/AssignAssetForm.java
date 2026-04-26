package com.airtel.assistant.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import com.airtel.assistant.config.DatabaseConfig;
import com.airtel.assistant.controller.AssignmentController;
import com.airtel.assistant.utils.AppStyle;

public class AssignAssetForm extends JFrame {

    JTextField txtAssetId, txtUsername, txtDepartment, txtDate; // Changed variable name for clarity
    JButton btnAssign, btnBack;

    AssignmentController controller = new AssignmentController();

    public AssignAssetForm() {
        setTitle("Airtel | Secure Asset Assignment");
        setSize(450, 480);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        // --- MAIN PANEL ---
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(AppStyle.DARK_BG);
        mainPanel.setBorder(new EmptyBorder(25, 25, 25, 25));
        setContentPane(mainPanel);

        // --- HEADER ---
        JLabel lblHeader = new JLabel("ASSIGNMENT FORM", SwingConstants.CENTER);
        lblHeader.setForeground(AppStyle.AIRTEL_RED);
        lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblHeader.setBorder(new EmptyBorder(0, 0, 20, 0));
        mainPanel.add(lblHeader, BorderLayout.NORTH);

        // --- FORM GRID ---
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 15, 20));
        formPanel.setOpaque(false);

        formPanel.add(createStyledLabel("Asset ID:"));
        txtAssetId = new JTextField();
        AppStyle.styleTextField(txtAssetId);
        formPanel.add(txtAssetId);

        // SPECIFIC CHANGE: Label changed to Employee Username
        formPanel.add(createStyledLabel("Employee Username:"));
        txtUsername = new JTextField();
        AppStyle.styleTextField(txtUsername);
        formPanel.add(txtUsername);

        formPanel.add(createStyledLabel("Department:"));
        txtDepartment = new JTextField();
        AppStyle.styleTextField(txtDepartment);
        formPanel.add(txtDepartment);

        formPanel.add(createStyledLabel("Assign Date:"));
        txtDate = new JTextField();
        AppStyle.styleTextField(txtDate);
        formPanel.add(txtDate);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // --- BUTTON SECTION ---
        JPanel btnPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        btnPanel.setOpaque(false);
        btnPanel.setBorder(new EmptyBorder(20, 0, 0, 0));

        btnAssign = new JButton("CONFIRM ASSIGN");
        AppStyle.styleDarkButton(btnAssign);

        btnBack = new JButton("CANCEL");
        AppStyle.styleDarkButton(btnBack);
        btnBack.setBackground(new Color(60, 60, 60));
        btnBack.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        btnPanel.add(btnAssign);
        btnPanel.add(btnBack);
        mainPanel.add(btnPanel, BorderLayout.SOUTH);

        // ================= ACTIONS =================
        btnAssign.addActionListener(e -> assign());
        btnBack.addActionListener(e -> this.dispose());

        setVisible(true);
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(AppStyle.TEXT_WHITE);
        label.setFont(AppStyle.NORMAL_FONT);
        return label;
    }

    private void assign() {
        String username = txtUsername.getText().trim();
        String assetIdStr = txtAssetId.getText().trim();
        String dept = txtDepartment.getText().trim();
        String date = txtDate.getText().trim();

        if (assetIdStr.isEmpty() || username.isEmpty() || date.isEmpty()) {
            styleOptionPane();
            JOptionPane.showMessageDialog(this, "Please fill in all required fields", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // --- STEP 1: SPECIFIC USERNAME VALIDATION ---
        if (!doesUserExist(username)) {
            styleOptionPane();
            int choice = JOptionPane.showConfirmDialog(this,
                "Username '" + username + "' is not registered.\nCreate this user now?",
                "Invalid Username", JOptionPane.YES_NO_OPTION);

            if (choice == JOptionPane.YES_OPTION) {
                new UserForm();
            }
            return;
        }

        // --- STEP 2: PROCEED WITH ASSIGNMENT ---
        try {
            int assetId = Integer.parseInt(assetIdStr);
            boolean success = controller.assignAsset(assetId, username, dept, date);

            if (success) {
                styleOptionPane();
                JOptionPane.showMessageDialog(this, "Asset " + assetId + " assigned to " + username);
                clearFields();
            } else {
                styleOptionPane();
                JOptionPane.showMessageDialog(this, "Assignment Failed. Is the Asset available?", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            styleOptionPane();
            JOptionPane.showMessageDialog(this, "Asset ID must be a number!", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * SPECIFIC CHECK: Matches the 'username' column in your MariaDB users table
     */
    private boolean doesUserExist(String username) {
        // We specifically check the username column here
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void clearFields() {
        txtAssetId.setText("");
        txtUsername.setText("");
        txtDepartment.setText("");
        txtDate.setText("");
    }

    private void styleOptionPane() {
        UIManager.put("OptionPane.background", AppStyle.DARK_BG);
        UIManager.put("Panel.background", AppStyle.DARK_BG);
        UIManager.put("OptionPane.messageForeground", Color.WHITE);
    }
}