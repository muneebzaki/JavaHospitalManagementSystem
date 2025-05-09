package com.hospital.dao;

import com.hospital.entities.Room;
import java.util.List;

public interface IRoomDAO {
    boolean insertRoom(Room room);
    boolean updateRoom(Room room);
    boolean deleteRoom(int roomId);
    Room findById(int roomId);
    Room findByRoomNumber(String roomNumber);
    List<Room> findAll();
    List<Room> findByType(String roomType);
    List<Room> findAvailable();
    boolean updateAvailability(int roomId, boolean isAvailable);
    boolean assignPatient(int roomId, int patientId);
    boolean removePatient(int roomId);
} 