package com.hospital.controller;

import com.hospital.dao.BillDAO;
import com.hospital.entities.Bill;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class BillManager {
    private final BillDAO billDAO;

    public BillManager() {
        this.billDAO = new BillDAO();
    }
    public BillManager(BillDAO billDAO) {
        this.billDAO = billDAO;
    }

    public boolean generateBill(int patientId, double amount, String status) {
        Bill bill = new Bill(0, patientId, amount, Date.valueOf(LocalDate.now()), status);
        return billDAO.insertBill(bill);
    }

    public List<Bill> getAllBills() {
        return billDAO.findAll();
    }

    public List<Bill> getBillsByPatientId(int patientId) {
        return billDAO.findByPatientId(patientId);
    }

    public boolean markBillAsPaid(int billId) {
        return billDAO.updateStatus(billId, "PAID");
    }

    public boolean cancelBill(int billId) {
        return billDAO.updateStatus(billId, "CANCELLED");
    }

    protected BillDAO getBillDAO() {
        return billDAO;
    }
}
