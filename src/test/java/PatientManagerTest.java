import com.hospital.entities.Patient;
import com.hospital.controller.PatientManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

public class PatientManagerTest {

    private PatientManager manager;

    @BeforeEach
    public void setUp() {
        manager = new PatientManager();
    }

    private Patient createSamplePatient(String id) {
        return new Patient(id, "Test Name", 25, "Male", "Flu",
                "1234567890", "test@mail.com", "Test Address", LocalDate.now());
    }

    @Test
    public void testAddAndFindPatient() {
        Patient p = createSamplePatient("P001");
        manager.addPatient(p);

        Patient result = manager.findPatientById("P001");
        assertNotNull(result);
        assertEquals("Test Name", result.getName());
    }

    @Test
    public void testRemovePatient() {
        Patient p = createSamplePatient("P002");
        manager.addPatient(p);

        boolean removed = manager.removePatientById("P002");
        assertTrue(removed);
        assertNull(manager.findPatientById("P002"));
    }

    @Test
    public void testRemoveNonExistentPatient() {
        boolean removed = manager.removePatientById("P999");
        assertFalse(removed);
    }

    @Test
    public void testUpdatePatient() {
        Patient original = createSamplePatient("P003");
        manager.addPatient(original);

        Patient updated = new Patient("P003", "Updated Name", 30, "Female", "Cold",
                "9876543210", "updated@mail.com", "Updated Address", LocalDate.now());

        boolean updatedResult = manager.updatePatient(updated);
        assertTrue(updatedResult);

        Patient fetched = manager.findPatientById("P003");
        assertEquals("Updated Name", fetched.getName());
        assertEquals("Cold", fetched.getDisease());
    }

    @Test
    public void testPatientExists() {
        Patient p = createSamplePatient("P004");
        manager.addPatient(p);
        assertTrue(manager.patientExists("P004"));
        assertFalse(manager.patientExists("P999"));
    }

    @Test
    public void testClearAllPatients() {
        manager.addPatient(createSamplePatient("P005"));
        manager.addPatient(createSamplePatient("P006"));
        manager.clearAllPatients();
        assertEquals(0, manager.getPatientCount());
    }

    @Test
    public void testGetPatientCount() {
        manager.addPatient(createSamplePatient("P007"));
        manager.addPatient(createSamplePatient("P008"));
        assertEquals(2, manager.getPatientCount());
    }
}
