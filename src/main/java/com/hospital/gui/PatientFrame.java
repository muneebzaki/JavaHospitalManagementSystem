package com.hospital.gui;

import com.hospital.controller.PatientManager;
import com.hospital.entities.Patient;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;

public class PatientFrame extends JFrame {
    private final PatientManager patientManager;
    private final JTable patientTable;
    private final DefaultTableModel tableModel;

    public PatientFrame() {
        setTitle("Patient Management");
        setSize(700, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        patientManager = new PatientManager();

        // Table and model
        tableModel = new DefaultTableModel(new String[]{"ID", "Name", "Age", "Gender", "Disease"}, 0);
        patientTable = new JTable(tableModel);
        refreshPatientTable();

        JButton addBtn = new JButton("Add Patient");
        JButton deleteBtn = new JButton("Delete Selected");

        addBtn.addActionListener(e -> addPatientDialog());
        deleteBtn.addActionListener(e -> deleteSelectedPatient());

        JPanel btnPanel = new JPanel();
        btnPanel.add(addBtn);
        btnPanel.add(deleteBtn);

        add(new JScrollPane(patientTable), BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);
    }

    private void refreshPatientTable() {
        tableModel.setRowCount(0);
        List<Patient> patients = patientManager.getAllPatients();
        for (Patient p : patients) {
            tableModel.addRow(new Object[]{p.getId(), p.getName(), p.getAge(), p.getGender(), p.getDisease()});
        }
    }

    private void addPatientDialog() {
        JTextField idField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField ageField = new JTextField();
        JTextField genderField = new JTextField();
        JTextField diseaseField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("ID:")); panel.add(idField);
        panel.add(new JLabel("Name:")); panel.add(nameField);
        panel.add(new JLabel("Age:")); panel.add(ageField);
        panel.add(new JLabel("Gender:")); panel.add(genderField);
        panel.add(new JLabel("Disease:")); panel.add(diseaseField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add Patient", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String id = idField.getText();
            String name = nameField.getText();
            int age = Integer.parseInt(ageField.getText());
            String gender = genderField.getText();
            String disease = diseaseField.getText();

            Patient newPatient = new Patient(Integer.parseInt(id), name, age, gender, disease, "", "", "", LocalDate.now());
            boolean added = patientManager.addPatient(newPatient);
            if (added) {
                JOptionPane.showMessageDialog(this, "Patient added");
                refreshPatientTable();
            } else {
                JOptionPane.showMessageDialog(this, "Error adding patient.");
            }
        }
    }

    private void deleteSelectedPatient() {
        int selected = patientTable.getSelectedRow();
        if (selected >= 0) {
            String id = (String) tableModel.getValueAt(selected, 0);
            boolean deleted = patientManager.removePatientById(Integer.parseInt(id));
            if (deleted) {
                JOptionPane.showMessageDialog(this, "Patient deleted.");
                refreshPatientTable();
            } else {
                JOptionPane.showMessageDialog(this, "Error deleting patient.");
            }
        }
    }
}

