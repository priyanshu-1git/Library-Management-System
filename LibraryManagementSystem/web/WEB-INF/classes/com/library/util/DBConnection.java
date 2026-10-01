package com.library.util;

import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Database Connection Utility Class
 * Provides connection to MySQL/MariaDB database using JDBC.
 */
public class DBConnection {

    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";

    // Blitz provides the managed database through DATABASE_URL in:
    // mysql://username:password@host:port/database
    private static final String DATABASE_URL = System.getenv("DATABASE_URL");

    // DB_URL / DB_USER / DB_PASSWORD remain supported for local development.
    private static final String LEGACY_URL = System.getenv("DB_URL");
    private static final String LEGACY_USER = System.getenv("DB_USER");
    private static final String LEGACY_PASSWORD = System.getenv("DB_PASSWORD");

    private static Connection connection = null;

    private DBConnection() {
        // Private constructor
    }

    public static Connection getConnection() throws SQLException {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName(DRIVER);

                if (DATABASE_URL != null && !DATABASE_URL.trim().isEmpty()) {
                    connection = createConnectionFromDatabaseUrl(DATABASE_URL);
                } else {
                    String url = LEGACY_URL != null && !LEGACY_URL.trim().isEmpty()
                            ? LEGACY_URL
                            : "jdbc:mysql://localhost:3306/library_db";

                    String username = LEGACY_USER != null ? LEGACY_USER : "root";
                    String password = LEGACY_PASSWORD != null ? LEGACY_PASSWORD : "";

                    connection = DriverManager.getConnection(url, username, password);
                }

                System.out.println("Database connected successfully!");
            }
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found!");
            throw new SQLException("Driver not found: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Database connection failed!");
            throw new SQLException("Connection error: " + e.getMessage());
        }

        return connection;
    }

    private static Connection createConnectionFromDatabaseUrl(String databaseUrl) throws SQLException {
        try {
            URI uri = new URI(databaseUrl);

            String host = uri.getHost();
            int port = uri.getPort() > 0 ? uri.getPort() : 3306;
            String database = uri.getPath();

            if (host == null || database == null || database.length() <= 1) {
                throw new SQLException("Invalid DATABASE_URL format.");
            }

            database = database.substring(1);

            String userInfo = uri.getUserInfo();
            if (userInfo == null || userInfo.trim().isEmpty()) {
                throw new SQLException("DATABASE_URL does not contain database credentials.");
            }

            int separator = userInfo.indexOf(':');
            if (separator < 0) {
                throw new SQLException("DATABASE_URL does not contain a password.");
            }

            String username = URLDecoder.decode(
                    userInfo.substring(0, separator),
                    StandardCharsets.UTF_8
            );

            String password = URLDecoder.decode(
                    userInfo.substring(separator + 1),
                    StandardCharsets.UTF_8
            );

            String jdbcUrl = "jdbc:mysql://" + host + ":" + port + "/" + database;

            return DriverManager.getConnection(jdbcUrl, username, password);

        } catch (Exception e) {
            if (e instanceof SQLException) {
                throw (SQLException) e;
            }
            throw new SQLException("Invalid DATABASE_URL: " + e.getMessage(), e);
        }
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }

    public static boolean testConnection() {
        try {
            Connection conn = getConnection();
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("Connection test failed: " + e.getMessage());
            return false;
        }
    }
}
