package com.library.frontend;

import javax.swing.*;
import java.awt.*;

public class RoleSelector {

    public RoleSelector(LibraryBackend backend) {
        JFrame frame = new JFrame("Select Role");
        frame.setSize(500, 350);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);

        JLabel header = new JLabel("📚 Library Book Tracker", SwingConstants.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 20));
        header.setOpaque(true);
        header.setBackground(new Color(70, 130, 180));
        header.setForeground(Color.WHITE);
        header.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        frame.add(header, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 20));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(40, 30, 20, 50));


        JButton userBtn = new JButton("Login/Register as User");
        JButton librarianBtn = new JButton("Login/Register as Librarian");
        userBtn.setPreferredSize(new Dimension(250, 40));
        librarianBtn.setPreferredSize(new Dimension(250, 40));
       // userBtn.setPreferredSize(new Dimension(200, 50));
        //librarianBtn.setPreferredSize(new Dimension(200, 50));


        styleButton(userBtn, new Color(34, 139, 34));
        styleButton(librarianBtn, new Color(178, 34, 34));

        centerPanel.add(userBtn);
        centerPanel.add(Box.createVerticalStrut(20)); // space between buttons
        centerPanel.add(librarianBtn);

        centerPanel.add(userBtn);
        centerPanel.add(librarianBtn);

        frame.add(centerPanel, BorderLayout.CENTER);

        // Button actions
        userBtn.addActionListener(e -> {
            frame.dispose();
            new LoginUI(backend, false); // false = user
        });

        librarianBtn.addActionListener(e -> {
            frame.dispose();
            new LoginUI(backend, true); // true = librarian
        });

        frame.setVisible(true);
    }

    private void styleButton(JButton btn, Color bg) {
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));


    }

    public static void main(String[] args) {
        LibraryBackend backend = new LibraryBackend();
        SwingUtilities.invokeLater(() -> new RoleSelector(backend));
    }
}


