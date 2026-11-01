package com.healthcare.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.healthcare.model.MedicalReport;
import com.healthcare.model.Patient;
import com.healthcare.repository.AccountRepository;
import com.healthcare.repository.MedicalReportRepository;
import com.healthcare.repository.PatientRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DemoDataInitializer implements ApplicationListener<ContextRefreshedEvent> {
    private static final String DEMO_DOCTOR_USERNAME = "demo.doctor";
    private static final String DEMO_DOCTOR_PASSWORD = "DemoDoctor2026!";
    private static final String DEMO_PATIENT_PASSWORD = "DemoPatient2026!";

    private final AccountRepository accountRepository;
    private final PatientRepository patientRepository;
    private final MedicalReportRepository reportRepository;
    private final PasswordEncoder passwordEncoder;

    public DemoDataInitializer(AccountRepository accountRepository,
                               PatientRepository patientRepository,
                               MedicalReportRepository reportRepository,
                               PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.patientRepository = patientRepository;
        this.reportRepository = reportRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() != null
                || "false".equalsIgnoreCase(System.getenv("DEMO_DATA_ENABLED"))) {
            return;
        }

        if (!accountRepository.existsByUsername(DEMO_DOCTOR_USERNAME)) {
            accountRepository.createDoctorAccount(
                    DEMO_DOCTOR_USERNAME, passwordEncoder.encode(DEMO_DOCTOR_PASSWORD), "Jordan Lee");
        }

        List<Patient> patients = new ArrayList<>();
        patients.add(createPatientIfMissing(
                "demo.healthy@example.com", "Avery Morgan", LocalDate.of(1988, 4, 12), "Female", "555-0101"));
        patients.add(createPatientIfMissing(
                "demo.followup@example.com", "Riley Taylor", LocalDate.of(1976, 9, 3), "Male", "555-0102"));
        patients.add(createPatientIfMissing(
                "demo.new@example.com", "Casey Jordan", LocalDate.of(2001, 2, 24), "Non-binary", "555-0103"));
        patients.add(createPatientIfMissing(
                "demo.no-report@example.com", "Morgan Blake", LocalDate.of(1995, 7, 18), "Female", "555-0104"));

        if (patients.get(0) != null) {
            createReport(patients.get(0), "Jordan Lee", "Routine wellness check",
                    "Fictional demo record. Routine follow-up and general wellness advice.",
                    "HEALTHY");
            createReport(patients.get(0), "Jordan Lee", "Follow-up: stable",
                    "Fictional demo record. No new concerns reported at this visit.",
                    "HEALTHY");
        }
        if (patients.get(1) != null) {
            createReport(patients.get(1), "Jordan Lee", "Follow-up recommended",
                    "Fictional demo record. Please arrange a routine follow-up with your care team.",
                    "NEEDS_ATTENTION");
        }
    }

    private Patient createPatientIfMissing(String username,
                                           String fullName,
                                           LocalDate dateOfBirth,
                                           String sex,
                                           String contactNumber) {
        if (accountRepository.existsByUsername(username)) {
            return null;
        }
        Patient patient = new Patient();
        patient.setFullName(fullName);
        patient.setDateOfBirth(dateOfBirth);
        patient.setSex(sex);
        patient.setContactNumber(contactNumber);
        patientRepository.create(patient);
        accountRepository.createPatientAccount(
                username, passwordEncoder.encode(DEMO_PATIENT_PASSWORD), fullName, patient.getPatientId());
        return patient;
    }

    private void createReport(Patient patient,
                              String doctorName,
                              String diagnosis,
                              String notes,
                              String healthStatus) {
        MedicalReport report = new MedicalReport();
        report.setPatientId(patient.getPatientId());
        report.setDoctorName(doctorName);
        report.setDiagnosis(diagnosis);
        report.setNotes(notes);
        report.setHealthStatus(healthStatus);
        reportRepository.create(report);
    }
}
