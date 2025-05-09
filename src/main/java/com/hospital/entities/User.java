package com.hospital.entities;

public class User {
    private int id;
    private String username;
    private String password;
    private String role; // ADMIN, DOCTOR, NURSE, PATIENT
    private int referenceId; // ID of the related entity (doctor, nurse, or patient)
    private boolean isActive;

    public User(int id, String username, String password, String role, int referenceId) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.referenceId = referenceId;
        this.isActive = true;
    }

    // Constructor for new user
    public User(String username, String password, String role, int referenceId) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.referenceId = referenceId;
        this.isActive = true;
    }

    // Getters
    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
    public int getReferenceId() { return referenceId; }
    public boolean isActive() { return isActive; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setRole(String role) { this.role = role; }
    public void setReferenceId(int referenceId) { this.referenceId = referenceId; }
    public void setActive(boolean active) { isActive = active; }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", role='" + role + '\'' +
                ", referenceId=" + referenceId +
                ", isActive=" + isActive +
                '}';
    }
}
