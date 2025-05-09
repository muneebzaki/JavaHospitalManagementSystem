package com.hospital.dao;

import com.hospital.entities.Bill;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BillDAO implements IBillDAO {
    private final Connection connection;

    public BillDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean insertBill(Bill bill) {
        String sql = "INSERT INTO Billing (patient_id, total_amount, billing_date, payment_status, payment_type) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, bill.getPatientId());
            stmt.setDouble(2, bill.getTotal_amount());
            stmt.setDate(3, bill.getBilling_date());
            stmt.setString(4, bill.getPayment_status());
            stmt.setString(5, bill.getPayment_type());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateBill(int billId, Bill newBill) {
        String sql = "UPDATE Billing SET patient_id=?, total_amount=?, billing_date=?, payment_status=?, payment_type=? WHERE bill_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, newBill.getPatientId());
            stmt.setDouble(2, newBill.getTotal_amount());
            stmt.setDate(3, newBill.getBilling_date());
            stmt.setString(4, newBill.getPayment_status());
            stmt.setString(5, newBill.getPayment_type());
            stmt.setInt(6, billId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateStatus(int billId, String status) {
        String sql = "UPDATE Billing SET payment_status=? WHERE bill_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, billId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Bill findById(int billId) {
        String sql = "SELECT * FROM Billing WHERE bill_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, billId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return extractBill(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Bill> findAll() {
        String sql = "SELECT * FROM Billing";
        List<Bill> bills = new ArrayList<>();
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                bills.add(extractBill(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bills;
    }

    @Override
    public List<Bill> findByPatientId(int patientId) {
        String sql = "SELECT * FROM Billing WHERE patient_id=?";
        List<Bill> bills = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, patientId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                bills.add(extractBill(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bills;
    }

    @Override
    public List<Bill> findByStatus(String status) {
        String sql = "SELECT * FROM Billing WHERE payment_status=?";
        List<Bill> bills = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, status);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                bills.add(extractBill(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bills;
    }

    @Override
    public List<Bill> findByType(String type) {
        String sql = "SELECT * FROM Billing WHERE payment_type=?";
        List<Bill> bills = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, type);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                bills.add(extractBill(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bills;
    }

    @Override
    public List<Bill> findByDate(Date date) {
        String sql = "SELECT * FROM Billing WHERE billing_date=?";
        List<Bill> bills = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDate(1, date);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                bills.add(extractBill(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bills;
    }

    @Override
    public List<Bill> findBetweenDates(LocalDate startDate, LocalDate endDate) {
        String sql = "SELECT * FROM Billing WHERE billing_date BETWEEN ? AND ?";
        List<Bill> bills = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(startDate));
            stmt.setDate(2, Date.valueOf(endDate));
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                bills.add(extractBill(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bills;
    }

    @Override
    public List<Bill> findByMonthAndYear(int month, int year) {
        String sql = "SELECT * FROM Billing WHERE MONTH(billing_date) = ? AND YEAR(billing_date) = ?";
        List<Bill> bills = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, month);
            stmt.setInt(2, year);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                bills.add(extractBill(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bills;
    }

    private Bill extractBill(ResultSet rs) throws SQLException {
        int billId = rs.getInt("bill_id");
        int patientId = rs.getInt("patient_id");
        double totalAmount = rs.getDouble("total_amount");
        Date billingDate = rs.getDate("billing_date");
        String paymentStatus = rs.getString("payment_status");
        String paymentType = rs.getString("payment_type");
        return new Bill(billId, patientId, totalAmount, billingDate, paymentStatus, paymentType);
    }
}
