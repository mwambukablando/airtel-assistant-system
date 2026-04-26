package com.airtel.assistant.controller;

import com.airtel.assistant.security.SessionManager;
import com.airtel.assistant.service.UserService;
import com.airtel.assistant.controller.AssetController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes; // Added for web state

@Controller
@SessionAttributes({"username", "role"}) // This keeps the user logged in across pages
public class LoginController {

    private UserService userService = new UserService();
    private AssetController assetController = new AssetController();

    @GetMapping("/")
    public String showLandingPage() {
        return "login";
    }

    @PostMapping("/login")
    public String handleWebLogin(@RequestParam String username, @RequestParam String password, Model model) {
        boolean success = login(username, password);
        
        if (success) {
            // We store these in the Model so @SessionAttributes can pick them up
            model.addAttribute("username", SessionManager.getUsername());
            model.addAttribute("role", SessionManager.getRole());
            return "redirect:/dashboard"; 
        } else {
            model.addAttribute("error", "Invalid Credentials!");
            return "login";
        }
    }

    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        // Double check if user is actually logged in
        String user = (String) model.getAttribute("username");
        if (user == null) {
            return "redirect:/"; // Kick back to login if session is empty
        }

        String role = (String) model.getAttribute("role");

        // Stats logic untouched, exactly as you wanted
        int total = assetController.getTotalAssets();
        int assigned = assetController.getAssignedAssets();
        int available = assetController.getAvailableAssets();

        model.addAttribute("total", total);
        model.addAttribute("assigned", assigned);
        model.addAttribute("available", available);

        return "dashboard";
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