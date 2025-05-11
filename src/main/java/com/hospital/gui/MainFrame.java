package com.hospital.gui;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public MainFrame() {
        setTitle("Hospital Management System");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(6, 1, 10, 10));

        JButton patientBtn = new JButton("Manage Patients");
        JButton doctorBtn = new JButton("Manage Doctors");
        JButton appointmentBtn = new JButton("Manage Appointments");
        JButton billBtn = new JButton("Manage Billing");
        JButton recordBtn = new JButton("Manage Medical Records");
        JButton userBtn = new JButton("User Login/Register");

        patientBtn.addActionListener(e -> new PatientFrame().setVisible(true));
        doctorBtn.addActionListener(e -> new DoctorFrame().setVisible(true));
        appointmentBtn.addActionListener(e -> new AppointmentFrame().setVisible(true));
        billBtn.addActionListener(e -> new BillingFrame().setVisible(true));
        recordBtn.addActionListener(e -> new MedicalRecordFrame().setVisible(true));
        userBtn.addActionListener(e -> new LoginFrame().setVisible(true));

        add(patientBtn);
        add(doctorBtn);
        add(appointmentBtn);
        add(billBtn);
        add(recordBtn);
        add(userBtn);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}
