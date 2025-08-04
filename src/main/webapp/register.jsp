<%-- 
    Document   : register
    Created on : Aug 4, 2025, 12:40:24 p.m.
    Author     : 1gr8p
--%>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Register - PTFMS</title>
    <link rel="stylesheet" type="text/css" href="css/style.css">
</head>
<body>
    <jsp:include page="header.jsp" />
    <div class="container">
        <h2>Register New Operator Account</h2>
        <form action="controller" method="post">
            <input type="hidden" name="action" value="register">
            <div class="form-group">
                <label for="name">Full Name:</label>
                <input type="text" id="name" name="name" required>
            </div>
            <div class="form-group">
                <label for="email">Email:</label>
                <input type="email" id="email" name="email" required>
            </div>
            <div class="form-group">
                <label for="password">Password:</label>
                <input type="password" id="password" name="password" required>
            </div>
            
            <!-- Display error message from RegisterUserCommand if it exists -->
            <c:if test="${not empty requestScope.error}">
                <p class="error">${requestScope.error}</p>
            </c:if>
            
            <button type="submit" class="button">Register</button>
        </form>
    </div>
    <jsp:include page="footer.jsp" />
</body>
</html>