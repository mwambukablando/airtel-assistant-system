package com.airtel.assistant.controller;

import com.airtel.assistant.security.SessionManager;
import com.airtel.assistant.service.UserService;
import com.airtel.assistant.service.AssetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import javax.servlet.http.HttpSession;

@Controller
@SessionAttributes({"username", "role"})
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private AssetService assetService;

    @GetMapping("/")
    public String showLandingPage() {
        return "login";
    }

    @PostMapping("/login")
    public String handleWebLogin(@RequestParam String username, @RequestParam String password, Model model) {
        boolean success = login(username, password);
        
        if (success) {
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
        String user = (String) model.getAttribute("username");
        if (user == null) return "redirect:/";

        int total = assetService.getTotalAssets();
        int assigned = assetService.getAssignedAssets();
        int available = assetService.getAvailableAssets();

        model.addAttribute("total", total);
        model.addAttribute("assigned", assigned);
        model.addAttribute("available", available);

        return "dashboard";
    }

    /**
     * LOGOUT LOGIC ADDED HERE
     */
    @PostMapping("/logout")
    public String logout(HttpSession session, SessionStatus status) {
        // 1. Clear custom SessionManager
        SessionManager.cleanSession();
        
        // 2. Clear @SessionAttributes (username, role)
        status.setComplete();
        
        // 3. Invalidate the actual HTTP session
        if (session != null) {
            session.invalidate();
        }
        
        return "redirect:/?logout=true";
    }

    public boolean login(String username, String password) {
        boolean isValid = userService.login(username, password);
        if (isValid) {
            String role = userService.getRole(username);
            SessionManager.setCurrentUser(username, role);
        }
        return isValid;
    }
}