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
import java.util.stream.Stream;
import java.util.Map;

@Controller
public class AuditController {

    @Autowired private AuditLogRepository auditRepo;
    @Autowired private AssetRepository assetRepo;

    @GetMapping("/admin/audit") 
    public String showAuditLogs(Model model) {
        model.addAttribute("logs", auditRepo.getAllLogs());
        return "audit-history";
    }

    @GetMapping("/admin/reports")
    public String showReportsHub() {
        return "reports-view"; // Now returns the Hub UI with buttons
    }

    @GetMapping("/admin/reports/export/assets")
    public void exportAssetsPDF(HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        // "inline" opens it in the browser tab directly
        response.setHeader("Content-Disposition", "inline; filename=Airtel_Asset_Report.pdf");

        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();
        
        // Custom Branding
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, new Color(225, 29, 72));
        Paragraph title = new Paragraph("AIRTEL ASSET INVENTORY REPORT", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(new Paragraph(" ")); // Spacer

        // Create Table
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        
        // Add Headers
        Stream.of("Tag", "Device Name", "Serial", "Status").forEach(columnTitle -> {
            PdfPCell header = new PdfPCell();
            header.setBackgroundColor(Color.LIGHT_GRAY);
            header.setPhrase(new Phrase(columnTitle));
            table.addCell(header);
        });

        // Add Data from Railway
        List<Map<String, String>> assets = assetRepo.searchAssetsList("");
        for (Map<String, String> asset : assets) {
            table.addCell(asset.get("tag"));
            table.addCell(asset.get("name"));
            table.addCell(asset.get("serial"));
            table.addCell(asset.get("status"));
        }

        document.add(table);
        document.close();
    }
    
    // You can repeat this pattern for /assignments, /returns, and /audit!
}