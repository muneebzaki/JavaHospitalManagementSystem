package com.hospital.controller;

import com.hospital.dao.IBillDAO;
import com.hospital.entities.Bill;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MockBillManager extends BillManager {
    private List<Bill> mockBills;
    private int nextBillId = 1;

    public MockBillManager() throws SQLException {
        super((IBillDAO) null); // Pass null as we won't use the DAO
        this.mockBills = new ArrayList<>();
        // Add some sample data
        addSampleBills();
    }

    private void addSampleBills() {
        mockBills.add(new Bill(nextBillId++, 101, 500.0, Date.valueOf(LocalDate.now()), "Unpaid", "Cash"));
        mockBills.add(new Bill(nextBillId++, 102, 750.0, Date.valueOf(LocalDate.now()), "Paid", "Credit Card"));
        mockBills.add(new Bill(nextBillId++, 103, 1200.0, Date.valueOf(LocalDate.now()), "Unpaid", "Insurance"));
    }

    @Override
    public List<Bill> getAllBills() {
        return new ArrayList<>(mockBills);
    }

    @Override
    public boolean generateBill(int patientId, String paymentStatus, String paymentType) {
        Bill newBill = new Bill(nextBillId++, patientId, 1000.0, // Sample amount
                               Date.valueOf(LocalDate.now()),
                               paymentStatus, paymentType);
        return mockBills.add(newBill);
    }

    @Override
    public boolean markBillAsPaid(int billId) {
        for (Bill bill : mockBills) {
            if (bill.getBillId() == billId) {
                bill.setPayment_status("Paid");
                return true;
            }
        }
        return false;
    }

    @Override
    public void applyDiscount(int billId, double discountAmount) {
        for (Bill bill : mockBills) {
            if (bill.getBillId() == billId) {
                bill.setTotal_amount(bill.getTotal_amount() - discountAmount);
                break;
            }
        }
    }
}