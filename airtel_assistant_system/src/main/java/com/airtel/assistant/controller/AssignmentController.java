package com.airtel.assistant.controller;

import com.airtel.assistant.service.AssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AssignmentController {

    @Autowired
    private AssignmentService service;

    // --- WEB ROUTE FOR ASSIGNMENT ---
    @PostMapping("/assignments/save")
    public String saveAssignment(@RequestParam int assetId,
                                 @RequestParam String employeeName,
                                 @RequestParam String department,
                                 @RequestParam String assignDate) {
        
        boolean success = service.assignAsset(assetId, employeeName, department, assignDate);
        
        if (success) {
            return "redirect:/dashboard"; // Go back to dashboard to see updated stats
        } else {
            return "redirect:/assets/assign?error=true";
        }
    }

    // --- SWING BRIDGE ---
    public boolean assignAsset(int assetId, String employee, String department, String date) {
        return service.assignAsset(assetId, employee, department, date);
    }
}