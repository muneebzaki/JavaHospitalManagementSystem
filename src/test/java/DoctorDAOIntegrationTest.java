import com.hospital.dao.DoctorDAO;
import com.hospital.entities.Doctor;
import com.hospital.util.TestDatabaseUtil;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DoctorDAOIntegrationTest {

    private static DoctorDAO dao;
    private static Doctor testDoctor;
    private static int savedDoctorId;

    @BeforeAll
    static void init() {
        dao = new DoctorDAO();
        TestDatabaseUtil.clearDoctorsTable();
    }

    @BeforeEach
    void setup() {
        testDoctor = new Doctor("Dr. Smith", "Cardiology");
    }

    @Test
    @Order(1)
    void testInsertDoctor() {
        boolean result = dao.insertDoctor(testDoctor);
        savedDoctorId = testDoctor.getId();
        assertTrue(result);
        assertTrue(savedDoctorId > 0);
    }

    @Test
    @Order(2)
    void testFindById() {
        Doctor doctor = dao.findById(savedDoctorId);
        assertNotNull(doctor);
        assertEquals("Dr. Smith", doctor.getName());
    }

    @Test
    @Order(3)
    void testUpdateDoctor() {
        testDoctor.setId(savedDoctorId);
        testDoctor.setName("Dr. John Smith");
        testDoctor.setSpecialization("Neurology");
        boolean updated = dao.updateDoctor(testDoctor);
        assertTrue(updated);

        Doctor updatedDoctor = dao.findById(savedDoctorId);
        assertEquals("Dr. John Smith", updatedDoctor.getName());
        assertEquals("Neurology", updatedDoctor.getSpecialization());
    }

    @Test
    @Order(4)
    void testFindAll() {
        List<Doctor> doctors = dao.findAll();
        assertFalse(doctors.isEmpty());
        assertTrue(doctors.stream().anyMatch(d -> d.getId() == savedDoctorId));
    }

    @Test
    @Order(5)
    void testDeleteDoctor() {
        boolean result = dao.deleteDoctor(savedDoctorId);
        assertTrue(result);
        assertNull(dao.findById(savedDoctorId));
    }

    @AfterAll
    static void tearDown() {
        TestDatabaseUtil.clearDoctorsTable();
    }
}
