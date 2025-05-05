package controller;

import entities.Bill;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public interface IBillManager {

    // Core Billing Operations
    boolean generateBill(int patientId, String paymentStatus, String paymentType);
    boolean updateBill(int billId, Bill newBill);
    boolean cancelBill(int billId);
    double calculateBillTotal(int patientId);

    // Bill Retrieval Operations & Queries
    Bill getBillById(int billId);
    List<Bill> getBillsByPatientId(int patientId);
    List<Bill> getAllBills();
    List<Bill> getBillsByPaymentStatus(String status);
    List<Bill> getBillsByPaymentType(String type);
    List<Bill> getBillsBetweenDates(LocalDate startDate, LocalDate endDate);

    // Payment Management Operations
    boolean markBillAsPaid(int billId);
    boolean markBillAsUnpaid(int billId);
    boolean isBillPaid(int billId);
    void applyDiscount(int billId, double discountAmount);

    // Audit & Reporting Operations
    List<Bill> generateDailyBillingReport(LocalDate date);
    List<Bill> generateMonthlyBillingReport(int month, int year);
    double getTotalRevenueBetween(LocalDate startDate, LocalDate endDate);

    // Utility / Validation Operations
    boolean billExists(int billId);
    boolean validateBill(Bill bill);
}
