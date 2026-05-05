package com.airtel.assistant.controller;

import com.airtel.assistant.repository.AssetRepository;
import com.airtel.assistant.service.ReturnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller 
public class ReturnController {

    @Autowired
    private ReturnService service;

    @Autowired
    private AssetRepository assetRepo;

    // --- WEB INTERFACE ROUTES ---

    @GetMapping("/assets/return")
    public String showReturnForm(Model model) {
        List<Map<String, String>> allAssets = assetRepo.searchAssetsList("");
        
        List<Map<String, String>> assignedAssets = allAssets.stream()
                .filter(asset -> "ASSIGNED".equals(asset.get("status")))
                .collect(Collectors.toList());

        model.addAttribute("assets", assignedAssets);
        return "return-asset";
    }

    @PostMapping("/assets/return/save")
    public String saveReturnWeb(@RequestParam("assetId") int assetId,
                                @RequestParam("returnDate") String returnDate,
                                @RequestParam("condition") String condition) {
        
        boolean success = service.returnAsset(assetId, returnDate, condition);
        
        if (success) {
            return "redirect:/dashboard?returned=true";
        } else {
            return "redirect:/assets/return?error=true";
        }
    }

    /**
     * BRIDGE METHOD FOR DESKTOP UI (ReturnAssetForm.java)
     * This resolves the red line error at line 115 in your Swing form.
     */
    public boolean returnAsset(int assetId, String date, String condition) {
        return service.returnAsset(assetId, date, condition);
    }
}