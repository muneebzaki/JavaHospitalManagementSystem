package com.hospital.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Doctor {
    private int id;
    private String name;
    private String specialization;
    private List<LocalDateTime> schedule = new ArrayList<>();
    private boolean isAvailable = true;

    public Doctor(int id, String name, String specialization) {
        this.id = id;
        this.name = name;
        this.specialization = specialization;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getSpecialization() { return specialization; }
    public List<LocalDateTime> getSchedule() { return new ArrayList<>(schedule); }
    public boolean isAvailable() { return isAvailable; }

    public void setAvailable(boolean available) { this.isAvailable = available; }
    public void addToSchedule(LocalDateTime time) { schedule.add(time); }
    public void removeFromSchedule(LocalDateTime time) { schedule.remove(time); }
    public boolean hasAppointmentAt(LocalDateTime time) { return schedule.contains(time); }

    @Override
    public String toString() {
        return name + " (" + specialization + ")";
    }
}
