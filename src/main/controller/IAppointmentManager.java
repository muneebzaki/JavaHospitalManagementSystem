package controller;

import entities.Appointment;
import java.time.LocalDateTime;
import java.util.List;

public interface IAppointmentManager {
    // Core Appointment Operations
    boolean scheduleAppointment(int patientId, int doctorId, LocalDateTime dateTime, String type, String notes, double cost);
    boolean updateAppointment(int appointmentId, Appointment newAppointment);
    boolean cancelAppointment(int appointmentId);
    boolean completeAppointment(int appointmentId);
    boolean markAsNoShow(int appointmentId);

    // Appointment Retrieval Operations
    Appointment getAppointmentById(int appointmentId);
    List<Appointment> getAppointmentsByPatientId(int patientId);
    List<Appointment> getAppointmentsByDoctorId(int doctorId);
    List<Appointment> getAllAppointments();
    List<Appointment> getAppointmentsByStatus(String status);
    List<Appointment> getAppointmentsByType(String type);
    List<Appointment> getAppointmentsBetweenDates(LocalDateTime startDate, LocalDateTime endDate);

    // Availability Checking
    boolean isDoctorAvailable(int doctorId, LocalDateTime dateTime);
    boolean isPatientAvailable(int patientId, LocalDateTime dateTime);
    List<LocalDateTime> getAvailableSlotsForDoctor(int doctorId, LocalDateTime date);

    // Utility Operations
    boolean appointmentExists(int appointmentId);
    boolean validateAppointment(Appointment appointment);
    double calculateAppointmentCost(String type, int duration);
} 