package com.healthcare.controller;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Pattern;

import com.healthcare.model.Account;
import com.healthcare.model.MedicalReport;
import com.healthcare.model.Patient;
import com.healthcare.repository.AccountRepository;
import com.healthcare.repository.MedicalReportRepository;
import com.healthcare.repository.PatientRepository;
import com.healthcare.service.PatientSignupService;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class PatientController {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");

    private final PatientRepository patientRepository;
    private final MedicalReportRepository reportRepository;
    private final AccountRepository accountRepository;
    private final PatientSignupService signupService;

    public PatientController(PatientRepository patientRepository,
                             MedicalReportRepository reportRepository,
                             AccountRepository accountRepository,
                             PatientSignupService signupService) {
        this.patientRepository = patientRepository;
        this.reportRepository = reportRepository;
        this.accountRepository = accountRepository;
        this.signupService = signupService;
    }

    @GetMapping("/signup")
    public String newPatient(Model model) {
        model.addAttribute("patient", new Patient());
        return "signup";
    }

    @PostMapping("/signup")
    public String createPatient(@RequestParam String fullName,
                                @RequestParam String username,
                                @RequestParam String password,
                                @RequestParam(required = false) String dateOfBirth,
                                @RequestParam(required = false) String sex,
                                @RequestParam(required = false) String contactNumber,
                                Model model) {
        String trimmedName = fullName.trim();
        String trimmedUsername = username.trim().toLowerCase(Locale.ROOT);
        Patient patientInput = patientFromValues(fullName, sex, contactNumber);
        model.addAttribute("patient", patientInput);
        model.addAttribute("usernameValue", username);
        model.addAttribute("dateOfBirthValue", dateOfBirth);

        if (trimmedName.isEmpty() || trimmedName.length() > 150) {
            model.addAttribute("error", "Enter a full name of no more than 150 characters.");
            return "signup";
        }
        if (trimmedUsername.length() > 150 || !EMAIL_PATTERN.matcher(trimmedUsername).matches()) {
            model.addAttribute("error", "Enter a valid email address.");
            return "signup";
        }
        int passwordBytes = password.getBytes(StandardCharsets.UTF_8).length;
        if (password.length() < 8 || passwordBytes > 72) {
            model.addAttribute("error", "Choose a password between 8 and 72 characters.");
            return "signup";
        }

        LocalDate parsedDate;
        try {
            if (dateOfBirth == null || dateOfBirth.isEmpty()) {
                throw new DateTimeParseException("Date of birth is required", "", 0);
            }
            parsedDate = LocalDate.parse(dateOfBirth);
            if (parsedDate.isAfter(LocalDate.now())) {
                throw new DateTimeParseException("Date is in the future", dateOfBirth, 0);
            }
        } catch (DateTimeParseException exception) {
            model.addAttribute("error", "Enter a valid date of birth that is not in the future.");
            return "signup";
        }

        patientInput.setFullName(trimmedName);
        patientInput.setDateOfBirth(parsedDate);
        patientInput.setSex(sex == null ? "" : sex.trim());
        patientInput.setContactNumber(contactNumber == null ? "" : contactNumber.trim());
        try {
            signupService.register(patientInput, trimmedUsername, password);
        } catch (DuplicateKeyException exception) {
            model.addAttribute("error", "An account already exists for that email address.");
            return "signup";
        }
        return "redirect:/login/patient?registered";
    }

    @GetMapping("/patients/{patientId}")
    public String patientHistory(@PathVariable String patientId, Authentication authentication, Model model) {
        enforcePatientOwnership(patientId, authentication);
        Optional<Patient> patient = patientRepository.findById(patientId);
        if (patient.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Patient not found.");
        }
        List<MedicalReport> reports = reportRepository.findByPatientId(patientId);
        model.addAttribute("patient", patient.get());
        model.addAttribute("reports", reports);
        model.addAttribute("currentReport", reports.isEmpty() ? null : reports.get(0));
        return "patient-history";
    }

    @GetMapping("/patients/me")
    public String myPatientHistory(Authentication authentication) {
        Account account = accountRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN));
        if (!"PATIENT".equals(account.getRole()) || account.getPatientId() == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        return "redirect:/patients/" + account.getPatientId();
    }

    private void enforcePatientOwnership(String patientId, Authentication authentication) {
        boolean patientRole = authentication.getAuthorities().stream()
                .anyMatch(authority -> "ROLE_PATIENT".equals(authority.getAuthority()));
        if (patientRole) {
            Account account = accountRepository.findByUsername(authentication.getName())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN));
            if (!patientId.equals(account.getPatientId())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN);
            }
        }
    }

    private Patient patientFromValues(String fullName, String sex, String contactNumber) {
        Patient patient = new Patient();
        patient.setFullName(fullName);
        patient.setSex(sex);
        patient.setContactNumber(contactNumber);
        return patient;
    }
}
