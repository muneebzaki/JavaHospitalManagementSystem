package com.hospital.entities;

import java.time.LocalDateTime;

public class MedicalRecord {
    private int recordId;
    private int patientId;
    private int doctorId;
    private LocalDateTime date;
    private String diagnosis;
    private String treatment;
    private String prescription;
    private String notes;
    private String status;

    public MedicalRecord(int recordId, int patientId, int doctorId, LocalDateTime date,
                         String diagnosis, String treatment, String prescription,
                         String notes, String status) {
        this.recordId = recordId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.date = date;
        this.diagnosis = diagnosis;
        this.treatment = treatment;
        this.prescription = prescription;
        this.notes = notes;
        this.status = status;
    }

    public MedicalRecord(int patientId, int doctorId, String diagnosis,
                         String treatment, String prescription, String notes) {
        this(0, patientId, doctorId, LocalDateTime.now(), diagnosis, treatment, prescription, notes, "ACTIVE");
    }

    public int getRecordId() { return recordId; }
    public int getPatientId() { return patientId; }
    public int getDoctorId() { return doctorId; }
    public LocalDateTime getDate() { return date; }
    public String getDiagnosis() { return diagnosis; }
    public String getTreatment() { return treatment; }
    public String getPrescription() { return prescription; }
    public String getNotes() { return notes; }
    public String getStatus() { return status; }

    @Override
    public String toString() {
        return "Record ID: " + recordId + ", Patient ID: " + patientId + ", Diagnosis: " + diagnosis;
    }
}
