package com.airtel.assistant.service;

import java.sql.ResultSet;
import com.airtel.assistant.repository.AssetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AssetService {

    @Autowired
    private AssetRepository repo;

    public boolean addAsset(String tag, String name, String serial, String brand, String model, String category) {
        if(name.isEmpty() || serial.isEmpty() || tag.isEmpty()) {
            return false;
        }
        return repo.addAsset(tag, name, serial, brand, model, category);
    }

    public ResultSet getAllAssets() {
        return repo.getAllAssets();
    }

    // THIS METHOD FIXES THE CONTROLLER ERROR
    public ResultSet searchAssets(String keyword) {
        return repo.searchAssets(keyword);
    }

    public int getTotalAssets() { return repo.countTotalAssets(); }
    public int getAssignedAssets() { return repo.countAssignedAssets(); }
    public int getAvailableAssets() { return repo.countAvailableAssets(); }
}