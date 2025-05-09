package com.hospital.gui;


import com.hospital.Main;
import com.hospital.dao.UserDAO;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

import static com.hospital.util.DBConnection.getConnection;


public class LoginFrame extends JFrame {
    private final JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel errorLabel;

    public LoginFrame() {
        setTitle("Login - Hospital Management System");
        setSize(350, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        panel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        panel.add(usernameField);

        panel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        loginButton = new JButton("Login");
        panel.add(new JLabel()); // empty cell
        panel.add(loginButton);

        add(panel, BorderLayout.CENTER);

        errorLabel = new JLabel("", SwingConstants.CENTER);
        errorLabel.setForeground(Color.RED);
        add(errorLabel, BorderLayout.SOUTH);

        loginButton.addActionListener(e -> handleLogin());

        setVisible(true);
    }

    private void handleLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (validateLogin(username, password)) {
            dispose(); // close login window
            Main.main(null); // launch main GUI
        } else {
            errorLabel.setText("Invalid credentials. Please try again.");
        }
    }

    private boolean validateLogin(String username, String password) {
        UserDAO userDAO = new UserDAO();
        return userDAO.isValidUser(username, password);
    }

}
