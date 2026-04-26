-- ============================================
--   Hospital Management System - Database
-- ============================================

CREATE DATABASE IF NOT EXISTS hospital_db;
USE hospital_db;

-- Patients Table
CREATE TABLE IF NOT EXISTS patients (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    age INT,
    gender VARCHAR(10),
    phone VARCHAR(15),
    address TEXT,
    blood_group VARCHAR(5),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Doctors Table
CREATE TABLE IF NOT EXISTS doctors (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    specialization VARCHAR(100),
    phone VARCHAR(15),
    email VARCHAR(100),
    available_days VARCHAR(100)
);

-- Appointments Table
CREATE TABLE IF NOT EXISTS appointments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    patient_id INT,
    doctor_id INT,
    appointment_date DATE,
    appointment_time VARCHAR(20),
    status VARCHAR(20) DEFAULT 'Scheduled',
    notes TEXT,
    FOREIGN KEY (patient_id) REFERENCES patients(id) ON DELETE CASCADE,
    FOREIGN KEY (doctor_id) REFERENCES doctors(id) ON DELETE CASCADE
);

-- Medical Records Table
CREATE TABLE IF NOT EXISTS medical_records (
    id INT AUTO_INCREMENT PRIMARY KEY,
    patient_id INT,
    doctor_id INT,
    diagnosis TEXT,
    prescription TEXT,
    record_date DATE,
    FOREIGN KEY (patient_id) REFERENCES patients(id) ON DELETE CASCADE,
    FOREIGN KEY (doctor_id) REFERENCES doctors(id) ON DELETE CASCADE
);

-- Bills Table
CREATE TABLE IF NOT EXISTS bills (
    id INT AUTO_INCREMENT PRIMARY KEY,
    patient_id INT,
    consultation_fee DOUBLE DEFAULT 0,
    medicine_fee DOUBLE DEFAULT 0,
    room_charges DOUBLE DEFAULT 0,
    other_charges DOUBLE DEFAULT 0,
    total_amount DOUBLE DEFAULT 0,
    payment_status VARCHAR(20) DEFAULT 'Pending',
    bill_date DATE,
    FOREIGN KEY (patient_id) REFERENCES patients(id) ON DELETE CASCADE
);

-- Sample Doctors Data
INSERT INTO doctors (name, specialization, phone, email, available_days) VALUES
('Dr. Rahul Sharma',  'Cardiologist',  '9876543210', 'rahul@hospital.com',  'Mon-Fri'),
('Dr. Priya Singh',   'Neurologist',   '9876543211', 'priya@hospital.com',  'Mon-Sat'),
('Dr. Amit Kumar',    'Orthopedic',    '9876543212', 'amit@hospital.com',   'Tue-Sat'),
('Dr. Sunita Patel',  'Pediatrician',  '9876543213', 'sunita@hospital.com', 'Mon-Fri'),
('Dr. Ravi Verma',    'Dermatologist', '9876543214', 'ravi@hospital.com',   'Wed-Sun');
