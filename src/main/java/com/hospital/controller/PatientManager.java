package com.hospital.controller;

import com.hospital.dao.PatientDAO;
import com.hospital.entities.Patient;
import java.util.List;

public class PatientManager {
    private final PatientDAO patientDAO;

    public PatientManager() {
        this.patientDAO = new PatientDAO();
    }

    public PatientManager(PatientDAO patientDAO) {
        this.patientDAO = patientDAO;
    }

    public boolean addPatient(Patient patient) {
        return patientDAO.insertPatient(patient);
    }

    public boolean updatePatient(Patient patient) {
        return patientDAO.updatePatientById(patient.getId(), patient);
    }

    public boolean removePatientById(Integer id) {
        return patientDAO.deletePatientById(id);
    }

    public Patient getPatientById(Integer id) {
        return id == null ? null : patientDAO.findPatientById(id);
    }

    public List<Patient> getAllPatients() {
        return patientDAO.findAllPatients();
    }

    public boolean patientExists(Integer id) {
        return getPatientById(id) != null;
    }

    public int getPatientCount() {
        return getAllPatients().size();
    }
}