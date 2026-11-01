package com.healthcare.service;

import com.healthcare.repository.AccountRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminAccountInitializer implements ApplicationListener<ContextRefreshedEvent> {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminAccountInitializer(AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        String username = environmentValue("ADMIN_USERNAME", "admin1826");
        String password = System.getenv("ADMIN_PASSWORD");
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalStateException("Set ADMIN_PASSWORD before starting CareTrack for the first time.");
        }
        if (!accountRepository.existsByUsername(username)) {
            accountRepository.createAdminAccount(username, passwordEncoder.encode(password));
        }
    }

    private String environmentValue(String name, String defaultValue) {
        String value = System.getenv(name);
        return value == null || value.trim().isEmpty() ? defaultValue : value;
    }
}
