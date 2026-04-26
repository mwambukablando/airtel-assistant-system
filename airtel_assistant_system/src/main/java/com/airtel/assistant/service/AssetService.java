package com.airtel.assistant.service;

import com.airtel.assistant.repository.AssetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AssetService {

    @Autowired
    private AssetRepository repo;

    public boolean addAsset(String tag, String name, String serial, String brand, String model, String category) {
        if(name == null || name.isEmpty() || serial == null || serial.isEmpty() || tag == null || tag.isEmpty()) {
            return false;
        }
        return repo.addAsset(tag, name, serial, brand, model, category);
    }

    // --- FIXED: Now returns List<Map> to match the new Repository logic ---
    public List<Map<String, String>> getAllAssets() {
        // We use the keyword search with an empty string to get all records
        return repo.searchAssetsList(""); 
    }

    // --- FIXED: This clears the error for your Search functionality ---
    public List<Map<String, String>> searchAssets(String keyword) {
        return repo.searchAssetsList(keyword);
    }

    public int getTotalAssets() { return repo.countTotalAssets(); }
    public int getAssignedAssets() { return repo.countAssignedAssets(); }
    public int getAvailableAssets() { return repo.countAvailableAssets(); }
}