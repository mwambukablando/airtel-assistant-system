package com.airtel.assistant.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.sql.ResultSet;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import com.airtel.assistant.controller.AssetController;
import com.airtel.assistant.utils.AppStyle;

public class SearchForm extends JFrame {

    JTextField txtSearch;
    JButton btnSearch, btnBack;
    JTable table;
    DefaultTableModel model;
    AssetController controller = new AssetController();

    public SearchForm() {
        setTitle("Airtel | Global Asset Search");
        setSize(850, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        // --- ROOT PANEL ---
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(AppStyle.DARK_BG);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        setContentPane(mainPanel);

        // ================= TOP SEARCH BAR PANEL =================
        JPanel top = new JPanel(new BorderLayout(15, 0));
        top.setOpaque(false);
        top.setBorder(new EmptyBorder(0, 0, 10, 0));

        // Keyword Label
        JLabel lblKeyword = new JLabel("ENTER KEYWORD:");
        lblKeyword.setForeground(AppStyle.TEXT_DIM);
        lblKeyword.setFont(new Font("Segoe UI", Font.BOLD, 12));
        top.add(lblKeyword, BorderLayout.NORTH);

        // Search Input & Buttons Container
        JPanel searchBox = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 10));
        searchBox.setOpaque(false);

        txtSearch = new JTextField(25);
        AppStyle.styleTextField(txtSearch);
        txtSearch.setPreferredSize(new Dimension(350, 40)); // Wider and taller

        btnSearch = new JButton("FIND ASSET");
        AppStyle.styleDarkButton(btnSearch);
        btnSearch.setPreferredSize(new Dimension(150, 40));

        btnBack = new JButton("BACK");
        AppStyle.styleDarkButton(btnBack);
        btnBack.setBackground(new Color(60, 60, 60));
        btnBack.setPreferredSize(new Dimension(100, 40));

        searchBox.add(txtSearch);
        searchBox.add(Box.createHorizontalStrut(10)); // Spacer
        searchBox.add(btnSearch);
        searchBox.add(Box.createHorizontalStrut(10)); // Spacer
        searchBox.add(btnBack);

        top.add(searchBox, BorderLayout.CENTER);
        mainPanel.add(top, BorderLayout.NORTH);

        // ================= RESULTS TABLE =================
        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        table = new JTable(model);
        model.addColumn("ID");
        model.addColumn("Asset Name");
        model.addColumn("Serial Number");
        model.addColumn("Type");
        model.addColumn("Status");

        // --- TABLE STYLING ---
        table.setBackground(AppStyle.PANEL_BG);
        table.setForeground(Color.WHITE);
        table.setGridColor(new Color(60, 60, 60));
        table.setRowHeight(35);
        table.setSelectionBackground(AppStyle.AIRTEL_RED);
        table.setSelectionForeground(Color.WHITE);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setShowVerticalLines(false);

        // --- HEADER STYLING ---
        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(20, 20, 20));
        header.setForeground(AppStyle.AIRTEL_RED);
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setPreferredSize(new Dimension(100, 40));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(AppStyle.DARK_BG);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 60)));

        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // ACTIONS
        btnSearch.addActionListener(e -> search());
        btnBack.addActionListener(e -> this.dispose());

        // ENTER key listener for searching
        txtSearch.addActionListener(e -> search());

        setVisible(true);
    }

    private void search() {
        String keyword = txtSearch.getText().trim();
        if (keyword.isEmpty()) {
            styleOptionPane();
            JOptionPane.showMessageDialog(this, "Type something to search!", "Search Empty", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            model.setRowCount(0);
            ResultSet rs = controller.searchAssets(keyword);

            boolean found = false;
            if (rs != null) {
                while (rs.next()) {
                    found = true;
                    model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("serial_number"),
                        rs.getString("type"),
                        rs.getString("status")
                    });
                }
                rs.close();
            }

            if (!found) {
                styleOptionPane();
                JOptionPane.showMessageDialog(this, "No matching records for: " + keyword);
            }
        } catch (Exception e) {
            e.printStackTrace();
            styleOptionPane();
            JOptionPane.showMessageDialog(this, "System error during search.");
        }
    }

    private void styleOptionPane() {
        UIManager.put("OptionPane.background", AppStyle.DARK_BG);
        UIManager.put("Panel.background", AppStyle.DARK_BG);
        UIManager.put("OptionPane.messageForeground", Color.WHITE);
    }
}