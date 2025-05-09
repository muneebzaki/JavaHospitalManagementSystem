package com.hospital.entities;

import java.sql.Date;

public class Bill {
    private int billId;
    private int patientId;
    private double total_amount;
    private Date billing_date;
    private String payment_status;
    private String payment_type;

    // Constructor for existing Bill
    public Bill(int billId, int patientId, double total_amount, Date billing_date, String payment_status, String payment_type) {
        this.patientId = patientId;
        this.total_amount = total_amount;
        this.billId = billId;
        this.billing_date = billing_date;
        this.payment_status = payment_status;
        this.payment_type = payment_type;
    }
    // Constructor for new Bill
    public Bill(int patientId, double total_amount, Date billing_date, String payment_status, String payment_type) {
        this.patientId = patientId;
        this.total_amount = total_amount;
        this.billing_date = billing_date;
        this.payment_status = payment_status;
        this.payment_type = payment_type;
    }

    // -------------------------------
    // Getters
    // -------------------------------
    public int getBillId() {
        return billId;
    }
    public int getPatientId() {
        return patientId;
    }
    public double getTotal_amount() {
        return total_amount;
    }
    public Date getBilling_date() {
        return billing_date;
    }
    public String getPayment_status() {
        return payment_status;
    }
    public String getPayment_type() {
        return payment_type;
    }

    // -------------------------------
    // Setters
    // -------------------------------
    public void setBillId(int billId) {
        this.billId = billId;
    }
    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }
    public void setTotal_amount(double total_amount) {
        this.total_amount = total_amount;
    }
    public void setBilling_date(Date billing_date) {
        this.billing_date = billing_date;
    }
    public void setPayment_status(String payment_status) {
        this.payment_status = payment_status;
    }
    public void setPayment_type(String payment_type) {
        this.payment_type = payment_type;
    }

    // -------------------------------
    // To String
    // -------------------------------
    @Override
    public String toString() {
        return "Bill{" +
                "billId=" + billId +
                ", patientId=" + patientId +
                ", total_amount=" + total_amount +
                ", billing_date=" + billing_date +
                ", payment_status='" + payment_status + '\'' +
                ", payment_type='" + payment_type + '\'' +
                '}';
    }
}
