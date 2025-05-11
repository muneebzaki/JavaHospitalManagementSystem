import com.hospital.controller.DoctorManager;
import com.hospital.dao.DoctorDAO;
import com.hospital.entities.Doctor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DoctorManagerTest {

    @Mock
    private DoctorDAO mockDao;

    @InjectMocks
    private DoctorManager manager;

    private Doctor doctor;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        doctor = new Doctor(1, "Dr. Smith", "Cardiology");
    }

    @Test
    void testAddDoctor() {
        when(mockDao.insertDoctor(doctor)).thenReturn(true);
        assertTrue(manager.addDoctor(doctor));
        verify(mockDao).insertDoctor(doctor);
    }

    @Test
    void testUpdateDoctor() {
        when(mockDao.updateDoctor(doctor)).thenReturn(true);
        assertTrue(manager.updateDoctor(doctor));
        verify(mockDao).updateDoctor(doctor);
    }

    @Test
    void testRemoveDoctor() {
        when(mockDao.deleteDoctor(1)).thenReturn(true);
        assertTrue(manager.removeDoctor(1));
        verify(mockDao).deleteDoctor(1);
    }

    @Test
    void testGetDoctorById() {
        when(mockDao.findById(1)).thenReturn(doctor);
        Doctor result = manager.getDoctorById(1);
        assertNotNull(result);
        assertEquals("Dr. Smith", result.getName());
    }

    @Test
    void testGetAllDoctors() {
        when(mockDao.findAll()).thenReturn(Collections.singletonList(doctor));
        List<Doctor> result = manager.getAllDoctors();
        assertEquals(1, result.size());
    }

    @Test
    void testDoctorExists() {
        when(mockDao.findById(1)).thenReturn(doctor);
        assertTrue(manager.doctorExists(1));
    }
}
