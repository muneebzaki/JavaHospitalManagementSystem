package dao;

import entities.Appointment;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDAO implements IAppointmentDAO {
    private final Connection connection;

    public AppointmentDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean insertAppointment(Appointment appointment) {
        String sql = "INSERT INTO Appointments (patient_id, doctor_id, appointment_datetime, duration_minutes, " +
                    "status, type, notes, cost, created_at, updated_at) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, appointment.getPatientId());
            stmt.setInt(2, appointment.getDoctorId());
            stmt.setTimestamp(3, Timestamp.valueOf(appointment.getAppointmentDateTime()));
            stmt.setLong(4, appointment.getDuration().toMinutes());
            stmt.setString(5, appointment.getStatus());
            stmt.setString(6, appointment.getType());
            stmt.setString(7, appointment.getNotes());
            stmt.setDouble(8, appointment.getCost());
            stmt.setTimestamp(9, Timestamp.valueOf(appointment.getCreatedAt()));
            stmt.setTimestamp(10, Timestamp.valueOf(appointment.getUpdatedAt()));
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        appointment.setAppointmentId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateAppointment(int appointmentId, Appointment newAppointment) {
        String sql = "UPDATE Appointments SET patient_id=?, doctor_id=?, appointment_datetime=?, " +
                    "duration_minutes=?, status=?, type=?, notes=?, cost=?, updated_at=? " +
                    "WHERE appointment_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, newAppointment.getPatientId());
            stmt.setInt(2, newAppointment.getDoctorId());
            stmt.setTimestamp(3, Timestamp.valueOf(newAppointment.getAppointmentDateTime()));
            stmt.setLong(4, newAppointment.getDuration().toMinutes());
            stmt.setString(5, newAppointment.getStatus());
            stmt.setString(6, newAppointment.getType());
            stmt.setString(7, newAppointment.getNotes());
            stmt.setDouble(8, newAppointment.getCost());
            stmt.setTimestamp(9, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setInt(10, appointmentId);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateStatus(int appointmentId, String status) {
        String sql = "UPDATE Appointments SET status=?, updated_at=? WHERE appointment_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setInt(3, appointmentId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Appointment findById(int appointmentId) {
        String sql = "SELECT * FROM Appointments WHERE appointment_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, appointmentId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return extractAppointment(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Appointment> findAll() {
        String sql = "SELECT * FROM Appointments ORDER BY appointment_datetime DESC";
        List<Appointment> appointments = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                appointments.add(extractAppointment(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointments;
    }

    @Override
    public List<Appointment> findByPatientId(int patientId) {
        String sql = "SELECT * FROM Appointments WHERE patient_id=? ORDER BY appointment_datetime DESC";
        List<Appointment> appointments = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, patientId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                appointments.add(extractAppointment(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointments;
    }

    @Override
    public List<Appointment> findByDoctorId(int doctorId) {
        String sql = "SELECT * FROM Appointments WHERE doctor_id=? ORDER BY appointment_datetime DESC";
        List<Appointment> appointments = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, doctorId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                appointments.add(extractAppointment(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointments;
    }

    @Override
    public List<Appointment> findByStatus(String status) {
        String sql = "SELECT * FROM Appointments WHERE status=? ORDER BY appointment_datetime DESC";
        List<Appointment> appointments = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, status);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                appointments.add(extractAppointment(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointments;
    }

    @Override
    public List<Appointment> findByType(String type) {
        String sql = "SELECT * FROM Appointments WHERE type=? ORDER BY appointment_datetime DESC";
        List<Appointment> appointments = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, type);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                appointments.add(extractAppointment(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointments;
    }

    @Override
    public List<Appointment> findByDate(LocalDateTime date) {
        String sql = "SELECT * FROM Appointments WHERE DATE(appointment_datetime)=? ORDER BY appointment_datetime";
        List<Appointment> appointments = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDate(1, java.sql.Date.valueOf(date.toLocalDate()));
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                appointments.add(extractAppointment(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointments;
    }

    @Override
    public List<Appointment> findBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        String sql = "SELECT * FROM Appointments WHERE appointment_datetime BETWEEN ? AND ? ORDER BY appointment_datetime";
        List<Appointment> appointments = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setTimestamp(1, Timestamp.valueOf(startDate));
            stmt.setTimestamp(2, Timestamp.valueOf(endDate));
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                appointments.add(extractAppointment(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointments;
    }

    private Appointment extractAppointment(ResultSet rs) throws SQLException {
        int appointmentId = rs.getInt("appointment_id");
        int patientId = rs.getInt("patient_id");
        int doctorId = rs.getInt("doctor_id");
        LocalDateTime appointmentDateTime = rs.getTimestamp("appointment_datetime").toLocalDateTime();
        Duration duration = Duration.ofMinutes(rs.getLong("duration_minutes"));
        String status = rs.getString("status");
        String type = rs.getString("type");
        String notes = rs.getString("notes");
        double cost = rs.getDouble("cost");
        LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();
        LocalDateTime updatedAt = rs.getTimestamp("updated_at").toLocalDateTime();

        Appointment appointment = new Appointment(appointmentId, patientId, doctorId, appointmentDateTime, 
            duration, status, type, notes, cost);
        return appointment;
    }
} 