package com.hospital.dao;

import com.hospital.entities.User;
import java.util.List;

public interface IUserDAO {
    boolean insertUser(User user);
    boolean updateUser(User user);
    boolean deleteUser(int id);
    User findById(int id);
    User findByUsername(String username);
    User findByUsernameAndPassword(String username, String password);
    List<User> findAll();
    List<User> findByRole(String role);
    boolean deactivateUser(int id);
    boolean activateUser(int id);
} 