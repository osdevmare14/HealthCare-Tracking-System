<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title><c:out value="${role}"/> sign in | CareTrack</title>
    <link rel="stylesheet" href="<c:url value='/assets/css/styles.css'/>">
</head>
<body>
<header class="topbar">
    <a class="brand" href="<c:url value='/'/>"><span class="brand-mark">+</span> CareTrack</a>
    <a class="topbar-link" href="<c:url value='/'/>">Back to home</a>
</header>
<main class="auth-layout">
    <section class="auth-card">
        <div class="card-icon">+</div>
        <p class="eyebrow"><c:out value="${role}"/> portal</p>
        <h1>Welcome back.</h1>
        <p class="muted">Sign in to your <c:out value="${role}"/> account to continue.</p>
        <c:if test="${param.error != null}"><div class="alert" role="alert">Sign-in failed. Check your username and password, and make sure you are using the correct portal.</div></c:if>
        <c:if test="${param.registered != null}"><div class="success-message" role="status">Your patient account is ready. Sign in to view your health record.</div></c:if>
        <form action="<c:url value='/login'/>" method="post" class="stack-form auth-form">
            <sec:csrfInput/>
            <input type="hidden" name="portal" value="<c:out value='${role}'/>">
            <label for="username">Email or username</label>
            <input id="username" name="username" type="text" autocomplete="username" required autofocus>
            <label for="password">Password</label>
            <input id="password" name="password" type="password" autocomplete="current-password" required>
            <button type="submit" class="button button-primary button-full">Sign in <span aria-hidden="true">→</span></button>
        </form>
        <c:if test="${role == 'patient'}">
            <p class="auth-footer">New patient? <a href="<c:url value='/signup'/>">Create an account</a></p>
        </c:if>
        <div class="portal-switch">
            <span>Other sign-in pages</span>
            <a href="<c:url value='/login/patient'/>">Patient</a>
            <a href="<c:url value='/login/doctor'/>">Doctor</a>
            <a href="<c:url value='/login/admin'/>">Admin</a>
        </div>
    </section>
</main>
</body>
</html>
