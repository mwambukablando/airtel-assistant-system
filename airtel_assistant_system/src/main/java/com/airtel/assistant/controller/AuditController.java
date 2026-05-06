package com.airtel.assistant.controller;

import com.airtel.assistant.repository.AssetRepository; // Added for Reports
import com.airtel.assistant.repository.AuditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;
import java.util.Map;

@Controller
public class AuditController {

    @Autowired
    private AuditLogRepository auditRepo;

    @Autowired
    private AssetRepository assetRepo; // Added to fetch data for reports

    // KEEPING YOUR FIXED MAPPING
    @GetMapping("/admin/audit") 
    public String showAuditLogs(Model model) {
        model.addAttribute("logs", auditRepo.getAllLogs());
        return "audit-history";
    }

    // NEW: Mapping for the Reports Tab
    @GetMapping("/admin/reports")
    public String showReports(Model model) {
        // Using your existing search method with an empty string to get everything
        List<Map<String, String>> reportData = assetRepo.searchAssetsList("");
        model.addAttribute("reportData", reportData);
        return "reports-view";
    }
}