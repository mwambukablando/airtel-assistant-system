package com.airtel.assistant.controller;

import com.airtel.assistant.model.Asset;
import com.airtel.assistant.service.AssetService;
import com.airtel.assistant.service.AssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Controller
public class AssignmentController {

    @Autowired
    private AssignmentService service;

    @Autowired
    private AssetService assetService; // Needed to load the dropdown list

    // --- 1. ADDED THIS: The route to actually SHOW the assign-asset.html page ---
    @GetMapping("/assets/assign")
    public String showAssignForm(Model model) {
        model.addAttribute("assets", loadAvailableAssetsOnly());
        return "assign-asset"; // This must match your HTML file name exactly
    }

    // --- 2. WEB ROUTE TO SAVE THE ASSIGNMENT ---
    @PostMapping("/assignments/save")
    public String saveAssignment(@RequestParam int assetId,
                                 @RequestParam String employeeName,
                                 @RequestParam String department,
                                 @RequestParam String assignDate) {
        
        boolean success = service.assignAsset(assetId, employeeName, department, assignDate);
        
        if (success) {
            return "redirect:/dashboard"; 
        } else {
            return "redirect:/assets/assign?error=true";
        }
    }

    // --- HELPER: Logic to filter assets for the dropdown ---
    private List<Asset> loadAvailableAssetsOnly() {
        List<Asset> list = new ArrayList<>();
        try (ResultSet rs = assetService.getAllAssets()) {
            while (rs != null && rs.next()) {
                // We only add to dropdown if the Smart Logic says it's AVAILABLE
                if ("AVAILABLE".equals(rs.getString("calculated_status"))) {
                    Asset asset = new Asset();
                    asset.setAssetId(rs.getInt("asset_id"));
                    asset.setAssetTag(rs.getString("asset_tag"));
                    asset.setDeviceName(rs.getString("device_name"));
                    list.add(asset);
                }
            }
        } catch (Exception e) { 
            e.printStackTrace(); 
        }
        return list;
    }

    // --- SWING BRIDGE ---
    public boolean assignAsset(int assetId, String employee, String department, String date) {
        return service.assignAsset(assetId, employee, department, date);
    }
}