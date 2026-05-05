package com.airtel.assistant.security;

public class SessionManager {
    private static String username;
    private static String role;

    public static void setCurrentUser(String user, String userRole) {
        username = user;
        role = userRole;
    }

    public static String getUsername() {
        return username;
    }

    public static String getRole() {
        return role;
    }

    public static void cleanSession() {
        username = null;
        role = null;
    }
}