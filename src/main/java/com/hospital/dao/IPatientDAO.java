package com.hospital.dao;

import com.hospital.entities.Patient;

import java.util.List;

public interface IPatientDAO {
    boolean insertPatient(Patient patient);
    boolean updatePatient(Patient patient);
    boolean deletePatientById(String id);
    Patient findPatientById(String id);
    List<Patient> findAllPatients();
}
