package com.healthcare.service;

import com.healthcare.model.Patient;
import com.healthcare.repository.AccountRepository;
import com.healthcare.repository.PatientRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PatientSignupService {
    private final PatientRepository patientRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public PatientSignupService(PatientRepository patientRepository,
                                AccountRepository accountRepository,
                                PasswordEncoder passwordEncoder) {
        this.patientRepository = patientRepository;
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Patient register(Patient patient, String username, String password) {
        patientRepository.create(patient);
        accountRepository.createPatientAccount(
                username, passwordEncoder.encode(password), patient.getFullName(), patient.getPatientId());
        return patient;
    }
}
