package com.airtel.assistant.view;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;
import javax.swing.border.TitledBorder;

import com.airtel.assistant.service.AssetService; // Changed from Controller to Service
import com.airtel.assistant.security.SessionManager;
import com.airtel.assistant.utils.AppStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component // Added so Spring can manage this if needed
public class Dashboard extends JFrame {

    JLabel lblUser, lblRole;
    JLabel lblTotalAssets, lblAssigned, lblAvailable;
    JButton btnAdd, btnAssign, btnReturn, btnSearch, btnAudit, btnReport, btnLogout, btnManageUsers;

    // FIX: Use the Service directly and avoid 'new'
    @Autowired
    private AssetService assetService;

    public Dashboard() {
        // ... (The UI code remains the same as your previous version)
        setTitle("Airtel Asset Management | Dashboard");
        setSize(850, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        // ... (Layout and Labels logic remains the same)
        
        // We call refresh at the end
        // Note: In Swing + Spring, refreshDashboard() might need to be called 
        // after the Bean is fully initialized.
    }

    // ... (createStatCard and other UI helper methods stay the same)

    public void refreshDashboard() {
        // FIX: Talk to the Service instead of the Controller
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