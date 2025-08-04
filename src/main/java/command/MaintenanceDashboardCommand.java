package command;

import MaintenanceLogDAO.MaintenanceLog;
import MaintenanceLogDAO.MaintenanceLogDAO;
import MaintenanceLogDAO.MaintenanceLogImpl;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import vehicelDAO.VehicleDAO;
import vehicelDAO.VehicleDAOImpl;
import vehicleSimpleFactory.Vehicle;

/**
 *
 * @author Chester
 */
public class MaintenanceDashboardCommand implements Command{
    private MaintenanceLogDAO maintenanceLogDAO;
    private VehicleDAO vehicleDAO;

    public MaintenanceDashboardCommand() {
        this.maintenanceLogDAO = new MaintenanceLogImpl();
        this.vehicleDAO = new VehicleDAOImpl();
    }
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            // Fetch all maintenance logs and tasks
            List<MaintenanceLog> allMaintenanceLogs = maintenanceLogDAO.getAlarmsAndTasks();
            
            // Separate alerts from regular maintenance tasks
            List<MaintenanceLog> alerts = allMaintenanceLogs.stream()
                .filter(log -> "Alert".equals(log.getStatus()))
                .collect(Collectors.toList());
            
            List<MaintenanceLog> scheduledTasks = allMaintenanceLogs.stream()
                .filter(log -> "Scheduled".equals(log.getStatus()))
                .collect(Collectors.toList());
            
            List<MaintenanceLog> inProgressTasks = allMaintenanceLogs.stream()
                .filter(log -> "In-Progress".equals(log.getStatus()))
                .collect(Collectors.toList());
            
            List<MaintenanceLog> completedTasks = allMaintenanceLogs.stream()
                .filter(log -> "Completed".equals(log.getStatus()))
                .collect(Collectors.toList());

            // Get all vehicles for dropdown in scheduling form
            List<Vehicle> allVehicles = vehicleDAO.getAllVehicles();

            // Group maintenance logs by status for statistics
            Map<String, Long> statusCounts = allMaintenanceLogs.stream()
                .collect(Collectors.groupingBy(
                    MaintenanceLog::getStatus,
                    Collectors.counting()
                ));

            // Calculate maintenance statistics
            long totalTasks = allMaintenanceLogs.size();
            long activeAlerts = alerts.size();
            long pendingTasks = scheduledTasks.size() + inProgressTasks.size();
            
            // Set attributes for JSP
            request.setAttribute("alerts", alerts);
            request.setAttribute("scheduledTasks", scheduledTasks);
            request.setAttribute("inProgressTasks", inProgressTasks);
            request.setAttribute("completedTasks", completedTasks);
            request.setAttribute("allVehicles", allVehicles);
            request.setAttribute("statusCounts", statusCounts);
            request.setAttribute("totalTasks", totalTasks);
            request.setAttribute("activeAlerts", activeAlerts);
            request.setAttribute("pendingTasks", pendingTasks);

            // Check for success message from session
            String successMessage = (String) request.getSession().getAttribute("successMessage");
            if (successMessage != null) {
                request.setAttribute("successMessage", successMessage);
                request.getSession().removeAttribute("successMessage");
            }

            // Forward to the maintenance dashboard JSP
            request.getRequestDispatcher("/WEB-INF/views/maintenance-dashboard.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Helper method to get maintenance logs filtered by vehicle ID
     */
    public void getMaintenanceLogsByVehicle(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String vehicleIdStr = request.getParameter("vehicleId");
        
        if (vehicleIdStr != null && !vehicleIdStr.trim().isEmpty()) {
            try {
                int vehicleId = Integer.parseInt(vehicleIdStr);
                List<MaintenanceLog> vehicleLogs = maintenanceLogDAO.getMaintenanceLogsByVehicleId(vehicleId);
                
                request.setAttribute("maintenanceLogs", vehicleLogs);
                request.setAttribute("selectedVehicleId", vehicleId);
                
            } catch (NumberFormatException e) {
                request.setAttribute("error", "Invalid vehicle ID");
            }
        }
    }
}
