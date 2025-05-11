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

    public static void clearAppointmentsTable() {
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate("DELETE FROM appointments");
            stmt.executeUpdate("ALTER TABLE appointments AUTO_INCREMENT = 1");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void clearPatientsTable() {
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate("DELETE FROM patients");
            stmt.executeUpdate("ALTER TABLE patients AUTO_INCREMENT = 1");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void clearDoctorsTable() {
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate("DELETE FROM doctors");
            stmt.executeUpdate("ALTER TABLE doctors AUTO_INCREMENT = 1");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}