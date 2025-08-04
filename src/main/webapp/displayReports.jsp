<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>${reportTitle} - PTFMS</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/commands.css">
</head>
<body>
    <jsp:include page="/header.jsp" />
    <div class="container">
        <h1>${reportTitle}</h1>
        <a class="btn" href="${pageContext.request.contextPath}/reports.jsp">Back to Reports</a>
        <br><br>
        
        <c:choose>
            <c:when test="${reportType == 'fuelCost'}">
                <h3>Summary</h3>
                <p><strong>Total Fleet Fuel Cost:</strong> $<fmt:formatNumber value="${reportData.totalFleetCost}" pattern="#,##0.00"/></p>
                <p><strong>Number of Vehicles:</strong> ${reportData.vehicleCosts.size()}</p>
                
                <table>
                    <tr>
                        <th>Vehicle Number</th>
                        <th>Vehicle Type</th>
                        <th>Fuel Type</th>
                        <th>Total Usage</th>
                        <th>Cost per Unit</th>
                        <th>Total Cost</th>
                    </tr>
                    <c:forEach var="vehicle" items="${reportData.vehicleCosts}">
                        <tr>
                            <td>${vehicle.vehicleNumber}</td>
                            <td>${vehicle.vehicleType}</td>
                            <td>${vehicle.fuelType}</td>
                            <td><fmt:formatNumber value="${vehicle.totalUsage}" pattern="#,##0.00"/></td>
                            <td>$<fmt:formatNumber value="${vehicle.costPerUnit}" pattern="#,##0.00"/></td>
                            <td>$<fmt:formatNumber value="${vehicle.totalCost}" pattern="#,##0.00"/></td>
                        </tr>
                    </c:forEach>
                </table>
            </c:when>
            
            <c:when test="${reportType == 'operatorPerformance'}">
                <h3>Summary</h3>
                <p><strong>Total Operators:</strong> ${reportData.operatorStats.size()}</p>
                
                <table>
                    <tr>
                        <th>Operator Name</th>
                        <th>Total Status Logs</th>
                        <th>In-Service Count</th>
                        <th>On Break Count</th>
                        <th>Out-of-Service Count</th>
                        <th>Service Rate</th>
                    </tr>
                    <c:forEach var="operator" items="${reportData.operatorStats}">
                        <tr>
                            <td>${operator.operatorName}</td>
                            <td>${operator.totalLogs}</td>
                            <td>${operator.inServiceCount}</td>
                            <td>${operator.onBreakCount}</td>
                            <td>${operator.outOfServiceCount}</td>
                            <td><fmt:formatNumber value="${operator.serviceRate}" pattern="#0.0"/>%</td>
                        </tr>
                    </c:forEach>
                </table>
            </c:when>
        </c:choose>
    </div>
    <jsp:include page="/footer.jsp" />
</body>
</html>