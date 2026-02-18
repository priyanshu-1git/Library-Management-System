package com.library.servlet;

import com.library.model.User;
import com.library.service.UserService;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Register Servlet - Handles user registration
 */
@WebServlet("/api/register")
public class RegisterServlet extends HttpServlet {
    
    private UserService userService;
    private Gson gson;
    
    @Override
    public void init() throws ServletException {
        userService = new UserService();
        gson = new Gson();
        System.out.println("RegisterServlet initialized");
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
            Map<String, String> userData = gson.fromJson(sb.toString(), Map.class);
            
            String username = userData.get("username");
            String password = userData.get("password");
            String fullName = userData.get("fullName");
            String email = userData.get("email");
            String role = userData.get("role");
            
            // Create user object
            User user = new User(username, password, fullName, email, role);
            
            // Register user
            String result = userService.registerUser(user);
            
            if ("SUCCESS".equals(result)) {
                jsonResponse.put("success", true);
                jsonResponse.put("message", "Registration successful! Please login.");
                System.out.println("New user registered: " + username);
            } else {
                jsonResponse.put("success", false);
                jsonResponse.put("message", result);
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