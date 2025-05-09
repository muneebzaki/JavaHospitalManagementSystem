package com.hospital.gui;

import com.hospital.entities.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DashboardFrame extends JFrame {
    private final User currentUser;
    private JTabbedPane tabbedPane;

    public DashboardFrame(User user) {
        this.currentUser = user;
        initializeComponents();
        setupLayout();
        setupTabs();
    }

    private void initializeComponents() {
        setTitle("Hospital Management System - Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        tabbedPane = new JTabbedPane();
    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        add(tabbedPane, BorderLayout.CENTER);

        // Add logout button to the top
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openLoginFrame();
            }
        });

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanel.add(logoutButton);
        add(topPanel, BorderLayout.NORTH);
    }

    private void setupTabs() {
        // Add tabs based on user role
        switch (currentUser.getRole()) {
            case "ADMIN":
                addAdminTabs();
                break;
            case "DOCTOR":
                addDoctorTabs();
                break;
            case "NURSE":
                addNurseTabs();
                break;
            case "PATIENT":
                addPatientTabs();
                break;
        }
    }

    private void addAdminTabs() {
        tabbedPane.addTab("Users", new UserManagementPanel());
        tabbedPane.addTab("Doctors", new DoctorManagementPanel());
        tabbedPane.addTab("Nurses", new NurseManagementPanel());
        tabbedPane.addTab("Patients", new PatientManagementPanel());
        tabbedPane.addTab("Appointments", new AppointmentManagementPanel());
        tabbedPane.addTab("Billing", new BillingManagementPanel());
    }

    private void addDoctorTabs() {
        tabbedPane.addTab("My Schedule", new DoctorSchedulePanel(currentUser.getReferenceId()));
        tabbedPane.addTab("Patients", new DoctorPatientPanel(currentUser.getReferenceId()));
        tabbedPane.addTab("Appointments", new DoctorAppointmentPanel(currentUser.getReferenceId()));
    }

    private void addNurseTabs() {
        tabbedPane.addTab("My Schedule", new NurseSchedulePanel(currentUser.getReferenceId()));
        tabbedPane.addTab("Patients", new NursePatientPanel(currentUser.getReferenceId()));
    }

    private void addPatientTabs() {
        tabbedPane.addTab("My Appointments", new PatientAppointmentPanel(currentUser.getReferenceId()));
        tabbedPane.addTab("Medical Records", new PatientMedicalRecordPanel(currentUser.getReferenceId()));
        tabbedPane.addTab("Bills", new PatientBillPanel(currentUser.getReferenceId()));
    }

    private void openLoginFrame() {
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
            this.dispose();
        });
    }
} 