package com.hospital.dao;

import com.hospital.entities.Doctor;
import java.util.List;

public interface IDoctorDAO {
    boolean insertDoctor(Doctor doctor);
    boolean updateDoctor(Doctor doctor);
    boolean deleteDoctor(int id);
    Doctor findById(int id);
    List<Doctor> findAll();
    List<Doctor> findBySpecialization(String specialization);
    List<Doctor> findAvailable();
} 