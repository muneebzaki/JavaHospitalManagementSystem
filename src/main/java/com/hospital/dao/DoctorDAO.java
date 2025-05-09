package com.hospital.dao;

import com.hospital.entities.Doctor;
import com.hospital.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DoctorDAO implements IDoctorDAO {
    private final Connection connection;

    public DoctorDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public boolean insertDoctor(Doctor doctor) {
        String sql = "INSERT INTO doctors (name, specialization, is_available) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, doctor.getName());
            stmt.setString(2, doctor.getSpecialization());
            stmt.setBoolean(3, doctor.isAvailable());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        doctor.setId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateDoctor(Doctor doctor) {
        String sql = "UPDATE doctors SET name=?, specialization=?, is_available=? WHERE id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, doctor.getName());
            stmt.setString(2, doctor.getSpecialization());
            stmt.setBoolean(3, doctor.isAvailable());
            stmt.setInt(4, doctor.getId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteDoctor(int id) {
        String sql = "DELETE FROM doctors WHERE id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Doctor findById(int id) {
        String sql = "SELECT * FROM doctors WHERE id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return extractDoctor(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Doctor> findAll() {
        String sql = "SELECT * FROM doctors";
        List<Doctor> doctors = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                doctors.add(extractDoctor(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return doctors;
    }

    @Override
    public List<Doctor> findBySpecialization(String specialization) {
        String sql = "SELECT * FROM doctors WHERE specialization=?";
        List<Doctor> doctors = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, specialization);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                doctors.add(extractDoctor(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return doctors;
    }

    @Override
    public List<Doctor> findAvailable() {
        String sql = "SELECT * FROM doctors WHERE is_available=true";
        List<Doctor> doctors = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                doctors.add(extractDoctor(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return doctors;
    }

    private Doctor extractDoctor(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String specialization = rs.getString("specialization");
        boolean isAvailable = rs.getBoolean("is_available");
        
        Doctor doctor = new Doctor(id, name, specialization);
        doctor.setAvailable(isAvailable);
        return doctor;
    }
} 