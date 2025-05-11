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

    @BeforeAll
    static void setup() {
        dao = new PatientDAO();
        TestDatabaseUtil.clearPatientsTable();
    }

    @BeforeEach
    void createTestPatient() {
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
    @Order(1)
    void testInsertPatient() {
        boolean result = dao.insertPatient(testPatient);
        assertTrue(result);
    }

    @Test
    @Order(2)
    void testFindPatientById() {
        Patient found = dao.findPatientById("P1001");
        assertNotNull(found);
        assertEquals("John Doe", found.getName());
    }

    @Test
    @Order(3)
    void testUpdatePatient() {
        testPatient.setName("John D.");
        testPatient.setAge(46);
        boolean result = dao.updatePatient(testPatient);
        assertTrue(result);

        Patient updated = dao.findPatientById("P1001");
        assertEquals("John D.", updated.getName());
        assertEquals(46, updated.getAge());
    }

    @Test
    @Order(4)
    void testFindAllPatients() {
        List<Patient> list = dao.findAllPatients();
        assertFalse(list.isEmpty());
        assertTrue(list.stream().anyMatch(p -> p.getId().equals("P1001")));
    }

    @Test
    @Order(5)
    void testDeletePatientById() {
        boolean result = dao.deletePatientById("P1001");
        assertTrue(result);

        Patient afterDelete = dao.findPatientById("P1001");
        assertNull(afterDelete);
    }

    @AfterAll
    static void tearDown() {
        TestDatabaseUtil.clearPatientsTable();
    }
}
