<%-- 
    Document   : index
    Created on : Aug 4, 2025, 12:39:40 p.m.
    Author     : Louis Tran
--%>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Welcome - PTFMS</title>
    <link rel="stylesheet" type="text/css" href="css/style.css">
</head>
<body>
    <jsp:include page="header.jsp" />
    <div class="container">
        <h1>Welcome to the PTFMS Portal</h1>
        <p>Please log in to manage fleet operations or register for an operator account.</p>
        <a href="login.jsp" class="button">Login</a>
        <a href="controller?action=navigateToRegister" class="button">Register</a>
    </div>
    <jsp:include page="footer.jsp" />
</body>
</html>