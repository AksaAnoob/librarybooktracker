package com.library.frontend;

import javax.swing.*;
import java.awt.*;

public class RegisterUI {

    private JFrame frame;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private LibraryBackend backend;
    private boolean isLibrarian; // true if librarian, false if user

    public RegisterUI(LibraryBackend backend, boolean isLibrarian) {
        this.backend = backend;
        this.isLibrarian = isLibrarian;

        frame = new JFrame(isLibrarian ? "Librarian Registration" : "User Registration");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);

        JLabel header = new JLabel(isLibrarian ? "📚 Librarian Registration" : "📖 User Registration", SwingConstants.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 20));
        header.setOpaque(true);
        header.setBackground(new Color(70, 130, 180));
        header.setForeground(Color.WHITE);
        header.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        frame.add(header, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(new Color(240, 248, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 0;
        centerPanel.add(usernameLabel, gbc);

        usernameField = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 0;
        centerPanel.add(usernameField, gbc);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 1;
        centerPanel.add(passwordLabel, gbc);

        passwordField = new JPasswordField(15);
        gbc.gridx = 1;
        gbc.gridy = 1;
        centerPanel.add(passwordField, gbc);

        frame.add(centerPanel, BorderLayout.CENTER);

        JPanel buttonsPanel = new JPanel(new FlowLayout());
        buttonsPanel.setBackground(new Color(240, 248, 255));

        JButton registerBtn = new JButton("Register");
        JButton loginBtn = new JButton("Back to Login");

        styleButton(registerBtn, new Color(34, 139, 34));
        styleButton(loginBtn, new Color(70, 130, 180));

        buttonsPanel.add(registerBtn);
        buttonsPanel.add(loginBtn);

        frame.add(buttonsPanel, BorderLayout.SOUTH);

        // Action listeners
        registerBtn.addActionListener(e -> registerAction());
        loginBtn.addActionListener(e -> {
            frame.dispose();
            new LoginUI(backend,isLibrarian); // go back to login page
        });

        frame.setVisible(true);
    }

    private void styleButton(JButton btn, Color bg) {
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    private void registerAction() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Enter both username and password.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean success = backend.registerUser(username, password, isLibrarian);

        if (success) {
            JOptionPane.showMessageDialog(frame, "Registration successful! You can now login.", "Success", JOptionPane.INFORMATION_MESSAGE);
            frame.dispose();
            new LoginUI(backend,isLibrarian);
        } else {
            JOptionPane.showMessageDialog(frame, "Username already exists or error occurred.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}



