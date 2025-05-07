import entities.Nurse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class NurseTest {
    private Nurse nurse;
    private LocalDateTime testTime;

    @BeforeEach
    void setUp() {
        nurse = new Nurse(1, "Nurse Johnson", "Emergency");
        testTime = LocalDateTime.now();
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
    void testAddToSchedule() {
        nurse.addToSchedule(testTime);
        assertTrue(nurse.hasShiftAt(testTime));
        assertEquals(1, nurse.getSchedule().size());
    }

    @Test
    void testRemoveFromSchedule() {
        nurse.addToSchedule(testTime);
        nurse.removeFromSchedule(testTime);
        assertFalse(nurse.hasShiftAt(testTime));
        assertTrue(nurse.getSchedule().isEmpty());
    }

    @Test
    void testSetAvailable() {
        nurse.setAvailable(false);
        assertFalse(nurse.isAvailable());
        nurse.setAvailable(true);
        assertTrue(nurse.isAvailable());
    }

    @Test
    void testGetScheduleReturnsNewList() {
        nurse.addToSchedule(testTime);
        var schedule = nurse.getSchedule();
        schedule.add(testTime.plusHours(1));
        assertEquals(1, nurse.getSchedule().size());
    }
} 