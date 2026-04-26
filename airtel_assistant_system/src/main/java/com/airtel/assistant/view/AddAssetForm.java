package com.airtel.assistant.view;

import java.awt.*;
import java.sql.ResultSet;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import com.airtel.assistant.controller.AssetController;
import com.airtel.assistant.utils.AppStyle;

public class AddAssetForm extends JFrame {

    // Added new fields: txtTag, txtBrand, txtModel
    JTextField txtTag, txtName, txtSerial, txtBrand, txtModel, txtType;
    JButton btnSave, btnBack;

    DefaultTableModel model;
    JTable table;
    JScrollPane scrollPane;

    AssetController controller = new AssetController();

    public AddAssetForm() {
        setTitle("Airtel Asset Management | Add Asset");
        setSize(850, 700); // Increased size to fit new fields
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(AppStyle.DARK_BG);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setContentPane(mainPanel);

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        // Updated GridLayout to 7 rows to accommodate new fields
        JPanel formPanel = new JPanel(new GridLayout(7, 2, 10, 15)); 
        formPanel.setOpaque(false);

        formPanel.add(createStyledLabel("Asset Tag:"));
        txtTag = new JTextField(); AppStyle.styleTextField(txtTag);
        formPanel.add(txtTag);

        formPanel.add(createStyledLabel("Device Name:"));
        txtName = new JTextField(); AppStyle.styleTextField(txtName);
        formPanel.add(txtName);

        formPanel.add(createStyledLabel("Serial Number:"));
        txtSerial = new JTextField(); AppStyle.styleTextField(txtSerial);
        formPanel.add(txtSerial);

        formPanel.add(createStyledLabel("Brand:"));
        txtBrand = new JTextField(); AppStyle.styleTextField(txtBrand);
        formPanel.add(txtBrand);

        formPanel.add(createStyledLabel("Model:"));
        txtModel = new JTextField(); AppStyle.styleTextField(txtModel);
        formPanel.add(txtModel);

        formPanel.add(createStyledLabel("Category/Type:"));
        txtType = new JTextField(); AppStyle.styleTextField(txtType);
        formPanel.add(txtType);

        btnSave = new JButton("SAVE ASSET");
        AppStyle.styleDarkButton(btnSave);

        btnBack = new JButton("DISCARD & BACK");
        AppStyle.styleDarkButton(btnBack);
        btnBack.setBackground(new Color(60, 60, 60));

        formPanel.add(btnSave);
        formPanel.add(btnBack);

        mainPanel.add(formPanel, BorderLayout.NORTH);

        // --- Table Logic ---
        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(model);
        model.addColumn("ID");
        model.addColumn("Tag");
        model.addColumn("Name");
        model.addColumn("Serial");
        model.addColumn("Status");

        // (Table styling code remains same as your original)
        table.setBackground(AppStyle.PANEL_BG);
        table.setForeground(Color.WHITE);
        table.setGridColor(new Color(60, 60, 60));
        table.setRowHeight(30);

        scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(AppStyle.DARK_BG);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

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
        String tag = txtTag.getText().trim();
        String name = txtName.getText().trim();
        String serial = txtSerial.getText().trim();
        String brand = txtBrand.getText().trim();
        String modelName = txtModel.getText().trim();
        String type = txtType.getText().trim();

        if (name.isEmpty() || serial.isEmpty() || tag.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Core fields (Tag, Name, Serial) are required!", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // FIX: Now calling the Controller with all 6 parameters
        boolean success = controller.addAsset(tag, name, serial, brand, modelName, type);

        if (success) {
            JOptionPane.showMessageDialog(this, "Asset added to inventory.");
            txtTag.setText(""); txtName.setText(""); txtSerial.setText("");
            txtBrand.setText(""); txtModel.setText(""); txtType.setText("");
            loadAssets();
        } else {
            JOptionPane.showMessageDialog(this, "Error: Duplicate Serial/Tag or DB error.", "System Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadAssets() {
        try {
            model.setRowCount(0);
            ResultSet rs = controller.getAssets();
            while (rs != null && rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("asset_tag"),
                    rs.getString("name"),
                    rs.getString("serial_number"),
                    rs.getString("status")
                });
            }
        } catch (Exception e) { e.printStackTrace(); }
    }
}