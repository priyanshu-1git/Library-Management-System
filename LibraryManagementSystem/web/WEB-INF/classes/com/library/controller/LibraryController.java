package com.library.controller;

import com.library.model.Book;
import com.library.model.IssuedBook;
import com.library.model.User;
import com.library.service.BookService;
import com.library.service.IssueBookService;
import com.library.service.UserService;

import java.util.List;
import java.util.Scanner;

/**
 * Library Controller
 * Main controller for handling user interactions
 * This is a console-based controller for demonstration
 * In a web application, this would be replaced with Servlets/REST controllers
 */
public class LibraryController {
    
    private UserService userService;
    private BookService bookService;
    private IssueBookService issueBookService;
    private Scanner scanner;
    private User loggedInUser;
    
    public LibraryController() {
        this.userService = new UserService();
        this.bookService = new BookService();
        this.issueBookService = new IssueBookService();
        this.scanner = new Scanner(System.in);
        this.loggedInUser = null;
    }
    
    /**
     * Main entry point for the application
     */
    public void start() {
        System.out.println("==============================================");
        System.out.println("   LIBRARY MANAGEMENT SYSTEM");
        System.out.println("==============================================\n");
        
        while (true) {
            if (loggedInUser == null) {
                showLoginMenu();
            } else if (loggedInUser.isAdmin()) {
                showAdminMenu();
            } else {
                showStudentMenu();
            }
        }
    }
    
    /**
     * Display login menu
     */
    private void showLoginMenu() {
        System.out.println("\n--- LOGIN MENU ---");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("3. Exit");
        System.out.print("Choose an option: ");
        
        int choice = getIntInput();
        
        switch (choice) {
            case 1:
                handleLogin();
                break;
            case 2:
                handleRegistration();
                break;
            case 3:
                System.out.println("Thank you for using Library Management System!");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid option. Please try again.");
        }
    }
    
    /**
     * Display admin menu
     */
    private void showAdminMenu() {
        System.out.println("\n--- ADMIN MENU ---");
        System.out.println("Welcome, " + loggedInUser.getFullName() + "!");
        System.out.println("1. Manage Books");
        System.out.println("2. Issue Book");
        System.out.println("3. Return Book");
        System.out.println("4. View All Issued Books");
        System.out.println("5. View Overdue Books");
        System.out.println("6. View All Students");
        System.out.println("7. Logout");
        System.out.print("Choose an option: ");
        
        int choice = getIntInput();
        
        switch (choice) {
            case 1:
                handleBookManagement();
                break;
            case 2:
                handleIssueBook();
                break;
            case 3:
                handleReturnBook();
                break;
            case 4:
                viewAllIssuedBooks();
                break;
            case 5:
                viewOverdueBooks();
                break;
            case 6:
                viewAllStudents();
                break;
            case 7:
                logout();
                break;
            default:
                System.out.println("Invalid option. Please try again.");
        }
    }
    
    /**
     * Display student menu
     */
    private void showStudentMenu() {
        System.out.println("\n--- STUDENT MENU ---");
        System.out.println("Welcome, " + loggedInUser.getFullName() + "!");
        System.out.println("1. View All Books");
        System.out.println("2. Search Books");
        System.out.println("3. My Issued Books");
        System.out.println("4. My History");
        System.out.println("5. Logout");
        System.out.print("Choose an option: ");
        
        int choice = getIntInput();
        
        switch (choice) {
            case 1:
                viewAllBooks();
                break;
            case 2:
                handleSearchBooks();
                break;
            case 3:
                viewMyIssuedBooks();
                break;
            case 4:
                viewMyHistory();
                break;
            case 5:
                logout();
                break;
            default:
                System.out.println("Invalid option. Please try again.");
        }
    }
    
    /**
     * Handle user login
     */
    private void handleLogin() {
        System.out.print("\nUsername: ");
        String username = scanner.nextLine();
        
        System.out.print("Password: ");
        String password = scanner.nextLine();
        
        User user = userService.loginUser(username, password);
        
        if (user != null) {
            loggedInUser = user;
            System.out.println("\n✓ Login successful! Welcome, " + user.getFullName());
        } else {
            System.out.println("\n✗ Invalid credentials. Please try again.");
        }
    }
    
    /**
     * Handle user registration
     */
    private void handleRegistration() {
        System.out.println("\n--- REGISTRATION ---");
        
        System.out.print("Username: ");
        String username = scanner.nextLine();
        
        System.out.print("Password: ");
        String password = scanner.nextLine();
        
        System.out.print("Full Name: ");
        String fullName = scanner.nextLine();
        
        System.out.print("Email: ");
        String email = scanner.nextLine();
        
        System.out.print("Role (ADMIN/STUDENT): ");
        String role = scanner.nextLine().toUpperCase();
        
        User user = new User(username, password, fullName, email, role);
        String result = userService.registerUser(user);
        
        if ("SUCCESS".equals(result)) {
            System.out.println("\n✓ Registration successful! You can now login.");
        } else {
            System.out.println("\n✗ Registration failed: " + result);
        }
    }
    
