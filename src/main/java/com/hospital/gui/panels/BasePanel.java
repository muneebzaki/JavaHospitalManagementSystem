package com.hospital.gui.panels;

import com.hospital.controller.gui.GuiController;
import com.hospital.gui.MainFrame;

import javax.swing.*;

public abstract class BasePanel extends JPanel {
    protected final MainFrame mainFrame;
    protected final GuiController controller;

    public BasePanel(MainFrame mainFrame, GuiController controller) {
        this.mainFrame = mainFrame;
        this.controller = controller;
    }

    protected void initialize() {
        initializeComponents();
        setupLayout();
        setupListeners();
    }

    protected abstract void initializeComponents();
    protected abstract void setupLayout();
    protected abstract void setupListeners();
}
