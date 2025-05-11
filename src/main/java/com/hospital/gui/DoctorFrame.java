package com.hospital.gui;

import com.hospital.controller.DoctorManager;
import com.hospital.entities.Doctor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class DoctorFrame extends JFrame {
    private final DoctorManager doctorManager;
    private final JTable doctorTable;
    private final DefaultTableModel tableModel;

    public DoctorFrame() {
        setTitle("Doctor Management");
        setSize(700, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        doctorManager = new DoctorManager();

        tableModel = new DefaultTableModel(new String[]{"ID", "Name", "Specialization"}, 0);
        doctorTable = new JTable(tableModel);
        refreshDoctorTable();

        JButton addBtn = new JButton("Add Doctor");
        JButton deleteBtn = new JButton("Delete Selected");

        addBtn.addActionListener(e -> addDoctorDialog());
        deleteBtn.addActionListener(e -> deleteSelectedDoctor());

        JPanel btnPanel = new JPanel();
        btnPanel.add(addBtn);
        btnPanel.add(deleteBtn);

        add(new JScrollPane(doctorTable), BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);
    }

    private void refreshDoctorTable() {
        tableModel.setRowCount(0);
        List<Doctor> doctors = doctorManager.getAllDoctors();
        for (Doctor d : doctors) {
            tableModel.addRow(new Object[]{d.getId(), d.getName(), d.getSpecialization()});
        }
    }

    private void addDoctorDialog() {
        JTextField idField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField specField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("ID:")); panel.add(idField);
        panel.add(new JLabel("Name:")); panel.add(nameField);
        panel.add(new JLabel("Specialization:")); panel.add(specField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add Doctor", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            int id = Integer.parseInt(idField.getText());
            String name = nameField.getText();
            String spec = specField.getText();

            Doctor doctor = new Doctor(id, name, spec);
            boolean added = doctorManager.addDoctor(doctor);
            if (added) {
                JOptionPane.showMessageDialog(this, "Doctor added!");
                refreshDoctorTable();
            } else {
                JOptionPane.showMessageDialog(this, "Error adding doctor.");
            }
        }
    }

    private void deleteSelectedDoctor() {
        int selected = doctorTable.getSelectedRow();
        if (selected >= 0) {
            int id = (int) tableModel.getValueAt(selected, 0);
            boolean deleted = doctorManager.removeDoctor(id);
            if (deleted) {
                JOptionPane.showMessageDialog(this, "Doctor deleted.");
                refreshDoctorTable();
            } else {
                JOptionPane.showMessageDialog(this, "Error deleting doctor.");
            }
        }
    }
}

