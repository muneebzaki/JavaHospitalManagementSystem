package com.hospital.controller.gui;

import com.hospital.controller.BillManager;
import com.hospital.entities.Bill;
import com.hospital.gui.MainFrame;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Date;
import java.util.List;

public class BillGuiController extends GuiController {
    private final BillManager billManager;
    private DefaultTableModel billTableModel;

    public BillGuiController(MainFrame mainFrame, BillManager billManager) {
        super(mainFrame);
        this.billManager = billManager;
        initializeTableModel();
    }

    private void initializeTableModel() {
        String[] columns = {"Bill ID", "Patient ID", "Amount", "Date", "Status", "Payment Type"};
        billTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    public void refreshBillTable() {
        billTableModel.setRowCount(0);
        List<Bill> bills = billManager.getAllBills();
        for (Bill bill : bills) {
            Object[] row = {
                bill.getBillId(),
                bill.getPatientId(),
                bill.getTotal_amount(),
                bill.getBilling_date(),
                bill.getPayment_status(),
                bill.getPayment_type()
            };
            billTableModel.addRow(row);
        }
    }

    public DefaultTableModel getTableModel() {
        return billTableModel;
    }

    public void handleGenerateBill(int patientId, String paymentType) {
        boolean success = billManager.generateBill(patientId, "Unpaid", paymentType);
        if (success) {
            JOptionPane.showMessageDialog(mainFrame, "Bill generated successfully!");
            refreshBillTable();
        } else {
            JOptionPane.showMessageDialog(mainFrame, "Failed to generate bill!", 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void handleMarkAsPaid(int billId) {
        boolean success = billManager.markBillAsPaid(billId);
        if (success) {
            refreshBillTable();
        }
    }

    public void handleApplyDiscount(int billId, double discountAmount) {
        billManager.applyDiscount(billId, discountAmount);
        refreshBillTable();
    }

    public void handleCancelBill(int billId) {
        boolean success = billManager.cancelBill(billId);
        if (success) {
            JOptionPane.showMessageDialog(mainFrame, "Bill cancelled successfully!");
            refreshBillTable();
        } else {
            JOptionPane.showMessageDialog(mainFrame, "Failed to cancel bill!",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void filterBills(String searchText, String status, Date startDate, Date endDate) {
        billTableModel.setRowCount(0);
        List<Bill> bills = billManager.getAllBills();

        for (Bill bill : bills) {
            // Skip if status filter is active and doesn't match
            if (!"All".equals(status) && !bill.getPayment_status().equals(status)) {
                continue;
            }

            // TODO: make this search on patient names
            // Skip if search text doesn't match bill ID or patient ID
            if (!searchText.isEmpty() &&
                    !String.valueOf(bill.getBillId()).contains(searchText) &&
                    !String.valueOf(bill.getPatientId()).contains(searchText)) {
                continue;
            }

            // Skip if start date filter is active and doesnt match
            if (startDate != null && bill.getBilling_date().before(startDate)) {
                continue;
            }

            // Skip if end date filter is active and doesnt match
            if (endDate != null && bill.getBilling_date().after(endDate)) {
                continue;
            }

            Object[] row = {
                    bill.getBillId(),
                    bill.getPatientId(),
                    bill.getTotal_amount(),
                    bill.getBilling_date(),
                    bill.getPayment_status(),
                    bill.getPayment_type()
            };
            billTableModel.addRow(row);
        }
    }

    public void generateReport() {
        List<Bill> bills = billManager.getAllBills();
        StringBuilder report = new StringBuilder();
        report.append("Bill Report\n\n");
        report.append(String.format("%-10s %-10s %-10s %-12s %-10s %-15s%n",
                "Bill ID", "Patient", "Amount", "Date", "Status", "Payment Type"));
        report.append("=".repeat(70)).append("\n");

        double totalAmount = 0;
        int totalBills = 0;

        for (Bill bill : bills) {
            report.append(String.format("%-10d %-10d $%-9.2f %-12s %-10s %-15s%n",
                    bill.getBillId(),
                    bill.getPatientId(),
                    bill.getTotal_amount(),
                    bill.getBilling_date(),
                    bill.getPayment_status(),
                    bill.getPayment_type()));
            totalAmount += bill.getTotal_amount();
            totalBills++;
        }

        report.append("\nSummary:\n");
        report.append("Total Bills: ").append(totalBills).append("\n");
        report.append("Total Amount: $").append(String.format("%.2f", totalAmount));

        JTextArea textArea = new JTextArea(report.toString());
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(600, 400));

        JDialog reportDialog = new JDialog(mainFrame, "Bill Report", true);
        reportDialog.add(scrollPane);
        reportDialog.pack();
        reportDialog.setLocationRelativeTo(mainFrame);
        reportDialog.setVisible(true);
    }

}