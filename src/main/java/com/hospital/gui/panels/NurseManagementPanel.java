package com.hospital.gui.panels;

import com.hospital.controller.NurseManager;
import com.hospital.entities.Nurse;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class NurseManagementPanel extends JPanel {
    private final NurseManager nurseManager;
    private JTable nurseTable;
    private DefaultTableModel tableModel;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton scheduleButton;

    public NurseManagementPanel() {
        this.nurseManager = new NurseManager();
        initializeComponents();
        setupLayout();
        loadNurses();
    }

    private void initializeComponents() {
        // Initialize table model
        String[] columns = {"ID", "Name", "Department", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        nurseTable = new JTable(tableModel);
        nurseTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Initialize buttons
        addButton = new JButton("Add Nurse");
        editButton = new JButton("Edit Nurse");
        deleteButton = new JButton("Delete Nurse");
        scheduleButton = new JButton("Schedule");
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        // Table setup
        JScrollPane scrollPane = new JScrollPane(nurseTable);
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
        addButton.addActionListener(e -> showAddNurseDialog());
        editButton.addActionListener(e -> showEditNurseDialog());
        deleteButton.addActionListener(e -> deleteSelectedNurse());
        scheduleButton.addActionListener(e -> showScheduleDialog());
    }

    private void loadNurses() {
        tableModel.setRowCount(0);
        List<Nurse> nurses = nurseManager.getAllNurses();
        for (Nurse nurse : nurses) {
            Object[] row = {
                nurse.getId(),
                nurse.getName(),
                nurse.getDepartment(),
                nurse.isAvailable() ? "Available" : "Unavailable"
            };
            tableModel.addRow(row);
        }
    }

    private void showAddNurseDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Add New Nurse", true);
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JTextField nameField = new JTextField(20);
        JTextField departmentField = new JTextField(20);

        // Add components to panel
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        panel.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Department:"), gbc);
        gbc.gridx = 1;
        panel.add(departmentField, gbc);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            String name = nameField.getText();
            String department = departmentField.getText();

            if (name.isEmpty() || department.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please fill in all fields");
                return;
            }

            Nurse newNurse = new Nurse(0, name, department);
            if (nurseManager.addNurse(newNurse)) {
                loadNurses();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Failed to add nurse");
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

    private void showEditNurseDialog() {
        int selectedRow = nurseTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a nurse to edit");
            return;
        }

        int nurseId = (int) tableModel.getValueAt(selectedRow, 0);
        Nurse nurse = nurseManager.findNurseById(nurseId);
        if (nurse == null) {
            JOptionPane.showMessageDialog(this, "Nurse not found");
            return;
        }

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Edit Nurse", true);
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JTextField nameField = new JTextField(nurse.getName(), 20);
        JTextField departmentField = new JTextField(nurse.getDepartment(), 20);
        JCheckBox availableCheckBox = new JCheckBox("Available", nurse.isAvailable());

        // Add components to panel
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        panel.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Department:"), gbc);
        gbc.gridx = 1;
        panel.add(departmentField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        panel.add(availableCheckBox, gbc);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            String name = nameField.getText();
            String department = departmentField.getText();

            if (name.isEmpty() || department.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please fill in all fields");
                return;
            }

            nurse.setName(name);
            nurse.setDepartment(department);
            nurse.setAvailable(availableCheckBox.isSelected());

            if (nurseManager.updateNurse(nurse)) {
                loadNurses();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Failed to update nurse");
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

    private void deleteSelectedNurse() {
        int selectedRow = nurseTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a nurse to delete");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this nurse?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            int nurseId = (int) tableModel.getValueAt(selectedRow, 0);
            if (nurseManager.removeNurse(nurseId)) {
                loadNurses();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete nurse");
            }
        }
    }

    private void showScheduleDialog() {
        int selectedRow = nurseTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a nurse to schedule");
            return;
        }

        int nurseId = (int) tableModel.getValueAt(selectedRow, 0);
        Nurse nurse = nurseManager.findNurseById(nurseId);
        if (nurse == null) {
            JOptionPane.showMessageDialog(this, "Nurse not found");
            return;
        }

        // TODO: Implement schedule dialog
        JOptionPane.showMessageDialog(this, "Schedule functionality to be implemented");
    }
} 