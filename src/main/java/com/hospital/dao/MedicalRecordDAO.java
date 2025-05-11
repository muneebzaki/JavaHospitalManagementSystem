package com.hospital.dao;

import com.hospital.entities.MedicalRecord;
import com.hospital.util.DBConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MedicalRecordDAO {

    public boolean insertRecord(MedicalRecord record) {
        String sql = "INSERT INTO medical_records (patient_id, doctor_id, record_date, diagnosis, treatment) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, record.getPatientId());
            stmt.setInt(2, record.getDoctorId());
            stmt.setTimestamp(3, Timestamp.valueOf(record.getDate()));
            stmt.setString(4, record.getDiagnosis());
            stmt.setString(5, record.getTreatment());

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        record.setRecordId(rs.getInt(1));
                    }
                }
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Failed to insert medical record:");
            e.printStackTrace();
        }
        return false;
    }

    public List<MedicalRecord> findByPatientId(int patientId) {
        List<MedicalRecord> records = new ArrayList<>();
        String sql = "SELECT * FROM medical_records WHERE patient_id = ? ORDER BY record_date DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, patientId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                records.add(extractRecord(rs));
            }

        } catch (SQLException e) {
            System.err.println("Failed to retrieve records:");
            e.printStackTrace();
        }

        return records;
    }

    private MedicalRecord extractRecord(ResultSet rs) throws SQLException {
        return new MedicalRecord(
                rs.getInt("record_id"),
                rs.getInt("patient_id"),
                rs.getInt("doctor_id"),
                rs.getTimestamp("record_date").toLocalDateTime(),
                rs.getString("diagnosis"),
                rs.getString("treatment")
        );
    }
}
