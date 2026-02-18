package com.library.servlet;

import com.library.model.Book;
import com.library.service.BookService;
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
 * Book Servlet - Handles book CRUD operations
 */
@WebServlet("/api/books")
public class BookServlet extends HttpServlet {
    
    private BookService bookService;
    private Gson gson;
    
    @Override
    public void init() throws ServletException {
        bookService = new BookService();
        gson = new Gson();
        System.out.println("BookServlet initialized");
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
            
            if ("search".equals(action)) {
                String searchType = request.getParameter("type");
                String query = request.getParameter("query");
                
                List<Book> books;
                if ("title".equals(searchType)) {
                    books = bookService.searchBooksByTitle(query);
                } else if ("author".equals(searchType)) {
                    books = bookService.searchBooksByAuthor(query);
                } else {
                    books = bookService.getAllBooks();
                }
                
                out.print(gson.toJson(books));
            } else if ("available".equals(action)) {
                List<Book> books = bookService.getAvailableBooks();
                out.print(gson.toJson(books));
            } else {
                // Get all books
                List<Book> books = bookService.getAllBooks();
                out.print(gson.toJson(books));
            }
            
            System.out.println("Books fetched successfully");
            
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
            Map<String, Object> bookData = gson.fromJson(sb.toString(), Map.class);
            
            String title = (String) bookData.get("title");
            String author = (String) bookData.get("author");
            String isbn = (String) bookData.get("isbn");
            String publisher = (String) bookData.get("publisher");
            int publicationYear = ((Double) bookData.get("publicationYear")).intValue();
            String category = (String) bookData.get("category");
            int totalCopies = ((Double) bookData.get("totalCopies")).intValue();
            
            // Create book object
            Book book = new Book(title, author, isbn, publisher, publicationYear, 
                               category, totalCopies, totalCopies);
            
            // Add book
            String result = bookService.addBook(book);
            
            if ("SUCCESS".equals(result)) {
                jsonResponse.put("success", true);
                jsonResponse.put("message", "Book added successfully");
                System.out.println("Book added: " + title);
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
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}