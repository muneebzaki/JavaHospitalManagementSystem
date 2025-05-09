package com.hospital.dao;

import com.hospital.entities.MedicalRecord;
import com.hospital.util.DatabaseConnection;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MedicalRecordDAO implements IMedicalRecordDAO {
    private final Connection connection;

    public MedicalRecordDAO() {
        this.connection = DatabaseConnection.getConnection();
    }

    @Override
    public boolean insertRecord(MedicalRecord record) {
        String sql = "INSERT INTO medical_records (patient_id, doctor_id, record_date, diagnosis, treatment, prescription, notes, status) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, record.getPatientId());
            stmt.setInt(2, record.getDoctorId());
            stmt.setTimestamp(3, Timestamp.valueOf(record.getRecordDate()));
            stmt.setString(4, record.getDiagnosis());
            stmt.setString(5, record.getTreatment());
            stmt.setString(6, record.getPrescription());
            stmt.setString(7, record.getNotes());
            stmt.setString(8, record.getStatus());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        record.setRecordId(rs.getInt(1));
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateRecord(MedicalRecord record) {
        String sql = "UPDATE medical_records SET patient_id=?, doctor_id=?, record_date=?, diagnosis=?, " +
                    "treatment=?, prescription=?, notes=?, status=? WHERE record_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, record.getPatientId());
            stmt.setInt(2, record.getDoctorId());
            stmt.setTimestamp(3, Timestamp.valueOf(record.getRecordDate()));
            stmt.setString(4, record.getDiagnosis());
            stmt.setString(5, record.getTreatment());
            stmt.setString(6, record.getPrescription());
            stmt.setString(7, record.getNotes());
            stmt.setString(8, record.getStatus());
            stmt.setInt(9, record.getRecordId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteRecord(int recordId) {
        String sql = "DELETE FROM medical_records WHERE record_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, recordId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public MedicalRecord findById(int recordId) {
        String sql = "SELECT * FROM medical_records WHERE record_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, recordId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractRecordFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<MedicalRecord> findByPatientId(int patientId) {
        List<MedicalRecord> records = new ArrayList<>();
        String sql = "SELECT * FROM medical_records WHERE patient_id=? ORDER BY record_date DESC";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, patientId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    records.add(extractRecordFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return records;
    }

    @Override
    public List<MedicalRecord> findByDoctorId(int doctorId) {
        List<MedicalRecord> records = new ArrayList<>();
        String sql = "SELECT * FROM medical_records WHERE doctor_id=? ORDER BY record_date DESC";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, doctorId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    records.add(extractRecordFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return records;
    }

    @Override
    public List<MedicalRecord> findAll() {
        List<MedicalRecord> records = new ArrayList<>();
        String sql = "SELECT * FROM medical_records ORDER BY record_date DESC";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                records.add(extractRecordFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return records;
    }

    @Override
    public List<MedicalRecord> findByStatus(String status) {
        List<MedicalRecord> records = new ArrayList<>();
        String sql = "SELECT * FROM medical_records WHERE status=? ORDER BY record_date DESC";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, status);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    records.add(extractRecordFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return records;
    }

    @Override
    public List<MedicalRecord> findByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        List<MedicalRecord> records = new ArrayList<>();
        String sql = "SELECT * FROM medical_records WHERE record_date BETWEEN ? AND ? ORDER BY record_date DESC";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setTimestamp(1, Timestamp.valueOf(startDate));
            stmt.setTimestamp(2, Timestamp.valueOf(endDate));
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    records.add(extractRecordFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return records;
    }

    @Override
    public boolean archiveRecord(int recordId) {
        String sql = "UPDATE medical_records SET status='ARCHIVED' WHERE record_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, recordId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean reactivateRecord(int recordId) {
        String sql = "UPDATE medical_records SET status='ACTIVE' WHERE record_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, recordId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private MedicalRecord extractRecordFromResultSet(ResultSet rs) throws SQLException {
        return new MedicalRecord(
            rs.getInt("record_id"),
            rs.getInt("patient_id"),
            rs.getInt("doctor_id"),
            rs.getTimestamp("record_date").toLocalDateTime(),
            rs.getString("diagnosis"),
            rs.getString("treatment"),
            rs.getString("prescription"),
            rs.getString("notes"),
            rs.getString("status")
        );
    }
} 