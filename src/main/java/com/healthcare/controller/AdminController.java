package com.healthcare.controller;

import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

import com.healthcare.repository.AccountRepository;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AdminController {
    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[A-Za-z0-9._@+-]{3,150}$");

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminController(AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/admin/doctors")
    public String doctors(Model model) {
        model.addAttribute("doctors", accountRepository.findDoctors());
        return "admin-doctors";
    }

    @PostMapping("/admin/doctors")
    public String createDoctor(@RequestParam String displayName,
                               @RequestParam String username,
                               @RequestParam String password,
                               Model model) {
        String doctorName = displayName.trim();
        String doctorUsername = username.trim();
        if (doctorName.isEmpty() || doctorName.length() > 150
                || !USERNAME_PATTERN.matcher(doctorUsername).matches()) {
            model.addAttribute("error", "Enter a doctor's name and a username between 3 and 150 valid characters.");
        } else if (password.length() < 8 || password.getBytes(StandardCharsets.UTF_8).length > 72) {
            model.addAttribute("error", "Choose a temporary password between 8 and 72 characters.");
        } else {
            try {
                accountRepository.createDoctorAccount(
                        doctorUsername, passwordEncoder.encode(password), doctorName);
                model.addAttribute("success", "Doctor account created. Share the username and temporary password securely.");
            } catch (DuplicateKeyException exception) {
                model.addAttribute("error", "That username is already in use.");
            }
        }
        model.addAttribute("doctors", accountRepository.findDoctors());
        return "admin-doctors";
    }
}
