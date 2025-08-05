package businesslayers.command.ReportAndGpsCommand;

import businesslayers.command.Command;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import javax.servlet.ServletException;
import businesslayers.strategy.ReportStrategy;
import businesslayers.strategy.FuelCostReportStrategy;
import businesslayers.strategy.OperatorPerformanceReportStrategy;

/**
 * Command that generates reports using the Strategy pattern.
 */
public class GenerateReportCommand implements Command {

    /**
     * Executes the report generation process based on the report type parameter.
     *
     * @param request  the HTTP request
     * @param response the HTTP response
     * @throws IOException      if an I/O error occurs
     * @throws ServletException if a servlet error occurs
     */
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        String reportType = request.getParameter("reportType");

        if (reportType == null || reportType.isEmpty()) {
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

            Map<String, Object> reportData = strategy.generate(request, response);

            request.setAttribute("reportTitle", strategy.getReportTitle());
            request.setAttribute("reportData", reportData);
            request.setAttribute("reportType", reportType);

            request.getRequestDispatcher("displayReports.jsp").forward(request, response);

        } catch (Exception e) {
            throw new ServletException("Error generating report", e);
        }
    }
}
