<%-- 
    Document   : schedule-maintenance
    Author     : Chester
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="vehicleSimpleFactory.Vehicle" %>
<%@ page import="java.time.LocalDate" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Schedule Maintenance - PTFMS</title>
    <link rel="stylesheet" href="views/maintenance.css">
</head>
<body>
    <jsp:include page="../header.jsp" />
    <div class="schedule-container">
        <h1>Schedule Maintenance Task</h1>
        
        <!-- Display success message -->
        <c:if test="${not empty sessionScope.successMessage}">
            <div class="success">Scheduled Successfully Made</div>
            <c:remove var="successMessage" scope="session"/>
        </c:if>
        
        <!-- Display error message -->
        <c:if test="${not empty requestScope.error}">
            <div class="error">Something Went Wrong</div>
        </c:if>
        
        <form method="POST" action="ShowMaintenance?action=schedule">
            <!-- Vehicle selection dropdown -->
            <div class="form-group">
                <label for="vehicleId">Select Vehicle <span class="required">*</span></label>
                <select id="vehicleId" name="vehicleId" required>
                    <option value="">-- Select a Vehicle --</option>
                    <c:forEach var="vehicle" items="${vehicles}">
                        <option value="${vehicle.vehicleId}" 
                                <c:if test="${param.vehicleId == vehicle.vehicleId}">selected</c:if>>
                            ${vehicle.number} - ${vehicle.vehicleType}
                        </option>
                    </c:forEach>
                </select>
            </div>
            
            <!-- Task description -->
            <div class="form-group">
                <label for="taskDescription">Task Description <span class="required">*</span></label>
                <textarea id="taskDescription" name="taskDescription" 
                         placeholder="Enter detailed description of the maintenance task..." 
                         required><c:out value="${param.taskDescription}"/></textarea>
            </div>
            
            <!-- Scheduled date -->
            <div class="form-group">
                <label for="scheduledDate">Scheduled Date</label>
                <input type="date" id="scheduledDate" name="scheduledDate" 
                       value="${param.scheduledDate}" 
                       min="<%= LocalDate.now().toString() %>">
                <small style="color: #666; font-size: 12px;">Leave empty if no specific date is required</small>
            </div>
            
            <!-- Status selection -->
            <div class="form-group">
                <label for="status">Status</label>
                <select id="status" name="status">
                    <c:set var="selectedStatus" value="${param.status != null ? param.status : 'Scheduled'}"/>
                    <c:forEach var="status" items="${['Scheduled', 'In-Progress', 'Alert']}">
                        <option value="${status}" 
                                <c:if test="${selectedStatus == status}">selected</c:if>>
                            ${status}
                        </option>
                    </c:forEach>
                </select>
            </div>
            
            <!-- Form buttons -->
            <div class="form-actions">
                <button type="submit" class="btn">Schedule Task</button>
                <button type="button" class="btn btn-secondary" 
                        onclick="window.location.href='ShowMaintenance'">Cancel</button>
            </div>
        </form>
    </div>
    <jsp:include page="../footer.jsp" />
</body>
</html>

