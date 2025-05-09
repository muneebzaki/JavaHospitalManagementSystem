package com.hospital.controller;

import com.hospital.dao.BillDAO;
import com.hospital.dao.IBillDAO;
import com.hospital.entities.Bill;
import com.hospital.exception.HospitalException;
import com.hospital.util.DatabaseConnection;
import com.hospital.util.LoggingUtils;
import com.hospital.util.ValidationUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.sql.Date;
import java.util.List;

public class BillManager implements IBillManager {
    private final IBillDAO billDAO;

    public BillManager() throws SQLException {
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            billDAO = new BillDAO(conn);
            LoggingUtils.info("BillManager initialized successfully");
        } catch (SQLException e) {
            LoggingUtils.error("Failed to initialize BillManager", e);
            throw new HospitalException(HospitalException.ErrorCode.SYSTEM_ERROR, 
                "Failed to initialize BillManager", e);
        }
    }

    // Added new constructor for testing
    public BillManager(IBillDAO billDAO) {
        if (billDAO == null) {
            throw new HospitalException(HospitalException.ErrorCode.SYSTEM_ERROR, 
                "BillDAO cannot be null");
        }
        this.billDAO = billDAO;
        LoggingUtils.info("BillManager initialized with custom DAO");
    }

    // Core Billing Operations
    @Override
    public boolean generateBill(int patientId, String paymentStatus, String paymentType) {
        try {
            if (!ValidationUtils.isValidId(patientId)) {
                throw new HospitalException(HospitalException.ErrorCode.VALIDATION_ERROR, 
                    "Invalid patient ID");
            }
            if (paymentStatus == null || paymentStatus.isEmpty()) {
                throw new HospitalException(HospitalException.ErrorCode.VALIDATION_ERROR, 
                    "Payment status cannot be empty");
            }
            if (paymentType == null || paymentType.isEmpty()) {
                throw new HospitalException(HospitalException.ErrorCode.VALIDATION_ERROR, 
                    "Payment type cannot be empty");
            }

            double total = calculateBillTotal(patientId);
            Bill newBill = new Bill(patientId, total, Date.valueOf(LocalDate.now()), 
                paymentStatus, paymentType);
            
            if (!ValidationUtils.isValidBill(newBill)) {
                throw new HospitalException(HospitalException.ErrorCode.VALIDATION_ERROR, 
                    "Invalid bill data");
            }

            boolean result = billDAO.insert(newBill);
            LoggingUtils.info("Generated new bill for patient " + patientId + 
                " with total " + total);
            return result;
        } catch (Exception e) {
            LoggingUtils.error("Failed to generate bill", e);
            throw new HospitalException(HospitalException.ErrorCode.SYSTEM_ERROR, 
                "Failed to generate bill", e);
        }
    }
    @Override
    public boolean updateBill(int billId, Bill newBill) {
        try {
            if (!ValidationUtils.isValidId(billId)) {
                throw new HospitalException(HospitalException.ErrorCode.VALIDATION_ERROR, 
                    "Invalid bill ID");
            }
            if (!ValidationUtils.isValidBill(newBill)) {
                throw new HospitalException(HospitalException.ErrorCode.VALIDATION_ERROR, 
                    "Invalid bill data");
            }
            if (!billExists(billId)) {
                throw new HospitalException(HospitalException.ErrorCode.NOT_FOUND, 
                    "Bill not found with ID: " + billId);
            }

            boolean result = billDAO.update(newBill);
            LoggingUtils.info("Updated bill " + billId);
            return result;
        } catch (Exception e) {
            LoggingUtils.error("Failed to update bill", e);
            throw new HospitalException(HospitalException.ErrorCode.SYSTEM_ERROR, 
                "Failed to update bill", e);
        }
    }
    @Override
    public boolean cancelBill(int billId) {
        try {
            if (!ValidationUtils.isValidId(billId)) {
                throw new HospitalException(HospitalException.ErrorCode.VALIDATION_ERROR, 
                    "Invalid bill ID");
            }
            if (!billExists(billId)) {
                throw new HospitalException(HospitalException.ErrorCode.NOT_FOUND, 
                    "Bill not found with ID: " + billId);
            }

            boolean result = billDAO.updateStatus(billId, "Cancelled");
            LoggingUtils.info("Cancelled bill " + billId);
            return result;
        } catch (Exception e) {
            LoggingUtils.error("Failed to cancel bill", e);
            throw new HospitalException(HospitalException.ErrorCode.SYSTEM_ERROR, 
                "Failed to cancel bill", e);
        }
    }
    @Override
    public double calculateBillTotal(int patientId) {
        try {
            if (!ValidationUtils.isValidId(patientId)) {
                throw new HospitalException(HospitalException.ErrorCode.VALIDATION_ERROR, 
                    "Invalid patient ID");
            }

            List<Bill> bills = getBillsByPatientId(patientId);
            double total = bills.stream()
                .mapToDouble(Bill::getTotal_amount)
                .sum();
            
            LoggingUtils.debug("Calculated total bill amount for patient " + patientId + 
                ": " + total);
            return total;
        } catch (Exception e) {
            LoggingUtils.error("Failed to calculate bill total", e);
            throw new HospitalException(HospitalException.ErrorCode.SYSTEM_ERROR, 
                "Failed to calculate bill total", e);
        }
    }

    // Bill Retrieval Operations & Queries
    @Override
    public Bill getBillById(int billId) {
        try {
            if (!ValidationUtils.isValidId(billId)) {
                throw new HospitalException(HospitalException.ErrorCode.VALIDATION_ERROR, 
                    "Invalid bill ID");
            }

            Bill bill = billDAO.findById(billId);
            if (bill == null) {
                LoggingUtils.warning("Bill not found with ID: " + billId);
            }
            return bill;
        } catch (Exception e) {
            LoggingUtils.error("Failed to get bill by ID", e);
            throw new HospitalException(HospitalException.ErrorCode.SYSTEM_ERROR, 
                "Failed to get bill by ID", e);
        }
    }
    @Override
    public List<Bill> getBillsByPatientId(int patientId) {
        try {
            if (!ValidationUtils.isValidId(patientId)) {
                throw new HospitalException(HospitalException.ErrorCode.VALIDATION_ERROR, 
                    "Invalid patient ID");
            }

            List<Bill> bills = billDAO.findByPatientId(patientId);
            LoggingUtils.debug("Found " + bills.size() + " bills for patient " + patientId);
            return bills;
        } catch (Exception e) {
            LoggingUtils.error("Failed to get bills by patient ID", e);
            throw new HospitalException(HospitalException.ErrorCode.SYSTEM_ERROR, 
                "Failed to get bills by patient ID", e);
        }
    }
    @Override
    public List<Bill> getAllBills() {
        try {
            List<Bill> bills = billDAO.findAll();
            LoggingUtils.debug("Retrieved all bills: " + bills.size() + " records");
            return bills;
        } catch (Exception e) {
            LoggingUtils.error("Failed to get all bills", e);
            throw new HospitalException(HospitalException.ErrorCode.SYSTEM_ERROR, 
                "Failed to get all bills", e);
        }
    }
    @Override
    public List<Bill> getBillsByPaymentStatus(String status) {
        try {
            if (status == null || status.isEmpty()) {
                throw new HospitalException(HospitalException.ErrorCode.VALIDATION_ERROR, 
                    "Payment status cannot be empty");
            }

            List<Bill> bills = billDAO.findByStatus(status);
            LoggingUtils.debug("Found " + bills.size() + " bills with status: " + status);
            return bills;
        } catch (Exception e) {
            LoggingUtils.error("Failed to get bills by payment status", e);
            throw new HospitalException(HospitalException.ErrorCode.SYSTEM_ERROR, 
                "Failed to get bills by payment status", e);
        }
    }
    @Override
    public List<Bill> getBillsByPaymentType(String type) {
        try {
            if (type == null || type.isEmpty()) {
                throw new HospitalException(HospitalException.ErrorCode.VALIDATION_ERROR, 
                    "Payment type cannot be empty");
            }

            List<Bill> bills = billDAO.findByType(type);
            LoggingUtils.debug("Found " + bills.size() + " bills with type: " + type);
            return bills;
        } catch (Exception e) {
            LoggingUtils.error("Failed to get bills by payment type", e);
            throw new HospitalException(HospitalException.ErrorCode.SYSTEM_ERROR, 
                "Failed to get bills by payment type", e);
        }
    }
    @Override
    public List<Bill> getBillsBetweenDates(LocalDate startDate, LocalDate endDate) {
        try {
            if (startDate == null || endDate == null) {
                throw new HospitalException(HospitalException.ErrorCode.VALIDATION_ERROR, 
                    "Start date and end date cannot be null");
            }
            if (startDate.isAfter(endDate)) {
                throw new HospitalException(HospitalException.ErrorCode.VALIDATION_ERROR, 
                    "Start date cannot be after end date");
            }

            List<Bill> bills = billDAO.findBetweenDates(startDate, endDate);
            LoggingUtils.debug("Found " + bills.size() + " bills between " + 
                startDate + " and " + endDate);
            return bills;
        } catch (Exception e) {
            LoggingUtils.error("Failed to get bills between dates", e);
            throw new HospitalException(HospitalException.ErrorCode.SYSTEM_ERROR, 
                "Failed to get bills between dates", e);
        }
    }

    // Payment Management Operations
    @Override
    public boolean markBillAsPaid(int billId) {
        try {
            if (!ValidationUtils.isValidId(billId)) {
                throw new HospitalException(HospitalException.ErrorCode.VALIDATION_ERROR, 
                    "Invalid bill ID");
            }
            if (!billExists(billId)) {
                throw new HospitalException(HospitalException.ErrorCode.NOT_FOUND, 
                    "Bill not found with ID: " + billId);
            }

            boolean result = billDAO.updateStatus(billId, "Paid");
            LoggingUtils.info("Marked bill " + billId + " as paid");
            return result;
        } catch (Exception e) {
            LoggingUtils.error("Failed to mark bill as paid", e);
            throw new HospitalException(HospitalException.ErrorCode.SYSTEM_ERROR, 
                "Failed to mark bill as paid", e);
        }
    }
    @Override
    public void applyDiscount(int billId, double discountAmount) {
        try {
            if (!ValidationUtils.isValidId(billId)) {
                throw new HospitalException(HospitalException.ErrorCode.VALIDATION_ERROR, 
                    "Invalid bill ID");
            }
            if (discountAmount <= 0) {
                throw new HospitalException(HospitalException.ErrorCode.VALIDATION_ERROR, 
                    "Discount amount must be positive");
            }
            if (!billExists(billId)) {
                throw new HospitalException(HospitalException.ErrorCode.NOT_FOUND, 
                    "Bill not found with ID: " + billId);
            }

            Bill bill = billDAO.findById(billId);
            double newTotal = bill.getTotal_amount() - discountAmount;
            if (newTotal < 0) {
                throw new HospitalException(HospitalException.ErrorCode.VALIDATION_ERROR, 
                    "Discount amount cannot be greater than bill total");
            }

            bill.setTotal_amount(newTotal);
            updateBill(billId, bill);
            LoggingUtils.info("Applied discount of " + discountAmount + 
                " to bill " + billId);
        } catch (Exception e) {
            LoggingUtils.error("Failed to apply discount", e);
            throw new HospitalException(HospitalException.ErrorCode.SYSTEM_ERROR, 
                "Failed to apply discount", e);
        }
    }
    @Override
    public boolean isBillPaid(int billId) {
        try {
            if (!ValidationUtils.isValidId(billId)) {
                throw new HospitalException(HospitalException.ErrorCode.VALIDATION_ERROR, 
                    "Invalid bill ID");
            }
            if (!billExists(billId)) {
                throw new HospitalException(HospitalException.ErrorCode.NOT_FOUND, 
                    "Bill not found with ID: " + billId);
            }

            Bill bill = billDAO.findById(billId);
            boolean isPaid = "Paid".equalsIgnoreCase(bill.getPayment_status());
            LoggingUtils.debug("Checked payment status for bill " + billId + ": " + isPaid);
            return isPaid;
        } catch (Exception e) {
            LoggingUtils.error("Failed to check bill payment status", e);
            throw new HospitalException(HospitalException.ErrorCode.SYSTEM_ERROR, 
                "Failed to check bill payment status", e);
        }
    }
    @Override
    public boolean markBillAsUnpaid(int billId) {
        try {
            if (!ValidationUtils.isValidId(billId)) {
                throw new HospitalException(HospitalException.ErrorCode.VALIDATION_ERROR, 
                    "Invalid bill ID");
            }
            if (!billExists(billId)) {
                throw new HospitalException(HospitalException.ErrorCode.NOT_FOUND, 
                    "Bill not found with ID: " + billId);
            }

            boolean result = billDAO.updateStatus(billId, "Unpaid");
            LoggingUtils.info("Marked bill " + billId + " as unpaid");
            return result;
        } catch (Exception e) {
            LoggingUtils.error("Failed to mark bill as unpaid", e);
            throw new HospitalException(HospitalException.ErrorCode.SYSTEM_ERROR, 
                "Failed to mark bill as unpaid", e);
        }
    }

    // Audit & Reporting Operations
    @Override
    public List<Bill> generateDailyBillingReport(LocalDate date) {
        try {
            if (date == null) {
                throw new HospitalException(HospitalException.ErrorCode.VALIDATION_ERROR, 
                    "Date cannot be null");
            }

            List<Bill> bills = billDAO.findByDate(Date.valueOf(date));
            LoggingUtils.info("Generated daily billing report for " + date + 
                ": " + bills.size() + " bills");
            return bills;
        } catch (Exception e) {
            LoggingUtils.error("Failed to generate daily billing report", e);
            throw new HospitalException(HospitalException.ErrorCode.SYSTEM_ERROR, 
                "Failed to generate daily billing report", e);
        }
    }
    @Override
    public List<Bill> generateMonthlyBillingReport(int month, int year) {
        try {
            if (month < 1 || month > 12) {
                throw new HospitalException(HospitalException.ErrorCode.VALIDATION_ERROR, 
                    "Invalid month: " + month);
            }
            if (year < 1900 || year > 2100) {
                throw new HospitalException(HospitalException.ErrorCode.VALIDATION_ERROR, 
                    "Invalid year: " + year);
            }

            List<Bill> bills = billDAO.findByMonthAndYear(month, year);
            LoggingUtils.info("Generated monthly billing report for " + month + "/" + year + 
                ": " + bills.size() + " bills");
            return bills;
        } catch (Exception e) {
            LoggingUtils.error("Failed to generate monthly billing report", e);
            throw new HospitalException(HospitalException.ErrorCode.SYSTEM_ERROR, 
                "Failed to generate monthly billing report", e);
        }
    }
    @Override
    public double getTotalRevenueBetween(LocalDate start, LocalDate end) {
        try {
            if (start == null || end == null) {
                throw new HospitalException(HospitalException.ErrorCode.VALIDATION_ERROR, 
                    "Start date and end date cannot be null");
            }
            if (start.isAfter(end)) {
                throw new HospitalException(HospitalException.ErrorCode.VALIDATION_ERROR, 
                    "Start date cannot be after end date");
            }

            List<Bill> bills = getBillsBetweenDates(start, end);
            double total = bills.stream()
                .filter(bill -> "Paid".equalsIgnoreCase(bill.getPayment_status()))
                .mapToDouble(Bill::getTotal_amount)
                .sum();

            LoggingUtils.info("Calculated total revenue between " + start + " and " + 
                end + ": " + total);
            return total;
        } catch (Exception e) {
            LoggingUtils.error("Failed to calculate total revenue", e);
            throw new HospitalException(HospitalException.ErrorCode.SYSTEM_ERROR, 
                "Failed to calculate total revenue", e);
        }
    }

    // Utility / Validation Operations
    @Override
    public boolean validateBill(Bill bill) {
        return ValidationUtils.isValidBill(bill);
    }
    @Override
    public boolean billExists(int billId) {
        try {
            if (!ValidationUtils.isValidId(billId)) {
                throw new HospitalException(HospitalException.ErrorCode.VALIDATION_ERROR, 
                    "Invalid bill ID");
            }

            boolean exists = billDAO.findById(billId) != null;
            LoggingUtils.debug("Checked bill existence for ID " + billId + ": " + exists);
            return exists;
        } catch (Exception e) {
            LoggingUtils.error("Failed to check bill existence", e);
            throw new HospitalException(HospitalException.ErrorCode.SYSTEM_ERROR, 
                "Failed to check bill existence", e);
        }
    }
}

