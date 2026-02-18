package com.library.servlet;

import com.library.model.IssuedBook;
import com.library.service.IssueBookService;
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
import java.util.List;
import java.util.Map;

/**
 * IssueBook Servlet - Handles book issue and return operations
 */
@WebServlet("/api/issue")
public class IssueBookServlet extends HttpServlet {
    
    private IssueBookService issueBookService;
    private Gson gson;
    
    @Override
    public void init() throws ServletException {
        issueBookService = new IssueBookService();
        gson = new Gson();
        System.out.println("IssueBookServlet initialized");
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        
        PrintWriter out = response.getWriter();
        
        try {
            String action = request.getParameter("action");
            String userIdParam = request.getParameter("userId");
            
            List<IssuedBook> issuedBooks;
            
            if ("mybooks".equals(action) && userIdParam != null) {
                int userId = Integer.parseInt(userIdParam);
                issuedBooks = issueBookService.getIssuedBooksByUserId(userId);
            } else if ("history".equals(action) && userIdParam != null) {
                int userId = Integer.parseInt(userIdParam);
                issuedBooks = issueBookService.getBookHistoryByUserId(userId);
            } else if ("overdue".equals(action)) {
                issuedBooks = issueBookService.getOverdueBooks();
            } else {
                issuedBooks = issueBookService.getAllIssuedBooks();
            }
            
            out.print(gson.toJson(issuedBooks));
            System.out.println("Issued books fetched successfully");
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error: " + e.getMessage());
            out.print(gson.toJson(errorResponse));
            e.printStackTrace();
        }
        
        out.flush();
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
            Map<String, Object> issueData = gson.fromJson(sb.toString(), Map.class);
            
            String action = (String) issueData.get("action");
            
            if ("issue".equals(action)) {
                int bookId = ((Double) issueData.get("bookId")).intValue();
                int userId = ((Double) issueData.get("userId")).intValue();
                
                String result = issueBookService.issueBook(bookId, userId);
                
                if ("SUCCESS".equals(result)) {
                    jsonResponse.put("success", true);
                    jsonResponse.put("message", "Book issued successfully");
                    System.out.println("Book issued - BookID: " + bookId + ", UserID: " + userId);
                } else {
                    jsonResponse.put("success", false);
                    jsonResponse.put("message", result);
                }
                
            } else if ("return".equals(action)) {
                int issueId = ((Double) issueData.get("issueId")).intValue();
                
                String result = issueBookService.returnBook(issueId);
                
                if (result.startsWith("SUCCESS")) {
                    jsonResponse.put("success", true);
                    String[] parts = result.split("\\|");
                    jsonResponse.put("message", parts.length > 1 ? parts[1] : "Book returned successfully");
                    System.out.println("Book returned - IssueID: " + issueId);
                } else {
                    jsonResponse.put("success", false);
                    jsonResponse.put("message", result);
                }
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