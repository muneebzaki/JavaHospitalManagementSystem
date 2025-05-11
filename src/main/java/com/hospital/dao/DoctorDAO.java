package com.hospital.dao;

import com.hospital.entities.Doctor;
import com.hospital.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DoctorDAO {
    private static final Map<Integer, Doctor> doctorCache = new ConcurrentHashMap<>();
    private static final long CACHE_DURATION = 5 * 60 * 1000; // 5 minutes
    private static long lastCacheUpdate = 0;

    private void updateCache() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastCacheUpdate > CACHE_DURATION) {
            List<Doctor> doctors = findAll();
            doctorCache.clear();
            for (Doctor doctor : doctors) {
                doctorCache.put(doctor.getId(), doctor);
            }
            lastCacheUpdate = currentTime;
        }
    }

    public boolean insertDoctor(Doctor doctor) {
        String sql = "INSERT INTO doctors (name, specialization) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, doctor.getName());
            stmt.setString(2, doctor.getSpecialization());

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    doctor.setId(rs.getInt(1));
                    doctorCache.put(doctor.getId(), doctor);
                }
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean updateDoctor(Doctor doctor) {
        String sql = "UPDATE doctors SET name=?, specialization=? WHERE id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, doctor.getName());
            stmt.setString(2, doctor.getSpecialization());
            stmt.setInt(3, doctor.getId());

            boolean success = stmt.executeUpdate() > 0;
            if (success) {
                doctorCache.put(doctor.getId(), doctor);
            }
            return success;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean deleteDoctor(int id) {
        String sql = "DELETE FROM doctors WHERE id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            boolean success = stmt.executeUpdate() > 0;
            if (success) {
                doctorCache.remove(id);
            }
            return success;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public Doctor findById(int id) {
        updateCache();
        return doctorCache.get(id);
    }

    public List<Doctor> findAll() {
        List<Doctor> doctors = new ArrayList<>();
        String sql = "SELECT * FROM doctors";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Doctor doctor = new Doctor(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("specialization")
                );
                doctors.add(doctor);
                doctorCache.put(doctor.getId(), doctor);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return doctors;
    }
}
