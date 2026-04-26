package com.airtel.assistant.service;

import com.airtel.assistant.repository.AssetRepository;
import com.airtel.assistant.repository.ReturnAssetRepository;

public class ReturnService {

    private ReturnAssetRepository returnRepo = new ReturnAssetRepository();
    private AssetRepository assetRepo = new AssetRepository();

    public boolean returnAsset(int assetId, String date, String condition) {

        if(condition.isEmpty()) {
            return false;
        }

        // 1. Save return record
        boolean saved = returnRepo.returnAsset(assetId, date, condition);

        // 2. Update asset status back to AVAILABLE
        if(saved) {
            assetRepo.updateStatus(assetId, "AVAILABLE");
        }

        return saved;
    }
}