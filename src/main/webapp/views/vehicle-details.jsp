
<%-- 
    Document   : vehicle-details
    Author     : Chester
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Vehicle Details - ${vehicle.number} - PTFMS</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="views/vehicle.css">
    <style>
        .success-message {
            color: #155724;
            background-color: #d4edda;
            border: 1px solid #c3e6cb;
            padding: 10px;
            border-radius: 4px;
            margin-bottom: 15px;
        }
        .btn-update {
            background-color: #28a745;
            color: white;
            text-decoration: none;
            padding: 8px 16px;
            border-radius: 4px;
            margin-left: 10px;
        }
        .btn-update:hover {
            background-color: #218838;
            color: white;
            text-decoration: none;
        }
    </style>
</head>
<body>
    <jsp:include page="../header.jsp" />
    
    <div class="container-id">
        <div class="nav">
            <a href="ShowVehicleList">Back to Vehicle List</a>
            <a href="ShowMaintenance">Maintenance Dashboard</a>
            <a href="reports.jsp">Reports</a>
            <a href="gpsDashboard.jsp">GPS Dashboard</a>
            <a href="dashboard.jsp">Dashboard</a>
        </div>
        
        <h1>Vehicle Details: ${vehicle.number}</h1>
        
        <c:if test="${not empty successMessage}">
            <div class="success-message">
                ${successMessage}
            </div>
        </c:if>
        
        <div class="detail-row">
            <span class="detail-label">Vehicle ID:</span>
            <span class="detail-value">${vehicle.vehicleId}</span>
        </div>
        
        <div class="detail-row">
            <span class="detail-label">Vehicle Number:</span>
            <span class="detail-value">${vehicle.number}</span>
        </div>
        
        <div class="detail-row">
            <span class="detail-label">Vehicle Type:</span>
            <span class="detail-value">${vehicle.vehicleType}</span>
        </div>
        
        <div class="detail-row">
            <span class="detail-label">Fuel Type:</span>
            <span class="detail-value">${vehicle.fuelType}</span>
        </div>
        
        <div class="detail-row">
            <span class="detail-label">Consumption Rate:</span>
            <span class="detail-value">${vehicle.consumptionRate}</span>
        </div>
        
        <div class="detail-row">
            <span class="detail-label">Maximum Passengers:</span>
            <span class="detail-value">${vehicle.maxCapacity}</span>
        </div>
        
        <div class="detail-row">
            <span class="detail-label">Assigned Route:</span>
            <span class="detail-value">${vehicle.route}</span>
        </div>
        
        <div style="margin-top: 30px;">
            <a href="ShowVehicleList" class="btn">Back to List</a>
            
            <c:if test="${isManager}">
                <a href="ShowMaintenance?action=schedule&vehicleId=${vehicle.vehicleId}" class="btn">Schedule Maintenance</a>
                <a href="UpdateVehicle?vehicleId=${vehicle.vehicleId}" class="btn-update">Update Vehicle</a>
                
                <!-- Delete Vehicle Form -->
                <form method="post" style="display: inline-block; margin-left: 10px;">
                    <input type="hidden" name="action" value="delete">
                    <input type="hidden" name="vehicleId" value="${vehicle.vehicleId}">
                    <button type="submit" class="btn btn-danger" 
                            style="background-color: #dc3545; color: white; border: none; padding: 8px 16px; cursor: pointer; border-radius: 4px;">
                        Delete Vehicle
                    </button>
                </form>
            </c:if>
        </div>
    </div>
    
    <jsp:include page="../footer.jsp" />
</body>
</html>