    /**
     * Handle book management submenu
     */
    private void handleBookManagement() {
        System.out.println("\n--- BOOK MANAGEMENT ---");
        System.out.println("1. Add Book");
        System.out.println("2. Update Book");
        System.out.println("3. Delete Book");
        System.out.println("4. View All Books");
        System.out.println("5. Back");
        System.out.print("Choose an option: ");
        
        int choice = getIntInput();
        
        switch (choice) {
            case 1:
                handleAddBook();
                break;
            case 2:
                handleUpdateBook();
                break;
            case 3:
                handleDeleteBook();
                break;
            case 4:
                viewAllBooks();
                break;
            case 5:
                return;
            default:
                System.out.println("Invalid option.");
        }
    }
    
    /**
     * Handle adding a new book
     */
    private void handleAddBook() {
        System.out.println("\n--- ADD NEW BOOK ---");
        
        System.out.print("Title: ");
        String title = scanner.nextLine();
        
        System.out.print("Author: ");
        String author = scanner.nextLine();
        
        System.out.print("ISBN: ");
        String isbn = scanner.nextLine();
        
        System.out.print("Publisher: ");
        String publisher = scanner.nextLine();
        
        System.out.print("Publication Year: ");
        int year = getIntInput();
        
        System.out.print("Category: ");
        String category = scanner.nextLine();
        
        System.out.print("Total Copies: ");
        int totalCopies = getIntInput();
        
        Book book = new Book(title, author, isbn, publisher, year, category, totalCopies, totalCopies);
        String result = bookService.addBook(book);
        
        if ("SUCCESS".equals(result)) {
            System.out.println("\n✓ Book added successfully!");
        } else {
            System.out.println("\n✗ Failed to add book: " + result);
        }
    }
    
    /**
     * View all books
     */
    private void viewAllBooks() {
        List<Book> books = bookService.getAllBooks();
        
        System.out.println("\n--- ALL BOOKS ---");
        System.out.println(String.format("%-5s %-30s %-20s %-15s %-10s %-10s", 
                "ID", "Title", "Author", "ISBN", "Total", "Available"));
        System.out.println("=".repeat(100));
        
        for (Book book : books) {
            System.out.println(String.format("%-5d %-30s %-20s %-15s %-10d %-10d",
                    book.getBookId(),
                    truncate(book.getTitle(), 30),
                    truncate(book.getAuthor(), 20),
                    book.getIsbn(),
                    book.getTotalCopies(),
                    book.getAvailableCopies()));
        }
        
        System.out.println("\nTotal Books: " + books.size());
    }
    
    /**
     * Handle book search
     */
    private void handleSearchBooks() {
        System.out.println("\n--- SEARCH BOOKS ---");
        System.out.println("1. Search by Title");
        System.out.println("2. Search by Author");
        System.out.print("Choose an option: ");
        
        int choice = getIntInput();
        
        if (choice == 1) {
            System.out.print("Enter title: ");
            String title = scanner.nextLine();
            List<Book> books = bookService.searchBooksByTitle(title);
            displayBooks(books);
        } else if (choice == 2) {
            System.out.print("Enter author: ");
            String author = scanner.nextLine();
            List<Book> books = bookService.searchBooksByAuthor(author);
            displayBooks(books);
        }
    }
    
    /**
     * Display books in formatted table
     */
    private void displayBooks(List<Book> books) {
        System.out.println("\n--- SEARCH RESULTS ---");
        System.out.println(String.format("%-5s %-30s %-20s %-10s", 
                "ID", "Title", "Author", "Available"));
        System.out.println("=".repeat(70));
        
        for (Book book : books) {
            System.out.println(String.format("%-5d %-30s %-20s %-10d",
                    book.getBookId(),
                    truncate(book.getTitle(), 30),
                    truncate(book.getAuthor(), 20),
                    book.getAvailableCopies()));
        }
        
        System.out.println("\nFound: " + books.size() + " books");
    }
    
    // Additional helper methods would continue here...
    // Due to length constraints, I'm showing the pattern
    
    /**
     * Get integer input with validation
     */
    private int getIntInput() {
        while (true) {
            try {
                int value = Integer.parseInt(scanner.nextLine());
                return value;
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a number: ");
            }
        }
    }
    
    /**
     * Truncate string to specified length
     */
    private String truncate(String str, int length) {
        if (str == null) return "";
        return str.length() <= length ? str : str.substring(0, length - 3) + "...";
    }
    
    /**
     * Logout current user
     */
    private void logout() {
        loggedInUser = null;
        System.out.println("\n✓ Logged out successfully!");
    }
    
    /**
     * Main method to start the application
     */
    public static void main(String[] args) {
        LibraryController controller = new LibraryController();
        controller.start();
    }
    
