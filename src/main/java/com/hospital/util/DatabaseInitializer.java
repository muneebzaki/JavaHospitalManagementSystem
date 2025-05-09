package com.hospital.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Statement;

public class DatabaseInitializer {
    private static final DatabaseInitializer instance = new DatabaseInitializer();
    
    private DatabaseInitializer() {}
    
    public static DatabaseInitializer getInstance() {
        return instance;
    }
    
    public void initializeDatabase() {
        try {
            // Read schema.sql
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(getClass().getResourceAsStream("/sql/schema.sql"))
            );
            
            StringBuilder sql = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sql.append(line).append("\n");
            }
            
            // Execute schema
            Connection conn = DatabaseConnection.getInstance().getConnection();
            Statement stmt = conn.createStatement();
            
            // Split and execute each statement
            String[] statements = sql.toString().split(";");
            for (String statement : statements) {
                if (!statement.trim().isEmpty()) {
                    stmt.execute(statement);
                }
            }
            
            // Create default admin user if not exists
            createDefaultAdmin();
            
            stmt.close();
            reader.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void createDefaultAdmin() {
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            Statement stmt = conn.createStatement();
            
            // Check if admin exists
            boolean adminExists = stmt.executeQuery(
                "SELECT COUNT(*) FROM users WHERE role = 'ADMIN'"
            ).getInt(1) > 0;
            
            if (!adminExists) {
                // Create admin user
                stmt.execute(
                    "INSERT INTO users (username, password, role, reference_id, is_active) " +
                    "VALUES ('admin', 'admin123', 'ADMIN', 0, true)"
                );
            }
            
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
} 