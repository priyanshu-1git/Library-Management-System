package com.library.servlet;

import com.library.model.User;
import com.library.service.UserService;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Login Servlet - Handles user authentication
 */
@WebServlet("/api/login")
public class LoginServlet extends HttpServlet {
    
    private UserService userService;
    private Gson gson;
    
    @Override
    public void init() throws ServletException {
        userService = new UserService();
        gson = new Gson();
        System.out.println("LoginServlet initialized");
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        
        PrintWriter out = response.getWriter();
        Map<String, Object> jsonResponse = new HashMap<>();
        
        try {
            // Read JSON from request body
            StringBuilder sb = new StringBuilder();
            BufferedReader reader = request.getReader();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            
            // Parse JSON
            @SuppressWarnings("unchecked")
            Map<String, String> loginData = gson.fromJson(sb.toString(), Map.class);
            
            String username = loginData.get("username");
            String password = loginData.get("password");
            
            // Validate input
            if (username == null || username.trim().isEmpty() || 
                password == null || password.trim().isEmpty()) {
                jsonResponse.put("success", false);
                jsonResponse.put("message", "Username and password are required");
                out.print(gson.toJson(jsonResponse));
                return;
            }
            
            // Attempt login
            User user = userService.loginUser(username, password);
            
            if (user != null) {
                // Create session
                HttpSession session = request.getSession();
                session.setAttribute("user", user);
                session.setAttribute("userId", user.getUserId());
                session.setAttribute("role", user.getRole());
                
                // Success response
                jsonResponse.put("success", true);
                jsonResponse.put("message", "Login successful");
                jsonResponse.put("userId", user.getUserId());
                jsonResponse.put("username", user.getUsername());
                jsonResponse.put("fullName", user.getFullName());
                jsonResponse.put("role", user.getRole());
                jsonResponse.put("email", user.getEmail());
                
                System.out.println("User logged in: " + username);
            } else {
                jsonResponse.put("success", false);
                jsonResponse.put("message", "Invalid username or password");
            }
            
        } catch (Exception e) {
            jsonResponse.put("success", false);
            jsonResponse.put("message", "Error: " + e.getMessage());
            e.printStackTrace();
        }
        
        out.print(gson.toJson(jsonResponse));
        out.flush();
    }
    
    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}