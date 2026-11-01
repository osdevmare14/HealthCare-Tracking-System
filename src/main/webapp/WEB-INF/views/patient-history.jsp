<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Health record | CareTrack</title>
    <link rel="stylesheet" href="<c:url value='/assets/css/styles.css'/>">
</head>
<body>
<header class="topbar">
    <a class="brand" href="<c:url value='/'/>"><span class="brand-mark">+</span> CareTrack</a>
    <nav class="topbar-actions"><a class="topbar-link" href="<c:url value='/'/>">Home</a><form action="<c:url value='/logout'/>" method="post" class="inline-form"><sec:csrfInput/><button class="topbar-link link-button" type="submit">Log out</button></form></nav>
</header>
<main class="record-shell">
    <div class="record-heading">
        <div>
            <p class="eyebrow">Patient health record</p>
            <h1><c:out value="${patient.fullName}"/></h1>
            <p class="muted">Patient ID <span class="patient-id"><c:out value="${patient.patientId}"/></span></p>
        </div>
        <sec:authorize access="hasRole('DOCTOR')"><a class="button button-secondary" href="<c:url value='/reports/new'><c:param name='patientId' value='${patient.patientId}'/></c:url>">Add doctor report <span aria-hidden="true">+</span></a></sec:authorize>
    </div>

    <section class="status-card">
        <div class="status-copy">
            <p class="eyebrow">Current health status</p>
            <c:choose>
                <c:when test="${empty currentReport}">
                    <h2 class="status-unknown">No assessment yet</h2>
                    <p class="status-detail">A doctor report is needed to show a current status.</p>
                </c:when>
                <c:when test="${currentReport.healthStatus == 'HEALTHY'}">
                    <h2 class="status-healthy"><span class="status-dot"></span>Healthy</h2>
                    <p class="status-detail">Based on the latest doctor report.</p>
                </c:when>
                <c:otherwise>
                    <h2 class="status-attention"><span class="status-dot"></span>Needs attention</h2>
                    <p class="status-detail">Please follow the doctor's care notes and follow-up guidance.</p>
                </c:otherwise>
            </c:choose>
        </div>
        <c:if test="${not empty currentReport}">
            <div class="latest-report">
                <span>Latest assessment</span>
                <strong><c:out value="${currentReport.diagnosis}"/></strong>
                <small>Dr. <c:out value="${currentReport.doctorName}"/> · <c:out value="${currentReport.createdAt}"/></small>
            </div>
        </c:if>
    </section>

    <section class="patient-details">
        <div><span>Date of birth</span><strong><c:out value="${patient.dateOfBirth}"/></strong></div>
        <div><span>Sex</span><strong><c:out value="${empty patient.sex ? 'Not provided' : patient.sex}"/></strong></div>
        <div><span>Contact</span><strong><c:out value="${empty patient.contactNumber ? 'Not provided' : patient.contactNumber}"/></strong></div>
    </section>

    <section class="history-section">
        <div class="section-heading">
            <div><p class="eyebrow">Record timeline</p><h2>Medical history</h2></div>
            <span class="report-count"><c:out value="${reports.size()}"/> reports</span>
        </div>
        <c:choose>
            <c:when test="${empty reports}">
                <div class="empty-state"><span class="empty-icon">✳</span><h3>No reports yet</h3><p>Doctor visit reports will appear here as they are added.</p></div>
            </c:when>
            <c:otherwise>
                <div class="timeline">
                    <c:forEach items="${reports}" var="report">
                        <article class="report-card">
                            <div class="report-card-top">
                                <span class="report-date"><c:out value="${report.createdAt}"/></span>
                                <c:choose>
                                    <c:when test="${report.healthStatus == 'HEALTHY'}"><span class="badge badge-healthy">Healthy</span></c:when>
                                    <c:otherwise><span class="badge badge-attention">Needs attention</span></c:otherwise>
                                </c:choose>
                            </div>
                            <h3><c:out value="${report.diagnosis}"/></h3>
                            <p class="report-doctor">Reported by Dr. <c:out value="${report.doctorName}"/> · Report #<c:out value="${report.reportId}"/></p>
                            <c:if test="${not empty report.notes}"><p class="report-notes"><c:out value="${report.notes}"/></p></c:if>
                        </article>
                    </c:forEach>
                </div>
            </c:otherwise>
        </c:choose>
    </section>
    <p class="privacy-note">For emergencies, contact local emergency services. This record is not a substitute for professional medical advice.</p>
</main>
</body>
</html>
