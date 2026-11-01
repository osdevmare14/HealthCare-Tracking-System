<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>CareTrack | Patient health records</title>
    <link rel="stylesheet" href="<c:url value='/assets/css/styles.css'/>">
</head>
<body>
<header class="topbar">
    <a class="brand" href="<c:url value='/'/>"><span class="brand-mark">+</span> CareTrack</a>
    <nav class="topbar-actions">
        <sec:authorize access="isAnonymous()">
            <a class="topbar-link" href="<c:url value='/login/doctor'/>">Doctor login</a>
            <a class="button button-primary button-small" href="<c:url value='/login/patient'/>">Patient login</a>
        </sec:authorize>
        <sec:authorize access="isAuthenticated()">
            <a class="topbar-link" href="<c:url value='/dashboard'/>">My dashboard</a>
            <form action="<c:url value='/logout'/>" method="post" class="inline-form"><sec:csrfInput/><button class="topbar-link link-button" type="submit">Log out</button></form>
        </sec:authorize>
    </nav>
</header>
<main class="page-shell">
    <section class="hero">
        <div class="hero-copy">
            <p class="eyebrow">A clearer picture of care</p>
            <h1>Health records,<br>all in one place.</h1>
            <p class="hero-text">Patients can securely access their own health history. Doctors can record visits after signing in with an account provided by the administrator.</p>
            <div class="hero-actions">
                <sec:authorize access="isAnonymous()">
                    <a class="button button-primary" href="<c:url value='/signup'/>">Create a patient account</a>
                    <a class="button button-secondary" href="<c:url value='/login/admin'/>">Administrator login</a>
                </sec:authorize>
                <sec:authorize access="hasRole('PATIENT')">
                    <a class="button button-primary" href="<c:url value='/patients/me'/>">View my health record</a>
                </sec:authorize>
                <sec:authorize access="hasRole('DOCTOR')">
                    <a class="button button-primary" href="<c:url value='/reports/new'/>">Create a report</a>
                </sec:authorize>
                <sec:authorize access="hasRole('ADMIN')">
                    <a class="button button-primary" href="<c:url value='/admin/doctors'/>">Manage doctor access</a>
                </sec:authorize>
            </div>
        </div>
        <aside class="lookup-card">
            <div class="card-icon">↗</div>
            <p class="eyebrow">Separate, secure access</p>
            <h2>Sign in to your workspace</h2>
            <p class="muted">Choose the correct portal to sign in. Patient accounts are self-registered; doctor accounts are created by an administrator.</p>
            <div class="stack-form">
                <a class="button button-primary button-full" href="<c:url value='/login/patient'/>">Patient sign in <span aria-hidden="true">→</span></a>
                <a class="button button-secondary button-full" href="<c:url value='/login/doctor'/>">Doctor sign in <span aria-hidden="true">→</span></a>
                <a class="portal-admin-link" href="<c:url value='/login/admin'/>">Administrator sign in</a>
            </div>
        </aside>
    </section>
    <section class="feature-row">
        <article class="feature-card">
            <span class="feature-number">01</span>
            <h3>Private patient accounts</h3>
            <p>Patients sign in with their own account to view only their health record.</p>
        </article>
        <article class="feature-card">
            <span class="feature-number">02</span>
            <h3>Admin-approved doctors</h3>
            <p>Only doctors invited by an administrator can create medical reports.</p>
        </article>
        <article class="feature-card">
            <span class="feature-number">03</span>
            <h3>History and current status</h3>
            <p>The latest doctor assessment sets the current status; earlier reports remain available.</p>
        </article>
    </section>
    <p class="privacy-note">Use secure passwords and keep account credentials private. This demo still needs production-grade privacy and deployment safeguards.</p>
</main>
</body>
</html>
