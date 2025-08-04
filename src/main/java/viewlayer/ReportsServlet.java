package viewlayer;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import strategy.ReportStrategy;
import strategy.FuelCostReportStrategy;
import strategy.OperatorPerformanceReportStrategy;

/**
 * Reports Servlet - Uses Strategy pattern to generate fleet management reports
 * @author Ali
 */
public class ReportsServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String reportType = request.getParameter("reportType");

        try (PrintWriter out = response.getWriter()) {
            if (reportType == null || reportType.isEmpty()) {
                showReportSelectionPage(out, request);
                return;
            }

            ReportStrategy strategy;
            switch (reportType) {
                case "fuelCost":
                    strategy = new FuelCostReportStrategy();
                    break;
                case "operatorPerformance":
                    strategy = new OperatorPerformanceReportStrategy();
                    break;
                default:
                    showReportSelectionPage(out, request);
                    return;
            }

            Map<String, Object> reportData = strategy.generate(request, response);
            displayReport(out, strategy.getReportTitle(), reportData, reportType, request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showReportSelectionPage(PrintWriter out, HttpServletRequest request) {
        out.println("<!DOCTYPE html>");
        out.println("<html><head><title>Reports - PTFMS</title>");
        out.println("<link rel=\"stylesheet\" href=\"commands.css\">  ");
        out.println("</head><body>");
        out.println("<div class='container'>");
        out.println("<h1>Fleet Management Reports</h1>");
        out.println("<p>Select a report to generate:</p>");
        out.println("<a class='btn' href='" + request.getContextPath() + "/reports?reportType=fuelCost'>Fuel Cost Report</a><br><br>");
        out.println("<a class='btn' href='" + request.getContextPath() + "/reports?reportType=operatorPerformance'>Operator Performance Report</a>");
        out.println("</div>");
        out.println("</body></html>");
    }

    private void displayReport(PrintWriter out, String title, Map<String, Object> data, String reportType, HttpServletRequest request) {
        out.println("<!DOCTYPE html>");
        out.println("<html><head><title>" + title + " - PTFMS</title>");
        out.println("<link rel=\"stylesheet\" href=\"commands.css\">  ");
        out.println("</head><body>");
        out.println("<div class='container'>");
        out.println("<h1>" + title + "</h1>");
        out.println("<a class='btn' href='" + request.getContextPath() + "/reports'>Back to Reports</a><br><br>");

        if ("fuelCost".equals(reportType)) {
            displayFuelCostReport(out, data);
        } else if ("operatorPerformance".equals(reportType)) {
            displayOperatorPerformanceReport(out, data);
        }

        out.println("</div>");
        out.println("</body></html>");
    }

    private void displayFuelCostReport(PrintWriter out, Map<String, Object> data) {
        List<Map<String, Object>> vehicleCosts = (List<Map<String, Object>>) data.get("vehicleCosts");
        double totalCost = (Double) data.get("totalFleetCost");

        out.println("<h3>Summary</h3>");
        out.println("<p><strong>Total Fleet Fuel Cost:</strong> $" + String.format("%.2f", totalCost) + "</p>");
        out.println("<p><strong>Number of Vehicles:</strong> " + vehicleCosts.size() + "</p>");

        out.println("<table><tr><th>Vehicle Number</th><th>Vehicle Type</th><th>Fuel Type</th><th>Total Usage</th><th>Cost per Unit</th><th>Total Cost</th></tr>");
        for (Map<String, Object> vehicle : vehicleCosts) {
            out.println("<tr>");
            out.println("<td>" + vehicle.get("vehicleNumber") + "</td>");
            out.println("<td>" + vehicle.get("vehicleType") + "</td>");
            out.println("<td>" + vehicle.get("fuelType") + "</td>");
            out.println("<td>" + String.format("%.2f", vehicle.get("totalUsage")) + "</td>");
            out.println("<td>$" + String.format("%.2f", vehicle.get("costPerUnit")) + "</td>");
            out.println("<td>$" + String.format("%.2f", vehicle.get("totalCost")) + "</td>");
            out.println("</tr>");
        }
        out.println("</table>");
    }

    private void displayOperatorPerformanceReport(PrintWriter out, Map<String, Object> data) {
        List<Map<String, Object>> operatorStats = (List<Map<String, Object>>) data.get("operatorStats");

        out.println("<h3>Summary</h3>");
        out.println("<p><strong>Total Operators:</strong> " + operatorStats.size() + "</p>");

        out.println("<table><tr><th>Operator Name</th><th>Total Status Logs</th><th>In-Service Count</th><th>On Break Count</th><th>Out-of-Service Count</th><th>Service Rate</th></tr>");
        for (Map<String, Object> operator : operatorStats) {
            out.println("<tr>");
            out.println("<td>" + operator.get("operatorName") + "</td>");
            out.println("<td>" + operator.get("totalLogs") + "</td>");
            out.println("<td>" + operator.get("inServiceCount") + "</td>");
            out.println("<td>" + operator.get("onBreakCount") + "</td>");
            out.println("<td>" + operator.get("outOfServiceCount") + "</td>");
            out.println("<td>" + String.format("%.1f%%", operator.get("serviceRate")) + "</td>");
            out.println("</tr>");
        }
        out.println("</table>");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Reports Servlet";
    }
}
