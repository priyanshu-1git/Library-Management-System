package com.library.service;

import com.library.dao.BookDAO;
import com.library.dao.IssuedBookDAO;
import com.library.model.Book;
import com.library.model.IssuedBook;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;

/**
 * IssueBook Service Layer
 * Contains business logic for book issuing and returning operations
 */
public class IssueBookService {
    
    private IssuedBookDAO issuedBookDAO;
    private BookDAO bookDAO;
    
    // Configuration constants
    private static final int ISSUE_DAYS = 14; // Default issue period: 14 days
    private static final int MAX_BOOKS_PER_USER = 3; // Maximum books a user can issue
    private static final double FINE_PER_DAY = 5.0; // Fine amount per day of delay (in Rupees)
    
    public IssueBookService() {
        this.issuedBookDAO = new IssuedBookDAO();
        this.bookDAO = new BookDAO();
    }
    
    /**
     * Issue a book to a user with validation
     * 
     * @param bookId Book ID
     * @param userId User ID
     * @return Result message
     */
    public String issueBook(int bookId, int userId) {
        // Validate IDs
        if (bookId <= 0 || userId <= 0) {
            return "Invalid book or user ID";
        }
        
        // Check if book exists
        Book book = bookDAO.getBookById(bookId);
        if (book == null) {
            return "Book not found";
        }
        
        // Check if book is available
        if (book.getAvailableCopies() <= 0) {
            return "Book is not available. All copies are issued";
        }
        
        // Check if user has already issued this book
        if (issuedBookDAO.hasUserIssuedBook(userId, bookId)) {
            return "You have already issued this book. Return it before issuing again";
        }
        
        // Check if user has reached maximum book limit
        int currentIssueCount = issuedBookDAO.getIssuedBookCountByUser(userId);
        if (currentIssueCount >= MAX_BOOKS_PER_USER) {
            return "You have reached the maximum limit of " + MAX_BOOKS_PER_USER + " books";
        }
        
        // Calculate dates
        Date issueDate = new Date(System.currentTimeMillis());
        Date dueDate = calculateDueDate(issueDate);
        
        // Create IssuedBook object
        IssuedBook issuedBook = new IssuedBook(bookId, userId, issueDate, dueDate);
        
        // Issue the book
        boolean issueSuccess = issuedBookDAO.issueBook(issuedBook);
        
        if (!issueSuccess) {
            return "Failed to issue book. Please try again";
        }
        
        // Update available copies
        boolean updateSuccess = bookDAO.updateAvailableCopies(bookId, -1);
        
        if (!updateSuccess) {
            return "Book issued but failed to update inventory. Contact administrator";
        }
        
        return "SUCCESS";
    }
    
    /**
     * Return a book with fine calculation
     * 
     * @param issueId Issue ID
     * @return Result message with fine details
     */
    public String returnBook(int issueId) {
        // Validate issue ID
        if (issueId <= 0) {
            return "Invalid issue ID";
        }
        
        // Get issued book details
        IssuedBook issuedBook = issuedBookDAO.getIssuedBookById(issueId);
        
        if (issuedBook == null) {
            return "Issue record not found";
        }
        
        if ("RETURNED".equals(issuedBook.getStatus())) {
            return "Book has already been returned";
        }
        
        // Calculate return date and fine
        Date returnDate = new Date(System.currentTimeMillis());
        double fineAmount = calculateFine(issuedBook.getDueDate(), returnDate);
        
        // Return the book
        boolean returnSuccess = issuedBookDAO.returnBook(issueId, returnDate, fineAmount);
        
        if (!returnSuccess) {
            return "Failed to return book. Please try again";
        }
        
        // Update available copies
        boolean updateSuccess = bookDAO.updateAvailableCopies(issuedBook.getBookId(), 1);
        
        if (!updateSuccess) {
            return "Book returned but failed to update inventory. Contact administrator";
        }
        
        if (fineAmount > 0) {
            return "SUCCESS|Book returned successfully. Fine: Rs " + String.format("%.2f", fineAmount);
        } else {
            return "SUCCESS|Book returned successfully. No fine";
        }
    }
    
    /**
     * Get all currently issued books
     * 
     * @return List of issued books
     */
    public List<IssuedBook> getAllIssuedBooks() {
        return issuedBookDAO.getAllIssuedBooks();
    }
    
    /**
     * Get issued books by user ID
     * 
     * @param userId User ID
     * @return List of books issued to the user
     */
    public List<IssuedBook> getIssuedBooksByUserId(int userId) {
        return issuedBookDAO.getIssuedBooksByUserId(userId);
    }
    
    /**
     * Get book history by user ID
     * 
     * @param userId User ID
     * @return List of all books issued to the user (including returned)
     */
    public List<IssuedBook> getBookHistoryByUserId(int userId) {
        return issuedBookDAO.getBookHistoryByUserId(userId);
    }
    
    /**
     * Get all overdue books
     * 
     * @return List of overdue books
     */
    public List<IssuedBook> getOverdueBooks() {
        return issuedBookDAO.getOverdueBooks();
    }
    
    /**
     * Get complete issue history
     * 
     * @return List of all issue records
     */
    public List<IssuedBook> getAllIssueHistory() {
        return issuedBookDAO.getAllIssueHistory();
    }
    
    /**
     * Get issued book by ID
     * 
     * @param issueId Issue ID
     * @return IssuedBook object if found, null otherwise
     */
    public IssuedBook getIssuedBookById(int issueId) {
        return issuedBookDAO.getIssuedBookById(issueId);
    }
    
    /**
     * Calculate due date based on issue date
     * 
     * @param issueDate Issue date
     * @return Due date
     */
    private Date calculateDueDate(Date issueDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(issueDate);
        calendar.add(Calendar.DAY_OF_MONTH, ISSUE_DAYS);
        return new Date(calendar.getTimeInMillis());
    }
    
    /**
     * Calculate fine based on due date and return date
     * 
     * @param dueDate Due date
     * @param returnDate Return date
     * @return Fine amount in Rupees
     */
    private double calculateFine(Date dueDate, Date returnDate) {
        // If returned on or before due date, no fine
        if (returnDate.before(dueDate) || returnDate.equals(dueDate)) {
            return 0.0;
        }
        
        // Calculate days overdue
        long diffInMillis = returnDate.getTime() - dueDate.getTime();
        long daysOverdue = diffInMillis / (1000 * 60 * 60 * 24);
        
        // Calculate fine
        return daysOverdue * FINE_PER_DAY;
    }
    
    /**
     * Get maximum books per user limit
     * 
     * @return Maximum books per user
     */
    public int getMaxBooksPerUser() {
        return MAX_BOOKS_PER_USER;
    }
    
    /**
     * Get issue period in days
     * 
     * @return Issue period
     */
    public int getIssueDays() {
        return ISSUE_DAYS;
    }
    
    /**
     * Get fine per day in Rupees
     * 
     * @return Fine per day
     */
    public double getFinePerDay() {
        return FINE_PER_DAY;
    }
}