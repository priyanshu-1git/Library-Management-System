package com.library.servlet;

import com.library.model.User;
import com.library.util.AppInitializationListener;
import com.library.util.DBConnection;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/api/developer/reset-demo")
public class DeveloperServlet extends HttpServlet {
    
    private Gson gson;

    @Override
    public void init() throws ServletException {
        gson = new Gson();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        Map<String, Object> jsonResponse = new HashMap<>();
        
        try {
            HttpSession session = request.getSession(false);
            User user = (session != null) ? (User) session.getAttribute("user") : null;
            
            if (user == null || !"DEVELOPER".equalsIgnoreCase(user.getRole())) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                jsonResponse.put("success", false);
                jsonResponse.put("message", "Unauthorized access");
                out.print(gson.toJson(jsonResponse));
                return;
            }
            
            System.out.println("Developer " + user.getUsername() + " initiated demo data reset.");
            
            try (Connection conn = DBConnection.getConnection()) {
                AppInitializationListener.executeSqlScript(conn, "database_schema.sql");
                AppInitializationListener.executeSqlScript(conn, "demo_data.sql");
                
                jsonResponse.put("success", true);
                jsonResponse.put("message", "Demo data reset successfully");
            } catch (Exception e) {
                jsonResponse.put("success", false);
                jsonResponse.put("message", "Failed to reset data: " + e.getMessage());
            }
            
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            jsonResponse.put("success", false);
            jsonResponse.put("message", "Error: " + e.getMessage());
            e.printStackTrace();
        }
        
        out.print(gson.toJson(jsonResponse));
        out.flush();
    }
}
