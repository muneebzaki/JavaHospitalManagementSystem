import com.hospital.dao.PatientDAO;
import com.hospital.entities.Patient;
import com.hospital.util.TestDatabaseUtil;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PatientDAOIntegrationTest {

    private static PatientDAO dao;
    private static Patient testPatient;
    private static Integer savedId;

    @BeforeAll
    static void init() {
        dao = new PatientDAO();
        TestDatabaseUtil.clearPatientsTable();
    }

    @BeforeEach
    void setup() {
        testPatient = new Patient(
                0, // ID will be set by database
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
    @Order(1)
    void testInsertPatient() {
        boolean result = dao.insertPatient(testPatient);
        savedId = testPatient.getId();
        testPatient.setId(savedId);

        assertTrue(result);
        assertNotNull(savedId);
        assertTrue(savedId > 0);
    }

    @Test
    @Order(2)
    void testFindById() {
        Patient found = dao.findPatientById(savedId);
        assertNotNull(found);
        assertEquals("John Doe", found.getName());
        assertEquals(45, found.getAge());
    }

    @Test
    @Order(3)
    void testUpdatePatient() {
        testPatient.setName("John D.");
        testPatient.setAge(46);
        boolean result = dao.updatePatientById(savedId, testPatient);
        System.out.println(testPatient);
        assertTrue(result);

        Patient updated = dao.findPatientById(savedId);
        assertEquals("John D.", updated.getName());
        assertEquals(46, updated.getAge());
    }

    @Test
    @Order(4)
    void testFindAll() {
        List<Patient> patients = dao.findAllPatients();
        assertFalse(patients.isEmpty());
        assertTrue(patients.stream().anyMatch(p -> p.getId() == savedId));
    }

    @Test
    @Order(5)
    void testDeletePatient() {
        assertTrue(dao.deletePatientById(savedId));
        assertNull(dao.findPatientById(savedId));
    }

    @Test
    @Order(6)
    void testDeleteNonExistentPatient() {
        assertFalse(dao.deletePatientById(-1));
    }

    @Test
    @Order(7)
    void testFindByNonExistentId() {
        assertNull(dao.findPatientById(-1));
    }

    @AfterAll
    static void cleanup() {
        TestDatabaseUtil.clearPatientsTable();
    }
}