CREATE TABLE IF NOT EXISTS patients (
    patient_id VARCHAR(40) PRIMARY KEY,
    full_name VARCHAR(150) NOT NULL,
    date_of_birth DATE NOT NULL,
    sex VARCHAR(40) NOT NULL DEFAULT '',
    contact_number VARCHAR(40) NOT NULL DEFAULT '',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS medical_reports (
    report_id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    patient_id VARCHAR(40) NOT NULL,
    doctor_name VARCHAR(150) NOT NULL,
    diagnosis VARCHAR(500) NOT NULL,
    notes TEXT NOT NULL,
    health_status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_medical_reports_patient
        FOREIGN KEY (patient_id) REFERENCES patients (patient_id)
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS user_accounts (
    username VARCHAR(150) NOT NULL PRIMARY KEY,
    password_hash VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL,
    display_name VARCHAR(150) NOT NULL,
    patient_id VARCHAR(40) NULL UNIQUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_user_account_role
        CHECK (role IN ('ADMIN', 'DOCTOR', 'PATIENT')),
    CONSTRAINT fk_user_account_patient
        FOREIGN KEY (patient_id) REFERENCES patients (patient_id)
        ON DELETE CASCADE
);
