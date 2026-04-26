package com.airtel.assistant.controller;

import com.airtel.assistant.repository.AssetRepository;
import com.airtel.assistant.service.ReturnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller // <--- Crucial for Spring to see this
public class ReturnController {

    @Autowired
    private ReturnService service;

    @Autowired
    private AssetRepository assetRepo;

    // --- WEB ROUTE: Show the Return Form ---
    @GetMapping("/assets/return")
    public String showReturnForm(Model model) {
        // We only want to show assets that are currently 'ASSIGNED'
        List<Map<String, String>> assignedAssets = assetRepo.searchAssetsList("")
                .stream()
                .filter(asset -> "ASSIGNED".equals(asset.get("status")))
                .collect(Collectors.toList());

        model.addAttribute("assets", assignedAssets);
        return "return-asset"; // This will look for return-asset.html
    }

    // --- WEB ROUTE: Process the form submission ---
    @PostMapping("/assets/return/save")
    public String saveReturnWeb(@RequestParam int assetId,
                                @RequestParam String returnDate,
                                @RequestParam String condition) {
        
        boolean success = service.returnAsset(assetId, returnDate, condition);
        if (success) {
            return "redirect:/dashboard?returned=true";
        } else {
            return "redirect:/assets/return?error=true";
        }
    }

    // --- SWING BRIDGE: Keep this for your Desktop ReturnAssetForm ---
    public boolean returnAsset(int assetId, String date, String condition) {
        return service.returnAsset(assetId, date, condition);
    }
}