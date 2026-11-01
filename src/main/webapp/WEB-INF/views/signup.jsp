<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Patient signup | CareTrack</title>
    <link rel="stylesheet" href="<c:url value='/assets/css/styles.css'/>">
</head>
<body>
<header class="topbar">
    <a class="brand" href="<c:url value='/'/>"><span class="brand-mark">+</span> CareTrack</a>
    <a class="topbar-link" href="<c:url value='/login/patient'/>">Already registered? Sign in</a>
</header>
<main class="form-layout">
    <section class="form-intro">
        <p class="eyebrow">Patient signup</p>
        <h1>Your health<br>record starts here.</h1>
        <p class="muted">Create a patient account to privately access your health status and medical history.</p>
        <div class="info-panel"><strong>Private by account</strong><span>Your sign-in is linked to your patient record. Only your account can access it.</span></div>
    </section>
    <section class="form-card">
        <h2>Create your account</h2>
        <p class="muted form-subtitle">A unique patient ID will be created automatically.</p>
        <c:if test="${not empty error}"><div class="alert" role="alert"><c:out value="${error}"/></div></c:if>
        <form action="<c:url value='/signup'/>" method="post" class="stack-form">
            <sec:csrfInput/>
            <label for="fullName">Full name <span class="required">*</span></label>
            <input id="fullName" name="fullName" value="<c:out value='${patient.fullName}'/>" maxlength="150" autocomplete="name" required>
            <label for="username">Email <span class="required">*</span></label>
            <input id="username" name="username" type="email" value="<c:out value='${usernameValue}'/>" maxlength="150" autocomplete="email" required>
            <label for="password">Password <span class="required">*</span></label>
            <input id="password" name="password" type="password" minlength="8" maxlength="72" autocomplete="new-password" required>
            <label for="dateOfBirth">Date of birth <span class="required">*</span></label>
            <input id="dateOfBirth" name="dateOfBirth" type="date" value="<c:out value='${dateOfBirthValue}'/>" required>
            <label for="sex">Sex</label>
            <input id="sex" name="sex" value="<c:out value='${patient.sex}'/>" maxlength="40" placeholder="Optional">
            <label for="contactNumber">Contact number</label>
            <input id="contactNumber" name="contactNumber" type="tel" value="<c:out value='${patient.contactNumber}'/>" maxlength="40" placeholder="Optional">
            <button type="submit" class="button button-primary button-full">Create patient account <span aria-hidden="true">→</span></button>
        </form>
    </section>
</main>
</body>
</html>
