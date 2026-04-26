package com.airtel.assistant.controller;

import com.airtel.assistant.security.SessionManager;
import com.airtel.assistant.service.UserService;
import org.springframework.stereotype.Controller; // Added
import org.springframework.ui.Model; // Added
import org.springframework.web.bind.annotation.GetMapping; // Added
import org.springframework.web.bind.annotation.PostMapping; // Added
import org.springframework.web.bind.annotation.RequestParam; // Added

@Controller // Added to make it a Web Controller
public class LoginController {

    private UserService userService = new UserService();

    // --- NEW WEB MAPPING: SHOW LOGIN PAGE ---
    @GetMapping("/")
    public String showLandingPage() {
        return "login"; // Points to src/main/resources/templates/login.html
    }

    // --- NEW WEB MAPPING: HANDLE FORM SUBMISSION ---
    @PostMapping("/login")
    public String handleWebLogin(@RequestParam String username, @RequestParam String password, Model model) {
        // We call your existing logic exactly as it is
        boolean success = login(username, password);
        
        if (success) {
            return "redirect:/dashboard"; 
        } else {
            model.addAttribute("error", "Invalid Credentials!");
            return "login";
        }
    }

    // --- YOUR ORIGINAL LOGIC (UNTOUCHED) ---
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