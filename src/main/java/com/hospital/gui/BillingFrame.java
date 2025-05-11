package com.hospital.gui;

import com.hospital.controller.BillManager;
import com.hospital.entities.Bill;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class BillingFrame extends JFrame {
    private final BillManager billManager;
    private final JTable billTable;
    private final DefaultTableModel tableModel;

    public BillingFrame() {
        setTitle("Billing Management");
        setSize(800, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        billManager = new BillManager();

        tableModel = new DefaultTableModel(new String[]{"ID", "Patient ID", "Amount", "Date", "Status"}, 0);
        billTable = new JTable(tableModel);
        refreshTable();

        JButton addBtn = new JButton("Add Bill");
        JButton payBtn = new JButton("Mark as Paid");
        JButton cancelBtn = new JButton("Cancel Bill");

        addBtn.addActionListener(e -> addBillDialog());
        payBtn.addActionListener(e -> updateStatus("PAID"));
        cancelBtn.addActionListener(e -> updateStatus("CANCELLED"));

        JPanel btnPanel = new JPanel();
        btnPanel.add(addBtn);
        btnPanel.add(payBtn);
        btnPanel.add(cancelBtn);

        add(new JScrollPane(billTable), BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        List<Bill> bills = billManager.getAllBills();
        for (Bill b : bills) {
            tableModel.addRow(new Object[]{
                    b.getId(), b.getPatientId(), b.getAmount(), b.getBillingDate(), b.getStatus()
            });
        }
    }

    private void addBillDialog() {
        JTextField patientIdField = new JTextField();
        JTextField amountField = new JTextField();
        JComboBox<String> statusBox = new JComboBox<>(new String[]{"UNPAID", "PAID", "CANCELLED"});

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Patient ID:")); panel.add(patientIdField);
        panel.add(new JLabel("Amount:")); panel.add(amountField);
        panel.add(new JLabel("Status:")); panel.add(statusBox);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add Bill", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                int patientId = Integer.parseInt(patientIdField.getText());
                double amount = Double.parseDouble(amountField.getText());
                String status = (String) statusBox.getSelectedItem();

                boolean added = billManager.generateBill(patientId, amount, status);
                if (added) {
                    JOptionPane.showMessageDialog(this, "Bill added!");
                    refreshTable();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to add bill.");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Invalid input.");
            }
        }
    }

    private void updateStatus(String status) {
        int row = billTable.getSelectedRow();
        if (row >= 0) {
            int billId = (int) tableModel.getValueAt(row, 0);
            boolean updated = status.equals("PAID")
                    ? billManager.markBillAsPaid(billId)
                    : billManager.cancelBill(billId);
            if (updated) {
                JOptionPane.showMessageDialog(this, "Bill updated.");
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update bill.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a bill first.");
        }
    }
}
