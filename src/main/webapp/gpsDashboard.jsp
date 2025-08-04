<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page import="dao.*, dto.*, vehicelDAO.*, vehicleSimpleFactory.*, java.util.*" %>
<%
    // Get data for display
    VehicleDAOImpl vehicleDAO = new VehicleDAOImpl();
    VehicleLocationsDAOImpl locationDAO = new VehicleLocationsDAOImpl();
    
    List<Vehicle> vehicles = vehicleDAO.getAllVehicles();
    List<VehicleLocation> locations = locationDAO.getAllCurrentLocations();
    
    // Store in request scope for JSTL
    request.setAttribute("vehicles", vehicles);
    request.setAttribute("locations", locations);
%>
<!DOCTYPE html>
<html>
<head>
    <title>GPS Dashboard - PTFMS</title>
    <link rel="stylesheet" type="text/css" href="commands.css">
</head>
<body>
    <jsp:include page="header.jsp" />
    <div class="container">
        <div class="nav">
            <a href="dashboard.jsp">Dashboard</a>
            <a href="ShowVehicleList">Vehicle List</a>
            <a href="ShowMaintenance">Maintenance Dashboard</a>
            <a href="reports.jsp">Reports</a>
        </div>
        
        <h1>GPS Vehicle Tracking Dashboard</h1>
        
        <div class="map-placeholder">
            <h3>Interactive Map View</h3>
            <p>In a production system, this would display an interactive map with vehicle positions.</p>
        </div>
        
        <a href="controller?action=simulateGPS" class="btn">Simulate GPS Updates</a>
        
        <h2>Current Vehicle Locations</h2>
        <table>
            <thead>
                <tr>
                    <th>Vehicle Number</th>
                    <th>Vehicle Type</th>
                    <th>Route</th>
                    <th>Latitude</th>
                    <th>Longitude</th>
                    <th>Last Updated</th>
                    <th>Status</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="vehicle" items="${vehicles}">
                    <c:set var="hasLocation" value="false" />
                    <c:forEach var="location" items="${locations}">
                        <c:if test="${location.vehicleId == vehicle.vehicleId}">
                            <c:set var="hasLocation" value="true" />
                            <tr>
                                <td>${vehicle.number}</td>
                                <td>${vehicle.vehicleType}</td>
                                <td>${vehicle.route}</td>
                                <td><fmt:formatNumber value="${location.latitude}" pattern="0.000000"/></td>
                                <td><fmt:formatNumber value="${location.longitude}" pattern="0.000000"/></td>
                                <td>${location.timestamp}</td>
                                <td style="color: green;">Active</td>
                            </tr>
                        </c:if>
                    </c:forEach>
                    <c:if test="${!hasLocation}">
                        <tr>
                            <td>${vehicle.number}</td>
                            <td>${vehicle.vehicleType}</td>
                            <td>${vehicle.route}</td>
                            <td colspan="3" style="text-align: center;">No GPS data available</td>
                            <td style="color: red;">Offline</td>
                        </tr>
                    </c:if>
                </c:forEach>
            </tbody>
        </table>
        
        <p><strong>Total Vehicles:</strong> ${vehicles.size()}</p>
        <p><strong>Vehicles with GPS:</strong> ${locations.size()}</p>
    </div>
    <jsp:include page="footer.jsp" />
</body>
</html>