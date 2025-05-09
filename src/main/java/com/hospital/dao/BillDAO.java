package com.hospital.dao;

import com.hospital.entities.Bill;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BillDAO extends BaseDAO<Bill> implements IBillDAO {
    
    public BillDAO(Connection connection) {
        super(connection, "Billing", "bill_id");
    }

    @Override
    protected Bill extractEntity(ResultSet rs) throws SQLException {
        return new Bill(
            rs.getInt("bill_id"),
            rs.getInt("patient_id"),
            rs.getDouble("total_amount"),
            rs.getDate("billing_date"),
            rs.getString("payment_status"),
            rs.getString("payment_type")
        );
    }

    @Override
    protected String getInsertQuery() {
        return "INSERT INTO Billing (patient_id, total_amount, billing_date, payment_status, payment_type) " +
               "VALUES (?, ?, ?, ?, ?)";
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE Billing SET patient_id=?, total_amount=?, billing_date=?, " +
               "payment_status=?, payment_type=? WHERE bill_id=?";
    }

    @Override
    protected void setInsertParameters(PreparedStatement stmt, Bill bill) throws SQLException {
        stmt.setInt(1, bill.getPatientId());
        stmt.setDouble(2, bill.getTotal_amount());
        stmt.setDate(3, bill.getBilling_date());
        stmt.setString(4, bill.getPayment_status());
        stmt.setString(5, bill.getPayment_type());
    }

    @Override
    protected void setUpdateParameters(PreparedStatement stmt, Bill bill) throws SQLException {
        setInsertParameters(stmt, bill);
        stmt.setInt(6, bill.getBillId());
    }

    @Override
    public List<Bill> findByPatientId(int patientId) {
        String sql = "SELECT * FROM Billing WHERE patient_id = ?";
        List<Bill> bills = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, patientId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                bills.add(extractEntity(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bills;
    }

    @Override
    public List<Bill> findByStatus(String status) {
        String sql = "SELECT * FROM Billing WHERE payment_status = ?";
        List<Bill> bills = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, status);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                bills.add(extractEntity(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bills;
    }

    @Override
    public List<Bill> findByType(String type) {
        String sql = "SELECT * FROM Billing WHERE payment_type = ?";
        List<Bill> bills = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, type);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                bills.add(extractEntity(rs));
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
                bills.add(extractEntity(rs));
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
                bills.add(extractEntity(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bills;
    }

    @Override
    public boolean updateStatus(int billId, String status) {
        String sql = "UPDATE Billing SET payment_status = ? WHERE bill_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, billId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
