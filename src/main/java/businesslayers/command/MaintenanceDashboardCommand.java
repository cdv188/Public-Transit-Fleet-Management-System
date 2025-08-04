package businesslayers.command;

import MaintenanceLogDAO.MaintenanceLog;
import MaintenanceLogDAO.MaintenanceLogDAO;
import MaintenanceLogDAO.MaintenanceLogImpl;
import MaintenanceLogDAO.MaintenanceLogLogic;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import dataaccesslayer.vehicles.VehicleDAO;
import dataaccesslayer.vehicles.VehicleDAOImpl;
import businesslayers.builder.Vehicle;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.List;
/**
 *
 * @author Chester
 */
public class MaintenanceDashboardCommand implements Command {
    private MaintenanceLogDAO maintenanceLogDAO;
    private VehicleDAO vehicleDAO;
    private MaintenanceLogLogic logic = new MaintenanceLogLogic();

    public MaintenanceDashboardCommand() {
        this.maintenanceLogDAO = new MaintenanceLogImpl();
        this.vehicleDAO = new VehicleDAOImpl();
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String method = request.getMethod();
        
        if ("GET".equalsIgnoreCase(method)) {
            showSchedulingForm(request, response);
        } else if ("POST".equalsIgnoreCase(method)) {
            processScheduling(request, response);
        }
        
    }

    private void showSchedulingForm(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
         try {
            // Get all vehicles for the dropdown
            List<Vehicle> allVehicles = vehicleDAO.getAllVehicles();
            
            // Set vehicles as request attribute for JSP
            request.setAttribute("vehicles", allVehicles);
            
            // Forward to JSP
            request.getRequestDispatcher("/views/schedule-maintenance.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Failed to load scheduling form");
            request.getRequestDispatcher("/views/error.jsp").forward(request, response);
        }
    }

    private void processScheduling(HttpServletRequest request, HttpServletResponse response) {
            try {
            // Extract form parameters
            String vehicleIdStr = request.getParameter("vehicleId");
            String taskDescription = request.getParameter("taskDescription");
            String scheduledDateStr = request.getParameter("scheduledDate");
            String status = request.getParameter("status");

            // Validate required fields
            if (vehicleIdStr == null || vehicleIdStr.trim().isEmpty()) {
                response.sendRedirect("ShowMaintenance?error=selectionrequired");
                return;
            }

            if (taskDescription == null || taskDescription.trim().isEmpty()) {
                response.sendRedirect("ShowMaintenance?error=descriptionrequired");
                return;
            }

            // Parse vehicle ID
            int vehicleId;
            try {
                vehicleId = Integer.parseInt(vehicleIdStr);
            } catch (NumberFormatException e) {
                response.sendRedirect("ShowMaintenance?error=invalidvehicleselections");
                return;
            }

            // Verify vehicle exists
            Vehicle vehicle = vehicleDAO.getVehicleById(vehicleId);
            if (vehicle == null) {
                response.sendRedirect("ShowMaintenance?error=notexist");
                return;
            }

            // Parse scheduled date if provided
            LocalDate scheduledDate = null;
            if (scheduledDateStr != null && !scheduledDateStr.trim().isEmpty()) {
                try {
                    scheduledDate = LocalDate.parse(scheduledDateStr);
                    
                    // Validate that the scheduled date is not in the past
                    if (scheduledDate.isBefore(LocalDate.now())) {
                        response.sendRedirect("ShowMaintenance?error=olddate");
                        return;
                    }
                } catch (DateTimeParseException e) {
                    response.sendRedirect("ShowMaintenance?error=invalidformat");
                    return;
                }
            }

            // Validate status
            if (status == null || status.trim().isEmpty()) {
                status = "Scheduled";
            }

            // Create the maintenance log entry
            MaintenanceLog maintenanceLog = new MaintenanceLog();
            maintenanceLog.setVehicleId(vehicleId);
            maintenanceLog.setTaskDescription(taskDescription.trim());
            maintenanceLog.setScheduledDate(scheduledDate);
            maintenanceLog.setStatus(status);

            // Save the maintenance task
            boolean success = maintenanceLogDAO.scheduleNewTask(maintenanceLog);

            if (success) {
                response.sendRedirect("ShowMaintenance?successMessage=scheduledsuccess");
            } else {
                response.sendRedirect("ShowMaintenance?successMessage=failed");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    } 
    /**
     * Helper method to update an existing maintenance task
     */
    public void updateMaintenanceTask(HttpServletRequest request, HttpServletResponse response) 
        throws ServletException, IOException {
        try {
            String taskIdStr = request.getParameter("taskId");
            String newStatus = request.getParameter("status");
            String completionDateStr = request.getParameter("completionDate");

            int taskId = Integer.parseInt(taskIdStr);
            MaintenanceLog existingTask = logic.getMaintenanceLogById(taskId);

            // Update completion date if provided
            if (completionDateStr != null && !completionDateStr.trim().isEmpty()) {
                try {
                    LocalDate completionDate = LocalDate.parse(completionDateStr);
                    existingTask.setCompletionDate(completionDate);
                } catch (DateTimeParseException e) {
                    response.sendRedirect("ShowMaintenance?error=invalidformat");
                    return;
                }
            }
            
            if (newStatus != null && !newStatus.trim().isEmpty() && isValidStatus(newStatus)) {
                existingTask.setStatus(newStatus);
            }
            
            // If status is being set to "Completed" and no completion date is set, use today
            if ("Completed".equals(existingTask.getStatus()) && existingTask.getCompletionDate() == null) {
                existingTask.setCompletionDate(LocalDate.now());
            }

            boolean success = logic.updateMaintenanceLog(existingTask);

            if (success) {
                if (newStatus.equals("In-Progress")){
                    response.sendRedirect("ShowMaintenance?successMessage=taskstarted");
                }else{
                    response.sendRedirect("ShowMaintenance?successMessage=success");
                }
            } else {
                response.sendRedirect("ShowMaintenance?error=failed");
            }

        } catch (NumberFormatException e) {
            response.sendRedirect("ShowMaintenance?error=invalidid");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * Validates maintenance status against allowed values
     * @param status The status to validate
     * @return true if valid, false otherwise
     */
    private boolean isValidStatus(String status) {
        return status != null && (
            "Scheduled".equals(status) ||
            "In-Progress".equals(status) ||
            "Completed".equals(status) ||
            "Alert".equals(status)
        );
    }
}
