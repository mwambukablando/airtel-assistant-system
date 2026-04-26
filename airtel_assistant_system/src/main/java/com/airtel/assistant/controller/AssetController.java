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

@Controller
public class AssetController {

    @Autowired
    private AssetService service;

    // ================= WEB ROUTES =================

    @GetMapping("/assets/add")
    public String showAddAssetForm(Model model) {
        // Loads existing assets to show in the table on the 'Add Asset' page
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
            // Redirect back to the form to see the new entry in the list
            return "redirect:/assets/add";
        } else {
            model.addAttribute("error", "Error: Duplicate serial or database failure.");
            model.addAttribute("assets", loadAssetsForWeb());
            return "add-asset";
        }
    }

    // ================= SWING BRIDGE METHODS =================
    // Used by AddAssetForm.java and SearchForm.java

    public boolean addAsset(String tag, String name, String serial, String brand, String model, String category) {
        return service.addAsset(tag, name, serial, brand, model, category);
    }

    public ResultSet getAssets() {
        try {
            return service.getAllAssets();
        } catch (Exception e) {
            return null;
        }
    }

    public ResultSet searchAssets(String keyword) {
        try {
            return service.searchAssets(keyword);
        } catch (Exception e) {
            return null;
        }
    }

    // ================= HELPER METHODS =================

    private List<Asset> loadAssetsForWeb() {
        List<Asset> list = new ArrayList<>();
        try (ResultSet rs = service.getAllAssets()) {
            while (rs != null && rs.next()) {
                Asset asset = new Asset();
                asset.setAssetId(rs.getInt("id"));
                asset.setAssetTag(rs.getString("asset_tag"));
                asset.setDeviceName(rs.getString("name"));
                asset.setSerialNumber(rs.getString("serial_number"));
                asset.setBrand(rs.getString("brand"));
                asset.setStatus(rs.getString("status"));
                list.add(asset);
            }
        } catch (Exception e) { 
            e.printStackTrace(); 
        }
        return list;
    }
}