package com.hospital.entities;

import java.sql.Date;

public class Bill {
    private int id;
    private int patientId;
    private double amount;
    private Date billingDate;
    private String status;

    public Bill(int id, int patientId, double amount, Date billingDate, String status) {
        this.id = id;
        this.patientId = patientId;
        this.amount = amount;
        this.billingDate = billingDate;
        this.status = status;
    }

    public Bill(int patientId, double amount, Date billingDate, String status) {
        this(0, patientId, amount, billingDate, status);
    }

    public int getId() { return id; }
    public int getPatientId() { return patientId; }
    public double getAmount() { return amount; }
    public Date getBillingDate() { return billingDate; }
    public String getStatus() { return status; }

    public void setId(int id) { this.id = id; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return "Bill #" + id + " | Patient: " + patientId + " | Amount: " + amount + " | Status: " + status;
    }
}
