package com.hospital.entities;

import java.time.LocalDateTime;

public class MedicalRecord {
    private int recordId;
    private int patientId;
    private int doctorId;
    private LocalDateTime date;
    private String diagnosis;
    private String treatment;

    public MedicalRecord(int recordId, int patientId, int doctorId, LocalDateTime date,
                         String diagnosis, String treatment) {
        this.recordId = recordId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.date = date;
        this.diagnosis = diagnosis;
        this.treatment = treatment;
    }

    public MedicalRecord(int patientId, int doctorId, String diagnosis, String treatment) {
        this(0, patientId, doctorId, LocalDateTime.now(), diagnosis, treatment);
    }

    public int getRecordId() { return recordId; }
    public int getPatientId() { return patientId; }
    public int getDoctorId() { return doctorId; }
    public LocalDateTime getDate() { return date; }
    public String getDiagnosis() { return diagnosis; }
    public String getTreatment() { return treatment; }

    public void setRecordId(int recordId) { this.recordId = recordId; }

    @Override
    public String toString() {
        return "MedicalRecord #" + recordId + " - Patient " + patientId + ": " + diagnosis;
    }
}
