package com.hospital.util;

import org.junit.jupiter.api.Test;
import java.sql.Connection;
import static org.junit.jupiter.api.Assertions.*;

public class DatabaseConnectionTest {

    @Test
    void testGetInstance() {
        DatabaseConnection instance1 = DatabaseConnection.getInstance();
        DatabaseConnection instance2 = DatabaseConnection.getInstance();
        
        // Test singleton pattern
        assertSame(instance1, instance2, "DatabaseConnection should be a singleton");
    }

    @Test
    void testGetConnection() {
        DatabaseConnection dbConnection = DatabaseConnection.getInstance();
        Connection connection = dbConnection.getConnection();
        
        assertNotNull(connection, "Connection should not be null");
        assertDoesNotThrow(() -> connection.isValid(5), "Connection should be valid");
    }

    @Test
    void testConnectionReuse() {
        DatabaseConnection dbConnection = DatabaseConnection.getInstance();
        Connection connection1 = dbConnection.getConnection();
        Connection connection2 = dbConnection.getConnection();
        
        assertSame(connection1, connection2, "Should reuse the same connection");
    }

    @Test
    void testCloseConnection() {
        DatabaseConnection dbConnection = DatabaseConnection.getInstance();
        Connection connection = dbConnection.getConnection();
        
        assertDoesNotThrow(() -> {
            dbConnection.closeConnection();
            assertTrue(connection.isClosed(), "Connection should be closed");
        });
    }
} 