package com.hospital.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Nurse {
    private int id;
    private String name;
    private String department;
    private List<LocalDateTime> schedule;
    private boolean isAvailable;

    public Nurse(int id, String name, String department) {
        this.id = id;
        this.name = name;
        this.department = department;
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

    public String getDepartment() {
        return department;
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

    public void addToSchedule(LocalDateTime shiftTime) {
        schedule.add(shiftTime);
    }

    public void removeFromSchedule(LocalDateTime shiftTime) {
        schedule.remove(shiftTime);
    }

    public boolean hasShiftAt(LocalDateTime time) {
        return schedule.stream()
                .anyMatch(scheduledTime -> scheduledTime.equals(time));
    }

    @Override
    public String toString() {
        return "Nurse{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", department='" + department + '\'' +
                ", isAvailable=" + isAvailable +
                '}';
    }
} 