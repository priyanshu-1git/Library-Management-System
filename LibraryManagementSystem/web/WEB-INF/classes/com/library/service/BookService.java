package com.library.service;

import com.library.dao.BookDAO;
import com.library.model.Book;

import java.util.List;

/**
 * Book Service Layer
 * Contains business logic for book operations
 */
public class BookService {
    
    private BookDAO bookDAO;
    
    public BookService() {
        this.bookDAO = new BookDAO();
    }
    
    /**
     * Add a new book with validation
     * 
     * @param book Book object
     * @return Result message
     */
    public String addBook(Book book) {
        // Validate input
        if (book.getTitle() == null || book.getTitle().trim().isEmpty()) {
            return "Title cannot be empty";
        }
        
        if (book.getAuthor() == null || book.getAuthor().trim().isEmpty()) {
            return "Author cannot be empty";
        }
        
        if (book.getIsbn() == null || book.getIsbn().trim().isEmpty()) {
            return "ISBN cannot be empty";
        }
        
        if (book.getTotalCopies() <= 0) {
            return "Total copies must be greater than 0";
        }
        
        if (book.getAvailableCopies() < 0 || book.getAvailableCopies() > book.getTotalCopies()) {
            return "Available copies must be between 0 and total copies";
        }
        
        if (book.getPublicationYear() < 1000 || book.getPublicationYear() > 2100) {
            return "Invalid publication year";
        }
        
        // Check if ISBN already exists
        if (bookDAO.isbnExists(book.getIsbn())) {
            return "ISBN already exists";
        }
        
        // Add book
        boolean success = bookDAO.addBook(book);
        
        if (success) {
            return "SUCCESS";
        } else {
            return "Failed to add book. Please try again";
        }
    }
    
    /**
     * Update book information with validation
     * 
     * @param book Book object with updated information
     * @return Result message
     */
    public String updateBook(Book book) {
        // Validate input
        if (book.getBookId() <= 0) {
            return "Invalid book ID";
        }
        
        if (book.getTitle() == null || book.getTitle().trim().isEmpty()) {
            return "Title cannot be empty";
        }
        
        if (book.getAuthor() == null || book.getAuthor().trim().isEmpty()) {
            return "Author cannot be empty";
        }
        
        if (book.getIsbn() == null || book.getIsbn().trim().isEmpty()) {
            return "ISBN cannot be empty";
        }
        
        if (book.getTotalCopies() <= 0) {
            return "Total copies must be greater than 0";
        }
        
        if (book.getAvailableCopies() < 0 || book.getAvailableCopies() > book.getTotalCopies()) {
            return "Available copies must be between 0 and total copies";
        }
        
        // Update book
        boolean success = bookDAO.updateBook(book);
        
        if (success) {
            return "SUCCESS";
        } else {
            return "Failed to update book. Please try again";
        }
    }
    
    /**
     * Delete a book
     * 
     * @param bookId Book ID
     * @return Result message
     */
    public String deleteBook(int bookId) {
        if (bookId <= 0) {
            return "Invalid book ID";
        }
        
        // Check if book is currently issued
        Book book = bookDAO.getBookById(bookId);
        if (book != null && book.getAvailableCopies() < book.getTotalCopies()) {
            return "Cannot delete book. Some copies are currently issued";
        }
        
        boolean success = bookDAO.deleteBook(bookId);
        
        if (success) {
            return "SUCCESS";
        } else {
            return "Failed to delete book. Please try again";
        }
    }
    
    /**
     * Get book by ID
     * 
     * @param bookId Book ID
     * @return Book object if found, null otherwise
     */
    public Book getBookById(int bookId) {
        return bookDAO.getBookById(bookId);
    }
    
    /**
     * Get all books
     * 
     * @return List of all books
     */
    public List<Book> getAllBooks() {
        return bookDAO.getAllBooks();
    }
    
    /**
     * Search books by title
     * 
     * @param title Title to search for
     * @return List of matching books
     */
    public List<Book> searchBooksByTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            return getAllBooks();
        }
        return bookDAO.searchBooksByTitle(title);
    }
    
    /**
     * Search books by author
     * 
     * @param author Author to search for
     * @return List of matching books
     */
    public List<Book> searchBooksByAuthor(String author) {
        if (author == null || author.trim().isEmpty()) {
            return getAllBooks();
        }
        return bookDAO.searchBooksByAuthor(author);
    }
    
    /**
     * Get books by category
     * 
     * @param category Category to filter by
     * @return List of matching books
     */
    public List<Book> getBooksByCategory(String category) {
        return bookDAO.getBooksByCategory(category);
    }
    
    /**
     * Get available books only
     * 
     * @return List of available books
     */
    public List<Book> getAvailableBooks() {
        return bookDAO.getAvailableBooks();
    }
    
    /**
     * Check if book is available for issuing
     * 
     * @param bookId Book ID
     * @return true if available, false otherwise
     */
    public boolean isBookAvailable(int bookId) {
        Book book = bookDAO.getBookById(bookId);
        return book != null && book.getAvailableCopies() > 0;
    }
    
    /**
     * Get total number of books
     * 
     * @return Total book count
     */
    public int getTotalBookCount() {
        return bookDAO.getTotalBookCount();
    }
    
    /**
     * Update available copies
     * 
     * @param bookId Book ID
     * @param change Change in available copies
     * @return true if update successful, false otherwise
     */
    public boolean updateAvailableCopies(int bookId, int change) {
        return bookDAO.updateAvailableCopies(bookId, change);
    }
}