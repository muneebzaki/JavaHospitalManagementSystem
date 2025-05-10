package com.hospital.entities;

import java.time.Duration;
import java.time.LocalDateTime;

public class Appointment {
    public enum Status { SCHEDULED, COMPLETED, CANCELLED, NO_SHOW }
    public enum Type { REGULAR, URGENT, FOLLOW_UP }

    private int id;
    private int patientId;
    private int doctorId;
    private LocalDateTime dateTime;
    private Duration duration;
    private Status status;
    private Type type;
    private String notes;
    private double cost;

    public Appointment(int id, int patientId, int doctorId, LocalDateTime dateTime,
                       Duration duration, Status status, Type type, String notes, double cost) {
        this.id = id;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.dateTime = dateTime;
        this.duration = duration;
        this.status = status;
        this.type = type;
        this.notes = notes;
        this.cost = cost;
    }

    public Appointment(int patientId, int doctorId, LocalDateTime dateTime,
                       Duration duration, Type type, String notes, double cost) {
        this(0, patientId, doctorId, dateTime, duration, Status.SCHEDULED, type, notes, cost);
    }

    public int getId() { return id; }
    public int getPatientId() { return patientId; }
    public int getDoctorId() { return doctorId; }
    public LocalDateTime getDateTime() { return dateTime; }
    public Duration getDuration() { return duration; }
    public Status getStatus() { return status; }
    public Type getType() { return type; }
    public String getNotes() { return notes; }
    public double getCost() { return cost; }

    public boolean isOverlapping(Appointment other) {
        return this.dateTime.isBefore(other.dateTime.plus(other.duration)) &&
                other.dateTime.isBefore(this.dateTime.plus(this.duration));
    }

    @Override
    public String toString() {
        return "Appointment ID: " + id + ", Patient ID: " + patientId + ", Doctor ID: " + doctorId;
    }
}
