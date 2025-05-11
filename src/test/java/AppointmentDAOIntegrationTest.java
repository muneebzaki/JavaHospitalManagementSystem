
import com.hospital.dao.AppointmentDAO;
import com.hospital.dao.DoctorDAO;
import com.hospital.entities.Appointment;
import com.hospital.entities.Appointment.Status;
import com.hospital.entities.Doctor;
import com.hospital.util.TestDatabaseUtil;
import org.junit.jupiter.api.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AppointmentDAOIntegrationTest {

    private static AppointmentDAO appointmentDao;
    private static DoctorDAO doctorDao;
    private static Appointment testAppointment;
    private static int savedId;
    private static int testDoctorId;

    @BeforeAll
    static void setup() {
        appointmentDao = new AppointmentDAO();
        doctorDao = new DoctorDAO();
        TestDatabaseUtil.clearAppointmentsTable();
        TestDatabaseUtil.clearDoctorsTable();

        // Create a test doctor first
        Doctor testDoctor = new Doctor("Dr. Test", "General");
        doctorDao.insertDoctor(testDoctor);
        testDoctorId = testDoctor.getId();
    }

    @BeforeEach
    void createAppointment() {
        testAppointment = new Appointment(
                1, // patientId
                testDoctorId, // using the actual doctor ID
                LocalDateTime.now().plusDays(1),
                Duration.ofMinutes(30)
        );
    }

    @Test
    @Order(1)
    void testInsert() {
        boolean result = appointmentDao.insert(testAppointment);
        savedId = testAppointment.getId();
        assertTrue(result);
        assertTrue(savedId > 0);
    }

    @Test
    @Order(2)
    void testFindById() {
        Appointment appt = appointmentDao.findById(savedId);
        assertNotNull(appt);
        assertEquals(testAppointment.getPatientId(), appt.getPatientId());
        assertEquals(testDoctorId, appt.getDoctorId());
    }

    @Test
    @Order(3)
    void testFindByPatientId() {
        List<Appointment> list = appointmentDao.findByPatientId(testAppointment.getPatientId());
        assertFalse(list.isEmpty());
        assertEquals(testAppointment.getPatientId(), list.get(0).getPatientId());
        assertEquals(testDoctorId, list.get(0).getDoctorId());
    }

    @Test
    @Order(4)
    void testUpdateStatus() {
        boolean result = appointmentDao.updateStatus(savedId, Status.COMPLETED);
        assertTrue(result);
        assertEquals(Status.COMPLETED, appointmentDao.findById(savedId).getStatus());
    }

    @Test
    @Order(5)
    void testFindAll() {
        List<Appointment> all = appointmentDao.findAll();
        assertFalse(all.isEmpty());
        assertTrue(all.stream().anyMatch(a -> a.getId() == savedId));
    }

    @Test
    @Order(6)
    void testUpdateStatusInvalidId() {
        assertFalse(appointmentDao.updateStatus(-1, Status.CANCELLED));
    }

    @AfterAll
    static void teardown() {
        TestDatabaseUtil.clearAppointmentsTable();
        TestDatabaseUtil.clearDoctorsTable();
    }
}