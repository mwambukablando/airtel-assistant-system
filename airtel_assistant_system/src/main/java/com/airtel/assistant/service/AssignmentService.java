package com.airtel.assistant.service;

import com.airtel.assistant.repository.AssignmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service // 1. Added this so Spring knows this is a Service
public class AssignmentService {

    @Autowired // 2. Let Spring "inject" the repository with the DB credentials
    private AssignmentRepository repo;

    public boolean assignAsset(int assetId, String employee, String department, String date) {

        // Keep your validation logic
        if(employee == null || employee.trim().isEmpty() || 
           department == null || department.trim().isEmpty()) {
            return false;
        }

        // Now this will call the repository correctly
        return repo.assignAsset(assetId, employee, department, date);
    }
}