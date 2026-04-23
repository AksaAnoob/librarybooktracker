Library Book Tracker

A Java-based desktop application for managing library books with user authentication and
role-based access. The system allows users and librarians to interact with the library through
a clean graphical interface.

Features

• User Login & Registration
• Role Selection (User / Librarian)
• Add, Borrow, Return, and Delete Books
• Search and Filter Books
• Interactive GUI using Java Swing

Project Structure

src/com/library/frontend/
├── AuthChoice.java # Authentication selection
├── LoginUI.java # Login interface
├── RegisterUI.java # Registration interface
├── RoleSelector.java # Role selection (Main Entry Point)
├── LibraryUI.java # Main library interface
└── LibraryBackend.java # Backend logic & database handling
README.md

Technologies Used

• Java (Core Java)
• Java Swing (GUI),html
• JDBC (if database is used)


How to Run

1. Clone the repository
2. Open the project in an IDE (IntelliJ / Eclipse / VS Code)
3. Compile all Java files
4. Run:RoleSelector.java
5. Use the interface to manage
6.  library operations
   
Notes

• RoleSelector.java is the main entry point of the application
• Ensure all files are inside the correct package:
com.library.frontend

Author
• Aksa Anoob
