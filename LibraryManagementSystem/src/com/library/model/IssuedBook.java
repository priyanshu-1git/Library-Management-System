package com.library.model;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * IssuedBook Model Class
 * Represents a book issuing transaction in the library system
 */
public class IssuedBook {
    
    private int issueId;
    private int bookId;
    private int userId;
    private Date issueDate;
    private Date dueDate;
    private Date returnDate;
    private String status; // ISSUED or RETURNED
    private double fineAmount;
    private Timestamp createdAt;
    
    // Additional fields for display (not in DB)
    private String bookTitle;
    private String bookAuthor;
    private String userName;
    
    // Default Constructor
    public IssuedBook() {
    }
    
    // Constructor for issuing a book
    public IssuedBook(int bookId, int userId, Date issueDate, Date dueDate) {
        this.bookId = bookId;
        this.userId = userId;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
        this.status = "ISSUED";
        this.fineAmount = 0.0;
    }
    
    // Constructor with all fields
    public IssuedBook(int issueId, int bookId, int userId, Date issueDate, 
                      Date dueDate, Date returnDate, String status, double fineAmount,
                      Timestamp createdAt) {
        this.issueId = issueId;
        this.bookId = bookId;
        this.userId = userId;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.status = status;
        this.fineAmount = fineAmount;
        this.createdAt = createdAt;
    }
    
    // Getters and Setters
    public int getIssueId() {
        return issueId;
    }
    
    public void setIssueId(int issueId) {
        this.issueId = issueId;
    }
    
    public int getBookId() {
        return bookId;
    }
    
    public void setBookId(int bookId) {
        this.bookId = bookId;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public Date getIssueDate() {
        return issueDate;
    }
    
    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }
    
    public Date getDueDate() {
        return dueDate;
    }
    
    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }
    
    public Date getReturnDate() {
        return returnDate;
    }
    
    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public double getFineAmount() {
        return fineAmount;
    }
    
    public void setFineAmount(double fineAmount) {
        this.fineAmount = fineAmount;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    public String getBookTitle() {
        return bookTitle;
    }
    
    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }
    
    public String getBookAuthor() {
        return bookAuthor;
    }
    
    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }
    
    public String getUserName() {
        return userName;
    }
    
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    // Check if book is overdue
    public boolean isOverdue() {
        if (returnDate != null) {
            return false; // Already returned
        }
        Date currentDate = new Date(System.currentTimeMillis());
        return currentDate.after(dueDate);
    }
    
    @Override
    public String toString() {
        return "IssuedBook{" +
                "issueId=" + issueId +
                ", bookId=" + bookId +
                ", userId=" + userId +
                ", issueDate=" + issueDate +
                ", dueDate=" + dueDate +
                ", returnDate=" + returnDate +
                ", status='" + status + '\'' +
                ", fineAmount=" + fineAmount +
                '}';
    }
}