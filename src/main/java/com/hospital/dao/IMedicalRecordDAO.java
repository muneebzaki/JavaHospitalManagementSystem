package com.hospital.dao;

import com.hospital.entities.MedicalRecord;
import java.time.LocalDateTime;
import java.util.List;

public interface IMedicalRecordDAO {
    boolean insertRecord(MedicalRecord record);
    boolean updateRecord(MedicalRecord record);
    boolean deleteRecord(int recordId);
    MedicalRecord findById(int recordId);
    List<MedicalRecord> findByPatientId(int patientId);
    List<MedicalRecord> findByDoctorId(int doctorId);
    List<MedicalRecord> findAll();
    List<MedicalRecord> findByStatus(String status);
    List<MedicalRecord> findByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    boolean archiveRecord(int recordId);
    boolean reactivateRecord(int recordId);
} 