import com.hospital.entities.Doctor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class DoctorTest {
    private Doctor doctor;
    private LocalDateTime testTime;

    @BeforeEach
    void setUp() {
        doctor = new Doctor(1, "Dr. Smith", "Cardiology");
        testTime = LocalDateTime.now();
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
    void testAddToSchedule() {
        doctor.addToSchedule(testTime);
        assertTrue(doctor.hasAppointmentAt(testTime));
        assertEquals(1, doctor.getSchedule().size());
    }

    @Test
    void testRemoveFromSchedule() {
        doctor.addToSchedule(testTime);
        doctor.removeFromSchedule(testTime);
        assertFalse(doctor.hasAppointmentAt(testTime));
        assertTrue(doctor.getSchedule().isEmpty());
    }

    @Test
    void testSetAvailable() {
        doctor.setAvailable(false);
        assertFalse(doctor.isAvailable());
        doctor.setAvailable(true);
        assertTrue(doctor.isAvailable());
    }

    @Test
    void testGetScheduleReturnsNewList() {
        doctor.addToSchedule(testTime);
        var schedule = doctor.getSchedule();
        schedule.add(testTime.plusHours(1));
        assertEquals(1, doctor.getSchedule().size());
    }
} 