package com.airtel.assistant.service;

import com.airtel.assistant.repository.AssetRepository;
import com.airtel.assistant.repository.ReturnAssetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReturnService {

    @Autowired
    private ReturnAssetRepository returnRepo;

    @Autowired
    private AssetRepository assetRepo;

    public boolean returnAsset(int assetId, String date, String condition) {
        if(condition == null || condition.isEmpty()) {
            return false;
        }

        boolean saved = returnRepo.returnAsset(assetId, date, condition);

        if(saved) {
            // This now matches the updateStatus(int, String) in AssetRepository
            assetRepo.updateStatus(assetId, "AVAILABLE");
        }

        return saved;
    }
}