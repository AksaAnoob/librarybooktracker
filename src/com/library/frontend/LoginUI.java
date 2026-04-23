package com.library.frontend;

import javax.swing.*;
import java.awt.*;

public class LoginUI {

    private JFrame frame;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private LibraryBackend backend;
    private boolean isLibrarian; // true = librarian login, false = user login

    public LoginUI(LibraryBackend backend, boolean isLibrarian) {
        this.backend = backend;
        this.isLibrarian = isLibrarian;

        frame = new JFrame(isLibrarian ? "Librarian Login/Register" : "User Login/Register");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);

        JLabel header = new JLabel("📚 Library Tracker", SwingConstants.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 20));
        header.setOpaque(true);
        header.setBackground(new Color(70, 130, 180));
        header.setForeground(Color.WHITE);
        header.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        frame.add(header, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel userLabel = new JLabel("Username:");
        usernameField = new JTextField(15);
        JLabel passLabel = new JLabel("Password:");
        passwordField = new JPasswordField(15);

        gbc.gridx = 0; gbc.gridy = 0; centerPanel.add(userLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 0; centerPanel.add(usernameField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; centerPanel.add(passLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 1; centerPanel.add(passwordField, gbc);

        frame.add(centerPanel, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout());
        JButton loginBtn = new JButton("Login");
        JButton registerBtn = new JButton("Register");
        styleButton(loginBtn, new Color(34, 139, 34));
        styleButton(registerBtn, new Color(70, 130, 180));
        btnPanel.add(loginBtn);
        btnPanel.add(registerBtn);

        frame.add(btnPanel, BorderLayout.SOUTH);

        loginBtn.addActionListener(e -> loginAction());
        registerBtn.addActionListener(e -> registerAction());

        frame.setVisible(true);
    }

    private void styleButton(JButton btn, Color bg) {
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    private void loginAction() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Enter both username and password.");
            return;
        }

        if (backend.checkLogin(username, password, isLibrarian)) {
            JOptionPane.showMessageDialog(frame, "Login successful! Welcome " + username);
            frame.dispose();
            new LibraryUI(backend, username, isLibrarian);
        } else {
            JOptionPane.showMessageDialog(frame, "Invalid credentials.");
        }
    }

    private void registerAction() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Enter both username and password.");
            return;
        }

        if (backend.registerUser(username, password, isLibrarian)) {
            JOptionPane.showMessageDialog(frame, "Registration successful! You can now login.");
        } else {
            JOptionPane.showMessageDialog(frame, "Username already exists or error occurred.");
        }
    }
}



