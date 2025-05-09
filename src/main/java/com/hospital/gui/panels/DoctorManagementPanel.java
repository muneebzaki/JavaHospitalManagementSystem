package com.hospital.gui.panels;

import com.hospital.controller.DoctorManager;
import com.hospital.entities.Doctor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class DoctorManagementPanel extends JPanel {
    private final DoctorManager doctorManager;
    private JTable doctorTable;
    private DefaultTableModel tableModel;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton scheduleButton;

    public DoctorManagementPanel() {
        this.doctorManager = new DoctorManager();
        initializeComponents();
        setupLayout();
        loadDoctors();
    }

    private void initializeComponents() {
        // Initialize table model
        String[] columns = {"ID", "Name", "Specialization", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        doctorTable = new JTable(tableModel);
        doctorTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Initialize buttons
        addButton = new JButton("Add Doctor");
        editButton = new JButton("Edit Doctor");
        deleteButton = new JButton("Delete Doctor");
        scheduleButton = new JButton("Schedule");
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        // Table setup
        JScrollPane scrollPane = new JScrollPane(doctorTable);
        add(scrollPane, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(scheduleButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Add action listeners
        setupListeners();
    }

    private void setupListeners() {
        addButton.addActionListener(e -> showAddDoctorDialog());
        editButton.addActionListener(e -> showEditDoctorDialog());
        deleteButton.addActionListener(e -> deleteSelectedDoctor());
        scheduleButton.addActionListener(e -> showScheduleDialog());
    }

    private void loadDoctors() {
        tableModel.setRowCount(0);
        List<Doctor> doctors = doctorManager.getAllDoctors();
        for (Doctor doctor : doctors) {
            Object[] row = {
                doctor.getId(),
                doctor.getName(),
                doctor.getSpecialization(),
                doctor.isAvailable() ? "Available" : "Unavailable"
            };
            tableModel.addRow(row);
        }
    }

    private void showAddDoctorDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Add New Doctor", true);
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JTextField nameField = new JTextField(20);
        JTextField specializationField = new JTextField(20);

        // Add components to panel
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        panel.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Specialization:"), gbc);
        gbc.gridx = 1;
        panel.add(specializationField, gbc);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            String name = nameField.getText();
            String specialization = specializationField.getText();

            if (name.isEmpty() || specialization.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please fill in all fields");
                return;
            }

            Doctor newDoctor = new Doctor(0, name, specialization);
            if (doctorManager.addDoctor(newDoctor)) {
                loadDoctors();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Failed to add doctor");
            }
        });

        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        panel.add(saveButton, gbc);

        dialog.add(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void showEditDoctorDialog() {
        int selectedRow = doctorTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a doctor to edit");
            return;
        }

        int doctorId = (int) tableModel.getValueAt(selectedRow, 0);
        Doctor doctor = doctorManager.findDoctorById(doctorId);
        if (doctor == null) {
            JOptionPane.showMessageDialog(this, "Doctor not found");
            return;
        }

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Edit Doctor", true);
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JTextField nameField = new JTextField(doctor.getName(), 20);
        JTextField specializationField = new JTextField(doctor.getSpecialization(), 20);
        JCheckBox availableCheckBox = new JCheckBox("Available", doctor.isAvailable());

        // Add components to panel
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        panel.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Specialization:"), gbc);
        gbc.gridx = 1;
        panel.add(specializationField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        panel.add(availableCheckBox, gbc);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            String name = nameField.getText();
            String specialization = specializationField.getText();

            if (name.isEmpty() || specialization.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please fill in all fields");
                return;
            }

            doctor.setName(name);
            doctor.setSpecialization(specialization);
            doctor.setAvailable(availableCheckBox.isSelected());

            if (doctorManager.updateDoctor(doctor)) {
                loadDoctors();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Failed to update doctor");
            }
        });

        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(saveButton, gbc);

        dialog.add(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void deleteSelectedDoctor() {
        int selectedRow = doctorTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a doctor to delete");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this doctor?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            int doctorId = (int) tableModel.getValueAt(selectedRow, 0);
            if (doctorManager.removeDoctor(doctorId)) {
                loadDoctors();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete doctor");
            }
        }
    }

    private void showScheduleDialog() {
        int selectedRow = doctorTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a doctor to schedule");
            return;
        }

        int doctorId = (int) tableModel.getValueAt(selectedRow, 0);
        Doctor doctor = doctorManager.findDoctorById(doctorId);
        if (doctor == null) {
            JOptionPane.showMessageDialog(this, "Doctor not found");
            return;
        }

        // TODO: Implement schedule dialog
        JOptionPane.showMessageDialog(this, "Schedule functionality to be implemented");
    }
} 