package com.hospital.controller;

import com.hospital.dao.IPatientDAO;
import com.hospital.dao.PatientDAO;
import com.hospital.entities.Patient;

import java.util.List;

public class PatientManager {
    private final IPatientDAO patientDAO;

    public PatientManager() {
        this.patientDAO = new PatientDAO();  // Uses JDBC now
    }

    public boolean addPatient(Patient patient) {
        return patientDAO.insertPatient(patient);
    }

    public boolean removePatientById(String id) {
        return patientDAO.deletePatientById(id);
    }

    public Patient findPatientById(String id) {
        return patientDAO.findPatientById(id);
    }

    public boolean updatePatient(Patient updated) {
        return patientDAO.updatePatient(updated);
    }

    public List<Patient> getAllPatients() {
        return patientDAO.findAllPatients();
    }

    public int getPatientCount() {
        return getAllPatients().size();
    }

    public boolean patientExists(String id) {
        return findPatientById(id) != null;
    }

    public boolean clearAllPatients() {
        List<Patient> all = getAllPatients();
        boolean success = true;
        for (Patient p : all) {
            success &= removePatientById(p.getId());
        }
        return success;
    }

    public void listAllPatients() {
        List<Patient> all = getAllPatients();
        if (all.isEmpty()) {
            System.out.println("No patients found.");
        } else {
            all.forEach(System.out::println);
        }
    }
}
