package com.healthcare.repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import com.healthcare.model.MedicalReport;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class MedicalReportRepository {
    private final JdbcTemplate jdbcTemplate;

    public MedicalReportRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public MedicalReport create(MedicalReport report) {
        report.setCreatedAt(LocalDateTime.now());
        PreparedStatementCreator statementCreator = connection -> {
            java.sql.PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO medical_reports "
                            + "(patient_id, doctor_name, diagnosis, notes, health_status, created_at) "
                            + "VALUES (?, ?, ?, ?, ?, ?)",
                    new String[]{"report_id"});
            statement.setString(1, report.getPatientId());
            statement.setString(2, report.getDoctorName());
            statement.setString(3, report.getDiagnosis());
            statement.setString(4, report.getNotes());
            statement.setString(5, report.getHealthStatus());
            statement.setTimestamp(6, Timestamp.valueOf(report.getCreatedAt()));
            return statement;
        };
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(statementCreator, keyHolder);
        Number reportId = keyHolder.getKey();
        if (reportId == null) {
            throw new IllegalStateException("The database did not return a medical report ID.");
        }
        report.setReportId(reportId.longValue());
        return report;
    }

    public List<MedicalReport> findByPatientId(String patientId) {
        return jdbcTemplate.query(
                "SELECT report_id, patient_id, doctor_name, diagnosis, notes, health_status, created_at "
                        + "FROM medical_reports WHERE patient_id = ? "
                        + "ORDER BY created_at DESC, report_id DESC",
                (resultSet, rowNumber) -> {
                    MedicalReport report = new MedicalReport();
                    report.setReportId(resultSet.getLong("report_id"));
                    report.setPatientId(resultSet.getString("patient_id"));
                    report.setDoctorName(resultSet.getString("doctor_name"));
                    report.setDiagnosis(resultSet.getString("diagnosis"));
                    report.setNotes(resultSet.getString("notes"));
                    report.setHealthStatus(resultSet.getString("health_status"));
                    report.setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime());
                    return report;
                },
                patientId);
    }
}
