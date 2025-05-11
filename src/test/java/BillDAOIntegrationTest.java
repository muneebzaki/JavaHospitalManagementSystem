import com.hospital.dao.BillDAO;
import com.hospital.entities.Bill;
import com.hospital.util.TestDatabaseUtil;
import org.junit.jupiter.api.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BillDAOIntegrationTest {

    private static BillDAO billDAO;
    private static Bill testBill;
    private static int savedBillId; // Add this to store the generated ID


    @BeforeAll
    static void setUp() {
        billDAO = new BillDAO();
        TestDatabaseUtil.clearBillingTable();
    }

    @BeforeEach
    void setupTestData() {
        testBill = new Bill(
                0,  // ID will be set by database
                1,  // patient ID
                100.0,  // amount
                Date.valueOf(LocalDate.now()),
                "PENDING"
        );
    }

    @Test
    @Order(1)
    void testInsertBill() {
        // Act
        boolean result = billDAO.insertBill(testBill);
        savedBillId = testBill.getId(); // Store the generated ID
        System.out.println("Generated Bill ID: " + savedBillId);

        // Assert
        assertTrue(result);
        assertTrue(savedBillId > 0, "Bill ID should be set after insertion");
    }


    @Test
    @Order(2)
    void testFindAll() {
        // Act
        List<Bill> bills = billDAO.findAll();

        // Assert
        assertFalse(bills.isEmpty(), "Bills list should not be empty");
        Bill firstBill = bills.get(0);
        assertEquals(testBill.getPatientId(), firstBill.getPatientId());
        assertEquals(testBill.getAmount(), firstBill.getAmount());
        assertEquals(testBill.getStatus(), firstBill.getStatus());
    }

    @Test
    @Order(3)
    void testFindByPatientId() {
        // Act
        List<Bill> bills = billDAO.findByPatientId(testBill.getPatientId());
        System.out.println(bills);
        // Assert
        assertFalse(bills.isEmpty(), "Bills list should not be empty");
        Bill foundBill = bills.get(0);
        assertEquals(testBill.getPatientId(), foundBill.getPatientId());
        assertEquals(testBill.getAmount(), foundBill.getAmount());
        assertEquals(testBill.getStatus(), foundBill.getStatus());
    }

    @Test
    @Order(4)
    void testUpdateStatus() {
        // Arrange
        String newStatus = "PAID";

        // Act - use the saved ID
        boolean result = billDAO.updateStatus(savedBillId, newStatus);

        // Assert
        assertTrue(result, "Status update should succeed");

        // Verify the update
        List<Bill> bills = billDAO.findByPatientId(testBill.getPatientId());
        assertFalse(bills.isEmpty());
        assertEquals(newStatus, bills.get(0).getStatus());
    }


    @Test
    @Order(5)
    void testUpdateStatus_NonExistentBill() {
        // Act
        boolean result = billDAO.updateStatus(-1, "PAID");

        // Assert
        assertFalse(result, "Update should fail for non-existent bill");
    }

    @Test
    @Order(6)
    void testInsertBill_InvalidPatientId() {
        // Arrange
        Bill invalidBill = new Bill(0, -1, 100.0, Date.valueOf(LocalDate.now()), "PENDING");

        // Act
        boolean result = billDAO.insertBill(invalidBill);

        // Assert
        assertFalse(result, "Insert should fail for invalid patient ID");
    }

    @AfterAll
    static void tearDown() {
        TestDatabaseUtil.clearBillingTable();
    }
}