SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS medical_records;
DROP TABLE IF EXISTS appointments;
DROP TABLE IF EXISTS billing;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS doctors;
DROP TABLE IF EXISTS patients;

SET FOREIGN_KEY_CHECKS = 1;

CREATE TABLE patients (
                          id INT AUTO_INCREMENT PRIMARY KEY,
                          name VARCHAR(100),
                          age INT,
                          gender VARCHAR(10),
                          disease VARCHAR(100),
                          phone VARCHAR(20),
                          email VARCHAR(100),
                          address VARCHAR(255),
                          admission_date DATE
);

CREATE TABLE doctors (
                         id INT AUTO_INCREMENT PRIMARY KEY,
                         name VARCHAR(100),
                         specialization VARCHAR(100),
                         phone VARCHAR(20),
                         email VARCHAR(100)
);

CREATE TABLE users (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       username VARCHAR(50) NOT NULL UNIQUE,
                       password VARCHAR(100) NOT NULL,
                       role VARCHAR(50),
                       reference_id INT,
                       is_active BOOLEAN DEFAULT TRUE
);

CREATE TABLE billing (
                         id INT AUTO_INCREMENT PRIMARY KEY,
                         patient_id INT,
                         amount DOUBLE,
                         billing_date DATE,
                         status VARCHAR(20),
                         FOREIGN KEY (patient_id) REFERENCES patients(id) ON DELETE CASCADE
);

CREATE TABLE appointments (
                              id INT AUTO_INCREMENT PRIMARY KEY,
                              patient_id INT,
                              doctor_id INT,
                              appointment_datetime DATETIME,
                              duration_minutes INT,
                              status VARCHAR(20),
                              FOREIGN KEY (patient_id) REFERENCES patients(id) ON DELETE CASCADE,
                              FOREIGN KEY (doctor_id) REFERENCES doctors(id) ON DELETE CASCADE
);

CREATE TABLE medical_records (
                                 record_id INT AUTO_INCREMENT PRIMARY KEY,
                                 patient_id INT,
                                 doctor_id INT,
                                 record_date DATETIME,
                                 diagnosis TEXT,
                                 treatment TEXT,
                                 FOREIGN KEY (patient_id) REFERENCES patients(id) ON DELETE CASCADE,
                                 FOREIGN KEY (doctor_id) REFERENCES doctors(id) ON DELETE CASCADE
);
