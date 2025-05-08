package controller;

import dao.AppointmentDAO;
import dao.IAppointmentDAO;
import entities.Appointment;
import entities.Doctor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AppointmentManager implements IAppointmentManager {
    private final Connection conn;
    private final IAppointmentDAO appointmentDAO;
    private final DoctorManager doctorManager;

    public AppointmentManager() throws SQLException {
        conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/yourDatabaseName",
                "yourUsername",
                "yourPassword"
        );
        appointmentDAO = new AppointmentDAO(conn);
        doctorManager = new DoctorManager();
    }

    @Override
    public boolean scheduleAppointment(int patientId, int doctorId, LocalDateTime dateTime, 
                                     String type, String notes, double cost) {
        if (!isDoctorAvailable(doctorId, dateTime) || !isPatientAvailable(patientId, dateTime)) {
            return false;
        }

        Appointment appointment = new Appointment(patientId, doctorId, dateTime, type, notes, cost);
        if (!validateAppointment(appointment)) {
            return false;
        }

        return appointmentDAO.insertAppointment(appointment);
    }

    @Override
    public boolean updateAppointment(int appointmentId, Appointment newAppointment) {
        if (!appointmentExists(appointmentId)) {
            return false;
        }

        Appointment existingAppointment = getAppointmentById(appointmentId);
        if (existingAppointment.getStatus().equals("COMPLETED") || 
            existingAppointment.getStatus().equals("CANCELLED")) {
            return false;
        }

        return appointmentDAO.updateAppointment(appointmentId, newAppointment);
    }

    @Override
    public boolean cancelAppointment(int appointmentId) {
        if (!appointmentExists(appointmentId)) {
            return false;
        }

        Appointment appointment = getAppointmentById(appointmentId);
        if (appointment.getStatus().equals("COMPLETED") || 
            appointment.getStatus().equals("CANCELLED")) {
            return false;
        }

        return appointmentDAO.updateStatus(appointmentId, "CANCELLED");
    }

    @Override
    public boolean completeAppointment(int appointmentId) {
        if (!appointmentExists(appointmentId)) {
            return false;
        }

        Appointment appointment = getAppointmentById(appointmentId);
        if (!appointment.getStatus().equals("SCHEDULED")) {
            return false;
        }

        return appointmentDAO.updateStatus(appointmentId, "COMPLETED");
    }

    @Override
    public boolean markAsNoShow(int appointmentId) {
        if (!appointmentExists(appointmentId)) {
            return false;
        }

        Appointment appointment = getAppointmentById(appointmentId);
        if (!appointment.getStatus().equals("SCHEDULED")) {
            return false;
        }

        return appointmentDAO.updateStatus(appointmentId, "NO_SHOW");
    }

    @Override
    public Appointment getAppointmentById(int appointmentId) {
        return appointmentDAO.findById(appointmentId);
    }

    @Override
    public List<Appointment> getAppointmentsByPatientId(int patientId) {
        return appointmentDAO.findByPatientId(patientId);
    }

    @Override
    public List<Appointment> getAppointmentsByDoctorId(int doctorId) {
        return appointmentDAO.findByDoctorId(doctorId);
    }

    @Override
    public List<Appointment> getAllAppointments() {
        return appointmentDAO.findAll();
    }

    @Override
    public List<Appointment> getAppointmentsByStatus(String status) {
        return appointmentDAO.findByStatus(status);
    }

    @Override
    public List<Appointment> getAppointmentsByType(String type) {
        return appointmentDAO.findByType(type);
    }

    @Override
    public List<Appointment> getAppointmentsBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        return appointmentDAO.findBetweenDates(startDate, endDate);
    }

    @Override
    public boolean isDoctorAvailable(int doctorId, LocalDateTime dateTime) {
        List<Appointment> doctorAppointments = getAppointmentsByDoctorId(doctorId);
        return doctorAppointments.stream()
                .noneMatch(appointment -> 
                    appointment.getAppointmentDateTime().equals(dateTime) &&
                    !appointment.getStatus().equals("CANCELLED"));
    }

    @Override
    public boolean isPatientAvailable(int patientId, LocalDateTime dateTime) {
        List<Appointment> patientAppointments = getAppointmentsByPatientId(patientId);
        return patientAppointments.stream()
                .noneMatch(appointment -> 
                    appointment.getAppointmentDateTime().equals(dateTime) &&
                    !appointment.getStatus().equals("CANCELLED"));
    }

    @Override
    public List<LocalDateTime> getAvailableSlotsForDoctor(int doctorId, LocalDateTime date) {
        List<LocalDateTime> availableSlots = new ArrayList<>();
        LocalTime startTime = LocalTime.of(9, 0); // 9 AM
        LocalTime endTime = LocalTime.of(17, 0);  // 5 PM

        for (LocalTime time = startTime; time.isBefore(endTime); time = time.plusMinutes(30)) {
            LocalDateTime slot = date.toLocalDate().atTime(time);
            if (isDoctorAvailable(doctorId, slot)) {
                availableSlots.add(slot);
            }
        }

        return availableSlots;
    }

    @Override
    public boolean appointmentExists(int appointmentId) {
        return getAppointmentById(appointmentId) != null;
    }

    @Override
    public boolean validateAppointment(Appointment appointment) {
        if (appointment == null) return false;
        if (appointment.getPatientId() <= 0) return false;
        if (appointment.getDoctorId() <= 0) return false;
        if (appointment.getAppointmentDateTime() == null) return false;
        if (appointment.getAppointmentDateTime().isBefore(LocalDateTime.now())) return false;
        if (appointment.getType() == null || appointment.getType().isEmpty()) return false;
        if (appointment.getCost() < 0) return false;
        return true;
    }

    @Override
    public double calculateAppointmentCost(String type, int duration) {
        double baseCost = switch (type.toUpperCase()) {
            case "REGULAR" -> 100.0;
            case "URGENT" -> 200.0;
            case "FOLLOW_UP" -> 75.0;
            default -> 100.0;
        };

        // Add cost based on duration (in minutes)
        return baseCost + (duration / 30.0 * 50.0);
    }
} 