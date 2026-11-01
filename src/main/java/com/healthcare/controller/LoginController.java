package com.healthcare.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class LoginController {
    @GetMapping("/login")
    public String login() {
        return "redirect:/login/patient";
    }

    @GetMapping("/login/{role}")
    public String loginPage(@PathVariable String role, Model model) {
        if (!"patient".equals(role) && !"doctor".equals(role) && !"admin".equals(role)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        model.addAttribute("role", role);
        return "login";
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication) {
        boolean admin = authentication.getAuthorities().stream()
                .anyMatch(authority -> "ROLE_ADMIN".equals(authority.getAuthority()));
        if (admin) {
            return "redirect:/admin/doctors";
        }
        boolean doctor = authentication.getAuthorities().stream()
                .anyMatch(authority -> "ROLE_DOCTOR".equals(authority.getAuthority()));
        return doctor ? "redirect:/reports/new" : "redirect:/patients/me";
    }
}
