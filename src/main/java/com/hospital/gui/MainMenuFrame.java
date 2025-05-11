package com.hospital.gui;

import javax.swing.*;
import java.awt.*;

public class MainMenuFrame extends JFrame {

    public MainMenuFrame(String username) {
        super("Hospital Management System - Dashboard");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel welcomeLabel = new JLabel("Welcome, " + username + "!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(welcomeLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JButton patientButton = new JButton("Patients");
        JButton doctorButton = new JButton("Doctors");
        JButton appointmentButton = new JButton("Appointments");
        JButton billingButton = new JButton("Billing");

        // TODO: Add action listeners to open respective GUIs
        patientButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Patient GUI coming soon"));
        doctorButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Doctor GUI coming soon"));
        appointmentButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Appointment GUI coming soon"));
        billingButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Billing GUI coming soon"));

        buttonPanel.add(patientButton);
        buttonPanel.add(doctorButton);
        buttonPanel.add(appointmentButton);
        buttonPanel.add(billingButton);

        add(buttonPanel, BorderLayout.CENTER);
    }
}

