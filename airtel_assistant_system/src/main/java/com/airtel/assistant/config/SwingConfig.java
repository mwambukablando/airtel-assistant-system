package com.airtel.assistant.config;

import javax.swing.UIManager;

public class SwingConfig {

    public static void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel(
                UIManager.getSystemLookAndFeelClassName()
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}