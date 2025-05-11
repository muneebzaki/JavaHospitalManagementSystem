package com.hospital.gui;

import com.hospital.controller.MedicalRecordManager;
import com.hospital.entities.MedicalRecord;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MedicalRecordFrame extends JFrame {
    private final MedicalRecordManager recordManager;
    private final JTable recordTable;
    private final DefaultTableModel tableModel;

    public MedicalRecordFrame() {
        setTitle("Medical Records");
        setSize(800, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        recordManager = new MedicalRecordManager();

        tableModel = new DefaultTableModel(new String[]{"Record ID", "Patient ID", "Doctor ID", "Date", "Diagnosis", "Treatment"}, 0);
        recordTable = new JTable(tableModel);

        JButton loadBtn = new JButton("Load by Patient ID");
        JButton addBtn = new JButton("Add Record");

        loadBtn.addActionListener(e -> loadRecordsDialog());
        addBtn.addActionListener(e -> addRecordDialog());

        JPanel btnPanel = new JPanel();
        btnPanel.add(loadBtn);
        btnPanel.add(addBtn);

        add(new JScrollPane(recordTable), BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);
    }

    private void loadRecordsDialog() {
        String input = JOptionPane.showInputDialog(this, "Enter Patient ID:");
        try {
            int patientId = Integer.parseInt(input);
            List<MedicalRecord> records = recordManager.getRecordsByPatientId(patientId);
            tableModel.setRowCount(0);
            for (MedicalRecord r : records) {
                tableModel.addRow(new Object[]{
                        r.getRecordId(), r.getPatientId(), r.getDoctorId(),
                        r.getDate(), r.getDiagnosis(), r.getTreatment()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid input or error fetching records.");
        }
    }

    private void addRecordDialog() {
        JTextField patientField = new JTextField();
        JTextField doctorField = new JTextField();
        JTextField diagnosisField = new JTextField();
        JTextField treatmentField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Patient ID:")); panel.add(patientField);
        panel.add(new JLabel("Doctor ID:")); panel.add(doctorField);
        panel.add(new JLabel("Diagnosis:")); panel.add(diagnosisField);
        panel.add(new JLabel("Treatment:")); panel.add(treatmentField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add Medical Record", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                int patientId = Integer.parseInt(patientField.getText());
                int doctorId = Integer.parseInt(doctorField.getText());
                String diagnosis = diagnosisField.getText();
                String treatment = treatmentField.getText();

                MedicalRecord record = new MedicalRecord(patientId, doctorId, diagnosis, treatment);
                boolean success = recordManager.addRecord(record);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Record added.");
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to add record.");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Invalid input.");
            }
        }
    }
}
