import com.hospital.controller.BillManager;
import com.hospital.dao.BillDAO;
import com.hospital.entities.Bill;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BillManagerTest {

    @Mock
    private BillDAO billDAO;

    private BillManager billManager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Create a new instance of BillManager with the mocked BillDAO
        billManager = new BillManager(billDAO);
    }

    @Test
    void generateBill_ValidInput_ReturnsTrue() {
        // Arrange
        int patientId = 1;
        double amount = 100.0;
        String status = "PENDING";
        when(billDAO.insertBill(any(Bill.class))).thenReturn(true);

        // Act
        boolean result = billManager.generateBill(patientId, amount, status);

        // Assert
        assertTrue(result);
        verify(billDAO).insertBill(any(Bill.class));
    }

    @Test
    void getAllBills_ReturnsListOfBills() {
        // Arrange
        List<Bill> expectedBills = Arrays.asList(
            new Bill(1, 1, 100.0, Date.valueOf(LocalDate.now()), "PENDING"),
            new Bill(2, 2, 200.0, Date.valueOf(LocalDate.now()), "PAID")
        );
        when(billDAO.findAll()).thenReturn(expectedBills);

        // Act
        List<Bill> actualBills = billManager.getAllBills();

        // Assert
        assertEquals(expectedBills, actualBills);
        verify(billDAO).findAll();
    }

    @Test
    void getBillsByPatientId_ValidPatientId_ReturnsPatientBills() {
        // Arrange
        int patientId = 1;
        List<Bill> expectedBills = Arrays.asList(
            new Bill(1, patientId, 100.0, Date.valueOf(LocalDate.now()), "PENDING"),
            new Bill(2, patientId, 150.0, Date.valueOf(LocalDate.now()), "PAID")
        );
        when(billDAO.findByPatientId(patientId)).thenReturn(expectedBills);

        // Act
        List<Bill> actualBills = billManager.getBillsByPatientId(patientId);

        // Assert
        assertEquals(expectedBills, actualBills);
        verify(billDAO).findByPatientId(patientId);
    }

    @Test
    void markBillAsPaid_ValidBillId_ReturnsTrue() {
        // Arrange
        int billId = 1;
        when(billDAO.updateStatus(billId, "PAID")).thenReturn(true);

        // Act
        boolean result = billManager.markBillAsPaid(billId);

        // Assert
        assertTrue(result);
        verify(billDAO).updateStatus(billId, "PAID");
    }

    @Test
    void cancelBill_ValidBillId_ReturnsTrue() {
        // Arrange
        int billId = 1;
        when(billDAO.updateStatus(billId, "CANCELLED")).thenReturn(true);

        // Act
        boolean result = billManager.cancelBill(billId);

        // Assert
        assertTrue(result);
        verify(billDAO).updateStatus(billId, "CANCELLED");
    }

    @Test
    void generateBill_DAOFailure_ReturnsFalse() {
        // Arrange
        int patientId = 1;
        double amount = 100.0;
        String status = "PENDING";
        when(billDAO.insertBill(any(Bill.class))).thenReturn(false);

        // Act
        boolean result = billManager.generateBill(patientId, amount, status);

        // Assert
        assertFalse(result);
        verify(billDAO).insertBill(any(Bill.class));
    }

    @Test
    void markBillAsPaid_DAOFailure_ReturnsFalse() {
        // Arrange
        int billId = 1;
        when(billDAO.updateStatus(billId, "PAID")).thenReturn(false);

        // Act
        boolean result = billManager.markBillAsPaid(billId);

        // Assert
        assertFalse(result);
        verify(billDAO).updateStatus(billId, "PAID");
    }

    @Test
    void cancelBill_DAOFailure_ReturnsFalse() {
        // Arrange
        int billId = 1;
        when(billDAO.updateStatus(billId, "CANCELLED")).thenReturn(false);

        // Act
        boolean result = billManager.cancelBill(billId);

        // Assert
        assertFalse(result);
        verify(billDAO).updateStatus(billId, "CANCELLED");
    }
}