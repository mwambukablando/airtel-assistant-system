package com.airtel.assistant.controller;

import com.airtel.assistant.model.Asset;
import com.airtel.assistant.service.AssetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class AssetController {

    @Autowired
    private AssetService service;

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

    // BRIDGE: Satisfaction for AddAssetForm logic
    public boolean addAsset(String tag, String name, String serial, String brand, String model, String category) {
        return service.addAsset(tag, name, serial, brand, model, category);
    }

    // BRIDGE: Satisfaction for AddAssetForm.java:141
    public List<Map<String, String>> getAssets() {
        return service.getAllAssets();
    }

    // BRIDGE: Satisfaction for SearchForm.java:142
    public List<Map<String, String>> searchAssets(String keyword) {
        return service.searchAssets(keyword);
    }

    private List<Asset> loadAssetsForWeb() {
        List<Asset> list = new ArrayList<>();
        List<Map<String, String>> dbData = service.getAllAssets();
        if (dbData != null) {
            for (Map<String, String> row : dbData) {
                Asset asset = new Asset();
                asset.setAssetId(Integer.parseInt(row.get("id"))); 
                asset.setAssetTag(row.get("tag"));
                asset.setDeviceName(row.get("name"));
                asset.setSerialNumber(row.get("serial"));
                asset.setBrand(row.get("brand"));
                asset.setStatus(row.get("status"));
                list.add(asset);
            }
        }
        return list;
    }
}