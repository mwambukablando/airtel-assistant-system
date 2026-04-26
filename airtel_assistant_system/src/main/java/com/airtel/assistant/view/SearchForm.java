package com.airtel.assistant.view;

import java.awt.*;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
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

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(AppStyle.DARK_BG);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setContentPane(mainPanel);

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        txtSearch = new JTextField(25);
        AppStyle.styleTextField(txtSearch);
        btnSearch = new JButton("FIND");
        AppStyle.styleDarkButton(btnSearch);
        btnBack = new JButton("BACK");
        AppStyle.styleDarkButton(btnBack);

        JPanel box = new JPanel(new FlowLayout(FlowLayout.LEFT));
        box.setOpaque(false);
        box.add(txtSearch); box.add(btnSearch); box.add(btnBack);
        top.add(box, BorderLayout.CENTER);
        mainPanel.add(top, BorderLayout.NORTH);

        model = new DefaultTableModel() { @Override public boolean isCellEditable(int r, int c) { return false; } };
        table = new JTable(model);
        model.addColumn("ID"); model.addColumn("Name"); model.addColumn("Serial"); model.addColumn("Tag"); model.addColumn("Status");
        mainPanel.add(new JScrollPane(table), BorderLayout.CENTER);

        btnSearch.addActionListener(e -> search());
        btnBack.addActionListener(e -> this.dispose());
        setVisible(true);
    }

    private void search() {
        String keyword = txtSearch.getText().trim();
        model.setRowCount(0);
        List<Map<String, String>> results = controller.searchAssets(keyword);
        if (results != null) {
            for (Map<String, String> row : results) {
                model.addRow(new Object[]{row.get("id"), row.get("name"), row.get("serial"), row.get("tag"), row.get("status")});
            }
        }
    }
}