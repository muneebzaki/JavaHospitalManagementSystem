package com.hospital.controller;

import com.hospital.dao.PatientDAO;
import com.hospital.entities.Patient;

import java.util.List;

public class PatientManager {
    private final PatientDAO patientDAO;

    public PatientManager() {
        this.patientDAO = new PatientDAO();
    }

    public boolean addPatient(Patient patient) {
        return patient != null && patientDAO.insertPatient(patient);
    }

    public boolean updatePatient(Patient updated) {
        return updated != null && patientDAO.updatePatient(updated);
    }

    public boolean removePatientById(String id) {
        return id != null && !id.isBlank() && patientDAO.deletePatientById(id);
    }

    public Patient getPatientById(String id) {
        return id == null || id.isBlank() ? null : patientDAO.findPatientById(id);
    }

    public List<Patient> getAllPatients() {
        return patientDAO.findAllPatients();
    }

    public boolean patientExists(String id) {
        return getPatientById(id) != null;
    }

    public int getPatientCount() {
        return getAllPatients().size();
    }
}
