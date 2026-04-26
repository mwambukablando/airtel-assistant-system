package com.airtel.assistant.view;

import java.awt.*;
import javax.swing.*;
import com.airtel.assistant.service.AssetService;
import com.airtel.assistant.security.SessionManager;
import com.airtel.assistant.utils.AppStyle;

// REMOVED @Component - This is the most important change!
public class Dashboard extends JFrame {

    JLabel lblUser, lblRole;
    JLabel lblTotalAssets, lblAssigned, lblAvailable;
    JButton btnAdd, btnAssign, btnReturn, btnSearch, btnAudit, btnReport, btnLogout, btnManageUsers;

    // REMOVED @Autowired - You cannot autowire in a class Spring isn't managing.
    private AssetService assetService;

    // Create a constructor that takes the service as a parameter if you still want to use it locally.
    public Dashboard(AssetService assetService) {
        this.assetService = assetService;
        
        setTitle("Airtel Asset Management | Dashboard");
        setSize(850, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        // ... (The rest of your UI code)
    }

    public void refreshDashboard() {
        if (assetService != null) {
            int total = assetService.getTotalAssets();
            int assigned = assetService.getAssignedAssets();
            int available = assetService.getAvailableAssets();

            lblTotalAssets.setText(String.valueOf(total));
            lblAssigned.setText(String.valueOf(assigned));
            lblAvailable.setText(String.valueOf(available));
        }
    }
}