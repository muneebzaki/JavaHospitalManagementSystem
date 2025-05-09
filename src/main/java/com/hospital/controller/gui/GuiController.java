package com.hospital.controller.gui;

import com.hospital.gui.MainFrame;

public abstract class GuiController {
    protected final MainFrame mainFrame;
    
    public GuiController(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }
}