# CareTrack Healthcare Tracking System

A small Spring MVC application for patient accounts, admin-approved doctor accounts, medical reports, and patient health history.

## Technology

- Java 11
- Spring MVC 5 and Spring JDBC (`JdbcTemplate`)
- JSP and JSTL
- Spring Security with BCrypt password hashing and CSRF protection
- MySQL 8
- Maven WAR deployed to Apache Tomcat 9

## Features

- Patients can sign up with an email and password; each signup creates a unique `PAT-...` patient ID and linked health record.
- Patients can view only their own patient record after signing in.
- Doctors cannot self-register. An administrator creates doctor accounts; only doctor accounts can write medical reports.
- Admin, doctor, and patient accounts have separate sign-in pages and role-protected routes.
- Create doctor reports with a diagnosis, care notes, and a health assessment.
- The newest report determines the displayed current status. If there are no reports, the status is shown as not assessed.
- Passwords are stored as BCrypt hashes. Patient signup and doctor provisioning enforce password length limits.
- Synthetic demonstration accounts and sample reports are seeded once when they are missing. Set `DEMO_DATA_ENABLED=false` in Tomcat to disable this for non-demo deployments.
- Database tables are initialized from `src/main/resources/schema.sql` when the application starts.

## Set up MySQL

Create the database before deploying:

```sql
CREATE DATABASE healthcare_tracker CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

By default, the application connects to `jdbc:mysql://localhost:3306/healthcare_tracker` with username `root` and an empty password. Configure credentials before starting Tomcat using these environment variables:

- `DB_URL` (optional; overrides the default JDBC URL)
- `DB_USERNAME`
- `DB_PASSWORD`

For example, in Windows PowerShell for the current terminal:

```powershell
$env:DB_USERNAME = "root"
$env:DB_PASSWORD = "your-mysql-password"
```

The MySQL user needs permission to create and modify tables in `healthcare_tracker`.

## Administrator account

On the first application startup, an administrator account is created if the configured admin username does not already exist. The default username is `admin1826`. Set `ADMIN_PASSWORD` to the requested bootstrap password before starting Tomcat for the first time. The password is supplied through the environment and is not stored in the project source.

Set `ADMIN_USERNAME` and `ADMIN_PASSWORD` in the Tomcat environment **before the first startup** to choose bootstrap credentials. Once the account exists, changing these environment variables does not change its stored password. Use a strong private password for any non-demo deployment.

For a Tomcat started from the same Windows PowerShell window:

```powershell
$env:ADMIN_USERNAME = "admin1826"
$env:ADMIN_PASSWORD = "<set the admin password you chose>"
```

Sign in at `/login/admin`, then use **Doctor access** to provision doctor usernames and temporary passwords. Doctors sign in at `/login/doctor`; patients create an account at `/signup` and sign in at `/login/patient`.

## Demonstration accounts and data

The app seeds fictional sample records and demo accounts on startup when they are not already present:

| Portal | Username | Password |
|---|---|---|
| Doctor | `demo.doctor` | `DemoDoctor2026!` |
| Patient | `demo.healthy@example.com` | `DemoPatient2026!` |
| Patient | `demo.followup@example.com` | `DemoPatient2026!` |
| Patient | `demo.new@example.com` | `DemoPatient2026!` |
| Patient | `demo.no-report@example.com` | `DemoPatient2026!` |

The sample records demonstrate a patient with multiple healthy reports, a patient whose latest report needs attention, a patient with a newly recorded report, and a patient with no reports yet. These synthetic records are for UI demonstration only, not medical guidance. Set `DEMO_DATA_ENABLED=false` in the Tomcat environment to disable demo seeding. Change or remove the demo credentials and data before any production deployment.

## Build and deploy

Build the WAR from the project directory:

```powershell
mvn clean package
```

Copy `target\healthcare-tracker.war` into the Tomcat 9 `webapps` folder and start Tomcat. Open:

```text
http://localhost:8080/healthcare-tracker/
```

Tomcat 9 is required for the included `javax.servlet` dependencies; Tomcat 10 uses the incompatible `jakarta.servlet` namespace.

## Basic workflow

1. Sign in as the administrator and create a doctor account.
2. The patient creates an account using the patient signup page.
3. The doctor signs in and creates a report using the patient's ID.
4. The patient signs in to see their current status and history.

## Important

This is a learning/demo application. Although role-based login and patient record ownership checks are implemented, it is not production-ready for real patient information. Use strong, private admin credentials and add HTTPS, audit logging, account recovery, stronger operational protections, and applicable healthcare privacy controls before deployment. Existing patient records created before this account feature are not automatically linked to a patient login; contact an administrator to plan account linkage before using those records.
