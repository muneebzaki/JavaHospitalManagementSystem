package controller;

import dao.AppointmentDAO;
import dao.IAppointmentDAO;
import entities.Appointment;
import entities.Doctor;
import entities.Patient;
import entities.Appointment.AppointmentStatus;
import entities.Appointment.AppointmentType;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AppointmentManager implements IAppointmentManager {
    private final Connection conn;
    private final IAppointmentDAO appointmentDAO;
    private final DoctorManager doctorManager;
    private final PatientManager patientManager;
    private final BillManager billManager;

    public AppointmentManager() throws SQLException {
        // TODO: Move database connection to a separate configuration class
        String url = "jdbc:mysql://localhost:3306/hospital_management";
        String username = "root";
        String password = "";
        
        try {
            conn = DriverManager.getConnection(url, username, password);
            appointmentDAO = new AppointmentDAO(conn);
            doctorManager = new DoctorManager();
            patientManager = new PatientManager();
            billManager = new BillManager();
        } catch (SQLException e) {
            throw new SQLException("Failed to initialize AppointmentManager: " + e.getMessage());
        }
    }

    @Override
    public boolean scheduleAppointment(int patientId, int doctorId, LocalDateTime dateTime, 
                                     String type, String notes, double cost) {
        try {
            // Validate inputs
            if (!validateAppointmentInputs(patientId, doctorId, dateTime, type)) {
                return false;
            }

            // Check doctor and patient availability
            if (!isDoctorAvailable(doctorId, dateTime) || !isPatientAvailable(patientId, dateTime)) {
                return false;
            }

            // Set default duration based on appointment type
            Duration duration = getDefaultDuration(type);

            // Create and validate appointment
            Appointment appointment = new Appointment(patientId, doctorId, dateTime, duration, type, notes, cost);
            if (!validateAppointment(appointment)) {
                return false;
            }

            // Schedule the appointment
            boolean success = appointmentDAO.insertAppointment(appointment);
            if (success) {
                // Update doctor's schedule
                doctorManager.scheduleDoctorAppointment(doctorId, dateTime);
            }
            return success;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateAppointment(int appointmentId, Appointment newAppointment) {
        try {
            if (!appointmentExists(appointmentId)) {
                return false;
            }

            Appointment existingAppointment = getAppointmentById(appointmentId);
            if (existingAppointment.isCompleted() || existingAppointment.isCancelled()) {
                return false;
            }

            // Validate the new appointment
            if (!validateAppointment(newAppointment)) {
                return false;
            }

            // Check for scheduling conflicts
            if (isSchedulingConflict(newAppointment)) {
                return false;
            }

            return appointmentDAO.updateAppointment(appointmentId, newAppointment);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean cancelAppointment(int appointmentId) {
        try {
            if (!appointmentExists(appointmentId)) {
                return false;
            }

            Appointment appointment = getAppointmentById(appointmentId);
            if (appointment.isCompleted() || appointment.isCancelled()) {
                return false;
            }

            boolean success = appointmentDAO.updateStatus(appointmentId, AppointmentStatus.CANCELLED.name());
            if (success) {
                // Update doctor's schedule
                doctorManager.cancelDoctorAppointment(appointment.getDoctorId(), appointment.getAppointmentDateTime());
            }
            return success;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean completeAppointment(int appointmentId) {
        try {
            if (!appointmentExists(appointmentId)) {
                return false;
            }

            Appointment appointment = getAppointmentById(appointmentId);
            if (!appointment.isScheduled()) {
                return false;
            }

            boolean success = appointmentDAO.updateStatus(appointmentId, AppointmentStatus.COMPLETED.name());
            if (success) {
                // Generate bill for the completed appointment
                billManager.generateBill(appointment.getPatientId(), "PENDING", "APPOINTMENT");
            }
            return success;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean markAsNoShow(int appointmentId) {
        try {
            if (!appointmentExists(appointmentId)) {
                return false;
            }

            Appointment appointment = getAppointmentById(appointmentId);
            if (!appointment.isScheduled()) {
                return false;
            }

            return appointmentDAO.updateStatus(appointmentId, AppointmentStatus.NO_SHOW.name());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Appointment getAppointmentById(int appointmentId) {
        try {
            return appointmentDAO.findById(appointmentId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Appointment> getAppointmentsByPatientId(int patientId) {
        try {
            return appointmentDAO.findByPatientId(patientId);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public List<Appointment> getAppointmentsByDoctorId(int doctorId) {
        try {
            return appointmentDAO.findByDoctorId(doctorId);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public List<Appointment> getAllAppointments() {
        try {
            return appointmentDAO.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public List<Appointment> getAppointmentsByStatus(String status) {
        try {
            AppointmentStatus.valueOf(status.toUpperCase());
            return appointmentDAO.findByStatus(status);
        } catch (IllegalArgumentException e) {
            return new ArrayList<>();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public List<Appointment> getAppointmentsByType(String type) {
        try {
            AppointmentType.valueOf(type.toUpperCase());
            return appointmentDAO.findByType(type);
        } catch (IllegalArgumentException e) {
            return new ArrayList<>();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public List<Appointment> getAppointmentsBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        try {
            if (startDate == null || endDate == null || startDate.isAfter(endDate)) {
                return new ArrayList<>();
            }
            return appointmentDAO.findBetweenDates(startDate, endDate);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public boolean isDoctorAvailable(int doctorId, LocalDateTime dateTime) {
        try {
            List<Appointment> doctorAppointments = getAppointmentsByDoctorId(doctorId);
            return doctorAppointments.stream()
                    .noneMatch(appointment -> 
                        appointment.isOverlapping(new Appointment(0, 0, doctorId, dateTime, 
                            Duration.ofMinutes(30), AppointmentType.REGULAR.name(), "", 0.0)));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean isPatientAvailable(int patientId, LocalDateTime dateTime) {
        try {
            List<Appointment> patientAppointments = getAppointmentsByPatientId(patientId);
            return patientAppointments.stream()
                    .noneMatch(appointment -> 
                        appointment.isOverlapping(new Appointment(0, patientId, 0, dateTime, 
                            Duration.ofMinutes(30), AppointmentType.REGULAR.name(), "", 0.0)));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<LocalDateTime> getAvailableSlotsForDoctor(int doctorId, LocalDateTime date) {
        try {
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
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public boolean appointmentExists(int appointmentId) {
        try {
            return getAppointmentById(appointmentId) != null;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean validateAppointment(Appointment appointment) {
        try {
            if (appointment == null) return false;
            if (appointment.getPatientId() <= 0) return false;
            if (appointment.getDoctorId() <= 0) return false;
            if (appointment.getAppointmentDateTime() == null) return false;
            if (appointment.getAppointmentDateTime().isBefore(LocalDateTime.now())) return false;
            if (appointment.getDuration() == null || appointment.getDuration().isNegative() || appointment.getDuration().isZero()) return false;
            if (appointment.getType() == null || appointment.getType().isEmpty()) return false;
            if (appointment.getCost() < 0) return false;
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public double calculateAppointmentCost(String type, int duration) {
        try {
            double baseCost = switch (type.toUpperCase()) {
                case "REGULAR" -> 100.0;
                case "URGENT" -> 200.0;
                case "FOLLOW_UP" -> 75.0;
                default -> 100.0;
            };

            // Add cost based on duration (in minutes)
            return baseCost + (duration / 30.0 * 50.0);
        } catch (Exception e) {
            e.printStackTrace();
            return 0.0;
        }
    }

    // Private helper methods
    private boolean validateAppointmentInputs(int patientId, int doctorId, LocalDateTime dateTime, String type) {
        try {
            if (patientId <= 0 || doctorId <= 0 || dateTime == null || type == null) {
                return false;
            }

            AppointmentType.valueOf(type.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private Duration getDefaultDuration(String type) {
        try {
            return switch (type.toUpperCase()) {
                case "REGULAR" -> Duration.ofMinutes(30);
                case "URGENT" -> Duration.ofMinutes(60);
                case "FOLLOW_UP" -> Duration.ofMinutes(20);
                default -> Duration.ofMinutes(30);
            };
        } catch (Exception e) {
            e.printStackTrace();
            return Duration.ofMinutes(30);
        }
    }

    private boolean isSchedulingConflict(Appointment appointment) {
        try {
            List<Appointment> doctorAppointments = getAppointmentsByDoctorId(appointment.getDoctorId());
            List<Appointment> patientAppointments = getAppointmentsByPatientId(appointment.getPatientId());

            return doctorAppointments.stream().anyMatch(a -> a.isOverlapping(appointment)) ||
                   patientAppointments.stream().anyMatch(a -> a.isOverlapping(appointment));
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }
} 