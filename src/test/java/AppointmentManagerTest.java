import com.hospital.controller.AppointmentManager;
import com.hospital.dao.AppointmentDAO;
import com.hospital.entities.Appointment;
import com.hospital.entities.Appointment.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AppointmentManagerTest {

    @Mock
    private AppointmentDAO mockDao;

    @InjectMocks
    private AppointmentManager manager;

    private Appointment testAppointment;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        testAppointment = new Appointment(
                1, 2,
                LocalDateTime.now().plusDays(1),
                Duration.ofMinutes(30)
        );
        testAppointment.setId(101);
    }

    @Test
    void testCreateAppointment() {
        when(mockDao.insert(any())).thenReturn(true);
        assertTrue(manager.createAppointment(1, 2, testAppointment.getDateTime(), testAppointment.getDuration()));
        verify(mockDao).insert(any());
    }

    @Test
    void testGetAppointment() {
        when(mockDao.findById(101)).thenReturn(testAppointment);
        Appointment appt = manager.getAppointment(101);
        assertNotNull(appt);
        assertEquals(1, appt.getPatientId());
    }

    @Test
    void testGetAppointmentsForPatient() {
        when(mockDao.findByPatientId(1)).thenReturn(Collections.singletonList(testAppointment));
        List<Appointment> list = manager.getAppointmentsForPatient(1);
        assertEquals(1, list.size());
    }

    @Test
    void testMarkAsCompleted() {
        when(mockDao.updateStatus(101, Status.COMPLETED)).thenReturn(true);
        assertTrue(manager.markAsCompleted(101));
    }

    @Test
    void testCancelAppointment() {
        when(mockDao.updateStatus(101, Status.CANCELLED)).thenReturn(true);
        assertTrue(manager.cancelAppointment(101));
    }

    @Test
    void testGetAllAppointments() {
        when(mockDao.findAll()).thenReturn(Collections.singletonList(testAppointment));
        List<Appointment> all = manager.getAllAppointments();
        assertEquals(1, all.size());
    }
}
