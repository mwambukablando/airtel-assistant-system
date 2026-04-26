package com.airtel.assistant.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import com.airtel.assistant.config.DatabaseConfig;
import com.airtel.assistant.utils.AppStyle;

public class UserForm extends JFrame {
    JTextField txtFullname, txtUsername;
    JButton btnSave, btnBack;
    DefaultTableModel model;
    JTable table;

    public UserForm() {
        setTitle("Airtel | User Management Portal");
        setSize(800, 600);
        setLocationRelativeTo(null);
        getContentPane().setBackground(AppStyle.DARK_BG);
        setLayout(new BorderLayout(20, 20));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // --- FORM PANEL (Simplified) ---
        // Only 2 rows for Full Name and Username
        JPanel form = new JPanel(new GridLayout(3, 2, 10, 15));
        form.setOpaque(false);
        form.setBorder(new EmptyBorder(20, 20, 20, 20));

        form.add(createLabel("Full Name:"));
        txtFullname = new JTextField();
        AppStyle.styleTextField(txtFullname);
        form.add(txtFullname);

        form.add(createLabel("Username:"));
        txtUsername = new JTextField();
        AppStyle.styleTextField(txtUsername);
        form.add(txtUsername);

        btnSave = new JButton("REGISTER AS USER");
        AppStyle.styleDarkButton(btnSave);
        form.add(btnSave);

        btnBack = new JButton("BACK");
        AppStyle.styleDarkButton(btnBack);
        btnBack.setBackground(new Color(60, 60, 60));
        form.add(btnBack);

        add(form, BorderLayout.NORTH);

        // --- TABLE SECTION ---
        model = new DefaultTableModel(new String[]{"ID", "Full Name", "Username", "System Role"}, 0);
        table = new JTable(model);
        styleTable(table);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(AppStyle.DARK_BG);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, BorderLayout.CENTER);

        // --- ACTIONS ---
        btnSave.addActionListener(e -> saveUser());
        btnBack.addActionListener(e -> dispose());

        loadUsers();
        setVisible(true);
    }

    private void saveUser() {
        // Automatically setting password to 'airtel123' and role to 'User'
        String sql = "INSERT INTO users (fullname, username, password, role) VALUES (?, ?, 'airtel123', 'User')";

        String full = txtFullname.getText().trim();
        String user = txtUsername.getText().trim();

        if (full.isEmpty() || user.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please provide both Full Name and Username.");
            return;
        }

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, full);
            ps.setString(2, user);
            ps.executeUpdate();

            // Styled Success Message
            UIManager.put("OptionPane.background", AppStyle.DARK_BG);
            UIManager.put("Panel.background", AppStyle.DARK_BG);
            UIManager.put("OptionPane.messageForeground", Color.WHITE);
            JOptionPane.showMessageDialog(this, "User registered! Default password: airtel123");

            txtFullname.setText("");
            txtUsername.setText("");
            loadUsers();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: Username might already exist.");
        }
    }

    private void loadUsers() {
        model.setRowCount(0);
        try (Connection conn = DatabaseConfig.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT user_id, fullname, username, role FROM users")) {
            while(rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("user_id"),
                    rs.getString("fullname"),
                    rs.getString("username"),
                    rs.getString("role")
                });
            }
        } catch (Exception ex) { ex.printStackTrace(); }
    }

    private JLabel createLabel(String t) {
        JLabel l = new JLabel(t);
        l.setForeground(Color.WHITE);
        l.setFont(AppStyle.NORMAL_FONT);
        return l;
    }

    private void styleTable(JTable t) {
        t.setBackground(AppStyle.PANEL_BG);
        t.setForeground(Color.WHITE);
        t.setRowHeight(35);
        t.setSelectionBackground(AppStyle.AIRTEL_RED);
        t.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JTableHeader header = t.getTableHeader();
        header.setBackground(new Color(30, 30, 30));
        header.setForeground(AppStyle.AIRTEL_RED);
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
    }
}