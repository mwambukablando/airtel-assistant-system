package com.airtel.assistant.service;

import com.airtel.assistant.repository.AuditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service // 1. Added this so Spring manages this service
public class AuditService {

    @Autowired // 2. Inject the repository
    private AuditLogRepository auditLogRepo;

    public void log(int assetId, String action) {
        // 3. Use the injected variable 'auditLogRepo' (lowercase 'a')
        auditLogRepo.logAction(assetId, action);
    }
}