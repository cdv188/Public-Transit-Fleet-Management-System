<%-- 
    Document   : login
    Created on : Aug 4, 2025, 12:39:55 p.m.
    Author     : Louis Tran
--%>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Login - PTFMS</title>
    <link rel="stylesheet" type="text/css" href="css/style.css">
</head>
<body>
    <jsp:include page="header.jsp" />
    <div class="container">
        <h2>Login</h2>
        <form action="controller" method="post">
            <input type="hidden" name="action" value="login">
            <div class="form-group">
                <label for="email">Email:</label>
                <input type="email" id="email" name="email" required>
            </div>
            <div class="form-group">
                <label for="password">Password:</label>
                <input type="password" id="password" name="password" required>
            </div>

            <!-- Display error message from LoginCommand if it exists -->
            <c:if test="${not empty requestScope.error}">
                <p class="error">${requestScope.error}</p>
            </c:if>

            <!-- Display success message from RegisterUserCommand if it exists -->
            <c:if test="${not empty sessionScope.message}">
                <p class="message">${sessionScope.message}</p>
                <%-- Remove the message so it doesn't show up again on refresh --%>
                <c:remove var="message" scope="session" />
            </c:if>

            <button type="submit" class="button">Login</button>
        </form>
    </div>
    <jsp:include page="footer.jsp" />
</body>
</html>
