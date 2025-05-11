package com.hospital.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class TestDatabaseUtil {
    public static void clearBillingTable() {
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM billing");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to clear billing table", e);
        }
    }
}