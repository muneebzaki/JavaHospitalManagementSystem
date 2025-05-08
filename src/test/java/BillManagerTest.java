import controller.BillManager;
import dao.IBillDAO;
import entities.Bill;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

class BillManagerTest {

    // BillManagerTest class using Mock BillDAO to simulate database operations
    // Mocking BillDAO allows us to test the BillManager without actually connecting to a database

    @Mock
    private IBillDAO billDAO;
    private BillManager billManager;
    private Bill testBill;
    private AutoCloseable closable;

    @BeforeEach
    void setUp() {
        closable = MockitoAnnotations.openMocks(this);
        // Initialize test data
        testBill = new Bill(1, 100.0, Date.valueOf(LocalDate.now()), "Unpaid", "Cash");
        // Inject mock into BillManager
        billManager = new BillManager(billDAO);
    }

    @AfterEach
    void tearDown() throws Exception {
        closable.close();
    }

    @Test
    void generateBill_ValidInput_ReturnsTrue() {
        when(billDAO.insertBill(any(Bill.class))).thenReturn(true);
        
        boolean result = billManager.generateBill(1, "Unpaid", "Cash");
        
        assertTrue(result);
        verify(billDAO).insertBill(any(Bill.class));
    }

    @Test
    void updateBill_ExistingBill_ReturnsTrue() {
        when(billDAO.findById(1)).thenReturn(testBill);
        when(billDAO.updateBill(anyInt(), any(Bill.class))).thenReturn(true);
        
        boolean result = billManager.updateBill(1, testBill);
        
        assertTrue(result);
        verify(billDAO).updateBill(eq(1), any(Bill.class));
    }

    @Test
    void updateBill_NonExistingBill_ReturnsFalse() {
        when(billDAO.findById(1)).thenReturn(null);
        
        boolean result = billManager.updateBill(1, testBill);
        
        assertFalse(result);
        verify(billDAO, never()).updateBill(anyInt(), any(Bill.class));
    }

    @Test
    void cancelBill_ExistingBill_ReturnsTrue() {
        when(billDAO.updateStatus(1, "Cancelled")).thenReturn(true);
        
        boolean result = billManager.cancelBill(1);
        
        assertTrue(result);
        verify(billDAO).updateStatus(1, "Cancelled");
    }

    @Test
    void markBillAsPaid_ValidBill_ReturnsTrue() {
        when(billDAO.updateStatus(1, "Paid")).thenReturn(true);
        
        boolean result = billManager.markBillAsPaid(1);
        
        assertTrue(result);
        verify(billDAO).updateStatus(1, "Paid");
    }

    @Test
    void isBillPaid_PaidBill_ReturnsTrue() {
        Bill paidBill = new Bill(1, 100.0, Date.valueOf(LocalDate.now()), "Paid", "Cash");
        when(billDAO.findById(1)).thenReturn(paidBill);
        
        boolean result = billManager.isBillPaid(1);
        
        assertTrue(result);
    }

    @Test
    void isBillPaid_UnpaidBill_ReturnsFalse() {
        when(billDAO.findById(1)).thenReturn(testBill);
        
        boolean result = billManager.isBillPaid(1);
        
        assertFalse(result);
    }

    @Test
    void applyDiscount_ValidDiscount_UpdatesBillAmount() {
        when(billDAO.findById(1)).thenReturn(testBill);
        when(billDAO.updateBill(eq(1), any(Bill.class))).thenReturn(true);
        
        billManager.applyDiscount(1, 20.0);
        
        assertEquals(80.0, testBill.getTotal_amount());
        verify(billDAO).updateBill(eq(1), any(Bill.class));
    }

    @Test
    void getTotalRevenueBetween_WithPaidBills_ReturnsCorrectTotal() {
        LocalDate start = LocalDate.now().minusDays(7);
        LocalDate end = LocalDate.now();
        List<Bill> bills = new ArrayList<>();
        bills.add(new Bill(1, 100.0, Date.valueOf(LocalDate.now()), "Paid", "Cash"));
        bills.add(new Bill(2, 150.0, Date.valueOf(LocalDate.now()), "Paid", "Card"));
        bills.add(new Bill(3, 200.0, Date.valueOf(LocalDate.now()), "Unpaid", "Cash"));
        
        when(billDAO.findBetweenDates(start, end)).thenReturn(bills);
        
        double total = billManager.getTotalRevenueBetween(start, end);
        
        assertEquals(250.0, total);
    }

    @Test
    void validateBill_ValidBill_ReturnsTrue() {
        Bill validBill = new Bill(1, 100.0, Date.valueOf(LocalDate.now()), "Unpaid", "Cash");
        
        boolean result = billManager.validateBill(validBill);
        
        assertTrue(result);
    }

    @Test
    void validateBill_InvalidBill_ReturnsFalse() {
        Bill invalidBill = new Bill(0, -100.0, Date.valueOf(LocalDate.now()), "Unpaid", "Cash");
        
        boolean result = billManager.validateBill(invalidBill);
        
        assertFalse(result);
    }

    @Test
    void getBillsByPaymentStatus_ReturnsFilteredBills() {
        List<Bill> paidBills = new ArrayList<>();
        paidBills.add(new Bill(1, 100.0, Date.valueOf(LocalDate.now()), "Paid", "Cash"));
        
        when(billDAO.findByStatus("Paid")).thenReturn(paidBills);
        
        List<Bill> result = billManager.getBillsByPaymentStatus("Paid");
        
        assertEquals(1, result.size());
        assertEquals("Paid", result.get(0).getPayment_status());
    }
}