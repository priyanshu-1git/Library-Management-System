package com.library.service;

import com.library.dao.UserDAO;
import com.library.model.User;

import java.util.List;

/**
 * User Service Layer
 * Contains business logic for user operations
 */
public class UserService {

    private UserDAO userDAO;

    public UserService() {
        this.userDAO = new UserDAO();
    }

    /**
     * Register a new user with validation
     * 
     * @param user User object
     * @return Result message
     */
    public String registerUser(User user) {
        // Validate input
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            return "Username cannot be empty";
        }

        if (user.getPassword() == null || user.getPassword().length() < 6) {
            return "Password must be at least 6 characters long";
        }

        if (user.getFullName() == null || user.getFullName().trim().isEmpty()) {
            return "Full name cannot be empty";
        }

        if (user.getEmail() == null || !isValidEmail(user.getEmail())) {
            return "Invalid email format";
        }

        if (user.getRole() == null || (!user.getRole().equals("ADMIN") && !user.getRole().equals("STUDENT"))) {
            return "Invalid role. Must be ADMIN or STUDENT";
        }

        // Check if username already exists
        if (userDAO.usernameExists(user.getUsername())) {
            return "Username already exists";
        }

        // Check if email already exists
        if (userDAO.emailExists(user.getEmail())) {
            return "Email already exists";
        }

        // Register user
        boolean success = userDAO.registerUser(user);

        if (success) {
            return "SUCCESS";
        } else {
            return "Registration failed. Please try again";
        }
    }

    /**
     * Login user with validation
     * 
     * @param username Username
     * @param password Password
     * @return User object if login successful, null otherwise
     */
    public User loginUser(String username, String password) {
        // Validate input
        if (username == null || username.trim().isEmpty()) {
            return null;
        }

        if (password == null || password.trim().isEmpty()) {
            return null;
        }

        // Attempt login
        return userDAO.loginUser(username, password);
    }

    /**
     * Get user by ID
     * 
     * @param userId User ID
     * @return User object if found, null otherwise
     */
    public User getUserById(int userId) {
        return userDAO.getUserById(userId);
    }

    /**
     * Get all users
     * 
     * @return List of all users
     */
    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }

    /**
     * Get all students
     * 
     * @return List of all student users
     */
    public List<User> getAllStudents() {
        return userDAO.getAllStudents();
    }

    /**
     * Update user information
     * 
     * @param user User object with updated information
     * @return Result message
     */
    public String updateUser(User user) {
        // Validate input
        if (user.getUserId() <= 0) {
            return "Invalid user ID";
        }

        if (user.getFullName() == null || user.getFullName().trim().isEmpty()) {
            return "Full name cannot be empty";
        }

        if (user.getEmail() == null || !isValidEmail(user.getEmail())) {
            return "Invalid email format";
        }

        // Update user
        boolean success = userDAO.updateUser(user);

        if (success) {
            return "SUCCESS";
        } else {
            return "Update failed. Please try again";
        }
    }

    /**
     * Delete user
     * 
     * @param userId User ID
     * @return Result message
     */
    public String deleteUser(int userId) {
        if (userId <= 0) {
            return "Invalid user ID";
        }

        boolean success = userDAO.deleteUser(userId);

        if (success) {
            return "SUCCESS";
        } else {
            return "Deletion failed. User may have issued books";
        }
    }

    /**
     * Validate email format
     * Simple email validation
     * 
     * @param email Email to validate
     * @return true if valid, false otherwise
     */
    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(emailRegex);
    }

    /**
     * Check if username exists
     * 
     * @param username Username to check
     * @return true if exists, false otherwise
     */
    public boolean usernameExists(String username) {
        return userDAO.usernameExists(username);
    }

    /**
     * Check if email exists
     * 
     * @param email Email to check
     * @return true if exists, false otherwise
     */
    public boolean emailExists(String email) {
        return userDAO.emailExists(email);
    }
}