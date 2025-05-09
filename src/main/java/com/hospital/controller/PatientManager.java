package com.hospital.controller;

import com.hospital.entities.Patient;

import java.util.ArrayList;
import java.util.List;

public class PatientManager {
    private List<Patient> patients;

    public PatientManager() {
        this.patients = new ArrayList<>();
    }

    // ✅ Add patient
    public void addPatient(Patient patient) {
        patients.add(patient);
    }

    // ✅ Remove patient by ID
    public boolean removePatientById(String id) {
        return patients.removeIf(p -> p.getId().equalsIgnoreCase(id));
    }

    // ✅ Find patient by ID
    public Patient findPatientById(String id) {
        for (Patient p : patients) {
            if (p.getId().equalsIgnoreCase(id)) {
                return p;
            }
        }
        return null;
    }

    // ✅ Update patient details
    public boolean updatePatient(Patient updated) {
        for (int i = 0; i < patients.size(); i++) {
            if (patients.get(i).getId().equalsIgnoreCase(updated.getId())) {
                patients.set(i, updated);
                return true;
            }
        }
        return false;
    }

    // ✅ Get patient count
    public int getPatientCount() {
        return patients.size();
    }

    // ✅ List all patients
    public void listAllPatients() {
        if (patients.isEmpty()) {
            System.out.println("No patients found.");
        } else {
            for (Patient p : patients) {
                System.out.println(p);
            }
        }
    }

    // ✅ Check if a patient exists
    public boolean patientExists(String id) {
        return findPatientById(id) != null;
    }

    // ✅ Clear all patients (for testing or reset)
    public void clearAllPatients() {
        patients.clear();
    }
}
