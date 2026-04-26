package com.airtel.assistant.controller;

import com.airtel.assistant.model.Asset;
import com.airtel.assistant.repository.UserRepository;
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
import java.util.Map;

@Controller
public class AssignmentController {

    @Autowired
    private AssignmentService service;

    @Autowired
    private AssetService assetService;

    @Autowired
    private UserRepository userRepo; // Injected to pull the registered users list

    // --- SHOW THE FORM WITH ASSETS AND REGISTERED USERS ---
    @GetMapping("/assets/assign")
    public String showAssignForm(Model model) {
        model.addAttribute("assets", loadAvailableAssetsOnly());
        // Pulling the list we created in Manage Users
        model.addAttribute("registeredUsers", userRepo.getAllUsersList());
        return "assign-asset"; 
    }

    // --- SAVE THE ASSIGNMENT ---
    @PostMapping("/assignments/save")
    public String saveAssignment(@RequestParam int assetId,
                                 @RequestParam String username, // Changed to match HTML 'name'
                                 @RequestParam String department,
                                 @RequestParam String assignDate) {
        
        boolean success = service.assignAsset(assetId, username, department, assignDate);
        
        if (success) {
            return "redirect:/dashboard"; 
        } else {
            return "redirect:/assets/assign?error=true";
        }
    }

    private List<Asset> loadAvailableAssetsOnly() {
        List<Asset> list = new ArrayList<>();
        try (ResultSet rs = assetService.getAllAssets()) {
            while (rs != null && rs.next()) {
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

    public boolean assignAsset(int assetId, String employee, String department, String date) {
        return service.assignAsset(assetId, employee, department, date);
    }
}