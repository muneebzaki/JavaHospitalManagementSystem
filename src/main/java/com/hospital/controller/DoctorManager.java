package com.hospital.controller;

import com.hospital.dao.IDoctorDAO;
import com.hospital.dao.DoctorDAO;
import com.hospital.entities.Doctor;

import java.time.LocalDateTime;
import java.util.List;

public class DoctorManager {
    private final IDoctorDAO doctorDAO;

    public DoctorManager() {
        this.doctorDAO = new DoctorDAO();
    }

    public boolean addDoctor(Doctor doctor) {
        return doctorDAO.insertDoctor(doctor);
    }

    public boolean updateDoctor(Doctor doctor) {
        return doctorDAO.updateDoctor(doctor);
    }

    public boolean removeDoctor(int id) {
        return doctorDAO.deleteDoctor(id);
    }

    public Doctor findDoctorById(int id) {
        return doctorDAO.findById(id);
    }

    public List<Doctor> getAllDoctors() {
        return doctorDAO.findAll();
    }

    public List<Doctor> getDoctorsBySpecialization(String specialization) {
        return doctorDAO.findBySpecialization(specialization);
    }

    public List<Doctor> getAvailableDoctors() {
        return doctorDAO.findAvailable();
    }

    public boolean doctorExists(int id) {
        return findDoctorById(id) != null;
    }

    public boolean scheduleDoctorAppointment(int doctorId, LocalDateTime appointmentTime) {
        Doctor doctor = findDoctorById(doctorId);
        if (doctor != null && doctor.isAvailable() && !doctor.hasAppointmentAt(appointmentTime)) {
            doctor.addToSchedule(appointmentTime);
            return updateDoctor(doctor);
        }
        return false;
    }

    public boolean cancelDoctorAppointment(int doctorId, LocalDateTime appointmentTime) {
        Doctor doctor = findDoctorById(doctorId);
        if (doctor != null && doctor.hasAppointmentAt(appointmentTime)) {
            doctor.removeFromSchedule(appointmentTime);
            return updateDoctor(doctor);
        }
        return false;
    }

    public void listAllDoctors() {
        List<Doctor> all = getAllDoctors();
        if (all.isEmpty()) {
            System.out.println("No doctors found.");
        } else {
            all.forEach(System.out::println);
        }
    }
} 