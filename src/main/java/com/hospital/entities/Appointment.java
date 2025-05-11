package com.hospital.entities;

import java.time.Duration;
import java.time.LocalDateTime;

public class Appointment {
    public enum Status {SCHEDULED, COMPLETED, CANCELLED, NO_SHOW}

    public enum Type {REGULAR, URGENT, FOLLOW_UP}

    private int id;
    private int patientId;
    private int doctorId;
    private LocalDateTime dateTime;
    private Duration duration;
    private Status status;

    public Appointment(int id, int patientId, int doctorId, LocalDateTime dateTime, Duration duration, Status status) {
        this.id = id;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.dateTime = dateTime;
        this.duration = duration;
        this.status = status;
    }

    public Appointment(int patientId, int doctorId, LocalDateTime dateTime, Duration duration) {
        this(0, patientId, doctorId, dateTime, duration, Status.SCHEDULED);
    }

    public int getId() {
        return id;
    }

    public int getPatientId() {
        return patientId;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public Duration getDuration() {
        return duration;
    }

    public Status getStatus() {
        return status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Appointment #" + id + " (Patient " + patientId + ", Doctor " + doctorId + ", " + dateTime + ")";
    }
}