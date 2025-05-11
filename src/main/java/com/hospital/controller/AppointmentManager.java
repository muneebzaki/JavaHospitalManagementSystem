package com.hospital.controller;

import com.hospital.dao.AppointmentDAO;
import com.hospital.entities.Appointment;
import com.hospital.entities.Appointment.Status;

import java.time.LocalDateTime;
import java.time.Duration;
import java.util.List;

public class AppointmentManager {
    private final AppointmentDAO dao;

    // Constructor for production use
    public AppointmentManager() {
        this.dao = new AppointmentDAO();
    }

    // Constructor for testing with mock DAO
    public AppointmentManager(AppointmentDAO dao) {
        this.dao = dao;
    }

    public boolean createAppointment(int patientId, int doctorId, LocalDateTime dateTime, Duration duration) {
        Appointment a = new Appointment(patientId, doctorId, dateTime, duration);
        return dao.insert(a);
    }

    public Appointment getAppointment(int id) {
        return dao.findById(id);
    }

    public List<Appointment> getAppointmentsForPatient(int patientId) {
        return dao.findByPatientId(patientId);
    }

    public boolean markAsCompleted(int id) {
        return dao.updateStatus(id, Status.COMPLETED);
    }

    public boolean cancelAppointment(int id) {
        return dao.updateStatus(id, Status.CANCELLED);
    }

    public List<Appointment> getAllAppointments() {
        return dao.findAll();
    }
}
