package com.hospital.gui.panels;

import com.hospital.controller.MedicalRecordManager;
import com.hospital.entities.MedicalRecord;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MedicalRecordManagementPanel extends BasePanel {
    private final MedicalRecordManager recordManager;
    private final JTable recordTable;
    private final DefaultTableModel tableModel;
    private final JTextField patientIdField;
    private final JTextField doctorIdField;
    private final JTextArea diagnosisArea;
    private final JTextArea treatmentArea;
    private final JTextArea prescriptionArea;
    private final JTextArea notesArea;
    private final JComboBox<String> statusComboBox;
    private final DateTimeFormatter dateFormatter;

    public MedicalRecordManagementPanel() {
        recordManager = new MedicalRecordManager();
        dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        // Initialize components
        String[] columns = {"Record ID", "Patient ID", "Doctor ID", "Date", "Diagnosis", "Treatment", "Prescription", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        recordTable = new JTable(tableModel);
        recordTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        patientIdField = new JTextField(10);
        doctorIdField = new JTextField(10);
        diagnosisArea = new JTextArea(3, 20);
        treatmentArea = new JTextArea(3, 20);
        prescriptionArea = new JTextArea(3, 20);
        notesArea = new JTextArea(3, 20);
        statusComboBox = new JComboBox<>(new String[]{"ACTIVE", "ARCHIVED"});

        // Make text areas scrollable
        diagnosisArea.setLineWrap(true);
        treatmentArea.setLineWrap(true);
        prescriptionArea.setLineWrap(true);
        notesArea.setLineWrap(true);

        initializeComponents();
        setupLayout();
        loadRecords();
    }

    private void initializeComponents() {
        // Add selection listener to table
        recordTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = recordTable.getSelectedRow();
                if (selectedRow >= 0) {
                    int recordId = (int) tableModel.getValueAt(selectedRow, 0);
                    MedicalRecord record = recordManager.getRecordById(recordId);
                    if (record != null) {
                        populateFields(record);
                    }
                }
            }
        });

        // Add button listeners
        JButton addButton = new JButton("Add Record");
        addButton.addActionListener(e -> addRecord());

        JButton updateButton = new JButton("Update Record");
        updateButton.addActionListener(e -> updateRecord());

        JButton deleteButton = new JButton("Delete Record");
        deleteButton.addActionListener(e -> deleteRecord());

        JButton clearButton = new JButton("Clear Fields");
        clearButton.addActionListener(e -> clearFields());

        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> loadRecords());

        // Add buttons to panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(refreshButton);

        // Add button panel to main panel
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        // Create input panel
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Add input fields
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(new JLabel("Patient ID:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(patientIdField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(new JLabel("Doctor ID:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(doctorIdField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        inputPanel.add(new JLabel("Diagnosis:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(new JScrollPane(diagnosisArea), gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        inputPanel.add(new JLabel("Treatment:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(new JScrollPane(treatmentArea), gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        inputPanel.add(new JLabel("Prescription:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(new JScrollPane(prescriptionArea), gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        inputPanel.add(new JLabel("Notes:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(new JScrollPane(notesArea), gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        inputPanel.add(new JLabel("Status:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(statusComboBox, gbc);

        // Add table
        JScrollPane tableScrollPane = new JScrollPane(recordTable);
        tableScrollPane.setPreferredSize(new Dimension(800, 300));

        // Add components to main panel
        add(inputPanel, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);
    }

    private void loadRecords() {
        tableModel.setRowCount(0);
        List<MedicalRecord> records = recordManager.getAllRecords();
        for (MedicalRecord record : records) {
            Object[] row = {
                record.getRecordId(),
                record.getPatientId(),
                record.getDoctorId(),
                record.getRecordDate().format(dateFormatter),
                record.getDiagnosis(),
                record.getTreatment(),
                record.getPrescription(),
                record.getStatus()
            };
            tableModel.addRow(row);
        }
    }

    private void addRecord() {
        try {
            int patientId = Integer.parseInt(patientIdField.getText().trim());
            int doctorId = Integer.parseInt(doctorIdField.getText().trim());
            String diagnosis = diagnosisArea.getText().trim();
            String treatment = treatmentArea.getText().trim();
            String prescription = prescriptionArea.getText().trim();
            String notes = notesArea.getText().trim();
            String status = (String) statusComboBox.getSelectedItem();

            if (diagnosis.isEmpty() || treatment.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Diagnosis and Treatment are required fields.");
                return;
            }

            MedicalRecord record = new MedicalRecord(patientId, doctorId, diagnosis, treatment, prescription, notes);
            record.setStatus(status);

            if (recordManager.addRecord(record)) {
                JOptionPane.showMessageDialog(this, "Record added successfully.");
                clearFields();
                loadRecords();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add record.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid IDs.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateRecord() {
        int selectedRow = recordTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a record to update.");
            return;
        }

        try {
            int recordId = (int) tableModel.getValueAt(selectedRow, 0);
            int patientId = Integer.parseInt(patientIdField.getText().trim());
            int doctorId = Integer.parseInt(doctorIdField.getText().trim());
            String diagnosis = diagnosisArea.getText().trim();
            String treatment = treatmentArea.getText().trim();
            String prescription = prescriptionArea.getText().trim();
            String notes = notesArea.getText().trim();
            String status = (String) statusComboBox.getSelectedItem();

            if (diagnosis.isEmpty() || treatment.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Diagnosis and Treatment are required fields.");
                return;
            }

            MedicalRecord record = new MedicalRecord(recordId, patientId, doctorId, LocalDateTime.now(),
                    diagnosis, treatment, prescription, notes, status);

            if (recordManager.updateRecord(record)) {
                JOptionPane.showMessageDialog(this, "Record updated successfully.");
                loadRecords();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update record.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid IDs.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteRecord() {
        int selectedRow = recordTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a record to delete.");
            return;
        }

        int recordId = (int) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this record?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            if (recordManager.deleteRecord(recordId)) {
                JOptionPane.showMessageDialog(this, "Record deleted successfully.");
                clearFields();
                loadRecords();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete record.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearFields() {
        patientIdField.setText("");
        doctorIdField.setText("");
        diagnosisArea.setText("");
        treatmentArea.setText("");
        prescriptionArea.setText("");
        notesArea.setText("");
        statusComboBox.setSelectedItem("ACTIVE");
        recordTable.clearSelection();
    }

    private void populateFields(MedicalRecord record) {
        patientIdField.setText(String.valueOf(record.getPatientId()));
        doctorIdField.setText(String.valueOf(record.getDoctorId()));
        diagnosisArea.setText(record.getDiagnosis());
        treatmentArea.setText(record.getTreatment());
        prescriptionArea.setText(record.getPrescription());
        notesArea.setText(record.getNotes());
        statusComboBox.setSelectedItem(record.getStatus());
    }
} 