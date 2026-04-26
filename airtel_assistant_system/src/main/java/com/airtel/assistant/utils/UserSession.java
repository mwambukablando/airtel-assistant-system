package com.airtel.assistant.utils;

/**
 * This class handles the global state of the logged-in user.
 * It uses static fields so the data persists across different Swing frames.
 */
public class UserSession {

    private static String username;
    private static String role;

    // Call this method in your LoginController after a successful login
    public static void initSession(String user, String userRole) {
        username = user;
        role = userRole;
    }

    public static String getUsername() {
        return username;
    }

    public static String getRole() {
        return role;
    }

    // Call this if you add a 'Logout' button later
    public static void clear() {
        username = null;
        role = null;
    }
}