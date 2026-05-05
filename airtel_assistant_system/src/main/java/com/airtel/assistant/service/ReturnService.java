package com.airtel.assistant.service;

import com.airtel.assistant.repository.AssetRepository;
import com.airtel.assistant.repository.AssignmentRepository; // Added for ID mapping
import com.airtel.assistant.repository.ReturnAssetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReturnService {

    @Autowired
    private ReturnAssetRepository returnRepo;

    @Autowired
    private AssetRepository assetRepo;

    @Autowired
    private AssignmentRepository assignmentRepo;

    @Transactional
    public boolean returnAsset(int assetId, String date, String condition) {
        // Validation check
        if(condition == null || condition.isEmpty()) {
            return false;
        }

        // 1. Find the active Assignment ID for this specific asset
        // This ensures we link to the 'assign_id' shown in your database
        Integer activeAssignId = assignmentRepo.findActiveIdByAssetId(assetId);

        if (activeAssignId == null) {
            return false; // No active assignment found for this asset
        }

        // 2. Save the return record using the Assignment ID
        boolean saved = returnRepo.returnAsset(activeAssignId, date, condition);

        if(saved) {
            // 3. Update the Asset status to AVAILABLE
            assetRepo.updateStatus(assetId, "AVAILABLE");

            // 4. Update the Assignment status to INACTIVE so it doesn't show up again
            assignmentRepo.updateStatus(activeAssignId, "INACTIVE");
        }

        return saved;
    }
}