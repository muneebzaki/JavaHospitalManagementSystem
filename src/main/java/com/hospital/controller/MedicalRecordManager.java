package com.hospital.controller;

import com.hospital.dao.MedicalRecordDAO;
import com.hospital.entities.MedicalRecord;

import java.util.List;

public class MedicalRecordManager {
    private final MedicalRecordDAO medicalRecordDAO;

    public MedicalRecordManager() {
        this.medicalRecordDAO = new MedicalRecordDAO();
    }

    public boolean addRecord(MedicalRecord record) {
        return record != null && medicalRecordDAO.insertRecord(record);
    }

    public List<MedicalRecord> getRecordsByPatientId(int patientId) {
        return medicalRecordDAO.findByPatientId(patientId);
    }
}
