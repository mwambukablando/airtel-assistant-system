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

    @GetMapping("/audit-logs")
    public String showAuditLogs(Model model) {
        // Fetches the list and sends it to the "logs" variable in Thymeleaf
        model.addAttribute("logs", auditRepo.getAllLogs());
        return "audit-history";
    }
}