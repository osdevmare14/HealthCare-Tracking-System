<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Doctor access | CareTrack Admin</title>
    <link rel="stylesheet" href="<c:url value='/assets/css/styles.css'/>">
</head>
<body>
<header class="topbar">
    <a class="brand" href="<c:url value='/'/>"><span class="brand-mark">+</span> CareTrack <span class="admin-label">ADMIN</span></a>
    <nav class="topbar-actions">
        <a class="topbar-link" href="<c:url value='/'/>">Home</a>
        <form action="<c:url value='/logout'/>" method="post" class="inline-form"><sec:csrfInput/><button class="topbar-link link-button" type="submit">Log out</button></form>
    </nav>
</header>
<main class="admin-layout">
    <section class="admin-heading">
        <p class="eyebrow">Administrator workspace</p>
        <h1>Doctor access</h1>
        <p class="muted">Create doctor accounts here. Only accounts created by an administrator can sign in to the doctor portal and write reports.</p>
    </section>
    <section class="form-card admin-form-card">
        <h2>Invite a doctor</h2>
        <p class="muted form-subtitle">Share the temporary password with the doctor through a secure channel.</p>
        <c:if test="${not empty error}"><div class="alert" role="alert"><c:out value="${error}"/></div></c:if>
        <c:if test="${not empty success}"><div class="success-message" role="status"><c:out value="${success}"/></div></c:if>
        <form action="<c:url value='/admin/doctors'/>" method="post" class="stack-form">
            <sec:csrfInput/>
            <label for="displayName">Doctor's full name <span class="required">*</span></label>
            <input id="displayName" name="displayName" maxlength="150" required>
            <label for="username">Username or email <span class="required">*</span></label>
            <input id="username" name="username" maxlength="150" autocomplete="off" required>
            <label for="password">Temporary password <span class="required">*</span></label>
            <input id="password" name="password" type="password" minlength="8" maxlength="72" autocomplete="new-password" required>
            <button type="submit" class="button button-primary">Create doctor account <span aria-hidden="true">→</span></button>
        </form>
    </section>
    <section class="doctor-list-section">
        <div class="section-heading">
            <div><p class="eyebrow">Approved staff</p><h2>Doctor accounts</h2></div>
            <span class="report-count"><c:out value="${doctors.size()}"/> doctors</span>
        </div>
        <c:choose>
            <c:when test="${empty doctors}">
                <div class="empty-state"><h3>No doctor accounts yet</h3><p>Create an account above to give a doctor access.</p></div>
            </c:when>
            <c:otherwise>
                <div class="doctor-list">
                    <c:forEach items="${doctors}" var="doctor">
                        <article class="doctor-row">
                            <span class="doctor-avatar"><c:out value="${doctor.displayName.substring(0, 1)}"/></span>
                            <div><strong><c:out value="${doctor.displayName}"/></strong><span><c:out value="${doctor.username}"/></span></div>
                            <span class="badge badge-healthy">Doctor</span>
                        </article>
                    </c:forEach>
                </div>
            </c:otherwise>
        </c:choose>
    </section>
</main>
</body>
</html>
