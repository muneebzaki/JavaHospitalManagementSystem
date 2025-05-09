package com.hospital;

import com.hospital.controller.BillManager;
import com.hospital.controller.MockBillManager;
import com.hospital.controller.gui.BillGuiController;
import com.hospital.gui.MainFrame;
import com.hospital.gui.panels.BillPanel;
import com.hospital.gui.panels.MainMenuPanel;

import javax.swing.*;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame();
            
            // Initialize controllers
            BillManager billManager;
            try {
                billManager = new MockBillManager();  // Use mock instead of real implementation
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            
            BillGuiController billGuiController = new BillGuiController(mainFrame, billManager);
            
            // Initialize panels
            MainMenuPanel mainMenuPanel = new MainMenuPanel(mainFrame, billGuiController);
            BillPanel billPanel = new BillPanel(mainFrame, billGuiController);
            
            // Add panels to main frame
            mainFrame.addScreen("MAIN_MENU", mainMenuPanel);
            mainFrame.addScreen("BILLS_SCREEN", billPanel);
            
            // Show main menu initially
            mainFrame.switchScreen("MAIN_MENU");
        });
    }
}