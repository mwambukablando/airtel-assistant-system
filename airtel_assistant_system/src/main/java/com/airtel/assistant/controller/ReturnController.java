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

@Controller 
public class ReturnController {

    @Autowired
    private ReturnService service;

    @Autowired
    private AssetRepository assetRepo;

    // --- WEB ROUTE ---
    @GetMapping("/assets/return")
    public String showReturnForm(Model model) {
        List<Map<String, String>> assignedAssets = assetRepo.searchAssetsList("")
                .stream()
                .filter(asset -> "ASSIGNED".equals(asset.get("status")))
                .collect(Collectors.toList());

        model.addAttribute("assets", assignedAssets);
        return "return-asset";
    }

    @PostMapping("/assets/return/save")
    public String saveReturnWeb(@RequestParam int assetId,
                                @RequestParam String returnDate,
                                @RequestParam String condition) {
        boolean success = service.returnAsset(assetId, returnDate, condition);
        return success ? "redirect:/dashboard?returned=true" : "redirect:/assets/return?error=true";
    }

    // --- BRIDGE: Satisfies ReturnAssetForm.java:[115,41] ---
    public boolean returnAsset(int assetId, String date, String condition) {
        return service.returnAsset(assetId, date, condition);
    }
}