package com.hospital.entities;

import java.time.LocalDate;

public class Patient {
    private String id;
    private String name;
    private int age;
    private String gender;
    private String disease;
    private String phone;
    private String email;
    private String address;
    private LocalDate admissionDate;

    public Patient(String id, String name, int age, String gender, String disease,
                   String phone, String email, String address, LocalDate admissionDate) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.disease = disease;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.admissionDate = admissionDate;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getGender() { return gender; }
    public String getDisease() { return disease; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public String getAddress() { return address; }
    public LocalDate getAdmissionDate() { return admissionDate; }

    public void setName(String name) { this.name = name; }
    public void setAge(int age) { this.age = age; }
    public void setGender(String gender) { this.gender = gender; }
    public void setDisease(String disease) { this.disease = disease; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setEmail(String email) { this.email = email; }
    public void setAddress(String address) { this.address = address; }
    public void setAdmissionDate(LocalDate admissionDate) { this.admissionDate = admissionDate; }

    @Override
    public String toString() {
        return name + " (" + id + ")";
    }
}
