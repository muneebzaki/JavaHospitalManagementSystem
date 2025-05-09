package com.hospital;

import com.hospital.controller.*;
import com.hospital.controller.gui.*;
import com.hospital.gui.LoginFrame;
import com.hospital.util.DatabaseInitializer;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Initialize database
        DatabaseInitializer.getInstance().initializeDatabase();
        
        // Start application with login screen
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}