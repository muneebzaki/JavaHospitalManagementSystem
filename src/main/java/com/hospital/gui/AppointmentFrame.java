package com.hospital.gui;

import com.hospital.controller.AppointmentManager;
import com.hospital.entities.Appointment;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class AppointmentFrame extends JFrame {
    private final AppointmentManager appointmentManager;
    private final JTable appointmentTable;
    private final DefaultTableModel tableModel;

    public AppointmentFrame() {
        setTitle("Appointment Management");
        setSize(800, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        appointmentManager = new AppointmentManager();

        tableModel = new DefaultTableModel(new String[]{"ID", "Patient ID", "Doctor ID", "DateTime", "Duration", "Status"}, 0);
        appointmentTable = new JTable(tableModel);
        refreshTable();

        JButton addBtn = new JButton("Add Appointment");
        JButton cancelBtn = new JButton("Cancel Selected");

        addBtn.addActionListener(e -> addAppointmentDialog());
        cancelBtn.addActionListener(e -> cancelSelectedAppointment());

        JPanel btnPanel = new JPanel();
        btnPanel.add(addBtn);
        btnPanel.add(cancelBtn);

        add(new JScrollPane(appointmentTable), BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        List<Appointment> list = appointmentManager.getAllAppointments();
        for (Appointment a : list) {
            tableModel.addRow(new Object[]{
                    a.getId(), a.getPatientId(), a.getDoctorId(),
                    a.getDateTime(), a.getDuration().toMinutes() + " min", a.getStatus()
            });
        }
    }

    private void addAppointmentDialog() {
        JTextField patientIdField = new JTextField();
        JTextField doctorIdField = new JTextField();
        JTextField dateTimeField = new JTextField("yyyy-MM-ddTHH:mm");
        JTextField durationField = new JTextField("30");

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Patient ID:")); panel.add(patientIdField);
        panel.add(new JLabel("Doctor ID:")); panel.add(doctorIdField);
        panel.add(new JLabel("DateTime (e.g., 2025-05-11T14:00):")); panel.add(dateTimeField);
        panel.add(new JLabel("Duration (minutes):")); panel.add(durationField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add Appointment", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                int pid = Integer.parseInt(patientIdField.getText());
                int did = Integer.parseInt(doctorIdField.getText());
                LocalDateTime dt = LocalDateTime.parse(dateTimeField.getText());
                Duration duration = Duration.ofMinutes(Integer.parseInt(durationField.getText()));

                boolean added = appointmentManager.createAppointment(pid, did, dt, duration);
                if (added) {
                    JOptionPane.showMessageDialog(this, "Appointment added!");
                    refreshTable();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to add appointment.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input. Please check your values.");
            }
        }
    }

    private void cancelSelectedAppointment() {
        int row = appointmentTable.getSelectedRow();
        if (row >= 0) {
            int id = (int) tableModel.getValueAt(row, 0);
            boolean success = appointmentManager.cancelAppointment(id);
            if (success) {
                JOptionPane.showMessageDialog(this, "Appointment cancelled.");
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to cancel appointment.");
            }
        }
    }
}
