package com.airtel.assistant.controller;

import com.airtel.assistant.service.AuditService;

public class AuditController {

    private AuditService service = new AuditService();

    // Change this to accept both the ID and the Action string
    public void logAction(int assetId, String action) {
        service.log(assetId, action);
    }
}