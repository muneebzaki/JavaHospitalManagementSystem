-- Hospital Management System Database Schema

-- Create database
CREATE DATABASE IF NOT EXISTS hospital_management;
USE hospital_management;

-- Patients table
CREATE TABLE IF NOT EXISTS patients (
    id VARCHAR(20) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    age INT NOT NULL,
    gender VARCHAR(10) NOT NULL,
    disease VARCHAR(200),
    phone VARCHAR(20),
    email VARCHAR(100),
    address TEXT,
    admission_date DATE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Doctors table
CREATE TABLE IF NOT EXISTS doctors (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    specialization VARCHAR(100) NOT NULL,
    is_available BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Nurses table
CREATE TABLE IF NOT EXISTS nurses (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    specialization VARCHAR(100),
    is_available BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Appointments table
CREATE TABLE IF NOT EXISTS appointments (
    appointment_id INT PRIMARY KEY AUTO_INCREMENT,
    patient_id VARCHAR(20) NOT NULL,
    doctor_id INT NOT NULL,
    appointment_datetime DATETIME NOT NULL,
    status ENUM('SCHEDULED', 'COMPLETED', 'CANCELLED', 'NO_SHOW') DEFAULT 'SCHEDULED',
    type ENUM('REGULAR', 'URGENT', 'FOLLOW_UP') NOT NULL,
    notes TEXT,
    cost DECIMAL(10,2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (patient_id) REFERENCES patients(id) ON DELETE CASCADE,
    FOREIGN KEY (doctor_id) REFERENCES doctors(id) ON DELETE CASCADE
);

-- Bills table
CREATE TABLE IF NOT EXISTS bills (
    bill_id INT PRIMARY KEY AUTO_INCREMENT,
    patient_id VARCHAR(20) NOT NULL,
    total_amount DECIMAL(10,2) NOT NULL,
    billing_date DATE NOT NULL,
    payment_status ENUM('PENDING', 'PAID', 'PARTIALLY_PAID') DEFAULT 'PENDING',
    payment_type ENUM('CASH', 'CARD', 'INSURANCE', 'ONLINE') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (patient_id) REFERENCES patients(id) ON DELETE CASCADE
);

-- Doctor Schedules table
CREATE TABLE IF NOT EXISTS doctor_schedules (
    schedule_id INT PRIMARY KEY AUTO_INCREMENT,
    doctor_id INT NOT NULL,
    schedule_datetime DATETIME NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (doctor_id) REFERENCES doctors(id) ON DELETE CASCADE,
    UNIQUE KEY unique_doctor_schedule (doctor_id, schedule_datetime)
);

-- Nurse Schedules table
CREATE TABLE IF NOT EXISTS nurse_schedules (
    schedule_id INT PRIMARY KEY AUTO_INCREMENT,
    nurse_id INT NOT NULL,
    schedule_datetime DATETIME NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (nurse_id) REFERENCES nurses(id) ON DELETE CASCADE,
    UNIQUE KEY unique_nurse_schedule (nurse_id, schedule_datetime)
);

-- Create indexes for better query performance
CREATE INDEX idx_patient_name ON patients(name);
CREATE INDEX idx_doctor_specialization ON doctors(specialization);
CREATE INDEX idx_appointment_datetime ON appointments(appointment_datetime);
CREATE INDEX idx_appointment_status ON appointments(status);
CREATE INDEX idx_bill_payment_status ON bills(payment_status);
CREATE INDEX idx_bill_date ON bills(billing_date); 