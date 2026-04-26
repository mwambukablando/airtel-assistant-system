package com.airtel.assistant.utils;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

public class AppStyle {
    // --- DARK MODE PALETTE ---
    public static final Color DARK_BG = new Color(30, 30, 30);      // Deep Grey/Black
    public static final Color PANEL_BG = new Color(45, 45, 45);     // Lighter Grey for panels
    public static final Color AIRTEL_RED = new Color(255, 0, 0);    // Airtel Brand Red
    public static final Color TEXT_WHITE = new Color(240, 240, 240);
    public static final Color TEXT_DIM = new Color(180, 180, 180);

    // --- FONTS ---
    public static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font NORMAL_FONT = new Font("Segoe UI", Font.PLAIN, 14);

    // --- BUTTON STYLING (The CSS) ---
    public static void styleDarkButton(JButton btn) {
        btn.setBackground(AIRTEL_RED);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(new LineBorder(AIRTEL_RED, 2, true));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    // --- FIELD STYLING ---
    public static void styleTextField(JTextField field) {
        field.setBackground(new Color(60, 60, 60));
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(80, 80, 80), 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
    }
}