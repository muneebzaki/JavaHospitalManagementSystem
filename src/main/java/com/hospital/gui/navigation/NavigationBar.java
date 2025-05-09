package com.hospital.gui.navigation;

import com.hospital.gui.MainFrame;

import javax.swing.*;
import java.awt.*;

public class NavigationBar extends JPanel {
    private MainFrame mainFrame;
    private JButton homeButton;
    private JButton backButton;
    private JPanel buttonPanel;
    
    public NavigationBar(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        initializeComponents();
        setupLayout();
        setupListeners();
    }
    
    private void initializeComponents() {
        homeButton = new JButton("Home");
        backButton = new JButton("Back");
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEtchedBorder());
        
        buttonPanel.add(homeButton);
        buttonPanel.add(backButton);
        
        add(buttonPanel, BorderLayout.WEST);
    }
    
    private void setupListeners() {
        homeButton.addActionListener(e -> mainFrame.switchScreen("MAIN_MENU"));
        backButton.addActionListener(e -> mainFrame.switchScreen("MAIN_MENU"));
    }
}