package com.hospital.gui.panels;

import com.hospital.controller.gui.BillGuiController;
import com.hospital.controller.gui.GuiController;
import com.hospital.gui.DatePicker;
import com.hospital.gui.MainFrame;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.Date;

public class BillPanel extends BasePanel {
    // Table Components
    private JTable billTable;
    private JScrollPane tableScrollPane;
    
    // Action Buttons
    private JButton addBillBtn;
    private JButton markAsPaidBtn;
    private JButton applyDiscountBtn;
    private JButton cancelBillBtn;
    private JButton generateReportBtn;
    
    // Search/Filter Components
    private JTextField searchField;
    private JComboBox<String> statusFilter;
    private JButton startDateFilter;
    private JButton endDateFilter;
    private JButton resetFilter;
    private Date startDate;
    private Date endDate;
    private DatePicker datePicker;
    
    // Form Components
    private JTextField patientIdField;
    private JComboBox<String> paymentTypeCombo;
    
    private BillGuiController billController;

    public BillPanel(MainFrame mainFrame, GuiController controller) {
        super(mainFrame, controller);
        this.billController = (BillGuiController) controller;
        initialize();
    }

    @Override
    protected void initializeComponents() {
        // Initialize table
        billTable = new JTable(billController.getTableModel());
        tableScrollPane = new JScrollPane(billTable);
        
        // Initialize buttons
        addBillBtn = new JButton("New Bill");
        markAsPaidBtn = new JButton("Mark as Paid");
        applyDiscountBtn = new JButton("Apply Discount");
        cancelBillBtn = new JButton("Cancel Bill");
        generateReportBtn = new JButton("Generate Report");
        
        // Initialize filters
        searchField = new JTextField(20);
        statusFilter = new JComboBox<>(new String[]{"All", "Paid", "Unpaid", "Cancelled"});
        startDateFilter = new JButton("Start Date");
        endDateFilter = new JButton("End Date");
        resetFilter = new JButton("Reset Filters!");
        startDate = null;
        endDate = null;
        datePicker = new DatePicker(mainFrame);
        
        // Initialize form components
        patientIdField = new JTextField(10);
        paymentTypeCombo = new JComboBox<>(new String[]{"Cash", "Credit Card", "Insurance"});
        
        // Set table properties
        billTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        billTable.getTableHeader().setReorderingAllowed(false);
    }

    @Override
    protected void setupLayout() {
        setLayout(new BorderLayout(10, 10));
        
        // North Panel - Search and Filters
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.add(new JLabel("Search:"));
        filterPanel.add(searchField);
        filterPanel.add(new JLabel("Status:"));
        filterPanel.add(statusFilter);
        filterPanel.add(new JLabel("Date Range:"));
        filterPanel.add(startDateFilter);
        filterPanel.add(endDateFilter);
        filterPanel.add(resetFilter);
        
        // Center Panel - Table
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(tableScrollPane, BorderLayout.CENTER);
        
        // East Panel - Action Buttons
        JPanel buttonPanel = new JPanel(new GridLayout(5, 1, 5, 5));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        buttonPanel.add(addBillBtn);
        buttonPanel.add(markAsPaidBtn);
        buttonPanel.add(applyDiscountBtn);
        buttonPanel.add(cancelBillBtn);
        buttonPanel.add(generateReportBtn);
        
        // Add all panels
        add(filterPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.EAST);
        
        // Initial data load
        billController.refreshBillTable();
    }

    @Override
    protected void setupListeners() {
        // Add Bill Button
        addBillBtn.addActionListener(e -> showAddBillDialog());


        // Mark as Paid Button
        markAsPaidBtn.addActionListener(e -> {
            int selectedRow = billTable.getSelectedRow();
            if (selectedRow != -1) {
                int billId = (int) billTable.getValueAt(selectedRow, 0);
                billController.handleMarkAsPaid(billId);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a bill first!");
            }
        });
        
        // Apply Discount Button
        applyDiscountBtn.addActionListener(e -> {
            int selectedRow = billTable.getSelectedRow();
            if (selectedRow != -1) {
                showDiscountDialog(selectedRow);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a bill first!");
            }
        });

        // Cancel Bill Button
        cancelBillBtn.addActionListener(e -> {
            int selectedRow = billTable.getSelectedRow();
            if (selectedRow != -1) {
                int billId = (int) billTable.getValueAt(selectedRow, 0);
                int confirm = JOptionPane.showConfirmDialog(this,
                        "Are you sure you want to cancel this bill?",
                        "Confirm Cancellation",
                        JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    billController.handleCancelBill(billId);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a bill first!");
            }
        });

        // Generate Report Button
        generateReportBtn.addActionListener(e -> billController.generateReport());

        // Search field listener
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            private void search() {
                String searchText = searchField.getText();
                String status = (String) statusFilter.getSelectedItem();
                billController.filterBills(searchText, status, startDate, endDate);
            }

            @Override
            public void insertUpdate(DocumentEvent e) { search(); }
            @Override
            public void removeUpdate(DocumentEvent e) { search(); }
            @Override
            public void changedUpdate(DocumentEvent e) { search(); }
        });

        // Status filter listener
        statusFilter.addActionListener(e -> {
            String searchText = searchField.getText();
            String status = (String) statusFilter.getSelectedItem();
            billController.filterBills(searchText, status, startDate, endDate);
        });

        // Start Date filter
        startDateFilter.addActionListener(e -> {
           String searchText = searchField.getText();
           String status = (String) statusFilter.getSelectedItem();
           startDate = datePicker.getPickedDate();
           billController.filterBills(searchText, status, startDate, endDate);
        });

        // End Date filter
        endDateFilter.addActionListener(e -> {
            String searchText = searchField.getText();
            String status = (String) statusFilter.getSelectedItem();
            endDate = datePicker.getPickedDate();
            billController.filterBills(searchText, status, startDate, endDate);
        });

        // Reset Date Filters
        resetFilter.addActionListener(e -> {
            startDate = null;
            endDate = null;
            searchField.setText("");
            String searchText = searchField.getText();
            statusFilter.setSelectedIndex(0);
            String status = (String) statusFilter.getSelectedItem();
            billController.filterBills(searchText, status, startDate, endDate);
        });
    }

    private void showAddBillDialog() {
        JDialog dialog = new JDialog(mainFrame, "New Bill", true);
        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
        
        panel.add(new JLabel("Patient ID:"));
        panel.add(patientIdField);
        panel.add(new JLabel("Payment Type:"));
        panel.add(paymentTypeCombo);
        
        JButton submitBtn = new JButton("Generate Bill");
        submitBtn.addActionListener(e -> {
            try {
                int patientId = Integer.parseInt(patientIdField.getText());
                String paymentType = (String) paymentTypeCombo.getSelectedItem();
                billController.handleGenerateBill(patientId, paymentType);
                dialog.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid Patient ID!");
            }
        });
        
        panel.add(submitBtn);
        
        dialog.add(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void showDiscountDialog(int selectedRow) {
        String input = JOptionPane.showInputDialog(this, 
            "Enter discount amount:", "Apply Discount", 
            JOptionPane.PLAIN_MESSAGE);
        
        if (input != null && !input.isEmpty()) {
            System.out.println("Discount amount: " + input);

            try {
                double discount = Double.parseDouble(input);
                int billId = (int) billTable.getValueAt(selectedRow, 0);
                billController.handleApplyDiscount(billId, discount);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                    "Invalid discount amount!", "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}