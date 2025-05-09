package com.hospital.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

public class NurseTest {
    private Nurse nurse;

    @BeforeEach
    void setUp() {
        nurse = new Nurse(1, "Nurse Johnson", "Emergency");
    }

    @Test
    void testNurseCreation() {
        assertEquals(1, nurse.getId());
        assertEquals("Nurse Johnson", nurse.getName());
        assertEquals("Emergency", nurse.getDepartment());
        assertTrue(nurse.isAvailable());
        assertTrue(nurse.getSchedule().isEmpty());
    }

    @Test
    void testSetAndGetMethods() {
        nurse.setId(2);
        nurse.setName("Nurse Smith");
        nurse.setDepartment("ICU");
        nurse.setAvailable(false);

        assertEquals(2, nurse.getId());
        assertEquals("Nurse Smith", nurse.getName());
        assertEquals("ICU", nurse.getDepartment());
        assertFalse(nurse.isAvailable());
    }

    @Test
    void testScheduleManagement() {
        LocalDateTime shiftTime = LocalDateTime.now();
        
        // Test adding shift
        nurse.addToSchedule(shiftTime);
        assertTrue(nurse.hasShiftAt(shiftTime));
        assertEquals(1, nurse.getSchedule().size());

        // Test removing shift
        nurse.removeFromSchedule(shiftTime);
        assertFalse(nurse.hasShiftAt(shiftTime));
        assertTrue(nurse.getSchedule().isEmpty());
    }

    @Test
    void testToString() {
        String expected = "Nurse{id=1, name='Nurse Johnson', department='Emergency', isAvailable=true}";
        assertEquals(expected, nurse.toString());
    }
} 