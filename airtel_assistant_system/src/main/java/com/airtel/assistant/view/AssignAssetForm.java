package com.airtel.assistant.view;

import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import com.airtel.assistant.controller.AssignmentController;
import com.airtel.assistant.utils.AppStyle;

public class AssignAssetForm extends JFrame {

    JTextField txtAssetId, txtUsername, txtDepartment, txtDate;
    JButton btnAssign, btnBack;

    // Use a managed instance or simple new if not yet Spring-integrated for Swing
    AssignmentController controller = new AssignmentController();

    public AssignAssetForm() {
        setTitle("Airtel | Secure Asset Assignment");
        setSize(450, 480);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(AppStyle.DARK_BG);
        mainPanel.setBorder(new EmptyBorder(25, 25, 25, 25));
        setContentPane(mainPanel);

        // ... (Header and Layout code remains the same as your version)
        
        btnAssign.addActionListener(e -> assign());
        btnBack.addActionListener(e -> this.dispose());
        setVisible(true);
    }

    private void assign() {
        String username = txtUsername.getText().trim();
        String assetIdStr = txtAssetId.getText().trim();
        String dept = txtDepartment.getText().trim();
        String date = txtDate.getText().trim();

        if (assetIdStr.isEmpty() || username.isEmpty() || date.isEmpty()) {
             JOptionPane.showMessageDialog(this, "Please fill in all required fields", "Validation", JOptionPane.WARNING_MESSAGE);
             return;
        }

        try {
            int assetId = Integer.parseInt(assetIdStr);
            // Calling the controller
            boolean success = controller.assignAsset(assetId, username, dept, date);

            if (success) {
                JOptionPane.showMessageDialog(this, "Asset " + assetId + " assigned to " + username);
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "Assignment Failed. Is the Asset available?", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Asset ID must be a number!", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Helper to clear fields after success
    private void clearFields() {
        txtAssetId.setText("");
        txtUsername.setText("");
        txtDepartment.setText("");
        txtDate.setText("");
    }
}