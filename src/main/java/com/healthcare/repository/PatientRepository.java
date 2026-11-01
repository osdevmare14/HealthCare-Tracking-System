package com.healthcare.repository;

import java.sql.Date;
import java.util.Optional;
import java.util.UUID;

import com.healthcare.model.Patient;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PatientRepository {
    private final JdbcTemplate jdbcTemplate;

    public PatientRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Patient create(Patient patient) {
        patient.setPatientId("PAT-" + UUID.randomUUID().toString().replace("-", "").toUpperCase());
        jdbcTemplate.update(
                "INSERT INTO patients (patient_id, full_name, date_of_birth, sex, contact_number) "
                        + "VALUES (?, ?, ?, ?, ?)",
                patient.getPatientId(),
                patient.getFullName(),
                Date.valueOf(patient.getDateOfBirth()),
                patient.getSex(),
                patient.getContactNumber());
        return patient;
    }

    public Optional<Patient> findById(String patientId) {
        return jdbcTemplate.query(
                "SELECT patient_id, full_name, date_of_birth, sex, contact_number "
                        + "FROM patients WHERE patient_id = ?",
                (resultSet, rowNumber) -> {
                    Patient patient = new Patient();
                    patient.setPatientId(resultSet.getString("patient_id"));
                    patient.setFullName(resultSet.getString("full_name"));
                    patient.setDateOfBirth(resultSet.getDate("date_of_birth").toLocalDate());
                    patient.setSex(resultSet.getString("sex"));
                    patient.setContactNumber(resultSet.getString("contact_number"));
                    return patient;
                },
                patientId).stream().findFirst();
    }
}
