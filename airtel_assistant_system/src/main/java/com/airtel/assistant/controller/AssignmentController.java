package com.airtel.assistant.controller;

import com.airtel.assistant.service.AssignmentService;

public class AssignmentController {

    private AssignmentService service = new AssignmentService();

    public boolean assignAsset(int assetId, String employee, String department, String date) {
        return service.assignAsset(assetId, employee, department, date);
    }
}