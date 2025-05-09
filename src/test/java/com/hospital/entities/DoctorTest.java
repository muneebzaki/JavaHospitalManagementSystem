package com.hospital.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

public class DoctorTest {
    private Doctor doctor;

    @BeforeEach
    void setUp() {
        doctor = new Doctor(1, "Dr. Smith", "Cardiology");
    }

    @Test
    void testDoctorCreation() {
        assertEquals(1, doctor.getId());
        assertEquals("Dr. Smith", doctor.getName());
        assertEquals("Cardiology", doctor.getSpecialization());
        assertTrue(doctor.isAvailable());
        assertTrue(doctor.getSchedule().isEmpty());
    }

    @Test
    void testSetAndGetMethods() {
        doctor.setId(2);
        doctor.setName("Dr. Johnson");
        doctor.setSpecialization("Neurology");
        doctor.setAvailable(false);

        assertEquals(2, doctor.getId());
        assertEquals("Dr. Johnson", doctor.getName());
        assertEquals("Neurology", doctor.getSpecialization());
        assertFalse(doctor.isAvailable());
    }

    @Test
    void testScheduleManagement() {
        LocalDateTime appointmentTime = LocalDateTime.now();
        
        // Test adding appointment
        doctor.addToSchedule(appointmentTime);
        assertTrue(doctor.hasAppointmentAt(appointmentTime));
        assertEquals(1, doctor.getSchedule().size());

        // Test removing appointment
        doctor.removeFromSchedule(appointmentTime);
        assertFalse(doctor.hasAppointmentAt(appointmentTime));
        assertTrue(doctor.getSchedule().isEmpty());
    }

    @Test
    void testToString() {
        String expected = "Doctor{id=1, name='Dr. Smith', specialization='Cardiology', isAvailable=true}";
        assertEquals(expected, doctor.toString());
    }
} 