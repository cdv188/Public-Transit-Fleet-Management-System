<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
    // Check if user is logged in
    if (session.getAttribute("user") == null) {
        response.sendRedirect("login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <title>Reports - PTFMS</title>
    <link rel="stylesheet" type="text/css" href="commands.css">
</head>
<body>
    <jsp:include page="header.jsp" />
    <div class="container">
        <div class="nav">
            <a href="dashboard.jsp">Dashboard</a>
            <a href="ShowVehicleList">Vehicle List</a>
            <a href="ShowMaintenance">Maintenance Dashboard</a>
            <a href="gpsDashboard.jsp">GPS Dashboard</a>
        </div>
        
        <h1>Fleet Management Reports</h1>
        <p>Select a report to generate:</p>
        
        <div class="report-card">
            <h3>Fuel Cost Analysis Report</h3>
            <p>Analyze fuel consumption and costs across the entire fleet. 
            View total costs per vehicle and identify vehicles with high consumption.</p>
            <a href="controller?action=generateReport&reportType=fuelCost" class="btn">Generate Report</a>
        </div>
        
        <div class="report-card">
            <h3>Operator Performance Report</h3>
            <p>View operator attendance, on-time rates, and service hours. 
            Track operator status logs and identify top performers.</p>
            <a href="controller?action=generateReport&reportType=operatorPerformance" class="btn">Generate Report</a>
        </div>
    </div>
    <jsp:include page="footer.jsp" />
</body>
</html>