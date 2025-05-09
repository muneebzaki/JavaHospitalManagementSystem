package com.hospital.util;

import com.hospital.entities.Appointment;
import com.hospital.entities.Bill;
import com.hospital.entities.Room;

import java.time.LocalDateTime;

public class ValidationUtils {
    
    public static boolean isValidId(int id) {
        return id > 0;
    }
    
    public static boolean isValidDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return startDate != null && endDate != null && !startDate.isAfter(endDate);
    }
    
    public static boolean isValidAppointment(Appointment appointment) {
        if (appointment == null) return false;
        if (!isValidId(appointment.getPatientId())) return false;
        if (!isValidId(appointment.getDoctorId())) return false;
        if (appointment.getAppointmentDateTime() == null) return false;
        if (appointment.getAppointmentDateTime().isBefore(LocalDateTime.now())) return false;
        if (appointment.getType() == null || appointment.getType().isEmpty()) return false;
        if (appointment.getCost() < 0) return false;
        return true;
    }
    
    public static boolean isValidBill(Bill bill) {
        if (bill == null) return false;
        if (!isValidId(bill.getPatientId())) return false;
        if (bill.getTotal_amount() <= 0) return false;
        if (bill.getBilling_date() == null) return false;
        if (bill.getPayment_status() == null || bill.getPayment_status().isEmpty()) return false;
        if (bill.getPayment_type() == null || bill.getPayment_type().isEmpty()) return false;
        return true;
    }
    
    public static boolean isValidRoom(Room room) {
        if (room == null) return false;
        if (room.getRoomNumber() == null || room.getRoomNumber().isEmpty()) return false;
        if (room.getRoomType() == null || room.getRoomType().isEmpty()) return false;
        if (room.getCapacity() <= 0) return false;
        if (room.getPricePerDay() < 0) return false;
        return true;
    }
} 