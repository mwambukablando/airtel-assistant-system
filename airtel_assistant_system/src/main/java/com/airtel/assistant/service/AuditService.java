package com.airtel.assistant.service;

import com.airtel.assistant.repository.AuditLogRepository;

public class AuditService {

    // You don't actually need the 'repo' variable anymore because the method is static

    public void log(int assetId, String action) {
        // Pass both the ID and the Action string
        AuditLogRepository.logAction(assetId, action);
    }
}