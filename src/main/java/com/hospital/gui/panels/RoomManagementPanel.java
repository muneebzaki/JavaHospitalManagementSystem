package com.hospital.gui.panels;

import com.hospital.controller.RoomManager;
import com.hospital.entities.Room;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class RoomManagementPanel extends BasePanel {
    private final RoomManager roomManager;
    private JTable roomTable;
    private DefaultTableModel tableModel;
    private JTextField roomNumberField;
    private JComboBox<String> roomTypeCombo;
    private JSpinner capacitySpinner;
    private JSpinner priceSpinner;
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton refreshButton;

    public RoomManagementPanel() {
        this.roomManager = new RoomManager();
        initializeComponents();
        setupLayout();
        loadRooms();
    }

    private void initializeComponents() {
        // Table setup
        String[] columns = {"Room ID", "Room Number", "Type", "Available", "Capacity", "Price/Day", "Current Patient"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        roomTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(roomTable);

        // Input fields
        roomNumberField = new JTextField(10);
        roomTypeCombo = new JComboBox<>(new String[]{"GENERAL", "PRIVATE", "ICU", "OPERATION"});
        capacitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
        priceSpinner = new JSpinner(new SpinnerNumberModel(100.0, 0.0, 10000.0, 100.0));

        // Buttons
        addButton = new JButton("Add Room");
        updateButton = new JButton("Update Room");
        deleteButton = new JButton("Delete Room");
        refreshButton = new JButton("Refresh");

        // Add action listeners
        addButton.addActionListener(e -> addRoom());
        updateButton.addActionListener(e -> updateRoom());
        deleteButton.addActionListener(e -> deleteRoom());
        refreshButton.addActionListener(e -> loadRooms());
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        // Input panel
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Add components to input panel
        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(new JLabel("Room Number:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(roomNumberField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        inputPanel.add(new JLabel("Room Type:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(roomTypeCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        inputPanel.add(new JLabel("Capacity:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(capacitySpinner, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        inputPanel.add(new JLabel("Price per Day:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(priceSpinner, gbc);

        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);

        // Add panels to main panel
        add(inputPanel, BorderLayout.NORTH);
        add(new JScrollPane(roomTable), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadRooms() {
        tableModel.setRowCount(0);
        List<Room> rooms = roomManager.getAllRooms();
        for (Room room : rooms) {
            Object[] row = {
                room.getRoomId(),
                room.getRoomNumber(),
                room.getRoomType(),
                room.isAvailable(),
                room.getCapacity(),
                room.getPricePerDay(),
                room.getCurrentPatientId() == -1 ? "None" : room.getCurrentPatientId()
            };
            tableModel.addRow(row);
        }
    }

    private void addRoom() {
        try {
            String roomNumber = roomNumberField.getText().trim();
            if (roomNumber.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a room number");
                return;
            }

            if (roomManager.roomNumberExists(roomNumber)) {
                JOptionPane.showMessageDialog(this, "Room number already exists");
                return;
            }

            Room room = new Room(
                roomNumber,
                (String) roomTypeCombo.getSelectedItem(),
                (Integer) capacitySpinner.getValue(),
                (Double) priceSpinner.getValue()
            );

            if (roomManager.addRoom(room)) {
                JOptionPane.showMessageDialog(this, "Room added successfully");
                clearInputs();
                loadRooms();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add room");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void updateRoom() {
        int selectedRow = roomTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a room to update");
            return;
        }

        try {
            int roomId = (int) tableModel.getValueAt(selectedRow, 0);
            Room room = roomManager.findRoomById(roomId);
            if (room == null) {
                JOptionPane.showMessageDialog(this, "Room not found");
                return;
            }

            room.setRoomNumber(roomNumberField.getText().trim());
            room.setRoomType((String) roomTypeCombo.getSelectedItem());
            room.setCapacity((Integer) capacitySpinner.getValue());
            room.setPricePerDay((Double) priceSpinner.getValue());

            if (roomManager.updateRoom(room)) {
                JOptionPane.showMessageDialog(this, "Room updated successfully");
                clearInputs();
                loadRooms();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update room");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void deleteRoom() {
        int selectedRow = roomTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a room to delete");
            return;
        }

        int roomId = (int) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete this room?", 
            "Confirm Delete", 
            JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            if (roomManager.deleteRoom(roomId)) {
                JOptionPane.showMessageDialog(this, "Room deleted successfully");
                clearInputs();
                loadRooms();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete room");
            }
        }
    }

    private void clearInputs() {
        roomNumberField.setText("");
        roomTypeCombo.setSelectedIndex(0);
        capacitySpinner.setValue(1);
        priceSpinner.setValue(100.0);
    }
} 