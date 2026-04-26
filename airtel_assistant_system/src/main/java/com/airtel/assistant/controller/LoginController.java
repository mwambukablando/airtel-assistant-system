package com.airtel.assistant.controller;

import com.airtel.assistant.security.SessionManager;
import com.airtel.assistant.service.UserService;
import com.airtel.assistant.controller.AssetController; // Added
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    private UserService userService = new UserService();
    private AssetController assetController = new AssetController(); // Added to match Dashboard.java

    @GetMapping("/")
    public String showLandingPage() {
        return "login";
    }

    @PostMapping("/login")
    public String handleWebLogin(@RequestParam String username, @RequestParam String password, Model model) {
        boolean success = login(username, password);
        
        if (success) {
            return "redirect:/dashboard"; 
        } else {
            model.addAttribute("error", "Invalid Credentials!");
            return "login";
        }
    }

    // --- NEW: DASHBOARD MAPPING ---
    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        // 1. Respecting SessionManager for user info
        String user = SessionManager.getUsername();
        String role = SessionManager.getRole();

        // 2. Respecting AssetController for stats
        int total = assetController.getTotalAssets();
        int assigned = assetController.getAssignedAssets();
        int available = assetController.getAvailableAssets();

        // 3. Passing to HTML
        model.addAttribute("username", user);
        model.addAttribute("role", role);
        model.addAttribute("total", total);
        model.addAttribute("assigned", assigned);
        model.addAttribute("available", available);

        return "dashboard"; // Points to dashboard.html
    }

    // --- ORIGINAL LOGIC (UNTOUCHED) ---
    public boolean login(String username, String password) {
        boolean isValid = userService.login(username, password);

        if (isValid) {
            String role = userService.getRole(username);
            SessionManager.setCurrentUser(username, role);
        }

        return isValid;
    }

    public String getRole(String username) {
        return userService.getRole(username);
    }
}