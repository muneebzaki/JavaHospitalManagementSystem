package com.hospital.controller;

import com.hospital.dao.IRoomDAO;
import com.hospital.dao.RoomDAO;
import com.hospital.entities.Room;

import java.util.List;

public class RoomManager {
    private final IRoomDAO roomDAO;

    public RoomManager() {
        this.roomDAO = new RoomDAO();
    }

    public boolean addRoom(Room room) {
        return roomDAO.insertRoom(room);
    }

    public boolean updateRoom(Room room) {
        return roomDAO.updateRoom(room);
    }

    public boolean deleteRoom(int roomId) {
        return roomDAO.deleteRoom(roomId);
    }

    public Room findRoomById(int roomId) {
        return roomDAO.findById(roomId);
    }

    public Room findRoomByNumber(String roomNumber) {
        return roomDAO.findByRoomNumber(roomNumber);
    }

    public List<Room> getAllRooms() {
        return roomDAO.findAll();
    }

    public List<Room> getRoomsByType(String roomType) {
        return roomDAO.findByType(roomType);
    }

    public List<Room> getAvailableRooms() {
        return roomDAO.findAvailable();
    }

    public boolean allocateRoom(int roomId, int patientId) {
        Room room = findRoomById(roomId);
        if (room != null && room.isAvailable()) {
            return roomDAO.assignPatient(roomId, patientId);
        }
        return false;
    }

    public boolean releaseRoom(int roomId) {
        Room room = findRoomById(roomId);
        if (room != null && !room.isAvailable()) {
            return roomDAO.removePatient(roomId);
        }
        return false;
    }

    public boolean isRoomAvailable(int roomId) {
        Room room = findRoomById(roomId);
        return room != null && room.isAvailable();
    }

    public boolean roomExists(int roomId) {
        return findRoomById(roomId) != null;
    }

    public boolean roomNumberExists(String roomNumber) {
        return findRoomByNumber(roomNumber) != null;
    }

    public double calculateRoomCost(int roomId, int numberOfDays) {
        Room room = findRoomById(roomId);
        if (room != null) {
            return room.getPricePerDay() * numberOfDays;
        }
        return 0.0;
    }
} 