package com.hospital.entities;

import java.time.LocalDateTime;
import java.time.Duration;

public class Appointment {
    private int appointmentId;
    private int patientId;
    private int doctorId;
    private LocalDateTime appointmentDateTime;
    private Duration duration;
    private String status; // SCHEDULED, COMPLETED, CANCELLED, NO_SHOW
    private String type; // REGULAR, URGENT, FOLLOW_UP
    private String notes;
    private double cost;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Enum for appointment status
    public enum AppointmentStatus {
        SCHEDULED,
        COMPLETED,
        CANCELLED,
        NO_SHOW
    }

    // Enum for appointment type
    public enum AppointmentType {
        REGULAR,
        URGENT,
        FOLLOW_UP
    }

    public Appointment(int appointmentId, int patientId, int doctorId, LocalDateTime appointmentDateTime, 
                      Duration duration, String status, String type, String notes, double cost) {
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.appointmentDateTime = appointmentDateTime;
        this.duration = duration;
        this.status = status;
        this.type = type;
        this.notes = notes;
        this.cost = cost;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Constructor for new appointments (without ID)
    public Appointment(int patientId, int doctorId, LocalDateTime appointmentDateTime, 
                      Duration duration, String type, String notes, double cost) {
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.appointmentDateTime = appointmentDateTime;
        this.duration = duration;
        this.status = AppointmentStatus.SCHEDULED.name();
        this.type = type;
        this.notes = notes;
        this.cost = cost;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Getters
    public int getAppointmentId() { return appointmentId; }
    public int getPatientId() { return patientId; }
    public int getDoctorId() { return doctorId; }
    public LocalDateTime getAppointmentDateTime() { return appointmentDateTime; }
    public Duration getDuration() { return duration; }
    public String getStatus() { return status; }
    public String getType() { return type; }
    public String getNotes() { return notes; }
    public double getCost() { return cost; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public LocalDateTime getEndTime() { return appointmentDateTime.plus(duration); }

    // Setters with validation
    public void setAppointmentId(int appointmentId) { 
        if (appointmentId > 0) {
            this.appointmentId = appointmentId;
            this.updatedAt = LocalDateTime.now();
        }
    }

    public void setPatientId(int patientId) { 
        if (patientId > 0) {
            this.patientId = patientId;
            this.updatedAt = LocalDateTime.now();
        }
    }

    public void setDoctorId(int doctorId) { 
        if (doctorId > 0) {
            this.doctorId = doctorId;
            this.updatedAt = LocalDateTime.now();
        }
    }

    public void setAppointmentDateTime(LocalDateTime appointmentDateTime) { 
        if (appointmentDateTime != null && appointmentDateTime.isAfter(LocalDateTime.now())) {
            this.appointmentDateTime = appointmentDateTime;
            this.updatedAt = LocalDateTime.now();
        }
    }

    public void setDuration(Duration duration) {
        if (duration != null && !duration.isNegative() && !duration.isZero()) {
            this.duration = duration;
            this.updatedAt = LocalDateTime.now();
        }
    }

    public void setStatus(String status) {
        try {
            AppointmentStatus.valueOf(status.toUpperCase());
            this.status = status.toUpperCase();
            this.updatedAt = LocalDateTime.now();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid appointment status: " + status);
        }
    }

    public void setType(String type) {
        try {
            AppointmentType.valueOf(type.toUpperCase());
            this.type = type.toUpperCase();
            this.updatedAt = LocalDateTime.now();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid appointment type: " + type);
        }
    }

    public void setNotes(String notes) { 
        this.notes = notes;
        this.updatedAt = LocalDateTime.now();
    }

    public void setCost(double cost) { 
        if (cost >= 0) {
            this.cost = cost;
            this.updatedAt = LocalDateTime.now();
        }
    }

    // Business logic methods
    public boolean isCompleted() {
        return AppointmentStatus.COMPLETED.name().equals(status);
    }

    public boolean isCancelled() {
        return AppointmentStatus.CANCELLED.name().equals(status);
    }

    public boolean isNoShow() {
        return AppointmentStatus.NO_SHOW.name().equals(status);
    }

    public boolean isScheduled() {
        return AppointmentStatus.SCHEDULED.name().equals(status);
    }

    public boolean isOverlapping(Appointment other) {
        LocalDateTime thisEnd = this.getEndTime();
        LocalDateTime otherEnd = other.getEndTime();
        
        return (this.appointmentDateTime.isBefore(otherEnd) && 
                other.appointmentDateTime.isBefore(thisEnd));
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "appointmentId=" + appointmentId +
                ", patientId=" + patientId +
                ", doctorId=" + doctorId +
                ", appointmentDateTime=" + appointmentDateTime +
                ", duration=" + duration +
                ", status='" + status + '\'' +
                ", type='" + type + '\'' +
                ", notes='" + notes + '\'' +
                ", cost=" + cost +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
} 