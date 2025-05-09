-- Users table
CREATE TABLE IF NOT EXISTS users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL, -- ADMIN, DOCTOR, NURSE, PATIENT
    reference_id INT NOT NULL, -- ID of the related entity (doctor, nurse, or patient)
    is_active BOOLEAN NOT NULL DEFAULT TRUE
);

-- Patients table
CREATE TABLE IF NOT EXISTS patients (
    patient_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    age INT NOT NULL,
    gender VARCHAR(10) NOT NULL,
    disease VARCHAR(255),
    phone VARCHAR(20),
    email VARCHAR(100),
    address TEXT,
    admission_date DATE,
    status VARCHAR(20) NOT NULL DEFAULT 'OUTPATIENT' -- OUTPATIENT, ADMITTED, DISCHARGED
);

-- Doctors table
CREATE TABLE IF NOT EXISTS doctors (
    doctor_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    specialization VARCHAR(100) NOT NULL,
    is_available BOOLEAN NOT NULL DEFAULT TRUE
);

-- Nurses table
CREATE TABLE IF NOT EXISTS nurses (
    nurse_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    department VARCHAR(100) NOT NULL,
    is_available BOOLEAN NOT NULL DEFAULT TRUE
);

-- Rooms table
CREATE TABLE IF NOT EXISTS rooms (
    room_id INT PRIMARY KEY AUTO_INCREMENT,
    room_number VARCHAR(20) NOT NULL UNIQUE,
    room_type VARCHAR(20) NOT NULL, -- GENERAL, PRIVATE, ICU, OPERATION
    is_available BOOLEAN NOT NULL DEFAULT TRUE,
    capacity INT NOT NULL,
    price_per_day DECIMAL(10,2) NOT NULL,
    current_patient_id INT DEFAULT -1,
    FOREIGN KEY (current_patient_id) REFERENCES patients(patient_id)
);

-- Appointments table
CREATE TABLE IF NOT EXISTS appointments (
    appointment_id INT PRIMARY KEY AUTO_INCREMENT,
    patient_id INT NOT NULL,
    doctor_id INT NOT NULL,
    appointment_datetime DATETIME NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'SCHEDULED', -- SCHEDULED, COMPLETED, CANCELLED, NO_SHOW
    type VARCHAR(20) NOT NULL, -- REGULAR, URGENT, FOLLOW_UP
    notes TEXT,
    cost DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (patient_id) REFERENCES patients(patient_id),
    FOREIGN KEY (doctor_id) REFERENCES doctors(doctor_id)
);

-- Medical Records table
CREATE TABLE IF NOT EXISTS medical_records (
    record_id INT PRIMARY KEY AUTO_INCREMENT,
    patient_id INT NOT NULL,
    doctor_id INT NOT NULL,
    record_date DATETIME NOT NULL,
    diagnosis TEXT NOT NULL,
    treatment TEXT NOT NULL,
    prescription TEXT,
    notes TEXT,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE', -- ACTIVE, ARCHIVED
    FOREIGN KEY (patient_id) REFERENCES patients(patient_id),
    FOREIGN KEY (doctor_id) REFERENCES doctors(doctor_id)
);

-- Bills table
CREATE TABLE IF NOT EXISTS bills (
    bill_id INT PRIMARY KEY AUTO_INCREMENT,
    patient_id INT NOT NULL,
    total_amount DECIMAL(10,2) NOT NULL,
    billing_date DATE NOT NULL,
    payment_status VARCHAR(20) NOT NULL DEFAULT 'PENDING', -- PENDING, PAID, PARTIAL
    payment_type VARCHAR(20) NOT NULL, -- CASH, CREDIT, INSURANCE
    FOREIGN KEY (patient_id) REFERENCES patients(patient_id)
);

-- Doctor Schedule table
CREATE TABLE IF NOT EXISTS doctor_schedule (
    schedule_id INT PRIMARY KEY AUTO_INCREMENT,
    doctor_id INT NOT NULL,
    schedule_datetime DATETIME NOT NULL,
    FOREIGN KEY (doctor_id) REFERENCES doctors(doctor_id)
);

-- Nurse Schedule table
CREATE TABLE IF NOT EXISTS nurse_schedule (
    schedule_id INT PRIMARY KEY AUTO_INCREMENT,
    nurse_id INT NOT NULL,
    schedule_datetime DATETIME NOT NULL,
    FOREIGN KEY (nurse_id) REFERENCES nurses(nurse_id)
); 