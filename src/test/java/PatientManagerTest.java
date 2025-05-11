import com.hospital.controller.PatientManager;
import com.hospital.dao.PatientDAO;
import com.hospital.entities.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PatientManagerTest {

    @Mock
    private PatientDAO mockDao;

    @InjectMocks
    private PatientManager manager;

    private Patient testPatient;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        testPatient = new Patient(
                "P1001",
                "John Doe",
                45,
                "Male",
                "Flu",
                "1234567890",
                "john.doe@example.com",
                "123 Main Street",
                LocalDate.now()
        );
    }

    @Test
    void testAddPatient() {
        when(mockDao.insertPatient(testPatient)).thenReturn(true);
        assertTrue(manager.addPatient(testPatient));
        verify(mockDao).insertPatient(testPatient);
    }

    @Test
    void testUpdatePatient() {
        when(mockDao.updatePatient(testPatient)).thenReturn(true);
        assertTrue(manager.updatePatient(testPatient));
        verify(mockDao).updatePatient(testPatient);
    }

    @Test
    void testRemovePatientById() {
        when(mockDao.deletePatientById("P1001")).thenReturn(true);
        assertTrue(manager.removePatientById("P1001"));
        verify(mockDao).deletePatientById("P1001");
    }

    @Test
    void testGetPatientById() {
        when(mockDao.findPatientById("P1001")).thenReturn(testPatient);
        Patient result = manager.getPatientById("P1001");
        assertNotNull(result);
        assertEquals("John Doe", result.getName());
    }

    @Test
    void testGetAllPatients() {
        when(mockDao.findAllPatients()).thenReturn(Collections.singletonList(testPatient));
        List<Patient> list = manager.getAllPatients();
        assertEquals(1, list.size());
    }

    @Test
    void testPatientExists() {
        when(mockDao.findPatientById("P1001")).thenReturn(testPatient);
        assertTrue(manager.patientExists("P1001"));
    }

    @Test
    void testGetPatientCount() {
        when(mockDao.findAllPatients()).thenReturn(Collections.singletonList(testPatient));
        assertEquals(1, manager.getPatientCount());
    }
}
