package com.hospital.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Doctor {
    private int id;
    private String name;
    private String specialization;
    private List<LocalDateTime> schedule;
    private boolean isAvailable;

    public Doctor(int id, String name, String specialization) {
        this.id = id;
        this.name = name;
        this.specialization = specialization;
        this.schedule = new ArrayList<>();
        this.isAvailable = true;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSpecialization() {
        return specialization;
    }

    public List<LocalDateTime> getSchedule() {
        return new ArrayList<>(schedule);
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public void addToSchedule(LocalDateTime appointmentTime) {
        schedule.add(appointmentTime);
    }

    public void removeFromSchedule(LocalDateTime appointmentTime) {
        schedule.remove(appointmentTime);
    }

    public boolean hasAppointmentAt(LocalDateTime time) {
        return schedule.stream()
                .anyMatch(scheduledTime -> scheduledTime.equals(time));
    }

    @Override
    public String toString() {
        return "Doctor{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", specialization='" + specialization + '\'' +
                ", isAvailable=" + isAvailable +
                '}';
    }
} 