package com.hospital.gui.panels;

import com.hospital.controller.UserManager;
import com.hospital.entities.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class UserManagementPanel extends JPanel {
    private final UserManager userManager;
    private JTable userTable;
    private DefaultTableModel tableModel;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton activateButton;
    private JButton deactivateButton;

    public UserManagementPanel() {
        this.userManager = new UserManager();
        initializeComponents();
        setupLayout();
        loadUsers();
    }

    private void initializeComponents() {
        // Initialize table model
        String[] columns = {"ID", "Username", "Role", "Reference ID", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        userTable = new JTable(tableModel);
        userTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Initialize buttons
        addButton = new JButton("Add User");
        editButton = new JButton("Edit User");
        deleteButton = new JButton("Delete User");
        activateButton = new JButton("Activate User");
        deactivateButton = new JButton("Deactivate User");
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        // Table setup
        JScrollPane scrollPane = new JScrollPane(userTable);
        add(scrollPane, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(activateButton);
        buttonPanel.add(deactivateButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Add action listeners
        setupListeners();
    }

    private void setupListeners() {
        addButton.addActionListener(e -> showAddUserDialog());
        editButton.addActionListener(e -> showEditUserDialog());
        deleteButton.addActionListener(e -> deleteSelectedUser());
        activateButton.addActionListener(e -> activateSelectedUser());
        deactivateButton.addActionListener(e -> deactivateSelectedUser());
    }

    private void loadUsers() {
        tableModel.setRowCount(0);
        List<User> users = userManager.getAllUsers();
        for (User user : users) {
            Object[] row = {
                user.getId(),
                user.getUsername(),
                user.getRole(),
                user.getReferenceId(),
                user.isActive() ? "Active" : "Inactive"
            };
            tableModel.addRow(row);
        }
    }

    private void showAddUserDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Add New User", true);
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JTextField usernameField = new JTextField(20);
        JPasswordField passwordField = new JPasswordField(20);
        JComboBox<String> roleComboBox = new JComboBox<>(new String[]{"ADMIN", "DOCTOR", "NURSE", "PATIENT"});
        JTextField referenceIdField = new JTextField(20);

        // Add components to panel
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        panel.add(usernameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Role:"), gbc);
        gbc.gridx = 1;
        panel.add(roleComboBox, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Reference ID:"), gbc);
        gbc.gridx = 1;
        panel.add(referenceIdField, gbc);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            try {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                String role = (String) roleComboBox.getSelectedItem();
                int referenceId = Integer.parseInt(referenceIdField.getText());

                if (username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Please fill in all fields");
                    return;
                }

                User newUser = new User(username, password, role, referenceId);
                if (userManager.registerUser(newUser)) {
                    loadUsers();
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Failed to add user");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid reference ID");
            }
        });

        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 2;
        panel.add(saveButton, gbc);

        dialog.add(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void showEditUserDialog() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a user to edit");
            return;
        }

        int userId = (int) tableModel.getValueAt(selectedRow, 0);
        User user = userManager.findUserById(userId);
        if (user == null) {
            JOptionPane.showMessageDialog(this, "User not found");
            return;
        }

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Edit User", true);
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JTextField usernameField = new JTextField(user.getUsername(), 20);
        JPasswordField passwordField = new JPasswordField(20);
        JComboBox<String> roleComboBox = new JComboBox<>(new String[]{"ADMIN", "DOCTOR", "NURSE", "PATIENT"});
        roleComboBox.setSelectedItem(user.getRole());
        JTextField referenceIdField = new JTextField(String.valueOf(user.getReferenceId()), 20);

        // Add components to panel
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        panel.add(usernameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Role:"), gbc);
        gbc.gridx = 1;
        panel.add(roleComboBox, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Reference ID:"), gbc);
        gbc.gridx = 1;
        panel.add(referenceIdField, gbc);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            try {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                String role = (String) roleComboBox.getSelectedItem();
                int referenceId = Integer.parseInt(referenceIdField.getText());

                if (username.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Username cannot be empty");
                    return;
                }

                user.setUsername(username);
                if (!password.isEmpty()) {
                    user.setPassword(password);
                }
                user.setRole(role);
                user.setReferenceId(referenceId);

                if (userManager.updateUser(user)) {
                    loadUsers();
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Failed to update user");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid reference ID");
            }
        });

        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 2;
        panel.add(saveButton, gbc);

        dialog.add(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void deleteSelectedUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a user to delete");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this user?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            int userId = (int) tableModel.getValueAt(selectedRow, 0);
            if (userManager.deleteUser(userId)) {
                loadUsers();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete user");
            }
        }
    }

    private void activateSelectedUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a user to activate");
            return;
        }

        int userId = (int) tableModel.getValueAt(selectedRow, 0);
        if (userManager.activateUser(userId)) {
            loadUsers();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to activate user");
        }
    }

    private void deactivateSelectedUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a user to deactivate");
            return;
        }

        int userId = (int) tableModel.getValueAt(selectedRow, 0);
        if (userManager.deactivateUser(userId)) {
            loadUsers();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to deactivate user");
        }
    }
} 