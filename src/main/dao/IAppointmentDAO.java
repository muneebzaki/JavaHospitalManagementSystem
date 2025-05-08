package dao;

import entities.Appointment;
import java.time.LocalDateTime;
import java.util.List;

public interface IAppointmentDAO {
    boolean insertAppointment(Appointment appointment);
    boolean updateAppointment(int appointmentId, Appointment newAppointment);
    boolean updateStatus(int appointmentId, String status);
    Appointment findById(int appointmentId);
    List<Appointment> findAll();
    List<Appointment> findByPatientId(int patientId);
    List<Appointment> findByDoctorId(int doctorId);
    List<Appointment> findByStatus(String status);
    List<Appointment> findByType(String type);
    List<Appointment> findByDate(LocalDateTime date);
    List<Appointment> findBetweenDates(LocalDateTime startDate, LocalDateTime endDate);
} 