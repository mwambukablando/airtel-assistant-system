package com.airtel.assistant.service;

import com.airtel.assistant.repository.AssetRepository;
import com.airtel.assistant.repository.AuditLogRepository; // Added for logs
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
public class AssetService {

    @Autowired
    private AssetRepository repo;

    @Autowired
    private AuditLogRepository auditLog; // Added for logs

    public boolean addAsset(String tag, String name, String serial, String brand, String model, String category) {
        if(name == null || name.isEmpty() || serial == null || serial.isEmpty() || tag == null || tag.isEmpty()) {
            return false;
        }
        
        // Original logic
        boolean success = repo.addAsset(tag, name, serial, brand, model, category);
        
        // New Hook: Only logs if the database save was successful
        if(success) {
            auditLog.logAction(0, "ADDED NEW ASSET: " + name + " (Tag: " + tag + ")");
        }
        
        return success;
    }

    public List<Map<String, String>> getAllAssets() {
        return repo.searchAssetsList(""); 
    }

    public List<Map<String, String>> searchAssets(String keyword) {
        return repo.searchAssetsList(keyword);
    }

    // I kept all these dashboard methods exactly as you had them:
    public int getTotalAssets() { 
        return repo.countTotalAssets(); 
    }
    
    public int getAssignedAssets() { 
        return repo.countAssignedAssets(); 
    }
    
    public int getAvailableAssets() { 
        return repo.countAvailableAssets(); 
    }
}