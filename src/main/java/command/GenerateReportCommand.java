package command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.List;
import javax.servlet.ServletException;
import strategy.ReportStrategy;
import strategy.FuelCostReportStrategy;
import strategy.OperatorPerformanceReportStrategy;

/**
 * Command to generate reports using Strategy pattern
 * Replaces the ReportsServlet functionality
 */
public class GenerateReportCommand implements Command {
    
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) 
            throws IOException, ServletException {
        
        String reportType = request.getParameter("reportType");
        
        if (reportType == null || reportType.isEmpty()) {
            // No report type specified, go back to reports page
            response.sendRedirect(request.getContextPath() + "/reports.jsp");
            return;
        }
        
        try {
            ReportStrategy strategy;
            switch (reportType) {
                case "fuelCost":
                    strategy = new FuelCostReportStrategy();
                    break;
                case "operatorPerformance":
                    strategy = new OperatorPerformanceReportStrategy();
                    break;
                default:
                    response.sendRedirect(request.getContextPath() + "/reports.jsp");
                    return;
            }
            
            // Generate the report data
            Map<String, Object> reportData = strategy.generate(request, response);
            
            // Set attributes for JSP
            request.setAttribute("reportTitle", strategy.getReportTitle());
            request.setAttribute("reportData", reportData);
            request.setAttribute("reportType", reportType);
            
            // Forward to report display JSP
            request.getRequestDispatcher("displayReports.jsp").forward(request, response);
            
        } catch (Exception e) {
            throw new ServletException("Error generating report", e);
        }
    }
}