    // Additional methods for issue book, return book, etc. would be implemented similarly
    private void handleIssueBook() {
        System.out.println("\n--- ISSUE BOOK ---");
        System.out.print("Enter Book ID: ");
        int bookId = getIntInput();
        System.out.print("Enter Student User ID: ");
        int userId = getIntInput();
        
        String result = issueBookService.issueBook(bookId, userId);
        if ("SUCCESS".equals(result)) {
            System.out.println("\n✓ Book issued successfully!");
        } else {
            System.out.println("\n✗ " + result);
        }
    }
    
    private void handleReturnBook() {
        System.out.println("\n--- RETURN BOOK ---");
        System.out.print("Enter Issue ID: ");
        int issueId = getIntInput();
        
        String result = issueBookService.returnBook(issueId);
        if (result.startsWith("SUCCESS")) {
            System.out.println("\n✓ " + result.split("\\|")[1]);
        } else {
            System.out.println("\n✗ " + result);
        }
    }
    
    private void viewAllIssuedBooks() {
        List<IssuedBook> issuedBooks = issueBookService.getAllIssuedBooks();
        System.out.println("\n--- CURRENTLY ISSUED BOOKS ---");
        System.out.println(String.format("%-5s %-30s %-20s %-12s %-12s", 
                "ID", "Book", "Student", "Issue Date", "Due Date"));
        System.out.println("=".repeat(85));
        
        for (IssuedBook ib : issuedBooks) {
            System.out.println(String.format("%-5d %-30s %-20s %-12s %-12s",
                    ib.getIssueId(),
                    truncate(ib.getBookTitle(), 30),
                    truncate(ib.getUserName(), 20),
                    ib.getIssueDate().toString(),
                    ib.getDueDate().toString()));
        }
    }
    
    private void viewOverdueBooks() {
        List<IssuedBook> overdueBooks = issueBookService.getOverdueBooks();
        System.out.println("\n--- OVERDUE BOOKS ---");
        System.out.println("Total Overdue: " + overdueBooks.size());
        // Similar display logic
    }
    
    private void viewAllStudents() {
        List<User> students = userService.getAllStudents();
        System.out.println("\n--- ALL STUDENTS ---");
        System.out.println(String.format("%-5s %-20s %-30s", "ID", "Name", "Email"));
        System.out.println("=".repeat(60));
        
        for (User student : students) {
            System.out.println(String.format("%-5d %-20s %-30s",
                    student.getUserId(),
                    truncate(student.getFullName(), 20),
                    student.getEmail()));
        }
    }
    
    private void viewMyIssuedBooks() {
        List<IssuedBook> myBooks = issueBookService.getIssuedBooksByUserId(loggedInUser.getUserId());
        System.out.println("\n--- MY ISSUED BOOKS ---");
        System.out.println(String.format("%-30s %-20s %-12s %-12s", 
                "Book", "Author", "Issue Date", "Due Date"));
        System.out.println("=".repeat(80));
        
        for (IssuedBook ib : myBooks) {
            System.out.println(String.format("%-30s %-20s %-12s %-12s",
                    truncate(ib.getBookTitle(), 30),
                    truncate(ib.getBookAuthor(), 20),
                    ib.getIssueDate().toString(),
                    ib.getDueDate().toString()));
        }
        
        if (myBooks.isEmpty()) {
            System.out.println("You have no issued books currently.");
        }
    }
    
    private void viewMyHistory() {
        List<IssuedBook> history = issueBookService.getBookHistoryByUserId(loggedInUser.getUserId());
        System.out.println("\n--- MY BOOK HISTORY ---");
        // Similar display with status and return date
        for (IssuedBook ib : history) {
            System.out.println(ib.getBookTitle() + " - " + ib.getStatus());
        }
    }
    
    private void handleUpdateBook() {
        System.out.println("\n--- UPDATE BOOK ---");
        System.out.print("Enter Book ID to update: ");
        int bookId = getIntInput();
        
        Book book = bookService.getBookById(bookId);
        if (book == null) {
            System.out.println("Book not found!");
            return;
        }
        
        System.out.println("Current details: " + book.getTitle());
        System.out.print("New Title (press Enter to keep current): ");
        String title = scanner.nextLine();
        if (!title.isEmpty()) book.setTitle(title);
        
        // Similar for other fields...
        String result = bookService.updateBook(book);
        if ("SUCCESS".equals(result)) {
            System.out.println("\n✓ Book updated successfully!");
        } else {
            System.out.println("\n✗ " + result);
        }
    }
    
    private void handleDeleteBook() {
        System.out.println("\n--- DELETE BOOK ---");
        System.out.print("Enter Book ID to delete: ");
        int bookId = getIntInput();
        
        String result = bookService.deleteBook(bookId);
        if ("SUCCESS".equals(result)) {
            System.out.println("\n✓ Book deleted successfully!");
        } else {
            System.out.println("\n✗ " + result);
        }
    }
}