package com.library.frontend;

import java.sql.*;

public class LibraryBackend {
    private static final String URL = "jdbc:mysql://localhost:3306/library_tracker?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";

    private static final String USER = "root";
    private static final String PASS = "Aksa@2006";

    private Connection conn;

    public LibraryBackend() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USER, PASS);
            System.out.println("Connected to MySQL!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ------------------ USER/LIBRARIAN ------------------
    public boolean registerUser(String username, String password, boolean isLibrarian) {
        String table = isLibrarian ? "librarians" : "users";
        try {
            String sql = "INSERT INTO " + table + " (username, password) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean checkLogin(String username, String password, boolean isLibrarian) {
        String table = isLibrarian ? "librarians" : "users";
        try {
            String sql = "SELECT * FROM " + table + " WHERE username=? AND password=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            return false;
        }
    }

    // ------------------ BOOKS ------------------
    public void addBook(String title, String author) {
        try {
            String sql = "INSERT INTO books (title, author, status, borrowed_by) VALUES (?, ?, 'Available', NULL)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, title);
            stmt.setString(2, author);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet getAllBooks() {
        try {
            Statement stmt = conn.createStatement();
            return stmt.executeQuery("SELECT * FROM books");
        } catch (SQLException e) {
            return null;
        }
    }

    public void updateStatus(int id, String status, String username) {
        try {
            String sql = "UPDATE books SET status=?, borrowed_by=? WHERE id=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, status);
            if (status.equalsIgnoreCase("Borrowed")) stmt.setString(2, username);
            else stmt.setNull(2, Types.VARCHAR);
            stmt.setInt(3, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteBook(int id) {
        try {
            String sql = "DELETE FROM books WHERE id=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

