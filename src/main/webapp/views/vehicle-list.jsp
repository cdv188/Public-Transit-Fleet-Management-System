<%-- 
    Document   : vehicle-list
    Author     : Chester
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html>
<head>
    <title>Vehicle List - PTFMS</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="views/vehicle.css">
</head>
<body>
    <jsp:include page="../header.jsp" />
    
    <div class="container">
        <div class="nav">
            <c:if test="${isManager}">
                <a href="RegisterVehicle">Register Vehicle</a>
                <a href="reports.jsp">Reports</a>
                <a href="gpsDashboard.jsp">GPS Dashboard</a>
            </c:if>
            <a href="ShowMaintenance">Maintenance Dashboard</a>
            <a href="dashboard.jsp">Dashboard</a>
        </div>
        
        <h1>Vehicle Fleet Management</h1>
        <c:if test="${param.error == 'deletefailed'}">
            <div class="error">
                Failed to delete the vehicle. Please try again.
            </div>
        </c:if>
        <c:if test="${param.successMessage == 'deleted'}">
            <div class="success">
                Vehicles Successfully Deleted
            </div>
        </c:if>
        <c:if test="${not empty successMessage}">
            <div class="success">${successMessage}</div>
        </c:if>
        
        <c:if test="${not empty error}">
            <div class="error">${error}</div>
        </c:if>
        
        <c:if test="${isManager}">
            <div style="margin-bottom: 20px;">
                <a href="RegisterVehicle" class="btn btn-success">Add New Vehicle</a>
            </div>
        </c:if>
        
        <c:choose>
            <c:when test="${empty vehicles}">
                <div class="no-data">
                    <p>No vehicles registered yet.</p>
                    <c:if test="${isManager}">
                        <a href="RegisterVehicle" class="btn">Register the first vehicle</a>
                    </c:if>
                </div>
            </c:when>
            <c:otherwise>
                <div style="overflow-x: auto;">
                    <table>
                        <thead>
                            <tr>
                                <th>Vehicle Number</th>
                                <th>Type</th>
                                <th>Fuel Type</th>
                                <th>Consumption Rate</th>
                                <th>Max Passengers</th>
                                <th>Assigned Route</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="vehicle" items="${vehicles}">
                                <tr>
                                    <td>${vehicle.number}</td>
                                    <td>${vehicle.vehicleType}</td>
                                    <td>${vehicle.fuelType}</td>
                                    <td>${vehicle.consumptionRate}</td>
                                    <td>${vehicle.maxCapacity}</td>
                                    <td>${vehicle.route}</td>
                                    <td>
                                        <a href="ShowVehicleById?id=${vehicle.vehicleId}" class="btn">View Details</a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
                
                <div class="summary">
                    <p><strong>Total Vehicles:</strong> ${fn:length(vehicles)}</p>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
    
    <jsp:include page="../footer.jsp" />
</body>
</html>