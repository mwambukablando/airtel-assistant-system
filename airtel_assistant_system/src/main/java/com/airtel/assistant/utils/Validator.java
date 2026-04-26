package com.airtel.assistant.utils;

public class Validator {

    public static boolean isNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }

    public static boolean isValidEmail(String email) {
        return email != null && email.contains("@") && email.contains(".");
    }

    public static boolean isNumeric(String value) {
        return value != null && value.matches("\\d+");
    }
}