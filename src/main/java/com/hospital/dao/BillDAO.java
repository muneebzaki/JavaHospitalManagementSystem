package com.hospital.dao;

import com.hospital.entities.Bill;
import com.hospital.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BillDAO {

    public boolean insertBill(Bill bill) {
        String sql = "INSERT INTO billing (patient_id, amount, billing_date, status) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, bill.getPatientId());
            stmt.setDouble(2, bill.getAmount());
            stmt.setDate(3, bill.getBillingDate());
            stmt.setString(4, bill.getStatus());

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        bill.setId(rs.getInt(1));
                    }
                }
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Error inserting bill:");
            e.printStackTrace();
        }

        return false;
    }

    public List<Bill> findAll() {
        List<Bill> bills = new ArrayList<>();
        String sql = "SELECT * FROM billing";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                bills.add(extractBill(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving all bills:");
            e.printStackTrace();
        }

        return bills;
    }

    public List<Bill> findByPatientId(int patientId) {
        List<Bill> bills = new ArrayList<>();
        String sql = "SELECT * FROM billing WHERE patient_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, patientId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                bills.add(extractBill(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving bills by patient:");
            e.printStackTrace();
        }

        return bills;
    }

    public boolean updateStatus(int billId, String newStatus) {
        String sql = "UPDATE billing SET status = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newStatus);
            stmt.setInt(2, billId);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error updating bill status:");
            e.printStackTrace();
        }

        return false;
    }

    private Bill extractBill(ResultSet rs) throws SQLException {
        return new Bill(
                rs.getInt("id"),
                rs.getInt("patient_id"),
                rs.getDouble("amount"),
                rs.getDate("billing_date"),
                rs.getString("status")
        );
    }
}
