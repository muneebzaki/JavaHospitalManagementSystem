import com.hospital.dao.AppointmentDAO;
import com.hospital.entities.Appointment;
import com.hospital.entities.Appointment.Status;
import com.hospital.util.TestDatabaseUtil;
import org.junit.jupiter.api.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AppointmentDAOIntegrationTest {

    private static AppointmentDAO dao;
    private static Appointment testAppointment;
    private static int savedId;

    @BeforeAll
    static void setup() {
        dao = new AppointmentDAO();
        TestDatabaseUtil.clearAppointmentsTable();
    }

    @BeforeEach
    void createAppointment() {
        testAppointment = new Appointment(
                1, // patientId
                2, // doctorId
                LocalDateTime.now().plusDays(1),
                Duration.ofMinutes(30)
        );
    }

    @Test
    @Order(1)
    void testInsert() {
        boolean result = dao.insert(testAppointment);
        savedId = testAppointment.getId();
        assertTrue(result);
        assertTrue(savedId > 0);
    }

    @Test
    @Order(2)
    void testFindById() {
        Appointment appt = dao.findById(savedId);
        assertNotNull(appt);
        assertEquals(testAppointment.getPatientId(), appt.getPatientId());
    }

    @Test
    @Order(3)
    void testFindByPatientId() {
        List<Appointment> list = dao.findByPatientId(testAppointment.getPatientId());
        assertFalse(list.isEmpty());
        assertEquals(testAppointment.getPatientId(), list.get(0).getPatientId());
    }

    @Test
    @Order(4)
    void testUpdateStatus() {
        boolean result = dao.updateStatus(savedId, Status.COMPLETED);
        assertTrue(result);
        assertEquals(Status.COMPLETED, dao.findById(savedId).getStatus());
    }

    @Test
    @Order(5)
    void testFindAll() {
        List<Appointment> all = dao.findAll();
        assertFalse(all.isEmpty());
        assertTrue(all.stream().anyMatch(a -> a.getId() == savedId));
    }

    @Test
    @Order(6)
    void testUpdateStatusInvalidId() {
        assertFalse(dao.updateStatus(-1, Status.CANCELLED));
    }

    @AfterAll
    static void teardown() {
        TestDatabaseUtil.clearAppointmentsTable();
    }
}
