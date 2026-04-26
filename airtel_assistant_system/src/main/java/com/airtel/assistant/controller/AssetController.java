package com.airtel.assistant.controller;

import java.sql.ResultSet;

import com.airtel.assistant.service.AssetService;

public class AssetController {

    private AssetService service = new AssetService();

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