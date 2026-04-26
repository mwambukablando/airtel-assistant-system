package com.airtel.assistant.controller;

import com.airtel.assistant.security.SessionManager; // Import this
import com.airtel.assistant.service.UserService;

public class LoginController {

    private UserService userService = new UserService();

    public boolean login(String username, String password) {
        boolean isValid = userService.login(username, password);

        if (isValid) {
            // Fetch the role and save it to the session immediately
            String role = userService.getRole(username);
            SessionManager.setCurrentUser(username, role);
        }

        return isValid;
    }

    public String getRole(String username) {
        return userService.getRole(username);
    }
}