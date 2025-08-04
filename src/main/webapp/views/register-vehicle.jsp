<%-- 
    Document   : register-vehicle
    Author     : Chester
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Register Vehicle - PTFMS</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="views/vehicle.css">
</head>
<body>
    <jsp:include page="../header.jsp" />
    
    <div class="container-register">
        <div class="nav">
            <a href="ShowVehicleList">Vehicle List</a>
            <a href="ShowMaintenance">Maintenance Dashboard</a>
            <a href="dashboard.jsp">Dashboard</a>
        </div>
        
        <h1>Register New Vehicle</h1>
        
        <!-- Display error message from request attribute -->
        <c:if test="${not empty errorMessage}">
            <div class="error">${errorMessage}</div>
        </c:if>
        
        <!-- Display success message from request attribute -->
        <c:if test="${not empty successMessage}">
            <div class="success">${successMessage}</div>
        </c:if>
        
        <form method="post" action="RegisterVehicle?action=register">
            <div class="form-group">
                <label for="vehicleNumber">Vehicle Number *</label>
                <input type="text" id="vehicleNumber" name="vehicleNumber" 
                       placeholder="e.g., BUS-101" value="${param.vehicleNumber}" required>
            </div>
            
            <div class="form-group">
                <label for="vehicleType">Vehicle Type *</label>
                <select id="vehicleType" name="vehicleType" required>
                    <option value="">Select Vehicle Type</option>
                    <option value="Diesel Bus" ${param.vehicleType == 'Diesel Bus' ? 'selected' : ''}>Diesel Bus</option>
                    <option value="Electric Light Rail" ${param.vehicleType == 'Electric Light Rail' ? 'selected' : ''}>Electric Light Rail</option>
                    <option value="Diesel-Electric Train" ${param.vehicleType == 'Diesel-Electric Train' ? 'selected' : ''}>Diesel-Electric Train</option>
                </select>
            </div>
            
            <div class="form-group">
                <label for="fuelType">Fuel Type *</label>
                <input type="text" id="fuelType" name="fuelType" 
                       placeholder="e.g., Diesel, Electricity, CNG" 
                       value="${param.fuelType}" required>
            </div>
            
            <div class="form-group">
                <label for="consumptionRate">Consumption Rate *</label>
                <input type="number" step="0.01" id="consumptionRate" name="consumptionRate" 
                       placeholder="e.g., 35.5" value="${param.consumptionRate}" required>
                <small>Liters per 100km for buses/trains, kWh per mile for electric vehicles</small>
            </div>
            
            <div class="form-group">
                <label for="maxPassengers">Maximum Passengers *</label>
                <input type="number" id="maxPassengers" name="maxPassengers" 
                       placeholder="e.g., 60" value="${param.maxPassengers}" required>
            </div>
            
            <div class="form-group">
                <label for="assignedRoute">Assigned Route *</label>
                <input type="text" id="assignedRoute" name="assignedRoute" 
                       placeholder="e.g., 88 Hurdman" value="${param.assignedRoute}" required> 
            </div>  
            
            <div class="form-actions">
                <button type="submit">Register Vehicle</button>
                <button type="button" onclick="location.href='ShowVehicleList'">Cancel</button>
            </div>
        </form>
    </div>
    
    <jsp:include page="../footer.jsp" />
</body>
</html>