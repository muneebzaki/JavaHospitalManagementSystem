package com.hospital.controller;

import com.hospital.dao.UserDAO;
import com.hospital.entities.User;

public class UserManager {
    private final UserDAO userDAO;

    public UserManager() {
        this.userDAO = new UserDAO();
    }

    public boolean registerUser(User user) {
        if (user == null || user.getUsername() == null || user.getPassword() == null) return false;
        if (userDAO.usernameExists(user.getUsername())) return false;
        return userDAO.insertUser(user);
    }

    public User login(String username, String password) {
        if (username == null || password == null) return null;
        return userDAO.findByUsernameAndPassword(username, password);
    }
}
