package com.library.dao;

import com.library.model.Book;
import com.library.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Book Data Access Object
 * Handles all database operations related to books
 */
public class BookDAO {
    
    /**
     * Add a new book to the library
     * 
     * @param book Book object
     * @return true if book added successfully, false otherwise
     */
    public boolean addBook(Book book) {
        String sql = "INSERT INTO books (title, author, isbn, publisher, publication_year, " +
                     "category, total_copies, available_copies) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getAuthor());
            pstmt.setString(3, book.getIsbn());
            pstmt.setString(4, book.getPublisher());
            pstmt.setInt(5, book.getPublicationYear());
            pstmt.setString(6, book.getCategory());
            pstmt.setInt(7, book.getTotalCopies());
            pstmt.setInt(8, book.getAvailableCopies());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error adding book: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Update book information
     * 
     * @param book Book object with updated information
     * @return true if update successful, false otherwise
     */
    public boolean updateBook(Book book) {
        String sql = "UPDATE books SET title = ?, author = ?, isbn = ?, publisher = ?, " +
                     "publication_year = ?, category = ?, total_copies = ?, available_copies = ? " +
                     "WHERE book_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getAuthor());
            pstmt.setString(3, book.getIsbn());
            pstmt.setString(4, book.getPublisher());
            pstmt.setInt(5, book.getPublicationYear());
            pstmt.setString(6, book.getCategory());
            pstmt.setInt(7, book.getTotalCopies());
            pstmt.setInt(8, book.getAvailableCopies());
            pstmt.setInt(9, book.getBookId());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating book: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Delete a book
     * 
     * @param bookId Book ID
     * @return true if deletion successful, false otherwise
     */
    public boolean deleteBook(int bookId) {
        String sql = "DELETE FROM books WHERE book_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, bookId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting book: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Get book by ID
     * 
     * @param bookId Book ID
     * @return Book object if found, null otherwise
     */
    public Book getBookById(int bookId) {
        String sql = "SELECT * FROM books WHERE book_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, bookId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return extractBookFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error fetching book: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Get all books
     * 
     * @return List of all books
     */
    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books ORDER BY title";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                books.add(extractBookFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error fetching books: " + e.getMessage());
        }
        return books;
    }
    
    /**
     * Search books by title
     * 
     * @param title Title to search for (partial match)
     * @return List of matching books
     */
    public List<Book> searchBooksByTitle(String title) {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books WHERE title LIKE ? ORDER BY title";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, "%" + title + "%");
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                books.add(extractBookFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error searching books by title: " + e.getMessage());
        }
        return books;
    }
    
    /**
     * Search books by author
     * 
     * @param author Author to search for (partial match)
     * @return List of matching books
     */
    public List<Book> searchBooksByAuthor(String author) {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books WHERE author LIKE ? ORDER BY title";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, "%" + author + "%");
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                books.add(extractBookFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error searching books by author: " + e.getMessage());
        }
        return books;
    }
    
    /**
     * Search books by category
     * 
     * @param category Category to filter by
     * @return List of matching books
     */
    public List<Book> getBooksByCategory(String category) {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books WHERE category = ? ORDER BY title";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, category);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                books.add(extractBookFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error fetching books by category: " + e.getMessage());
        }
        return books;
    }
    
    /**
     * Get available books only
     * 
     * @return List of available books
     */
    public List<Book> getAvailableBooks() {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books WHERE available_copies > 0 ORDER BY title";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                books.add(extractBookFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error fetching available books: " + e.getMessage());
        }
        return books;
    }
    
    /**
     * Update available copies count
     * 
     * @param bookId Book ID
     * @param change Change in available copies (positive or negative)
     * @return true if update successful, false otherwise
     */
    public boolean updateAvailableCopies(int bookId, int change) {
        String sql = "UPDATE books SET available_copies = available_copies + ? WHERE book_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, change);
            pstmt.setInt(2, bookId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating available copies: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Check if ISBN already exists
     * 
     * @param isbn ISBN to check
     * @return true if ISBN exists, false otherwise
     */
    public boolean isbnExists(String isbn) {
        String sql = "SELECT COUNT(*) FROM books WHERE isbn = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, isbn);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            System.err.println("Error checking ISBN: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Get total number of books
     * 
     * @return Total book count
     */
    public int getTotalBookCount() {
        String sql = "SELECT COUNT(*) FROM books";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting book count: " + e.getMessage());
        }
        return 0;
    }
    
    /**
     * Extract Book object from ResultSet
     * Helper method to avoid code duplication
     * 
     * @param rs ResultSet
     * @return Book object
     * @throws SQLException if extraction fails
     */
    private Book extractBookFromResultSet(ResultSet rs) throws SQLException {
        Book book = new Book();
        book.setBookId(rs.getInt("book_id"));
        book.setTitle(rs.getString("title"));
        book.setAuthor(rs.getString("author"));
        book.setIsbn(rs.getString("isbn"));
        book.setPublisher(rs.getString("publisher"));
        book.setPublicationYear(rs.getInt("publication_year"));
        book.setCategory(rs.getString("category"));
        book.setTotalCopies(rs.getInt("total_copies"));
        book.setAvailableCopies(rs.getInt("available_copies"));
        book.setCreatedAt(rs.getTimestamp("created_at"));
        book.setUpdatedAt(rs.getTimestamp("updated_at"));
        return book;
    }
}