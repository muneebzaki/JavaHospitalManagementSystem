package com.hospital.controller;

import com.hospital.dao.BillDAO;
import com.hospital.dao.IBillDAO;
import com.hospital.entities.Bill;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.sql.Date;
import java.util.List;

public class BillManager implements IBillManager {
    // TODO: implement a new connection manager class to handle db connection
    Connection conn;
    private final IBillDAO billDAO; // DAO handles database queries

    public BillManager() throws SQLException {
        conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/yourDatabaseName",
                "yourUsername",
                "yourPassword"
        );
        billDAO = new BillDAO(conn);
    }

    // Added new constructor for testing
    public BillManager(IBillDAO billDAO) {
        this.billDAO = billDAO;
        // Note: conn is not needed when DAO is injected
    }

    // Core Billing Operations
    @Override
    public boolean generateBill(int patientId, String paymentStatus, String paymentType) {
        // Example: sum appointment costs + treatments + prescriptions
        double total = calculateBillTotal(patientId);
        Bill newBill = new Bill(patientId, total, Date.valueOf(LocalDate.now()), paymentStatus, paymentType);
        return billDAO.insertBill(newBill);
    }
    @Override
    public boolean updateBill(int billId, Bill newBill) {
        if (billExists(billId)) {
            return billDAO.updateBill(billId, newBill);
        }
        return false;
    }
    @Override
    public boolean cancelBill(int billId) {
       return billDAO.updateStatus(billId, "Cancelled");
    }
    @Override
    public double calculateBillTotal(int patientId) {
        // Simplified; would involve joins or service-layer calculation
        List<Bill> bills = getBillsByPatientId(patientId);
        double total = 0;
        for (Bill bill : bills) {
            total += bill.getTotal_amount();
        }
        return total;
    }

    // Bill Retrieval Operations & Queries
    @Override
    public Bill getBillById(int billId) {
        return billDAO.findById(billId);
    }
    @Override
    public List<Bill> getBillsByPatientId(int patientId) {
        return billDAO.findByPatientId(patientId);
    }
    @Override
    public List<Bill> getAllBills() {
        return billDAO.findAll();
    }
    @Override
    public List<Bill> getBillsByPaymentStatus(String status) {
        return billDAO.findByStatus(status);
    }
    @Override
    public List<Bill> getBillsByPaymentType(String type) {
        return billDAO.findByType(type);
    }
    @Override
    public List<Bill> getBillsBetweenDates(LocalDate startDate, LocalDate endDate) {
        return billDAO.findBetweenDates(startDate, endDate);
    }

    // Payment Management Operations
    @Override
    public boolean markBillAsPaid(int billId) {
        return billDAO.updateStatus(billId, "Paid");
    }
    @Override
    public void applyDiscount(int billId, double discountAmount) {
        Bill bill = billDAO.findById(billId);
        double newTotal = bill.getTotal_amount() - discountAmount;
        bill.setTotal_amount(newTotal);
        updateBill(billId, bill);
    }
    @Override
    public boolean isBillPaid(int billId) {
        Bill bill = billDAO.findById(billId);
        return "Paid".equalsIgnoreCase(bill.getPayment_status());
    }
    @Override
    public boolean markBillAsUnpaid(int billId) {
        return billDAO.updateStatus(billId, "Unpaid");
    }

    // Audit & Reporting Operations
    @Override
    public List<Bill> generateDailyBillingReport(LocalDate date) {
        return billDAO.findByDate(Date.valueOf(date));
    }
    @Override
    public List<Bill> generateMonthlyBillingReport(int month, int year) {
        return billDAO.findByMonthAndYear(month, year);
    }
    @Override
    public double getTotalRevenueBetween(LocalDate start, LocalDate end) {
        List<Bill> bills = getBillsBetweenDates(start, end);
        double total = 0;
        for (Bill bill : bills) {
            if (bill.getPayment_status().equalsIgnoreCase("Paid")) {
                total += bill.getTotal_amount();
            }
        }
        return total;
    }

    // Utility / Validation Operations
    @Override
    public boolean validateBill(Bill bill) {
        return bill.getTotal_amount() > 0 && bill.getPatientId() > 0;
    }
    @Override
    public boolean billExists(int billId) {
        return billDAO.findById(billId) != null;
    }
}

