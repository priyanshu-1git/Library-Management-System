package com.library.util;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

@WebListener
public class AppInitializationListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("Checking database initialization...");
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {
             
            boolean needsInit = false;
            try {
                ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM users");
                if (rs.next() && rs.getInt(1) == 0) {
                    needsInit = true; // Table exists but empty
                }
            } catch (Exception e) {
                // Table probably doesn't exist
                needsInit = true;
            }

            if (needsInit) {
                System.out.println("Database is empty or uninitialized. Initializing schema and demo data...");
                executeSqlScript(conn, "database_schema.sql");
                executeSqlScript(conn, "demo_data.sql");
                System.out.println("Initialization complete.");
            } else {
                System.out.println("Database already initialized.");
            }

        } catch (Exception e) {
            System.err.println("Failed to initialize database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void executeSqlScript(Connection conn, String scriptName) {
        String lastSql = "";
        try (java.io.InputStream is = AppInitializationListener.class.getResourceAsStream("/" + scriptName);
             java.io.BufferedReader br = new java.io.BufferedReader(new java.io.InputStreamReader(is, "UTF-8"))) {
            StringBuilder sb = new StringBuilder();
            String line;
            try (Statement stmt = conn.createStatement()) {
                while ((line = br.readLine()) != null) {
                    if (line.trim().startsWith("--") || line.trim().isEmpty()) continue;
                    
                    int commentIndex = line.indexOf("--");
                    if (commentIndex != -1) {
                        line = line.substring(0, commentIndex);
                    }
                    
                    if (line.trim().isEmpty()) continue;
                    
                    sb.append(line).append("\n");
                    if (line.trim().endsWith(";")) {
                        lastSql = sb.toString();
                        if (lastSql.trim().length() > 1) {
                            stmt.execute(lastSql);
                        }
                        sb.setLength(0);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error executing script " + scriptName + " on SQL: [" + lastSql + "]");
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        DBConnection.closeConnection();
    }
}
