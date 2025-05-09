package com.hospital.controller;

import com.hospital.dao.IUserDAO;
import com.hospital.dao.UserDAO;
import com.hospital.entities.User;

import java.util.List;

public class UserManager {
    private final IUserDAO userDAO;

    public UserManager() {
        this.userDAO = new UserDAO();
    }

    public boolean registerUser(User user) {
        // Check if username already exists
        if (userDAO.findByUsername(user.getUsername()) != null) {
            return false;
        }
        return userDAO.insertUser(user);
    }

    public User login(String username, String password) {
        return userDAO.findByUsernameAndPassword(username, password);
    }

    public boolean updateUser(User user) {
        return userDAO.updateUser(user);
    }

    public boolean deleteUser(int id) {
        return userDAO.deleteUser(id);
    }

    public User findUserById(int id) {
        return userDAO.findById(id);
    }

    public User findUserByUsername(String username) {
        return userDAO.findByUsername(username);
    }

    public List<User> getAllUsers() {
        return userDAO.findAll();
    }

    public List<User> getUsersByRole(String role) {
        return userDAO.findByRole(role);
    }

    public boolean deactivateUser(int id) {
        return userDAO.deactivateUser(id);
    }

    public boolean activateUser(int id) {
        return userDAO.activateUser(id);
    }

    public boolean changePassword(int userId, String newPassword) {
        User user = userDAO.findById(userId);
        if (user != null) {
            user.setPassword(newPassword);
            return userDAO.updateUser(user);
        }
        return false;
    }

    public boolean isUsernameAvailable(String username) {
        return userDAO.findByUsername(username) == null;
    }
} 