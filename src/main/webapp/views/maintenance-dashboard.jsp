<%-- 
    Document   : maintenance-dashboard
    Author     : Chester
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html>
<head>
    <title>Maintenance Dashboard - PTFMS</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="views/maintenance.css">
</head>
<body>
    <jsp:include page="../header.jsp" />
    
    <div class="container">
        <div class="nav">
            <a href="ShowVehicleList">Vehicle List</a>
            <c:if test="${isManager}">
                <a href="ShowMaintenance?action=schedule">Schedule Maintenance</a>
                <a href="RegisterVehicle">Register Vehicle</a>
                <a href="reports.jsp">Reports</a>
                <a href="gpsDashboard.jsp">GPS Dashboard</a>
            </c:if>
            <a href="dashboard.jsp">Dashboard</a>
        </div>
        
        <h1>Maintenance Dashboard</h1>

        <c:if test="${param.successMessage == 'success'}">
            <div class="success">Successfully Completed</div>
        </c:if>
        
        <c:if test="${param.successMessage == 'scheduledsuccess'}">
            <div class="success">New Schedule Added</div>
        </c:if>
        
        <c:if test="${param.error == 'failed'}">
            <div class="error">Failed to Register</div>
        </c:if>
        
        <c:if test="${param.error == 'unauthorized'}">
            <div class="error">Access Denied: Manager privileges required</div>
        </c:if>
        
        <c:if test="${param.successMessage == 'taskstarted'}">
            <div class="inprogress">Task Started</div>
        </c:if>

        <div class="dashboard-grid">
            <div class="card alert">
                <div class="card-title">Active Alerts</div>
                <div class="card-count">${fn:length(alerts)}</div>
                <p>Required immediate attention</p>
            </div>
            
            <div class="card scheduled">
                <div class="card-title">Scheduled Tasks</div>
                <div class="card-count">${fn:length(scheduledTasks)}</div>
                <p>Upcoming Maintenance</p>
            </div>
            
            <div class="card in-progress">
                <div class="card-title">In Progress</div>
                <div class="card-count">${fn:length(inProgressTasks)}</div>
                <p>Active maintenance</p>
            </div>
            
            <div class="card completed">
                <div class="card-title">Completed</div>
                <div class="card-count">${fn:length(completedTasks)}</div>
                <p>Completed Maintenance</p>
            </div>
        </div>
        
        <c:if test="${isManager}">
            <div style="margin: 30px 0;">
                <a href="ShowMaintenance?action=schedule" class="btn btn-success">Schedule New Maintenance</a>
                <a href="RegisterVehicle" class="btn">Register Vehicle</a>
            </div>
        </c:if>
        
        <c:if test="${not empty alerts}">
            <h2 style="color: #dc3545; margin-top: 40px;">Active Alerts</h2>
            <div style="overflow-x: auto;">
                <table>
                    <thead>
                        <tr>
                            <th>Vehicle</th>
                            <th>Description</th>
                            <th>Scheduled Date</th>
                            <th>Status</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="alert" items="${alerts}">
                            <tr>
                                <td>${alert.vehicleNumber}</td>
                                <td>${alert.taskDescription}</td>
                                <td>${task.scheduledDate}</td>
                                <td><span class="status-badge status-alert">${alert.status}</span></td>
                                <td>
                                    <a href="ShowMaintenanceById?id=${alert.taskId}" class="btn">View</a>
                                    <c:if test="${isManager}">
                                        <form style="display:inline;" method="post" action="ShowMaintenance?action=update">
                                                <input type="hidden" name="taskId" value="${alert.taskId}">
                                                <input type="hidden" name="status" value="In-Progress">
                                                <button type="submit" class="btn btn-warning">Start</button>
                                        </form>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </c:if>
        
        <c:if test="${not empty scheduledTasks}">
            <h2 style="margin-top: 40px;">Scheduled Maintenance</h2>
            <div style="overflow-x: auto;">
                <table>
                    <thead>
                        <tr>
                            <th>Vehicle</th>
                            <th>Description</th>
                            <th>Scheduled Date</th>
                            <th>Status</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="task" items="${scheduledTasks}">
                            <tr>
                                <td>${task.vehicleNumber}</td>
                                <td>${task.taskDescription}</td>
                                <td>${task.scheduledDate}</td>
                                <td>
                                    <span class="status-badge status-scheduled">
                                        ${task.status}
                                    </span></td>
                                <td>
                                    <a href="ShowMaintenanceById?id=${task.taskId}" class="btn">View</a>
                                    <c:if test="${isManager}">
                                        <form style="display:inline;" method="post" action="ShowMaintenance?action=update">
                                            <input type="hidden" name="taskId" value="${task.taskId}">
                                            <input type="hidden" name="status" value="In-Progress">
                                            <button type="submit" class="btn btn-warning">Start</button>
                                        </form>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </c:if>
        <c:if test="${not empty inProgressTasks}">
            <h2 style='margin-top: 40px;'>In Progress</h2>
            <div style="overflow-x: auto;">
                <table>
                    <thead>
                        <tr>
                            <th>Vehicle</th>
                            <th>Description</th>
                            <th>Scheduled Date</th>
                            <th>Status</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="task" items="${inProgressTasks}">
                            <tr>
                                <td>${task.vehicleNumber}</td>
                                <td>${task.taskDescription}</td>
                                <td>${task.scheduledDate}</td>
                                <td>
                                    <span class="status-badge status-in-progress">
                                    ${task.status}
                                    </span>
                                </td>
                                 <td>
                                    <a href="ShowMaintenanceById?id=${task.taskId}" class="btn">View</a>
                                    <c:if test="${isManager}">
                                        <form style="display:inline;" method="post" action="ShowMaintenance?action=update">
                                            <input type="hidden" name="taskId" value="${task.taskId}">
                                            <input type="hidden" name="status" value="Completed">
                                            <button type="submit" class="btn btn-warning">Complete</button>
                                        </form>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </c:if>
        <c:if test="${not empty completedTasks}">
            <h2 style='margin-top: 40px;'>Completed Task</h2>
            <div style="overflow-x: auto;">
                <table>
                    <thead>
                        <tr>
                            <th>Vehicle</th>
                            <th>Description</th>
                            <th>Scheduled Date</th>
                            <th>Status</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="task" items="${completedTasks}">
                            <tr>
                                <td>${task.vehicleNumber}</td>
                                <td>${task.taskDescription}</td>
                                <td>${task.scheduledDate}</td>
                                <td>${task.completionDate}</td>
                                <td>
                                    <span class="status-badge status-completed">
                                    ${task.status}
                                    </span>
                                </td>
                                 <td></td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </c:if>
        <c:if test="${empty allMaintenanceLogs}">
            <div style='text-align: center; margin: 40px 0;'>
                <p>No maintenance records found.</p>
                <a href='ScheduleNewMaintence' class='btn btn-success'>Schedule First Maintenance Task</a>
            </div>
        </c:if>
        </div>
        <jsp:include page="../footer.jsp" />
    </body>
</html>