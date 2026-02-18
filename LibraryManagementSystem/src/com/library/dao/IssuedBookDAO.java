package com.library.dao;

import com.library.model.IssuedBook;
import com.library.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * IssuedBook Data Access Object
 * Handles all database operations related to book issuing
 */
public class IssuedBookDAO {
    
    /**
     * Issue a book to a user
     * 
     * @param issuedBook IssuedBook object
     * @return true if book issued successfully, false otherwise
     */
    public boolean issueBook(IssuedBook issuedBook) {
        String sql = "INSERT INTO issued_books (book_id, user_id, issue_date, due_date, status) " +
                     "VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, issuedBook.getBookId());
            pstmt.setInt(2, issuedBook.getUserId());
            pstmt.setDate(3, issuedBook.getIssueDate());
            pstmt.setDate(4, issuedBook.getDueDate());
            pstmt.setString(5, "ISSUED");
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error issuing book: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Return a book
     * 
     * @param issueId Issue ID
     * @param returnDate Return date
     * @param fineAmount Fine amount if any
     * @return true if book returned successfully, false otherwise
     */
    public boolean returnBook(int issueId, Date returnDate, double fineAmount) {
        String sql = "UPDATE issued_books SET return_date = ?, status = 'RETURNED', " +
                     "fine_amount = ? WHERE issue_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setDate(1, returnDate);
            pstmt.setDouble(2, fineAmount);
            pstmt.setInt(3, issueId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error returning book: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Get issued book by ID
     * 
     * @param issueId Issue ID
     * @return IssuedBook object if found, null otherwise
     */
    public IssuedBook getIssuedBookById(int issueId) {
        String sql = "SELECT ib.*, b.title as book_title, b.author as book_author, u.full_name as user_name " +
                     "FROM issued_books ib " +
                     "JOIN books b ON ib.book_id = b.book_id " +
                     "JOIN users u ON ib.user_id = u.user_id " +
                     "WHERE ib.issue_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, issueId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return extractIssuedBookFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error fetching issued book: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Get all issued books (currently issued, not returned)
     * 
     * @return List of currently issued books
     */
    public List<IssuedBook> getAllIssuedBooks() {
        List<IssuedBook> issuedBooks = new ArrayList<>();
        String sql = "SELECT ib.*, b.title as book_title, b.author as book_author, u.full_name as user_name " +
                     "FROM issued_books ib " +
                     "JOIN books b ON ib.book_id = b.book_id " +
                     "JOIN users u ON ib.user_id = u.user_id " +
                     "WHERE ib.status = 'ISSUED' " +
                     "ORDER BY ib.issue_date DESC";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                issuedBooks.add(extractIssuedBookFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error fetching issued books: " + e.getMessage());
        }
        return issuedBooks;
    }
    
    /**
     * Get issued books by user ID
     * 
     * @param userId User ID
     * @return List of books issued to the user
     */
    public List<IssuedBook> getIssuedBooksByUserId(int userId) {
        List<IssuedBook> issuedBooks = new ArrayList<>();
        String sql = "SELECT ib.*, b.title as book_title, b.author as book_author, u.full_name as user_name " +
                     "FROM issued_books ib " +
                     "JOIN books b ON ib.book_id = b.book_id " +
                     "JOIN users u ON ib.user_id = u.user_id " +
                     "WHERE ib.user_id = ? AND ib.status = 'ISSUED' " +
                     "ORDER BY ib.issue_date DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                issuedBooks.add(extractIssuedBookFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error fetching user's issued books: " + e.getMessage());
        }
        return issuedBooks;
    }
    
    /**
     * Get book history by user ID (including returned books)
     * 
     * @param userId User ID
     * @return List of all books issued to the user (including returned)
     */
    public List<IssuedBook> getBookHistoryByUserId(int userId) {
        List<IssuedBook> issuedBooks = new ArrayList<>();
        String sql = "SELECT ib.*, b.title as book_title, b.author as book_author, u.full_name as user_name " +
                     "FROM issued_books ib " +
                     "JOIN books b ON ib.book_id = b.book_id " +
                     "JOIN users u ON ib.user_id = u.user_id " +
                     "WHERE ib.user_id = ? " +
                     "ORDER BY ib.issue_date DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                issuedBooks.add(extractIssuedBookFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error fetching book history: " + e.getMessage());
        }
        return issuedBooks;
    }
    
