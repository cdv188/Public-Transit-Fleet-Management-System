<%-- 
    Document   : update-vehicle
    Author     : Chester
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Update Vehicle - ${vehicle.number} - PTFMS</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="views/vehicle.css">
    <style>
        .form-container {
            max-width: 600px;
            margin: 20px auto;
            padding: 20px;
            background: #f9f9f9;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }
        .form-group {
            margin-bottom: 15px;
        }
        .form-group label {
            display: block;
            margin-bottom: 5px;
            font-weight: bold;
            color: #333;
        }
        .form-group input, .form-group select {
            width: 100%;
            padding: 8px 12px;
            border: 1px solid #ddd;
            border-radius: 4px;
            font-size: 14px;
            box-sizing: border-box;
        }
        .form-group input:focus, .form-group select:focus {
            outline: none;
            border-color: #007bff;
            box-shadow: 0 0 0 2px rgba(0,123,255,0.25);
        }
        .form-buttons {
            margin-top: 20px;
            text-align: center;
        }
        .btn {
            padding: 10px 20px;
            margin: 0 5px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            text-decoration: none;
            display: inline-block;
            font-size: 14px;
        }
        .btn-primary {
            background-color: #007bff;
            color: white;
        }
        .btn-secondary {
            background-color: #6c757d;
            color: white;
        }
        .btn:hover {
            opacity: 0.9;
        }
        .error-message {
            color: #dc3545;
            background-color: #f8d7da;
            border: 1px solid #f5c6cb;
            padding: 10px;
            border-radius: 4px;
            margin-bottom: 15px;
        }
        .success-message {
            color: #155724;
            background-color: #d4edda;
            border: 1px solid #c3e6cb;
            padding: 10px;
            border-radius: 4px;
            margin-bottom: 15px;
        }
    </style>
</head>
<body>
    <jsp:include page="../header.jsp" />
    
    <div class="container-id">
        <div class="nav">
            <a href="ShowVehicleById?id=${vehicle.vehicleId}">Back to Vehicle Details</a>
            <a href="ShowVehicleList">Vehicle List</a>
            <a href="dashboard.jsp">Dashboard</a>
        </div>
        
        <h1>Update Vehicle: ${vehicle.number}</h1>
        
        <div class="form-container">
            <c:if test="${not empty errorMessage}">
                <div class="error-message">
                    ${errorMessage}
                </div>
            </c:if>
            
            <c:if test="${not empty successMessage}">
                <div class="success-message">
                    ${successMessage}
                </div>
            </c:if>
            
            <form method="post" action="UpdateVehicle">
                <input type="hidden" name="vehicleId" value="${vehicle.vehicleId}">
                
                <div class="form-group">
                    <label for="vehicleNumber">Vehicle Number *</label>
                    <input type="text" id="vehicleNumber" name="vehicleNumber" 
                           value="${vehicle.number}" required maxlength="20">
                </div>
                
                <div class="form-group">
                    <label for="vehicleType">Vehicle Type *</label>
                    <select id="vehicleType" name="vehicleType" required>
                        <option value="">Select Vehicle Type</option>
                        <option value="Bus" ${vehicle.vehicleType == 'Bus' ? 'selected' : ''}>Bus</option>
                        <option value="Van" ${vehicle.vehicleType == 'Van' ? 'selected' : ''}>Van</option>
                        <option value="Truck" ${vehicle.vehicleType == 'Truck' ? 'selected' : ''}>Truck</option>
                        <option value="Car" ${vehicle.vehicleType == 'Car' ? 'selected' : ''}>Car</option>
                        <option value="Train" ${vehicle.vehicleType == 'Train' ? 'selected' : ''}>Train</option>
                    </select>
                </div>
                
                <div class="form-group">
                    <label for="fuelType">Fuel Type *</label>
                    <select id="fuelType" name="fuelType" required>
                        <option value="">Select Fuel Type</option>
                        <option value="Petrol" ${vehicle.fuelType == 'Petrol' ? 'selected' : ''}>Petrol</option>
                        <option value="Diesel" ${vehicle.fuelType == 'Diesel' ? 'selected' : ''}>Diesel</option>
                        <option value="Electric" ${vehicle.fuelType == 'Electric' ? 'selected' : ''}>Electric</option>
                        <option value="Hybrid" ${vehicle.fuelType == 'Hybrid' ? 'selected' : ''}>Hybrid</option>
                    </select>
                </div>
                
                <div class="form-group">
                    <label for="consumptionRate">Consumption Rate (L/100km or kWh/100km) *</label>
                    <input type="number" id="consumptionRate" name="consumptionRate" 
                           value="${vehicle.consumptionRate}" step="0.1" min="0" required>
                </div>
                
                <div class="form-group">
                    <label for="maxPassengers">Maximum Passengers *</label>
                    <input type="number" id="maxPassengers" name="maxPassengers" 
                           value="${vehicle.maxCapacity}" min="1" max="100" required>
                </div>
                
                <div class="form-group">
                    <label for="assignedRoute">Assigned Route *</label>
                    <input type="text" id="assignedRoute" name="assignedRoute" 
                           value="${vehicle.route}" required maxlength="100">
                </div>
                
                <div class="form-buttons">
                    <button type="submit" class="btn btn-primary">Update Vehicle</button>
                    <a href="ShowVehicleById?id=${vehicle.vehicleId}" class="btn btn-secondary">Cancel</a>
                </div>
            </form>
        </div>
    </div>
    
    <jsp:include page="../footer.jsp" />
</body>
</html>