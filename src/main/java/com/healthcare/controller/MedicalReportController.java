package com.healthcare.controller;

import java.util.Optional;

import com.healthcare.model.Account;
import com.healthcare.model.MedicalReport;
import com.healthcare.model.Patient;
import com.healthcare.repository.AccountRepository;
import com.healthcare.repository.MedicalReportRepository;
import com.healthcare.repository.PatientRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MedicalReportController {
    private final PatientRepository patientRepository;
    private final MedicalReportRepository reportRepository;
    private final AccountRepository accountRepository;

    public MedicalReportController(PatientRepository patientRepository,
                                   MedicalReportRepository reportRepository,
                                   AccountRepository accountRepository) {
        this.patientRepository = patientRepository;
        this.reportRepository = reportRepository;
        this.accountRepository = accountRepository;
    }

    @GetMapping("/reports/new")
    public String newReport(@RequestParam(required = false) String patientId, Model model) {
        MedicalReport report = new MedicalReport();
        report.setPatientId(patientId);
        model.addAttribute("report", report);
        return "report-form";
    }

    @PostMapping("/reports")
    public String createReport(@ModelAttribute("report") MedicalReport report,
                               Authentication authentication,
                               Model model) {
        report.setPatientId(report.getPatientId() == null ? "" : report.getPatientId().trim());
        report.setDoctorName(report.getDoctorName() == null ? "" : report.getDoctorName().trim());
        report.setDiagnosis(report.getDiagnosis() == null ? "" : report.getDiagnosis().trim());
        report.setNotes(report.getNotes() == null ? "" : report.getNotes().trim());

        Optional<Patient> patient = patientRepository.findById(report.getPatientId());
        if (patient.isEmpty()) {
            model.addAttribute("error", "No patient was found with that patient ID.");
            return "report-form";
        }
        if (report.getDiagnosis().isEmpty()
                || !("HEALTHY".equals(report.getHealthStatus())
                || "NEEDS_ATTENTION".equals(report.getHealthStatus()))) {
            model.addAttribute("error", "Enter a diagnosis and a valid health status.");
            return "report-form";
        }

        Account doctor = accountRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new IllegalStateException("Authenticated doctor account was not found."));
        report.setDoctorName(doctor.getDisplayName());
        reportRepository.create(report);
        return "redirect:/patients/" + report.getPatientId();
    }
}
