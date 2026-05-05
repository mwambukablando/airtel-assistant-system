package com.airtel.assistant.service;

import com.airtel.assistant.repository.AssetRepository;
import com.airtel.assistant.repository.AssignmentRepository;
import com.airtel.assistant.repository.ReturnAssetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReturnService {

    @Autowired
    private ReturnAssetRepository returnRepo;

    @Autowired
    private AssetRepository assetRepo;

    @Autowired
    private AssignmentRepository assignmentRepo;

    public boolean returnAsset(int assetId, String date, String condition) {
        // 1. Validation
        if(condition == null || condition.isEmpty()) {
            return false;
        }

        // 2. Find the active assign_id from the assignments table
        // This uses the method we just verified in your AssignmentRepository
        Integer activeAssignId = assignmentRepo.findActiveIdByAssetId(assetId);

        if (activeAssignId == null) {
            System.out.println("DEBUG: No active assignment found for Asset ID: " + assetId);
            return false;
        }

        // 3. Save to returns table
        boolean saved = returnRepo.returnAsset(activeAssignId, date, condition);

        if(saved) {
            // 4. Update Asset to AVAILABLE in assets table
            assetRepo.updateStatus(assetId, "AVAILABLE");
            
            // 5. Update Assignment to INACTIVE in assignments table
            assignmentRepo.updateStatus(activeAssignId, "INACTIVE");
        }

        return saved;
    }
}