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
    <style>
        .dashboard-links {
            margin: 20px 0;
        }
        .dashboard-links h3 {
            color: #333;
            margin-top: 20px;
            margin-bottom: 10px;
        }
        .button {
            display: inline-block;
            margin: 5px;
            padding: 10px 20px;
            background-color: #007bff;
            color: white;
            text-decoration: none;
            border-radius: 5px;
        }
        .button:hover {
            background-color: #0056b3;
        }
    </style>
</head>
<body>
    <jsp:include page="header.jsp" />
    <div class="container">
        <h2>Welcome, ${sessionScope.user.name}!</h2>
        <p>Your Role: <strong>${sessionScope.user.userType}</strong></p>
        
        <div class="dashboard-links">
            <h3>Fleet Management</h3>
            <a href="ShowVehicleList" class="button">Vehicle List</a>
            <a href="RegisterVehicle" class="button">Register Vehicle</a>
            
            <h3>Maintenance</h3>
            <a href="ShowMaintenance" class="button">Maintenance Dashboard</a>
            <a href="controller?action=runSystemChecks" class="button">Run System Checks</a>
            
            <h3>Monitoring & Reports</h3>
            <a href="gpsDashboard.jsp" class="button">GPS Dashboard</a>
            <a href="reports.jsp" class="button">Reports</a>
            
            <c:if test="${sessionScope.user.userType == 'Operator'}">
                <h3>Operator Functions</h3>
                <a href="controller?action=logOperatorStatus" class="button">Update Status</a>
            </c:if>
        </div>
        
        <hr style="margin: 30px 0;">
        
        <a href="controller?action=logout" class="button" style="background-color: #dc3545;">Logout</a>
    </div>
    <jsp:include page="footer.jsp" />
</body>
</html>