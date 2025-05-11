package com.hospital.dao;

import com.hospital.entities.Patient;
import com.hospital.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PatientDAO {


    public boolean insertPatient(Patient patient) {
        String sql = "INSERT INTO patients (name, age, gender, disease, phone, email, address, admission_date) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, patient.getName());
            stmt.setInt(2, patient.getAge());
            stmt.setString(3, patient.getGender());
            stmt.setString(4, patient.getDisease());
            stmt.setString(5, patient.getPhone());
            stmt.setString(6, patient.getEmail());
            stmt.setString(7, patient.getAddress());
            stmt.setDate(8, Date.valueOf(patient.getAdmissionDate()));

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        // Convert the auto-generated integer ID to string and set it in the patient object
                        String generatedId = String.valueOf(generatedKeys.getInt(1));
                        patient.setId(generatedId);
                        return true;
                    }
                }
            }
            return false;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean batchInsertPatients(List<Patient> patients) {
        if (patients == null || patients.isEmpty()) {
            return false;
        }

        String sql = "INSERT INTO patients (id, name, age, gender, disease, phone, email, address, admission_date) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            conn.setAutoCommit(false);
            int batchSize = 1000;
            int count = 0;

            for (Patient patient : patients) {
                stmt.setString(1, patient.getId());
                stmt.setString(2, patient.getName());
                stmt.setInt(3, patient.getAge());
                stmt.setString(4, patient.getGender());
                stmt.setString(5, patient.getDisease());
                stmt.setString(6, patient.getPhone());
                stmt.setString(7, patient.getEmail());
                stmt.setString(8, patient.getAddress());
                stmt.setDate(9, Date.valueOf(patient.getAdmissionDate()));
                stmt.addBatch();

                if (++count % batchSize == 0) {
                    stmt.executeBatch();
                }
            }

            if (count % batchSize != 0) {
                stmt.executeBatch();
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updatePatient(Patient patient) {
        String sql = "UPDATE patients SET name=?, age=?, gender=?, disease=?, phone=?, email=?, address=?, admission_date=? " +
                "WHERE id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, patient.getName());
            stmt.setInt(2, patient.getAge());
            stmt.setString(3, patient.getGender());
            stmt.setString(4, patient.getDisease());
            stmt.setString(5, patient.getPhone());
            stmt.setString(6, patient.getEmail());
            stmt.setString(7, patient.getAddress());
            stmt.setDate(8, Date.valueOf(patient.getAdmissionDate()));
            stmt.setString(9, patient.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deletePatientById(String id) {
        String sql = "DELETE FROM patients WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Patient findPatientById(String id) {
        String sql = "SELECT * FROM patients WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return extractPatient(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Patient> findAllPatients() {
        List<Patient> patients = new ArrayList<>();
        String sql = "SELECT * FROM patients";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                patients.add(extractPatient(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return patients;
    }

    private Patient extractPatient(ResultSet rs) throws SQLException {
        return new Patient(
                rs.getString("id"),
                rs.getString("name"),
                rs.getInt("age"),
                rs.getString("gender"),
                rs.getString("disease"),
                rs.getString("phone"),
                rs.getString("email"),
                rs.getString("address"),
                rs.getDate("admission_date").toLocalDate()
        );
    }
}
