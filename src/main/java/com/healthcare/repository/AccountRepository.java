package com.healthcare.repository;

import java.util.List;
import java.util.Optional;

import com.healthcare.model.Account;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AccountRepository {
    private static final String ACCOUNT_COLUMNS =
            "username, password_hash, role, display_name, patient_id";

    private final JdbcTemplate jdbcTemplate;

    public AccountRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<Account> findByUsername(String username) {
        return jdbcTemplate.query(
                "SELECT " + ACCOUNT_COLUMNS + " FROM user_accounts WHERE username = ?",
                (resultSet, rowNumber) -> {
                    Account account = new Account();
                    account.setUsername(resultSet.getString("username"));
                    account.setPasswordHash(resultSet.getString("password_hash"));
                    account.setRole(resultSet.getString("role"));
                    account.setDisplayName(resultSet.getString("display_name"));
                    account.setPatientId(resultSet.getString("patient_id"));
                    return account;
                },
                username).stream().findFirst();
    }

    public boolean existsByUsername(String username) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM user_accounts WHERE username = ?", Integer.class, username);
        return count != null && count > 0;
    }

    public void createPatientAccount(String username, String passwordHash, String displayName, String patientId) {
        jdbcTemplate.update(
                "INSERT INTO user_accounts (username, password_hash, role, display_name, patient_id) "
                        + "VALUES (?, ?, 'PATIENT', ?, ?)",
                username, passwordHash, displayName, patientId);
    }

    public void createDoctorAccount(String username, String passwordHash, String displayName) {
        jdbcTemplate.update(
                "INSERT INTO user_accounts (username, password_hash, role, display_name) "
                        + "VALUES (?, ?, 'DOCTOR', ?)",
                username, passwordHash, displayName);
    }

    public void createAdminAccount(String username, String passwordHash) {
        jdbcTemplate.update(
                "INSERT INTO user_accounts (username, password_hash, role, display_name) "
                        + "VALUES (?, ?, 'ADMIN', 'Administrator')",
                username, passwordHash);
    }

    public List<Account> findDoctors() {
        return jdbcTemplate.query(
                "SELECT " + ACCOUNT_COLUMNS + " FROM user_accounts WHERE role = 'DOCTOR' ORDER BY display_name",
                (resultSet, rowNumber) -> {
                    Account account = new Account();
                    account.setUsername(resultSet.getString("username"));
                    account.setRole(resultSet.getString("role"));
                    account.setDisplayName(resultSet.getString("display_name"));
                    return account;
                });
    }
}
