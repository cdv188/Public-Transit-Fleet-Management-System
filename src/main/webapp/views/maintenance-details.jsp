<%-- 
    Document   : maintenance-details
    Author     : Chester
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Maintenance Task Details - ${task.vehicleNumber} - PTFMS</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="views/maintenance.css">
</head>
<body>
    <jsp:include page="../header.jsp" />
    
    <div class="container-id">
        <div class="nav">
            <a href="ShowMaintenance">Back to Dashboard</a>
            <a href="ShowVehicleList">Vehicle List</a>
            <c:if test="${isManager}">
            <a href="reports.jsp">Reports</a>
            <a href="gpsDashboard.jsp">GPS Dashboard</a>
            <a href="dashboard.jsp">Dashboard</a>
            </c:if>
        </div>
        
        <h1>Maintenance Task Details</h1>
        
        <div class="detail-row">
            <span class="detail-label">Task ID:</span>
            <span class="detail-value">${task.taskId}</span>
        </div>
        
        <div class="detail-row">
            <span class="detail-label">Vehicle:</span>
            <span class="detail-value">${task.vehicleNumber} (ID: ${task.vehicleId})</span>
        </div>
        
        <div class="detail-row">
            <span class="detail-label">Task Description:</span>
            <span class="detail-value">${task.taskDescription}</span>
        </div>
        
        <div class="detail-row">
            <span class="detail-label">Status:</span>
            <span class="detail-value">
                <span class="status-badge status-${task.status.toLowerCase().replace('-', '')}">${task.status}</span>
            </span>
        </div>
        
        <div class="detail-row">
            <span class="detail-label">Scheduled Date:</span>
            <span class="detail-value">
                <c:choose>
                    <c:when test="${task.scheduledDate != null}">
                        ${task.scheduledDate}
                    </c:when>
                    <c:otherwise>
                        Not Set
                    </c:otherwise>
                </c:choose>
            </span>
        </div>
        
        <div class="detail-row">
            <span class="detail-label">Completion Date:</span>
            <span class="detail-value">
                <c:choose>
                    <c:when test="${task.completionDate != null}">
                        ${task.completionDate}
                    </c:when>
                    <c:otherwise>
                        Not Completed
                    </c:otherwise>
                </c:choose>
            </span>
        </div>
        
        <div style="margin-top: 30px;">
            <a href="ShowMaintenance" class="btn">Back to Dashboard</a>
            <a href="ShowVehicleById?id=${task.vehicleId}" class="btn">View Vehicle</a>
            
            <c:if test="${isManager}">
                <c:choose>
                    <c:when test="${task.status == 'Scheduled'}">
                        <form style="display:inline;" method="post" action="ShowMaintenance?action=update">
                            <input type="hidden" name="taskId" value="${task.taskId}">
                            <input type="hidden" name="status" value="In-Progress">
                            <button type="submit" class="btn btn-warning">Start Task</button>
                        </form>
                    </c:when>
                    <c:when test="${task.status == 'In-Progress'}">
                        <form style="display:inline;" method="post" action="ShowMaintenance?action=update">
                            <input type="hidden" name="taskId" value="${task.taskId}">
                            <input type="hidden" name="status" value="Completed">
                            <button type="submit" class="btn btn-success">Complete Task</button>
                        </form>
                    </c:when>
                </c:choose>
            </c:if>
        </div>
    </div>
    
    <jsp:include page="../footer.jsp" />
</body>
</html>

