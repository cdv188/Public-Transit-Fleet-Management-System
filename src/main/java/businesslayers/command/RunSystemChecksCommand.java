package businesslayers.command;

import businesslayers.observer.ComponentWearMonitor;
import businesslayers.observer.AlertCreationObserver;
import businesslayers.observer.FuelConsumptionMonitor;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Command to run system checks using Observer pattern monitors
 * @author Ali
 */
public class RunSystemChecksCommand implements Command {
    
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            System.out.println("=== RUNNING SYSTEM CHECKS ===");
            
            response.setContentType("text/html;charset=UTF-8");
            PrintWriter out = response.getWriter();
            
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>System Checks - PTFMS</title>");
            out.println("<meta charset='UTF-8'>");
            out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
            out.println("<link rel='stylesheet' href='commands.css'>");
            out.println("</head>");
            out.println("<body>");
            out.println("<div class='container'>");
            
            // Navigation
            out.println("<div class='nav'>");
            out.println("<a href='dashboard.jsp'>Dashboard</a>");
            out.println("<a href='ShowVehicleList'>Vehicle List</a>");
            out.println("<a href='ShowMaintenance'>Maintenance Dashboard</a>");
            out.println("<a href='gpsDashboard.jsp'>GPS Dashboard</a>");
            out.println("</div>");
            
            out.println("<h1>System Health Checks</h1>");
            out.println("<p>Running automated system checks...</p>");
            
            // Create monitors and observer
            ComponentWearMonitor componentMonitor = new ComponentWearMonitor();
            FuelConsumptionMonitor fuelMonitor = new FuelConsumptionMonitor();
            AlertCreationObserver alertObserver = new AlertCreationObserver();
            
            // Register observer with monitors
            componentMonitor.attach(alertObserver);
            fuelMonitor.attach(alertObserver);
            
            // Run component wear check
            out.println("<div class='check-result'>");
            out.println("<h3>Component Wear Check</h3>");
            out.println("<p>Checking all vehicle components for wear thresholds...</p>");
            
            System.out.println("Running component wear check...");
            componentMonitor.check();
            
            out.println("<p class='success'>✓ Component wear check completed</p>");
            out.println("</div>");
            
            // Run fuel consumption check
            out.println("<div class='check-result'>");
            out.println("<h3>Fuel Consumption Check</h3>");
            out.println("<p>Analyzing fuel consumption patterns for anomalies...</p>");
            
            System.out.println("Running fuel consumption check...");
            fuelMonitor.check();
            
            out.println("<p class='success'>✓ Fuel consumption check completed</p>");
            out.println("</div>");
            
            // Summary
            out.println("<div class='check-result'>");
            out.println("<h3>Check Summary</h3>");
            out.println("<p>All system checks have been completed. Any issues found have been ");
            out.println("automatically logged as alerts in the maintenance system.</p>");
            out.println("<p><a href='ShowMaintenance' class='btn'>View Maintenance Alerts</a></p>");
            out.println("</div>");
            
            out.println("</div>");
            out.println("</body>");
            out.println("</html>");
            
            System.out.println("=== SYSTEM CHECKS COMPLETE ===");
            
        } catch (Exception e) {
            System.err.println("Error in RunSystemChecksCommand: " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/error.html");
        }
    }
}