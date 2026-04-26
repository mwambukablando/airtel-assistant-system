package com.airtel.assistant.service;

import com.airtel.assistant.repository.AssignmentRepository;

public class AssignmentService {

    private AssignmentRepository repo = new AssignmentRepository();

    public boolean assignAsset(int assetId, String employee, String department, String date) {

        if(employee.isEmpty() || department.isEmpty()) {
            return false;
        }

        return repo.assignAsset(assetId, employee, department, date);
    }
}