    /**
     * Get all overdue books
     * 
     * @return List of overdue books
     */
    public List<IssuedBook> getOverdueBooks() {
        List<IssuedBook> overdueBooks = new ArrayList<>();
        String sql = "SELECT ib.*, b.title as book_title, b.author as book_author, u.full_name as user_name " +
                     "FROM issued_books ib " +
                     "JOIN books b ON ib.book_id = b.book_id " +
                     "JOIN users u ON ib.user_id = u.user_id " +
                     "WHERE ib.status = 'ISSUED' AND ib.due_date < CURDATE() " +
                     "ORDER BY ib.due_date";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                overdueBooks.add(extractIssuedBookFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error fetching overdue books: " + e.getMessage());
        }
        return overdueBooks;
    }
    
    /**
     * Get complete issue history
     * 
     * @return List of all issue records
     */
    public List<IssuedBook> getAllIssueHistory() {
        List<IssuedBook> history = new ArrayList<>();
        String sql = "SELECT ib.*, b.title as book_title, b.author as book_author, u.full_name as user_name " +
                     "FROM issued_books ib " +
                     "JOIN books b ON ib.book_id = b.book_id " +
                     "JOIN users u ON ib.user_id = u.user_id " +
                     "ORDER BY ib.issue_date DESC";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                history.add(extractIssuedBookFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error fetching issue history: " + e.getMessage());
        }
        return history;
    }
    
    /**
     * Check if user has already issued a specific book
     * 
     * @param userId User ID
     * @param bookId Book ID
     * @return true if user has already issued the book, false otherwise
     */
    public boolean hasUserIssuedBook(int userId, int bookId) {
        String sql = "SELECT COUNT(*) FROM issued_books " +
                     "WHERE user_id = ? AND book_id = ? AND status = 'ISSUED'";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            pstmt.setInt(2, bookId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            System.err.println("Error checking issued book: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Get count of books currently issued to a user
     * 
     * @param userId User ID
     * @return Number of books issued to the user
     */
    public int getIssuedBookCountByUser(int userId) {
        String sql = "SELECT COUNT(*) FROM issued_books WHERE user_id = ? AND status = 'ISSUED'";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting issued book count: " + e.getMessage());
        }
        return 0;
    }
    
    /**
     * Extract IssuedBook object from ResultSet
     * Helper method to avoid code duplication
     * 
     * @param rs ResultSet
     * @return IssuedBook object
     * @throws SQLException if extraction fails
     */
    private IssuedBook extractIssuedBookFromResultSet(ResultSet rs) throws SQLException {
        IssuedBook issuedBook = new IssuedBook();
        issuedBook.setIssueId(rs.getInt("issue_id"));
        issuedBook.setBookId(rs.getInt("book_id"));
        issuedBook.setUserId(rs.getInt("user_id"));
        issuedBook.setIssueDate(rs.getDate("issue_date"));
        issuedBook.setDueDate(rs.getDate("due_date"));
        issuedBook.setReturnDate(rs.getDate("return_date"));
        issuedBook.setStatus(rs.getString("status"));
        issuedBook.setFineAmount(rs.getDouble("fine_amount"));
        issuedBook.setCreatedAt(rs.getTimestamp("created_at"));
        
        // Additional fields from JOIN
        issuedBook.setBookTitle(rs.getString("book_title"));
        issuedBook.setBookAuthor(rs.getString("book_author"));
        issuedBook.setUserName(rs.getString("user_name"));
        
        return issuedBook;
    }
}