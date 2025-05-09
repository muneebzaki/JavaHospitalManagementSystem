package com.hospital.gui;

import com.hospital.entities.User;
import com.hospital.gui.navigation.NavigationBar;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class MainFrame extends JFrame {
    private final User currentUser;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private NavigationBar navigationBar;
    private Map<String, JPanel> screens;

    public MainFrame(User user) {
        this.currentUser = user;
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        screens = new HashMap<>();
        navigationBar = new NavigationBar(this);
        initializeFrame();
        setupComponents();
        showDashboard();
    }

    private void initializeFrame() {
        setTitle("Hospital Management System");
        setSize(1280, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Setup main layout
        setLayout(new BorderLayout());
        
        // Add navigation bar at the top
        add(navigationBar, BorderLayout.NORTH);
        
        // Add main panel in the center
        add(mainPanel, BorderLayout.CENTER);
        
        setVisible(true);
    }

    private void setupComponents() {
        // Create menu bar
        JMenuBar menuBar = new JMenuBar();
        
        // File menu
        JMenu fileMenu = new JMenu("File");
        JMenuItem logoutItem = new JMenuItem("Logout");
        logoutItem.addActionListener(e -> handleLogout());
        fileMenu.add(logoutItem);
        
        // Help menu
        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> showAboutDialog());
        helpMenu.add(aboutItem);
        
        menuBar.add(fileMenu);
        menuBar.add(helpMenu);
        setJMenuBar(menuBar);
    }

    private void handleLogout() {
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to logout?",
            "Confirm Logout",
            JOptionPane.YES_NO_OPTION
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            // Close current window
            dispose();
            
            // Show login window
            SwingUtilities.invokeLater(() -> {
                LoginFrame loginFrame = new LoginFrame();
                loginFrame.setVisible(true);
            });
        }
    }

    private void showAboutDialog() {
        JOptionPane.showMessageDialog(
            this,
            "Hospital Management System\nVersion 1.0\n© 2024 All Rights Reserved",
            "About",
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    public void switchScreen(String screenName) {
        cardLayout.show(mainPanel, screenName);
    }

    public void addScreen(String screenName, JPanel screen) {
        screens.put(screenName, screen);
        mainPanel.add(screen, screenName);
    }

    private void showDashboard() {
        // Show appropriate dashboard based on user role
        switch (currentUser.getRole()) {
            case "ADMIN":
                switchScreen("AdminDashboard");
                break;
            case "DOCTOR":
                switchScreen("DoctorDashboard");
                break;
            case "NURSE":
                switchScreen("NurseDashboard");
                break;
            default:
                JOptionPane.showMessageDialog(this, "Invalid user role!");
                dispose();
                break;
        }
    }

    public User getCurrentUser() {
        return currentUser;
    }
}