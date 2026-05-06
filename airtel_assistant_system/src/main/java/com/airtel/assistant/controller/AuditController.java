package com.airtel.assistant.controller;

import com.airtel.assistant.repository.*;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;
import java.awt.Color;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
public class AuditController {

    @Autowired private AuditLogRepository auditRepo;
    @Autowired private AssetRepository assetRepo;
    @Autowired private AssignmentRepository assignmentRepo;

    @GetMapping("/admin/audit") 
    public String showAuditLogs(Model model) {
        model.addAttribute("logs", auditRepo.getAllLogs());
        return "audit-history";
    }

    @GetMapping("/admin/reports")
    public String showReportsHub() {
        return "reports-view"; 
    }

    // 1. ASSET REPORT
    @GetMapping("/admin/reports/export/assets")
    public void exportAssetsPDF(HttpServletResponse response) throws IOException {
        generatePDF(response, "ASSET INVENTORY REPORT", 
            new String[]{"Tag", "Device Name", "Serial", "Status"}, 
            assetRepo.searchAssetsList(""), 
            new String[]{"tag", "name", "serial", "status"});
    }

    // 2. ASSIGNMENT REPORT
    @GetMapping("/admin/reports/export/assignments")
    public void exportAssignmentsPDF(HttpServletResponse response) throws IOException {
        generatePDF(response, "ACTIVE ASSIGNMENTS REPORT", 
            new String[]{"Asset ID", "Employee", "Department", "Date"}, 
            assignmentRepo.getAllAssignmentsList(), 
            new String[]{"asset_id", "employee_name", "department", "assignment_date"});
    }

    // 3. AUDIT LOG REPORT
    @GetMapping("/admin/reports/export/audit")
    public void exportAuditPDF(HttpServletResponse response) throws IOException {
        generatePDF(response, "SYSTEM AUDIT HISTORY REPORT", 
            new String[]{"Date", "Admin", "Action", "Asset ID"}, 
            auditRepo.getAllLogs(), 
            new String[]{"date", "user", "action", "assetId"});
    }

    // 4. RETURN REPORT
    @GetMapping("/admin/reports/export/returns")
    public void exportReturnsPDF(HttpServletResponse response) throws IOException {
        generatePDF(response, "ASSET RETURNS REPORT", 
            new String[]{"Asset ID", "Return Date", "Condition"}, 
            assetRepo.getReturnedAssetsList(), 
            new String[]{"asset_id", "return_date", "condition"});
    }

    /**
     * UPDATED UNIVERSAL PDF GENERATOR
     * Using List<? extends Map<String, ?>> fixes the "String vs Object" map error.
     */
    private void generatePDF(HttpServletResponse response, String titleStr, String[] headers, 
                             List<? extends Map<String, ?>> data, String[] mapKeys) throws IOException {
        
        response.setContentType("application/pdf");
        // "inline" opens it in the browser tab
        response.setHeader("Content-Disposition", "inline; filename=Report.pdf");

        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        // Header Styling
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, new Color(225, 29, 72));
        Paragraph title = new Paragraph(titleStr, titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(new Paragraph("Generated on: " + new java.util.Date().toString()));
        document.add(new Paragraph(" "));

        // Table Configuration
        PdfPTable table = new PdfPTable(headers.length);
        table.setWidthPercentage(100);

        // Styling Table Headers
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Color.WHITE);
        for (String headerTitle : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(headerTitle, headerFont));
            cell.setBackgroundColor(new Color(225, 29, 72));
            cell.setPadding(8);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
        }

        // Filling Data Rows
        if (data != null) {
            for (Map<String, ?> row : data) {
                for (String key : mapKeys) {
                    Object value = row.get(key);
                    table.addCell(value != null ? String.valueOf(value) : "N/A");
                }
            }
        }

        document.add(table);
        document.close();
    }
}