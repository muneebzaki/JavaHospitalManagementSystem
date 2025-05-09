package com.hospital.entities;

public class Room {
    private int roomId;
    private String roomNumber;
    private String roomType; // GENERAL, PRIVATE, ICU, OPERATION
    private boolean isAvailable;
    private int capacity;
    private double pricePerDay;
    private int currentPatientId; // -1 if no patient

    public Room(int roomId, String roomNumber, String roomType, boolean isAvailable, 
                int capacity, double pricePerDay, int currentPatientId) {
        this.roomId = roomId;
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.isAvailable = isAvailable;
        this.capacity = capacity;
        this.pricePerDay = pricePerDay;
        this.currentPatientId = currentPatientId;
    }

    // Constructor for new room
    public Room(String roomNumber, String roomType, int capacity, double pricePerDay) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.isAvailable = true;
        this.capacity = capacity;
        this.pricePerDay = pricePerDay;
        this.currentPatientId = -1;
    }

    // Getters
    public int getRoomId() { return roomId; }
    public String getRoomNumber() { return roomNumber; }
    public String getRoomType() { return roomType; }
    public boolean isAvailable() { return isAvailable; }
    public int getCapacity() { return capacity; }
    public double getPricePerDay() { return pricePerDay; }
    public int getCurrentPatientId() { return currentPatientId; }

    // Setters
    public void setRoomId(int roomId) { this.roomId = roomId; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }
    public void setRoomType(String roomType) { this.roomType = roomType; }
    public void setAvailable(boolean available) { isAvailable = available; }
    public void setCapacity(int capacity) { this.capacity = capacity; }
    public void setPricePerDay(double pricePerDay) { this.pricePerDay = pricePerDay; }
    public void setCurrentPatientId(int currentPatientId) { this.currentPatientId = currentPatientId; }

    @Override
    public String toString() {
        return "Room{" +
                "roomId=" + roomId +
                ", roomNumber='" + roomNumber + '\'' +
                ", roomType='" + roomType + '\'' +
                ", isAvailable=" + isAvailable +
                ", capacity=" + capacity +
                ", pricePerDay=" + pricePerDay +
                ", currentPatientId=" + currentPatientId +
                '}';
    }
} 