package com.airtel.assistant.controller;

import com.airtel.assistant.repository.AssetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;
import java.util.Map;

@Controller
public class SearchController {

    @Autowired
    private AssetRepository assetRepo;

    @GetMapping("/search")
    public String showSearchPage(@RequestParam(required = false) String query, Model model) {
        List<Map<String, String>> results;

        // If query is empty/null, it now fetches ALL assets
        if (query == null || query.trim().isEmpty()) {
            results = assetRepo.searchAssetsList(""); 
            model.addAttribute("lastQuery", "");
        } else {
            results = assetRepo.searchAssetsList(query);
            model.addAttribute("lastQuery", query);
        }

        model.addAttribute("results", results);
        return "search-page";
    }
}