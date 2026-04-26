package com.airtel.assistant.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import com.airtel.assistant.controller.AssetController;
import com.airtel.assistant.security.SessionManager;
import com.airtel.assistant.utils.AppStyle;

public class Dashboard extends JFrame {

    JLabel lblUser, lblRole;
    JLabel lblTotalAssets, lblAssigned, lblAvailable;
    JButton btnAdd, btnAssign, btnReturn, btnSearch, btnAudit, btnReport, btnLogout, btnManageUsers;

    AssetController assetController = new AssetController();

    public Dashboard() {
        setTitle("Airtel Asset Management | Dashboard");
        setSize(850, 650); // Increased height slightly for the extra button row
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        // --- ROOT PANEL SETTINGS ---
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBackground(AppStyle.DARK_BG);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setContentPane(mainPanel);

        // ================= TOP PANEL (Info Bar + Logout) =================
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        String user = SessionManager.getUsername();
        String role = SessionManager.getRole();

        // User Info Container (Left)
        JPanel userInfo = new JPanel(new GridLayout(2, 1));
        userInfo.setOpaque(false);

        lblUser = new JLabel("Welcome: " + (user != null ? user : "Unknown"));
        lblRole = new JLabel("Access: " + (role != null ? role : "Unknown"));

        lblUser.setForeground(AppStyle.AIRTEL_RED);
        lblUser.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblRole.setForeground(AppStyle.TEXT_DIM);
        lblRole.setFont(new Font("Segoe UI", Font.ITALIC, 14));

        userInfo.add(lblUser);
        userInfo.add(lblRole);

        // Logout Button (Right)
        btnLogout = new JButton("LOGOUT");
        AppStyle.styleDarkButton(btnLogout);
        btnLogout.setPreferredSize(new Dimension(120, 40));
        btnLogout.setBackground(new Color(40, 40, 40));
        btnLogout.setBorder(BorderFactory.createLineBorder(AppStyle.AIRTEL_RED, 1));

        topPanel.add(userInfo, BorderLayout.WEST);
        topPanel.add(btnLogout, BorderLayout.EAST);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        // ================= CENTER STATS (Cards) =================
        JPanel center = new JPanel(new GridLayout(1, 3, 20, 0));
        center.setOpaque(false);

        lblTotalAssets = createStatCard("Total Assets");
        lblAssigned = createStatCard("Assigned");
        lblAvailable = createStatCard("Available");

        center.add(lblTotalAssets);
        center.add(lblAssigned);
        center.add(lblAvailable);
        mainPanel.add(center, BorderLayout.CENTER);

        // ================= BUTTON PANEL (Actions) =================
        // Changed to 3 rows to accommodate Manage Users comfortably
        JPanel bottom = new JPanel(new GridLayout(3, 3, 15, 15));
        bottom.setOpaque(false);

        btnAdd = new JButton("Add Asset");
        btnAssign = new JButton("Assign Asset");
        btnReturn = new JButton("Return Asset");
        btnSearch = new JButton("Search");
        btnAudit = new JButton("Audit Logs");
        btnReport = new JButton("Reports");
        btnManageUsers = new JButton("Manage Users"); // New Button

        // Style the standard buttons
        JButton[] allButtons = {btnAdd, btnAssign, btnReturn, btnSearch, btnAudit, btnReport};
        for (JButton btn : allButtons) {
            AppStyle.styleDarkButton(btn);
            bottom.add(btn);
        }

        // --- ADMIN ONLY SECTION ---
        AppStyle.styleDarkButton(btnManageUsers);
        // Make the Manage Users button visually distinct
        btnManageUsers.setBorder(BorderFactory.createLineBorder(Color.CYAN, 1));

        if (role == null || !role.equalsIgnoreCase("ADMIN")) {
            btnAudit.setEnabled(false);
            btnReport.setEnabled(false);
            btnManageUsers.setVisible(false); // Hide completely if not Admin
            btnAudit.setBackground(new Color(60, 40, 40));
        } else {
            bottom.add(btnManageUsers);
        }

        mainPanel.add(bottom, BorderLayout.SOUTH);

        // ================= ACTIONS =================

        btnLogout.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                "Exit to Login screen?", "Confirm Logout",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                SessionManager.cleanSession();
                dispose();
                new LoginForm();
            }
        });

        btnAdd.addActionListener(e -> openForm(new AddAssetForm()));
        btnAssign.addActionListener(e -> openForm(new AssignAssetForm()));
        btnReturn.addActionListener(e -> openForm(new ReturnAssetForm()));
        btnSearch.addActionListener(e -> new SearchForm());
        btnAudit.addActionListener(e -> openForm(new AuditHistoryForm()));
        btnReport.addActionListener(e -> new ReportForm());

        // Action for the new Manage Users portal
        btnManageUsers.addActionListener(e -> openForm(new UserForm()));

        refreshDashboard();
        setVisible(true);
    }

    private JLabel createStatCard(String title) {
        JLabel label = new JLabel("0", SwingConstants.CENTER);
        label.setOpaque(true);
        label.setBackground(AppStyle.PANEL_BG);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Segoe UI", Font.BOLD, 28));

        TitledBorder border = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(AppStyle.AIRTEL_RED, 1), title);
        border.setTitleColor(AppStyle.TEXT_DIM);
        border.setTitleFont(new Font("Segoe UI", Font.PLAIN, 12));

        label.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        return label;
    }

    private void openForm(JFrame form) {
        form.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) { refreshDashboard(); }
        });
    }

    public void refreshDashboard() {
        int total = assetController.getTotalAssets();
        int assigned = assetController.getAssignedAssets();
        int available = assetController.getAvailableAssets();

        lblTotalAssets.setText(String.valueOf(total));
        lblAssigned.setText(String.valueOf(assigned));
        lblAvailable.setText(String.valueOf(available));
    }
}