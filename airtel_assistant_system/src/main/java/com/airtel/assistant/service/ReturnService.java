package com.airtel.assistant.service;

import com.airtel.assistant.repository.AssetRepository;
import com.airtel.assistant.repository.AssignmentRepository;
import com.airtel.assistant.repository.ReturnAssetRepository;
import com.airtel.assistant.repository.AuditLogRepository; // Added this
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
    @Autowired
    private AuditLogRepository auditLogRepo; // Added this

    public boolean returnAsset(int assetId, String date, String condition) {
        // 1. Get the correct ID from the assignments table
        Integer activeAssignId = assignmentRepo.findActiveIdByAssetId(assetId);

        if (activeAssignId == null) {
            System.out.println("DEBUG: No active assignment for Asset " + assetId);
            return false;
        }

        // 2. Save the return record to 'returns_table'
        boolean saved = returnRepo.returnAsset(activeAssignId, date, condition);

        if(saved) {
            // 3. Flip asset status to AVAILABLE
            assetRepo.updateStatus(assetId, "AVAILABLE");
            // 4. Flip assignment status to INACTIVE (or RETURNED)
            assignmentRepo.updateStatus(activeAssignId, "INACTIVE");
            
            // Added Hook for Audit Logs
            auditLogRepo.logAction(assetId, "ASSET RETURNED - Condition: " + condition);
        }

        return saved;
    }
}