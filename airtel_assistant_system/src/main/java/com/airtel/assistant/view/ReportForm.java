package com.airtel.assistant.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import com.airtel.assistant.utils.AppStyle;
import com.airtel.assistant.utils.ReportGenerator;

public class ReportForm extends JFrame {

    JButton btnAssetReport, btnAssignReport, btnReturnReport, btnBack;

    public ReportForm() {
        setTitle("Airtel | Report Management");
        setSize(450, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);

        // --- ROOT PANEL ---
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBackground(AppStyle.DARK_BG);
        mainPanel.setBorder(new EmptyBorder(30, 30, 30, 30));
        setContentPane(mainPanel);

        // --- HEADER ---
        JLabel lblHeader = new JLabel("GENERATE REPORTS", SwingConstants.CENTER);
        lblHeader.setForeground(AppStyle.AIRTEL_RED);
        lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 22));
        mainPanel.add(lblHeader, BorderLayout.NORTH);

        // --- BUTTON CONTAINER (GRID) ---
        JPanel gridPanel = new JPanel(new GridLayout(4, 1, 0, 15));
        gridPanel.setOpaque(false);

        btnAssetReport = new JButton("DOWNLOAD ASSET REPORT (PDF)");
        btnAssignReport = new JButton("DOWNLOAD ASSIGNMENT REPORT (PDF)");
        btnReturnReport = new JButton("DOWNLOAD RETURN REPORT (PDF)");
        btnBack = new JButton("RETURN TO DASHBOARD");

        // Style primary report buttons
        JButton[] reports = {btnAssetReport, btnAssignReport, btnReturnReport};
        for (JButton btn : reports) {
            AppStyle.styleDarkButton(btn);
            btn.setPreferredSize(new Dimension(0, 50)); // Make them nice and tall
            gridPanel.add(btn);
        }

        // Style the back button differently to separate it from reports
        AppStyle.styleDarkButton(btnBack);
        btnBack.setBackground(new Color(60, 60, 60));
        btnBack.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        gridPanel.add(btnBack);

        mainPanel.add(gridPanel, BorderLayout.CENTER);

        // ================= ACTIONS =================

        btnAssetReport.addActionListener(e -> {
            styleOptionPane();
            JOptionPane.showMessageDialog(this, "Generating Comprehensive Asset Report...", "System Action", JOptionPane.INFORMATION_MESSAGE);
            ReportGenerator.generateAssetReport();
        });

        btnAssignReport.addActionListener(e -> {
            styleOptionPane();
            JOptionPane.showMessageDialog(this, "Generating Assignment History...", "System Action", JOptionPane.INFORMATION_MESSAGE);
            ReportGenerator.generateAssignmentReport();
        });

        btnReturnReport.addActionListener(e -> {
            styleOptionPane();
            JOptionPane.showMessageDialog(this, "Generating Return Logs...", "System Action", JOptionPane.INFORMATION_MESSAGE);
            ReportGenerator.generateReturnReport();
        });

        btnBack.addActionListener(e -> this.dispose());

        setVisible(true);
    }

    private void styleOptionPane() {
        UIManager.put("OptionPane.background", AppStyle.DARK_BG);
        UIManager.put("Panel.background", AppStyle.DARK_BG);
        UIManager.put("OptionPane.messageForeground", Color.WHITE);
        UIManager.put("Button.background", AppStyle.AIRTEL_RED);
        UIManager.put("Button.foreground", Color.WHITE);
    }
}