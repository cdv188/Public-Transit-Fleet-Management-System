<%-- 
    Document   : dashboard
    Created on : Aug 4, 2025, 12:40:38 p.m.
    Author     : 1gr8p
--%>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
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
        
        <!-- Display role-specific content -->
        <c:choose>
            <c:when test="${sessionScope.user.userType == 'Manager'}">
                <p>This is the Manager Dashboard. You have access to administrative functions.</p>
                <p>Your Role: <strong>${sessionScope.user.userType}</strong></p>
                
                <!-- Manager Quick Actions -->
                <div class="quick-actions">
                    <h3>Quick Actions</h3>
                    <a href="ShowVehicleList" class="button">Manage Vehicles</a>
                    <a href="RegisterVehicle" class="button">Register New Vehicle</a>
                    <a href="ShowMaintenance" class="button">Maintenance Dashboard</a>
                    <a href="ShowMaintenance?action=schedule" class="button">Schedule Maintenance</a>
                    <a href="gpsDashboard.jsp" class="button">GPS Dashboard</a>
                    <a href="reports.jsp" class="button">Reports</a>
                </div>
                
                <!-- Manager Features -->
                <div class="feature-list">
                    <h3>Available Features</h3>
                    <ul>
                        <li>✓ View and manage vehicle fleet</li>
                        <li>✓ Register new vehicles</li>
                        <li>✓ Schedule maintenance tasks</li>
                        <li>✓ Update maintenance status</li>
                        <li>✓ View maintenance reports</li>
                        <li>✓ Monitor system</li>
                        <li>✓ Make reports</li>
                        <li>✓ Full system access</li>
                    </ul>
                </div>
            </c:when>
            <c:otherwise>
                <p>This is the Operator Dashboard. You have access to operational functions.</p>
                <p>Your Role: <strong>${sessionScope.user.userType}</strong></p>
                
                <!-- Operator Quick Actions -->
                <div class="quick-actions">
                    <h3>Quick Actions</h3>
                    <a href="ShowVehicleList" class="button">View Vehicles</a>
                    <a href="ShowMaintenance" class="button">View Maintenance</a>
                </div>
                
                <!-- Operator Features -->
                <div class="feature-list">
                    <h3>Available Features</h3>
                    <ul>
                        <li>✓ View vehicle fleet information</li>
                        <li>✓ View vehicle details</li>
                        <li>✓ View maintenance schedules</li>
                        <li>✓ View maintenance status</li>
                        <li>✗ Register new vehicles (Manager only)</li>
                        <li>✗ Schedule maintenance (Manager only)</li>
                        <li>✗ Update maintenance status (Manager only)</li>
                        <li>✗ Monitor System (Manager only)</li>
                        <li>✗ Make Report (Manager only)</li>
                    </ul>
                </div>
            </c:otherwise>
        </c:choose>
        
        <!-- Display error messages -->
        <c:if test="${param.error == 'unauthorized'}">
            <div class="error">Access Denied: You don't have permission to perform this action.</div>
        </c:if>
        
        <c:if test="${param.error == 'access_denied'}">
            <div class="error">Access Denied: Manager privileges required for this feature.</div>
        </c:if>
        
        <!-- Common Actions -->
        <div class="common-actions">
            <a href="controller?action=logout" class="button logout">Logout</a>
        </div>
    </div>
    
    <jsp:include page="footer.jsp" />
    
    <style>
        .quick-actions {
            margin: 20px 0;
            padding: 15px;
            background-color: #f8f9fa;
            border-radius: 5px;
        }
        a{
            margin-top: 5px;
        }
        
        .feature-list {
            margin: 20px 0;
            padding: 15px;
            background-color: #e9ecef;
            border-radius: 5px;
        }
        
        .feature-list ul {
            list-style: none;
            padding: 0;
        }
        
        .feature-list li {
            padding: 5px 0;
            font-size: 14px;
        }
        
        .common-actions {
            margin-top: 30px;
            text-align: center;
        }
        
        .button.logout {
            background-color: #dc3545;
            color: white;
        }
        
        .button.logout:hover {
            background-color: #c82333;
        }
        
        .error {
            background-color: #f8d7da;
            color: #721c24;
            padding: 10px;
            border: 1px solid #f5c6cb;
            border-radius: 4px;
            margin: 10px 0;
        }
    </style>
</body>
</html>
