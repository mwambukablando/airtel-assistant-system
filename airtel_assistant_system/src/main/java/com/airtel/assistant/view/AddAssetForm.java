package com.airtel.assistant.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.sql.ResultSet;

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
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import com.airtel.assistant.controller.AssetController;
import com.airtel.assistant.utils.AppStyle;

public class AddAssetForm extends JFrame {

    JTextField txtName, txtSerial, txtType;
    JButton btnSave, btnBack;

    DefaultTableModel model;
    JTable table;
    JScrollPane scrollPane;

    AssetController controller = new AssetController();

    public AddAssetForm() {
        setTitle("Airtel Asset Management | Add Asset");
        setSize(800, 600);
        setLocationRelativeTo(null);

        // --- ROOT PANEL ---
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(AppStyle.DARK_BG);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setContentPane(mainPanel);

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        // ================= TOP FORM PANEL (CSS STYLED) =================
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 15)); // Reduced rows, increased gap
        formPanel.setOpaque(false);

        formPanel.add(createStyledLabel("Asset Name:"));
        txtName = new JTextField();
        AppStyle.styleTextField(txtName);
        formPanel.add(txtName);

        formPanel.add(createStyledLabel("Serial Number:"));
        txtSerial = new JTextField();
        AppStyle.styleTextField(txtSerial);
        formPanel.add(txtSerial);

        formPanel.add(createStyledLabel("Asset Type:"));
        txtType = new JTextField();
        AppStyle.styleTextField(txtType);
        formPanel.add(txtType);

        btnSave = new JButton("SAVE ASSET");
        AppStyle.styleDarkButton(btnSave);

        btnBack = new JButton("DISCARD & BACK");
        AppStyle.styleDarkButton(btnBack);
        btnBack.setBackground(new Color(60, 60, 60)); // Greyish for "Back"
        btnBack.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        formPanel.add(btnSave);
        formPanel.add(btnBack);

        mainPanel.add(formPanel, BorderLayout.NORTH);

        // ================= TABLE PANEL (DARK MODE TABLE CSS) =================
        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        table = new JTable(model);
        model.addColumn("ID");
        model.addColumn("Name");
        model.addColumn("Serial");
        model.addColumn("Type");
        model.addColumn("Status");

        // --- STYLING THE TABLE ---
        table.setBackground(AppStyle.PANEL_BG);
        table.setForeground(Color.WHITE);
        table.setGridColor(new Color(60, 60, 60));
        table.setRowHeight(30);
        table.setSelectionBackground(AppStyle.AIRTEL_RED);
        table.setSelectionForeground(Color.WHITE);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        // --- STYLING THE HEADER ---
        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(20, 20, 20));
        header.setForeground(AppStyle.AIRTEL_RED);
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBorder(BorderFactory.createLineBorder(new Color(40, 40, 40)));

        scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(AppStyle.DARK_BG); // Fixes the white area below table
        scrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(60, 60, 60)),
                "INVENTORY PREVIEW", TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 12), AppStyle.TEXT_DIM));

        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // ================= BUTTON ACTIONS =================
        btnSave.addActionListener(e -> saveAsset());
        btnBack.addActionListener(e -> this.dispose());

        loadAssets();
        setVisible(true);
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(AppStyle.TEXT_WHITE);
        label.setFont(AppStyle.NORMAL_FONT);
        return label;
    }

    private void saveAsset() {
        String name = txtName.getText().trim();
        String serial = txtSerial.getText().trim();
        String type = txtType.getText().trim();

        if (name.isEmpty() || serial.isEmpty() || type.isEmpty()) {
            styleOptionPane();
            JOptionPane.showMessageDialog(this, "All fields are required!", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean success = controller.addAsset(name, serial, type);

        if (success) {
            styleOptionPane();
            JOptionPane.showMessageDialog(this, "Asset added to inventory.");
            txtName.setText("");
            txtSerial.setText("");
            txtType.setText("");
            loadAssets();
        } else {
            styleOptionPane();
            JOptionPane.showMessageDialog(this, "Error: Duplicate Serial Number.", "System Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void styleOptionPane() {
        UIManager.put("OptionPane.background", AppStyle.DARK_BG);
        UIManager.put("Panel.background", AppStyle.DARK_BG);
        UIManager.put("OptionPane.messageForeground", Color.WHITE);
    }

    private void loadAssets() {
        try {
            model.setRowCount(0);
            ResultSet rs = controller.getAssets();
            while (rs != null && rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("serial_number"),
                    rs.getString("type"),
                    rs.getString("status")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}