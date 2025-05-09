package com.hospital.gui.panels;

import com.hospital.controller.gui.GuiController;
import com.hospital.gui.MainFrame;

import javax.swing.*;
import java.awt.*;

public class MainMenuPanel extends BasePanel {
    private JButton billsButton;
    private JLabel welcomeLabel;
    private JPanel buttonPanel;

    public MainMenuPanel(MainFrame mainFrame, GuiController controller) {
        super(mainFrame, controller);
        initialize();
    }

    @Override
    protected void initializeComponents() {
        welcomeLabel = new JLabel("Hospital Management System", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        
        billsButton = new JButton("Billing Management");
        billsButton.setPreferredSize(new Dimension(200, 40));
        
        buttonPanel = new JPanel();
    }

    @Override
    protected void setupLayout() {
        setLayout(new BorderLayout(10, 10));
        
        // Center the welcome label at the top
        add(welcomeLabel, BorderLayout.NORTH);
        
        // Setup button panel
        buttonPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        buttonPanel.add(billsButton, gbc);
        
        // Center the button panel
        add(buttonPanel, BorderLayout.CENTER);
    }

    @Override
    protected void setupListeners() {
        billsButton.addActionListener(e -> mainFrame.switchScreen("BILLS_SCREEN"));
    }
}