package com.airtel.assistant.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

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

import com.airtel.assistant.controller.ReturnController;
import com.airtel.assistant.utils.AppStyle;

public class ReturnAssetForm extends JFrame {

    private JTextField txtAssetId, txtDate, txtCondition;
    private JButton btnReturn, btnBack;

    // FIXED: Removed the "new ReturnController()" line to prevent null pointer errors
    private ReturnController controller;

    // FIXED: Updated constructor to accept the controller from Spring
    public ReturnAssetForm(ReturnController controller) {
        this.controller = controller;
        
        setTitle("Airtel | Return Asset");
        setSize(450, 420);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        // --- ROOT PANEL ---
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(AppStyle.DARK_BG);
        mainPanel.setBorder(new EmptyBorder(30, 30, 30, 30));
        setContentPane(mainPanel);

        // --- HEADER ---
        JLabel lblHeader = new JLabel("ASSET RETURN LOG", SwingConstants.CENTER);
        lblHeader.setForeground(AppStyle.AIRTEL_RED);
        lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblHeader.setBorder(new EmptyBorder(0, 0, 25, 0));
        mainPanel.add(lblHeader, BorderLayout.NORTH);

        // --- FORM GRID ---
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 15, 25));
        formPanel.setOpaque(false);

        formPanel.add(createStyledLabel("Asset ID:"));
        txtAssetId = new JTextField();
        AppStyle.styleTextField(txtAssetId);
        formPanel.add(txtAssetId);

        formPanel.add(createStyledLabel("Return Date:"));
        txtDate = new JTextField();
        AppStyle.styleTextField(txtDate);
        formPanel.add(txtDate);

        formPanel.add(createStyledLabel("Condition:"));
        txtCondition = new JTextField();
        AppStyle.styleTextField(txtCondition);
        formPanel.add(txtCondition);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // --- BUTTON SECTION ---
        JPanel btnPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        btnPanel.setOpaque(false);
        btnPanel.setBorder(new EmptyBorder(25, 0, 0, 0));

        btnReturn = new JButton("CONFIRM RETURN");
        AppStyle.styleDarkButton(btnReturn);

        btnBack = new JButton("CANCEL");
        AppStyle.styleDarkButton(btnBack);
        btnBack.setBackground(new Color(60, 60, 60)); 
        btnBack.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        btnPanel.add(btnReturn);
        btnPanel.add(btnBack);
        mainPanel.add(btnPanel, BorderLayout.SOUTH);

        // ================= ACTIONS =================
        btnReturn.addActionListener(e -> processReturn());
        btnBack.addActionListener(e -> this.dispose());

        setVisible(true);
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(AppStyle.TEXT_WHITE);
        label.setFont(AppStyle.NORMAL_FONT);
        return label;
    }

    private void processReturn() {
        try {
            String assetIdStr = txtAssetId.getText().trim();
            String date = txtDate.getText().trim();
            String condition = txtCondition.getText().trim();

            if (assetIdStr.isEmpty() || date.isEmpty() || condition.isEmpty()) {
                styleOptionPane();
                JOptionPane.showMessageDialog(this, "Please fill in all details", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int assetId = Integer.parseInt(assetIdStr);
            
            // This now calls the ReturnService through the bridge method we added
            boolean success = controller.returnAsset(assetId, date, condition);

            if (success) {
                styleOptionPane();
                JOptionPane.showMessageDialog(this, "Asset ID " + assetId + " has been successfully returned.");
                clearFields();
            } else {
                styleOptionPane();
                JOptionPane.showMessageDialog(this, "Return failed. Verify Asset ID is currently assigned.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            styleOptionPane();
            JOptionPane.showMessageDialog(this, "Invalid Asset ID format!", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        txtAssetId.setText("");
        txtDate.setText("");
        txtCondition.setText("");
    }

    private void styleOptionPane() {
        UIManager.put("OptionPane.background", AppStyle.DARK_BG);
        UIManager.put("Panel.background", AppStyle.DARK_BG);
        UIManager.put("OptionPane.messageForeground", Color.WHITE);
    }
}