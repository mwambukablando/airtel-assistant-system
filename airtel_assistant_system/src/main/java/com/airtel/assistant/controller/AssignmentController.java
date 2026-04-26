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
    private UserRepository userRepo; 

    @GetMapping("/assets/assign")
    public String showAssignForm(Model model) {
        model.addAttribute("assets", loadAvailableAssetsOnly());
        model.addAttribute("registeredUsers", userRepo.getAllUsersList());
        return "assign-asset"; 
    }

    @PostMapping("/assignments/save")
    public String saveAssignment(@RequestParam int assetId,
                                 @RequestParam String username, 
                                 @RequestParam String department,
                                 @RequestParam String assignDate) {
        
        boolean success = service.assignAsset(assetId, username, department, assignDate);
        
        if (success) {
            return "redirect:/dashboard"; 
        } else {
            return "redirect:/assets/assign?error=true";
        }
    }

    // --- FIXED: Now works with List<Map> from AssetService ---
    private List<Asset> loadAvailableAssetsOnly() {
        List<Asset> list = new ArrayList<>();
        // assetService.getAllAssets() now returns List<Map<String, String>>
        List<Map<String, String>> dbAssets = assetService.getAllAssets();
        
        for (Map<String, String> row : dbAssets) {
            // Only add to the dropdown if it is actually AVAILABLE
            if ("AVAILABLE".equals(row.get("status"))) {
                Asset asset = new Asset();
                // Convert the String ID back to an Integer for the model
                asset.setAssetId(Integer.parseInt(row.get("id")));
                asset.setAssetTag(row.get("tag"));
                asset.setDeviceName(row.get("name"));
                list.add(asset);
            }
        }
        return list;
    }

    public boolean assignAsset(int assetId, String employee, String department, String date) {
        return service.assignAsset(assetId, employee, department, date);
    }
}