
import com.hospital.dao.AppointmentDAO;
import com.hospital.dao.DoctorDAO;
import com.hospital.dao.PatientDAO;
import com.hospital.entities.Appointment;
import com.hospital.entities.Doctor;
import com.hospital.entities.Patient;
import com.hospital.util.TestDatabaseUtil;
import org.junit.jupiter.api.*;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AppointmentDAOIntegrationTest {

    private static AppointmentDAO appointmentDao;
    private static DoctorDAO doctorDao;
    private static PatientDAO patientDao;
    private static Appointment testAppointment;
    private static Doctor testDoctor;
    private static Patient testPatient;
    private static int savedId;
    private static int testDoctorId;
    private static int testPatientId;

    @BeforeAll
    static void setup() {
        appointmentDao = new AppointmentDAO();
        doctorDao = new DoctorDAO();
        patientDao = new PatientDAO();

        TestDatabaseUtil.clearAppointmentsTable();
        TestDatabaseUtil.clearDoctorsTable();
        TestDatabaseUtil.clearPatientsTable();

        // Create test doctor
        testDoctor = new Doctor("Dr. Test", "General");
        assertTrue(doctorDao.insertDoctor(testDoctor));
        testDoctorId = testDoctor.getId();

        // Create test patient
        testPatient = new Patient(
                0,
                "Test Patient",
                30,
                "Male",
                "Test Disease",
                "1234567890",
                "test@example.com",
                "Test Address",
                LocalDate.now()
        );
        assertTrue(patientDao.insertPatient(testPatient));
        testPatientId = testPatient.getId();
    }

    @BeforeEach
    void createTestAppointment() {
        testAppointment = new Appointment(
                testPatientId,
                testDoctorId,
                LocalDateTime.now().plusDays(1),
                Duration.ofMinutes(30)
        );
    }

    @Test
    @Order(1)
    void testInsertAppointment() {
        assertTrue(appointmentDao.insert(testAppointment));
        savedId = testAppointment.getId();
        assertTrue(savedId > 0);
    }

    @Test
    @Order(2)
    void testFindAppointmentById() {
        Appointment found = appointmentDao.findById(savedId);
        assertNotNull(found);
        assertEquals(testPatientId, found.getPatientId());
        assertEquals(testDoctorId, found.getDoctorId());
    }

    @Test
    @Order(3)
    void testFindAppointmentsByPatientId() {
        List<Appointment> appointments = appointmentDao.findByPatientId(testPatientId);
        assertFalse(appointments.isEmpty());
        assertEquals(testPatientId, appointments.get(0).getPatientId());
    }

    @Test
    @Order(4)
    void testUpdateAppointmentStatus() {
        assertTrue(appointmentDao.updateStatus(savedId, Appointment.Status.COMPLETED));
        assertEquals(Appointment.Status.COMPLETED, appointmentDao.findById(savedId).getStatus());
    }

    @Test
    @Order(5)
    void testFindAllAppointments() {
        List<Appointment> all = appointmentDao.findAll();
        assertFalse(all.isEmpty());
        assertTrue(all.stream().anyMatch(a -> a.getId() == savedId));
    }

    @Test
    @Order(6)
    void testUpdateInvalidAppointment() {
        assertFalse(appointmentDao.updateStatus(-1, Appointment.Status.CANCELLED));
    }

    @Test
    @Order(7)
    void testInsertAppointmentWithInvalidPatient() {
        Appointment invalid = new Appointment(-1, testDoctorId,
                LocalDateTime.now().plusDays(1), Duration.ofMinutes(30));
        assertFalse(appointmentDao.insert(invalid));
    }

    @Test
    @Order(8)
    void testInsertAppointmentWithInvalidDoctor() {
        Appointment invalid = new Appointment(testPatientId, -1,
                LocalDateTime.now().plusDays(1), Duration.ofMinutes(30));
        assertFalse(appointmentDao.insert(invalid));
    }

    @Test
    @Order(9)
    void testInsertAppointmentInPast() {
        Appointment pastAppointment = new Appointment(
                testPatientId,
                testDoctorId,
                LocalDateTime.now().minusDays(1),
                Duration.ofMinutes(30)
        );
        assertFalse(appointmentDao.insert(pastAppointment));
    }

    @Test
    @Order(10)
    void testInsertOverlappingAppointment() {
        Appointment overlapping = new Appointment(
                testPatientId,
                testDoctorId,
                testAppointment.getDateTime(),
                Duration.ofMinutes(30)
        );
        assertFalse(appointmentDao.insert(overlapping));
    }

    @AfterAll
    static void cleanup() {
        TestDatabaseUtil.clearAppointmentsTable();
        TestDatabaseUtil.clearDoctorsTable();
        TestDatabaseUtil.clearPatientsTable();
    }
}