package com.library.frontend;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;

public class LibraryUI {

    private JFrame frame;
    private DefaultTableModel tableModel;
    private JTable table;
    private TableRowSorter<DefaultTableModel> rowSorter;
    private LibraryBackend backend;
    private String currentUser;
    private boolean isLibrarian;

    // Constructor
    public LibraryUI(LibraryBackend backend, String username, boolean isLibrarian) {
        this.backend = backend;
        this.currentUser = username;
        this.isLibrarian = isLibrarian;

        frame = new JFrame("Library Book Tracker");
        frame.setSize(900, 550);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(10, 10));

        // Heading
        JLabel title = new JLabel("📚 Library Book Tracker", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 30));
        title.setForeground(new Color(25, 25, 112));
        title.setBorder(new EmptyBorder(10, 0, 10, 0));
        frame.add(title, BorderLayout.NORTH);

        // Table
        String[] columns = {"Sl no", "Book ID", "Title", "Author", "Status", "Borrowed By"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        table.setRowHeight(32);

        // Header style
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 16));
        table.getTableHeader().setBackground(new Color(70, 130, 180));
        table.getTableHeader().setForeground(Color.WHITE);

        // Color rows based on status
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, value, isSelected, hasFocus, row, col);
                int modelRow = t.convertRowIndexToModel(row);
                String status = tableModel.getValueAt(modelRow, 4).toString();
                if (status.equalsIgnoreCase("Available")) {
                    c.setBackground(Color.GREEN.brighter());
                } else {
                    c.setBackground(Color.RED.brighter());
                }
                c.setForeground(Color.BLACK);
                if (isSelected) c.setBackground(new Color(135, 206, 250));
                return c;
            }
        });

        rowSorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(rowSorter);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(new EmptyBorder(10, 20, 10, 20));
        frame.add(scrollPane, BorderLayout.CENTER);

        // Bottom panel
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBorder(new EmptyBorder(0, 20, 20, 20));

        // Buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));

        JButton addBtn = new JButton("Add Book");
        JButton borrowBtn = new JButton("Borrow Book");
        JButton returnBtn = new JButton("Return Book");
        JButton deleteBtn = new JButton("Delete Book");
        JButton refreshBtn = new JButton("Refresh");
        JButton logoutBtn = new JButton("Logout");

        // Style buttons
        styleButton(addBtn, new Color(34, 139, 34));
        styleButton(borrowBtn, new Color(255, 140, 0));
        styleButton(returnBtn, new Color(70, 130, 180));
        styleButton(deleteBtn, new Color(178, 34, 34));
        styleButton(refreshBtn, new Color(105, 105, 105));
        styleButton(logoutBtn, new Color(220, 20, 60));

        // Only librarian can add/delete
        if (isLibrarian) {
            buttonsPanel.add(addBtn);
            buttonsPanel.add(deleteBtn);
        }

        buttonsPanel.add(borrowBtn);
        buttonsPanel.add(returnBtn);
        buttonsPanel.add(refreshBtn);
        buttonsPanel.add(logoutBtn);

        bottomPanel.add(buttonsPanel, BorderLayout.WEST);

        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JLabel searchLabel = new JLabel("Search: ");
        searchLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JTextField searchField = new JTextField(20);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        bottomPanel.add(searchPanel, BorderLayout.EAST);

        frame.add(bottomPanel, BorderLayout.SOUTH);

        // Button actions
        addBtn.addActionListener(e -> addBookDialog());
        borrowBtn.addActionListener(e -> borrowSelectedBook());
        returnBtn.addActionListener(e -> returnSelectedBook());
        deleteBtn.addActionListener(e -> deleteSelectedBook());
        refreshBtn.addActionListener(e -> refreshTable());
        logoutBtn.addActionListener(e -> {
            frame.dispose();
            new RoleSelector(backend); // Back to role selection
        });

        // Search filter
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            private void filter() {
                String text = searchField.getText().trim();
                if (text.length() == 0) rowSorter.setRowFilter(null);
                else rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text, 1, 2));
            }

            @Override public void insertUpdate(DocumentEvent e) { filter(); }
            @Override public void removeUpdate(DocumentEvent e) { filter(); }
            @Override public void changedUpdate(DocumentEvent e) { filter(); }
        });

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Load data
        refreshTable();
    }

    private void styleButton(JButton btn, Color bg) {
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    private void addBookDialog() {
        JTextField titleField = new JTextField();
        JTextField authorField = new JTextField();
        Object[] message = {"Title:", titleField, "Author:", authorField};
        int option = JOptionPane.showConfirmDialog(frame, message, "Add New Book", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String title = titleField.getText().trim();
            String author = authorField.getText().trim();
            if (!title.isEmpty() && !author.isEmpty()) {
                backend.addBook(title, author);
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(frame, "Please enter both title and author.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void borrowSelectedBook() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            int modelRow = table.convertRowIndexToModel(row);
            int id = Integer.parseInt(tableModel.getValueAt(modelRow, 1).toString());
            String status = tableModel.getValueAt(modelRow, 4).toString();
            if (status.equalsIgnoreCase("Available")) {
                backend.updateStatus(id, "Borrowed", currentUser);
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(frame, "This book is already borrowed.", "Cannot Borrow", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Select a book to borrow.", "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void returnSelectedBook() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            int modelRow = table.convertRowIndexToModel(row);
            int id = Integer.parseInt(tableModel.getValueAt(modelRow, 1).toString());
            String status = tableModel.getValueAt(modelRow, 4).toString();
            String borrowedBy = tableModel.getValueAt(modelRow, 5).toString();
            if (status.equalsIgnoreCase("Borrowed")) {
                if (borrowedBy.equals(currentUser)) {
                    backend.updateStatus(id, "Available", null);
                    refreshTable();
                } else {
                    JOptionPane.showMessageDialog(frame, "Only the user who borrowed this book can return it.", "Cannot Return", JOptionPane.WARNING_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(frame, "This book is already available.", "Cannot Return", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Select a book to return.", "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void deleteSelectedBook() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            int modelRow = table.convertRowIndexToModel(row);
            int id = Integer.parseInt(tableModel.getValueAt(modelRow, 1).toString());
            int confirm = JOptionPane.showConfirmDialog(frame,
                    "Are you sure you want to delete the selected book?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                backend.deleteBook(id);
                refreshTable();
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Select a book to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        try {
            var rs = backend.getAllBooks();
            int slno = 1;
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        slno++,
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("status"),
                        rs.getString("borrowed_by") == null ? "" : rs.getString("borrowed_by")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
