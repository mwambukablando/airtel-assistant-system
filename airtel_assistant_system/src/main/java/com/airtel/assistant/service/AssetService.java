package com.airtel.assistant.service;

import java.sql.ResultSet;
import com.airtel.assistant.repository.AssetRepository;
import org.springframework.beans.factory.annotation.Autowired; // Added
import org.springframework.stereotype.Service; // Added

@Service // Tells Spring to manage this service
public class AssetService {

    @Autowired // Replaces "new" - Spring will now provide the Repository with DB config loaded
    private AssetRepository repo;

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