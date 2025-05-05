package dao;

import entities.Bill;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

// TODO: Implement BillDAO methods & queries (JDBC operations)

public class BillDAO implements IBillDAO {
    @Override
    public boolean insertBill(Bill bill) {
        return false;
    }

    @Override
    public boolean updateBill(int billId, Bill newBill) {
        return false;
    }

    @Override
    public boolean updateStatus(int billId, String status) {
        return false;
    }

    @Override
    public double sumChargesForPatient(int patientId) {
        return 0;
    }

    @Override
    public Bill findById(int billId) {
        return null;
    }

    @Override
    public List<Bill> findAll() {
        return List.of();
    }

    @Override
    public List<Bill> findByPatientId(int patientId) {
        return List.of();
    }

    @Override
    public List<Bill> findByStatus(String status) {
        return List.of();
    }

    @Override
    public List<Bill> findByType(String type) {
        return List.of();
    }

    @Override
    public List<Bill> findByDate(Date date) {
        return List.of();
    }

    @Override
    public List<Bill> findBetweenDates(LocalDate startDate, LocalDate endDate) {
        return List.of();
    }

    @Override
    public List<Bill> findByMonthAndYear(int month, int year) {
        return List.of();
    }

    @Override
    public double sumPaidBillsBetween(LocalDate start, LocalDate end) {
        return 0;
    }
}
