<%-- 
    Document   : dashboard
    Created on : Aug 4, 2025, 12:40:38 p.m.
    Author     : 1gr8p
--%>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Dashboard - PTFMS</title>
    <link rel="stylesheet" type="text/css" href="css/style.css">
</head>
<body>
    <jsp:include page="header.jsp" />
    <div class="container">
        <!-- Display user's name from the User object stored in the session -->
        <h2>Welcome, ${sessionScope.user.name}!</h2>
        <p>This is the Manager Dashboard. You have access to administrative functions.</p>
        <p>Your Role: <strong>${sessionScope.user.userType}</strong></p>
        <a href="controller?action=logout" class="button">Logout</a>
    </div>
    <jsp:include page="footer.jsp" />
</body>
</html>
