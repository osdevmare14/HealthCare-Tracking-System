<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Create report | CareTrack</title>
    <link rel="stylesheet" href="<c:url value='/assets/css/styles.css'/>">
</head>
<body>
<header class="topbar">
    <a class="brand" href="<c:url value='/'/>"><span class="brand-mark">+</span> CareTrack</a>
    <nav class="topbar-actions"><a class="topbar-link" href="<c:url value='/'/>">Home</a><form action="<c:url value='/logout'/>" method="post" class="inline-form"><sec:csrfInput/><button class="topbar-link link-button" type="submit">Log out</button></form></nav>
</header>
<main class="form-layout">
    <section class="form-intro">
        <p class="eyebrow">Doctor workspace</p>
        <h1>Document a<br>patient visit.</h1>
        <p class="muted">Record the diagnosis and care notes, then choose the current health assessment for this visit.</p>
        <div class="info-panel"><strong>Current status</strong><span>The latest report becomes the patient's current health status.</span></div>
    </section>
    <section class="form-card">
        <h2>Medical report</h2>
        <p class="muted form-subtitle">All fields marked with * are required.</p>
        <c:if test="${not empty error}"><div class="alert" role="alert"><c:out value="${error}"/></div></c:if>
        <form action="<c:url value='/reports'/>" method="post" class="stack-form">
            <sec:csrfInput/>
            <label for="patientId">Patient ID <span class="required">*</span></label>
            <input id="patientId" name="patientId" value="<c:out value='${report.patientId}'/>" maxlength="40" placeholder="PAT-..." required>

            <label for="diagnosis">Diagnosis <span class="required">*</span></label>
            <input id="diagnosis" name="diagnosis" value="<c:out value='${report.diagnosis}'/>" maxlength="500" required>

            <label for="notes">Care notes</label>
            <textarea id="notes" name="notes" rows="4" placeholder="Treatment, recommendations, or follow-up"><c:out value="${report.notes}"/></textarea>

            <label for="healthStatus">Health assessment <span class="required">*</span></label>
            <select id="healthStatus" name="healthStatus" required>
                <option value="">Choose an assessment</option>
                <option value="HEALTHY" ${report.healthStatus == 'HEALTHY' ? 'selected' : ''}>Healthy</option>
                <option value="NEEDS_ATTENTION" ${report.healthStatus == 'NEEDS_ATTENTION' ? 'selected' : ''}>Needs attention</option>
            </select>

            <button type="submit" class="button button-primary button-full">Save medical report <span aria-hidden="true">→</span></button>
        </form>
    </section>
</main>
</body>
</html>
