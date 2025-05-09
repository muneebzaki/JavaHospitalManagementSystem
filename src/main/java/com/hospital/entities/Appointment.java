package com.hospital.entities;

import java.time.LocalDateTime;

public class Appointment {
    private int appointmentId;
    private int patientId;
    private int doctorId;
    private LocalDateTime appointmentDateTime;
    private String status; // SCHEDULED, COMPLETED, CANCELLED, NO_SHOW
    private String type; // REGULAR, URGENT, FOLLOW_UP
    private String notes;
    private double cost;

    public Appointment(int appointmentId, int patientId, int doctorId, LocalDateTime appointmentDateTime, 
                      String status, String type, String notes, double cost) {
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.appointmentDateTime = appointmentDateTime;
        this.status = status;
        this.type = type;
        this.notes = notes;
        this.cost = cost;
    }

    // Constructor for new appointments (without ID)
    public Appointment(int patientId, int doctorId, LocalDateTime appointmentDateTime, 
                      String type, String notes, double cost) {
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.appointmentDateTime = appointmentDateTime;
        this.status = "SCHEDULED";
        this.type = type;
        this.notes = notes;
        this.cost = cost;
    }

    // Getters
    public int getAppointmentId() { return appointmentId; }
    public int getPatientId() { return patientId; }
    public int getDoctorId() { return doctorId; }
    public LocalDateTime getAppointmentDateTime() { return appointmentDateTime; }
    public String getStatus() { return status; }
    public String getType() { return type; }
    public String getNotes() { return notes; }
    public double getCost() { return cost; }

    // Setters
    public void setAppointmentId(int appointmentId) { this.appointmentId = appointmentId; }
    public void setPatientId(int patientId) { this.patientId = patientId; }
    public void setDoctorId(int doctorId) { this.doctorId = doctorId; }
    public void setAppointmentDateTime(LocalDateTime appointmentDateTime) { this.appointmentDateTime = appointmentDateTime; }
    public void setStatus(String status) { this.status = status; }
    public void setType(String type) { this.type = type; }
    public void setNotes(String notes) { this.notes = notes; }
    public void setCost(double cost) { this.cost = cost; }

    @Override
    public String toString() {
        return "Appointment{" +
                "appointmentId=" + appointmentId +
                ", patientId=" + patientId +
                ", doctorId=" + doctorId +
                ", appointmentDateTime=" + appointmentDateTime +
                ", status='" + status + '\'' +
                ", type='" + type + '\'' +
                ", notes='" + notes + '\'' +
                ", cost=" + cost +
                '}';
    }
} 