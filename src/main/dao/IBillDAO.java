package dao;

import entities.Bill;

import java.util.List;

public interface IBillDAO {
    boolean insertBill(Bill bill);
    boolean updateBill(int billId, Bill newBill);
    boolean updateStatus(int billId, String status);
    Bill findById(int billId);
    List<Bill> findAll();
    List<Bill> findByPatientId(int patientId);
    List<Bill> findByStatus(String status);
    List<Bill> findByType(String type);
    List<Bill> findByDate(java.sql.Date date);
    List<Bill> findBetweenDates(java.time.LocalDate startDate, java.time.LocalDate endDate);
    List<Bill> findByMonthAndYear(int month, int year);
}
