package com.airtel.assistant.controller;

import com.airtel.assistant.repository.AuditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuditController {

    @Autowired
    private AuditLogRepository auditRepo;

    // FIXED: Changed mapping to match the URL in your screenshot
    @GetMapping("/admin/audit") 
    public String showAuditLogs(Model model) {
        model.addAttribute("logs", auditRepo.getAllLogs());
        return "audit-history";
    }
}