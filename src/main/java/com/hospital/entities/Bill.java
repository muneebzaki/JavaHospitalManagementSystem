package com.hospital.entities;

import java.sql.Date;

public class Bill {
    private int billId;
    private int patientId;
    private double totalAmount;
    private Date billingDate;
    private String paymentStatus;
    private String paymentType;

    public Bill(int billId, int patientId, double totalAmount, Date billingDate,
                String paymentStatus, String paymentType) {
        this.billId = billId;
        this.patientId = patientId;
        this.totalAmount = totalAmount;
        this.billingDate = billingDate;
        this.paymentStatus = paymentStatus;
        this.paymentType = paymentType;
    }

    public int getBillId() { return billId; }
    public int getPatientId() { return patientId; }
    public double getTotalAmount() { return totalAmount; }
    public Date getBillingDate() { return billingDate; }
    public String getPaymentStatus() { return paymentStatus; }
    public String getPaymentType() { return paymentType; }

    @Override
    public String toString() {
        return "Bill ID: " + billId + ", Patient ID: " + patientId + ", Amount: " + totalAmount;
    }
}
