package com.airtel.assistant.controller;

import java.sql.ResultSet;
import com.airtel.assistant.service.AssetService;
import org.springframework.beans.factory.annotation.Autowired; // Added
import org.springframework.stereotype.Controller; // Added

@Controller // This makes the class a "Bean" so LoginController can find it
public class AssetController {

    @Autowired // Spring will now inject AssetService with all DB variables loaded
    private AssetService service;

    public boolean addAsset(String name, String serial, String type) {
        return service.addAsset(name, serial, type);
    }

    public ResultSet getAssets() {
        return service.getAllAssets();
    }

    public boolean updateStatus(int id, String status) {
        return service.updateStatus(id, status);
    }

    public int getTotalAssets() {
        return service.getTotalAssets();
    }

    public int getAssignedAssets() {
        return service.getAssignedAssets();
    }

    public int getAvailableAssets() {
        return service.getAvailableAssets();
    }

    public ResultSet searchAssets(String keyword) {
        return service.searchAssets(keyword);
    }
}