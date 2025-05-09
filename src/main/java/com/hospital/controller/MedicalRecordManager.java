package com.hospital.controller;

import com.hospital.dao.IMedicalRecordDAO;
import com.hospital.dao.MedicalRecordDAO;
import com.hospital.entities.MedicalRecord;
import java.time.LocalDateTime;
import java.util.List;

public class MedicalRecordManager {
    private final IMedicalRecordDAO medicalRecordDAO;

    public MedicalRecordManager() {
        this.medicalRecordDAO = new MedicalRecordDAO();
    }

    public boolean addRecord(MedicalRecord record) {
        if (record == null) {
            return false;
        }
        return medicalRecordDAO.insertRecord(record);
    }

    public boolean updateRecord(MedicalRecord record) {
        if (record == null || record.getRecordId() <= 0) {
            return false;
        }
        return medicalRecordDAO.updateRecord(record);
    }

    public boolean deleteRecord(int recordId) {
        if (recordId <= 0) {
            return false;
        }
        return medicalRecordDAO.deleteRecord(recordId);
    }

    public MedicalRecord getRecordById(int recordId) {
        if (recordId <= 0) {
            return null;
        }
        return medicalRecordDAO.findById(recordId);
    }

    public List<MedicalRecord> getRecordsByPatientId(int patientId) {
        if (patientId <= 0) {
            return List.of();
        }
        return medicalRecordDAO.findByPatientId(patientId);
    }

    public List<MedicalRecord> getRecordsByDoctorId(int doctorId) {
        if (doctorId <= 0) {
            return List.of();
        }
        return medicalRecordDAO.findByDoctorId(doctorId);
    }

    public List<MedicalRecord> getAllRecords() {
        return medicalRecordDAO.findAll();
    }

    public List<MedicalRecord> getRecordsByStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            return List.of();
        }
        return medicalRecordDAO.findByStatus(status);
    }

    public List<MedicalRecord> getRecordsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate == null || endDate == null || startDate.isAfter(endDate)) {
            return List.of();
        }
        return medicalRecordDAO.findByDateRange(startDate, endDate);
    }

    public boolean archiveRecord(int recordId) {
        if (recordId <= 0) {
            return false;
        }
        return medicalRecordDAO.archiveRecord(recordId);
    }

    public boolean reactivateRecord(int recordId) {
        if (recordId <= 0) {
            return false;
        }
        return medicalRecordDAO.reactivateRecord(recordId);
    }
} 