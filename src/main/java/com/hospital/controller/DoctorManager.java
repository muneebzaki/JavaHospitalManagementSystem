package com.hospital.controller;

import com.hospital.dao.DoctorDAO;
import com.hospital.entities.Doctor;

import java.util.List;

public class DoctorManager {
    private final DoctorDAO doctorDAO;

    // Default constructor
    public DoctorManager() {
        this.doctorDAO = new DoctorDAO();
    }

    // Constructor for dependency injection (used in testing)
    public DoctorManager(DoctorDAO doctorDAO) {
        this.doctorDAO = doctorDAO;
    }

    public boolean addDoctor(Doctor doctor) {
        return doctor != null && doctorDAO.insertDoctor(doctor);
    }

    public boolean updateDoctor(Doctor doctor) {
        return doctor != null && doctorDAO.updateDoctor(doctor);
    }

    public boolean removeDoctor(int id) {
        return id > 0 && doctorDAO.deleteDoctor(id);
    }

    public Doctor getDoctorById(int id) {
        return id <= 0 ? null : doctorDAO.findById(id);
    }

    public List<Doctor> getAllDoctors() {
        return doctorDAO.findAll();
    }

    public boolean doctorExists(int id) {
        return getDoctorById(id) != null;
    }
}
