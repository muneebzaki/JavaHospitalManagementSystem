package com.hospital.gui;

import com.hospital.gui.navigation.NavigationBar;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private NavigationBar navigationBar;
    private Map<String, JPanel> screens;

    public MainFrame() {
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        screens = new HashMap<>();
        navigationBar = new NavigationBar(this);
        initializeFrame();
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

    public void switchScreen(String screenName) {
        cardLayout.show(mainPanel, screenName);
    }

    public void addScreen(String screenName, JPanel screen) {
        screens.put(screenName, screen);
        mainPanel.add(screen, screenName);
    }
}