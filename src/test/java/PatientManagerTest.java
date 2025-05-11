
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
                1001, // Integer ID instead of String
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
        when(mockDao.updatePatientById(testPatient.getId(), testPatient)).thenReturn(true);
        assertTrue(manager.updatePatient(testPatient));
        verify(mockDao).updatePatientById(testPatient.getId(), testPatient);
    }

    @Test
    void testRemovePatientById() {
        when(mockDao.deletePatientById(1001)).thenReturn(true);
        assertTrue(manager.removePatientById(1001));
        verify(mockDao).deletePatientById(1001);
    }

    @Test
    void testGetPatientById() {
        when(mockDao.findPatientById(1001)).thenReturn(testPatient);
        Patient result = manager.getPatientById(1001);
        assertNotNull(result);
        assertEquals("John Doe", result.getName());
    }

    @Test
    void testGetPatientByNullId() {
        assertNull(manager.getPatientById(null));
        verify(mockDao, never()).findPatientById(any());
    }

    @Test
    void testGetAllPatients() {
        when(mockDao.findAllPatients()).thenReturn(Collections.singletonList(testPatient));
        List<Patient> list = manager.getAllPatients();
        assertEquals(1, list.size());
    }

    @Test
    void testPatientExists() {
        when(mockDao.findPatientById(1001)).thenReturn(testPatient);
        assertTrue(manager.patientExists(1001));
    }

    @Test
    void testPatientDoesNotExist() {
        when(mockDao.findPatientById(1001)).thenReturn(null);
        assertFalse(manager.patientExists(1001));
    }

    @Test
    void testGetPatientCount() {
        when(mockDao.findAllPatients()).thenReturn(Collections.singletonList(testPatient));
        assertEquals(1, manager.getPatientCount());
    }
}