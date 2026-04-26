package com.airtel.assistant.controller;

import com.airtel.assistant.model.Asset;
import com.airtel.assistant.service.AssetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class AssetController {

    @Autowired
    private AssetService service;

    // ================= WEB ROUTES (For Render) =================

    @GetMapping("/assets/add")
    public String showAddAssetForm(Model model) {
        model.addAttribute("assets", loadAssetsForWeb());
        return "add-asset";
    }

    @PostMapping("/assets/save")
    public String saveAsset(@RequestParam String assetTag,
                            @RequestParam String deviceName,
                            @RequestParam String serialNumber,
                            @RequestParam String brand,
                            @RequestParam String modelName,
                            @RequestParam String category,
                            Model model) {
        
        boolean success = addAsset(assetTag, deviceName, serialNumber, brand, modelName, category);
        
        if (success) {
            return "redirect:/assets/add";
        } else {
            model.addAttribute("error", "Error: Database failure.");
            model.addAttribute("assets", loadAssetsForWeb());
            return "add-asset";
        }
    }

    // ================= SWING BRIDGE METHODS (Updated to avoid errors) =================

    public boolean addAsset(String tag, String name, String serial, String brand, String model, String category) {
        return service.addAsset(tag, name, serial, brand, model, category);
    }

    // Note: Since we moved to List<Map> for Web, if your Swing app strictly 
    // requires a ResultSet, we would need a separate legacy method. 
    // For now, these are changed to return the List to stop the red lines.
    public List<Map<String, String>> getAssets() {
        return service.getAllAssets();
    }

    public List<Map<String, String>> searchAssets(String keyword) {
        return service.searchAssets(keyword);
    }

    // ================= HELPER METHODS (Web Logic) =================

    private List<Asset> loadAssetsForWeb() {
        List<Asset> list = new ArrayList<>();
        // FIXED: Now uses the List<Map> coming from service
        List<Map<String, String>> dbData = service.getAllAssets();
        
        for (Map<String, String> row : dbData) {
            Asset asset = new Asset();
            // We use the keys we defined in AssetRepository.searchAssetsList
            asset.setAssetId(Integer.parseInt(row.get("id"))); 
            asset.setAssetTag(row.get("tag"));
            asset.setDeviceName(row.get("name"));
            asset.setSerialNumber(row.get("serial"));
            asset.setBrand(row.get("brand"));
            asset.setStatus(row.get("status"));
            list.add(asset);
        }
        return list;
    }
}