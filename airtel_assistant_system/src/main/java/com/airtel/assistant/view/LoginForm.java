package com.airtel.assistant.view;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import com.airtel.assistant.controller.LoginController;
import com.airtel.assistant.utils.AppStyle; // Ensure this matches your package structure

public class LoginForm extends JFrame {

    // Components
    private JTextField txtUser;
    private JPasswordField txtPass;
    private JButton btnLogin;
    private JLabel lblTitle, lblUser, lblPass;

    private LoginController controller = new LoginController();

    public LoginForm() {
        // --- WINDOW SETTINGS ---
        setTitle("Airtel Assistant | Secure Login");
        setSize(400, 300); // Slightly wider for a better "Modern" feel
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        // --- CONTENT PANE SETUP ---
        JPanel panel = new JPanel();
        panel.setBackground(AppStyle.DARK_BG);
        panel.setLayout(null);
        setContentPane(panel);

        // --- HEADER / LOGO SECTION ---
        lblTitle = new JLabel("AIRTEL LOGIN", SwingConstants.CENTER);
        lblTitle.setBounds(50, 20, 300, 40);
        lblTitle.setForeground(AppStyle.AIRTEL_RED);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        panel.add(lblTitle);

        // --- USERNAME SECTION ---
        lblUser = new JLabel("Username");
        lblUser.setBounds(50, 80, 100, 20);
        lblUser.setForeground(AppStyle.TEXT_DIM);
        lblUser.setFont(AppStyle.NORMAL_FONT);
        panel.add(lblUser);

        txtUser = new JTextField();
        txtUser.setBounds(50, 105, 300, 35);
        AppStyle.styleTextField(txtUser);
        panel.add(txtUser);

        // --- PASSWORD SECTION ---
        lblPass = new JLabel("Password");
        lblPass.setBounds(50, 150, 100, 20);
        lblPass.setForeground(AppStyle.TEXT_DIM);
        lblPass.setFont(AppStyle.NORMAL_FONT);
        panel.add(lblPass);

        txtPass = new JPasswordField();
        txtPass.setBounds(50, 175, 300, 35);
        AppStyle.styleTextField(txtPass);
        panel.add(txtPass);

        // --- LOGIN BUTTON ---
        btnLogin = new JButton("ACCESS SYSTEM");
        btnLogin.setBounds(50, 225, 300, 45);
        AppStyle.styleDarkButton(btnLogin);
        panel.add(btnLogin);

        // Adjust window size to fit everything comfortably
        setSize(415, 350);

        // Action Listener
        btnLogin.addActionListener(e -> login());

        setVisible(true);
    }

    private void login() {
        String username = txtUser.getText();
        String password = new String(txtPass.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both fields!", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean success = controller.login(username, password);

        if (success) {
            JOptionPane.showMessageDialog(this, "Welcome Back, " + username + "!");
            dispose();
            new Dashboard(); // This will open your next styled window
        } else {
            // Custom red error message for dark mode
            UIManager.put("OptionPane.background", AppStyle.DARK_BG);
            UIManager.put("Panel.background", AppStyle.DARK_BG);
            UIManager.put("OptionPane.messageForeground", Color.WHITE);
            JOptionPane.showMessageDialog(this, "Invalid Credentials!", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }
}