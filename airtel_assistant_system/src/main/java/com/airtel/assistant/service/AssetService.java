package com.airtel.assistant.service;

import java.sql.ResultSet;

import com.airtel.assistant.repository.AssetRepository;

public class AssetService {

    private AssetRepository repo = new AssetRepository();

    public boolean addAsset(String name, String serial, String type) {

        if(name.isEmpty() || serial.isEmpty() || type.isEmpty()) {
            return false;
        }

        return repo.addAsset(name, serial, type);
    }

    public ResultSet getAllAssets() {
        return repo.getAllAssets();
    }

    public boolean updateStatus(int id, String status) {
        return repo.updateStatus(id, status);
    }
    public int getTotalAssets() {
        return repo.countTotalAssets();
    }

    public int getAssignedAssets() {
        return repo.countAssignedAssets();
    }

    public int getAvailableAssets() {
        return repo.countAvailableAssets();
    }
    public ResultSet searchAssets(String keyword) {
        return repo.searchAssets(keyword);
    }

}