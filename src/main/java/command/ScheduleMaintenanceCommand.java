package command;

import MaintenanceLogDAO.MaintenanceLog;
import MaintenanceLogDAO.MaintenanceLogDAO;
import MaintenanceLogDAO.MaintenanceLogImpl;
import MaintenanceLogDAO.MaintenanceLogLogic;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import vehicelDAO.VehicleDAO;
import vehicelDAO.VehicleDAOImpl;
import vehicleSimpleFactory.Vehicle;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.List;
/**
 *
 * @author Chester
 */
public class ScheduleMaintenanceCommand implements Command {
    private MaintenanceLogDAO maintenanceLogDAO;
    private VehicleDAO vehicleDAO;
    private MaintenanceLogLogic logic = new MaintenanceLogLogic();

    public ScheduleMaintenanceCommand() {
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

    private void showSchedulingForm(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        try {
            // Get all vehicles for the dropdown
            List<Vehicle> allVehicles = vehicleDAO.getAllVehicles();
            
            // Get error message if any
            String errorMessage = (String) request.getAttribute("error");
            String successMessage = (String) request.getSession().getAttribute("successMessage");
            
            // Clear success message from session after displaying
            if (successMessage != null) {
                request.getSession().removeAttribute("successMessage");
            }
            
            // Generate HTML form
            out.println("<!DOCTYPE html>");
            out.println("<html lang='en'>");
            out.println("<head>");
            out.println("<meta charset='UTF-8'>");
            out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
            out.println("<title>Schedule Maintenance - PTFMS</title>");
            out.println("<link rel=\"stylesheet\" href=\"maintenance.css\">");
            out.println("</head>");
            out.println("<body>");
            out.println("<div class='schedule-container'>");
            out.println("<h1>Schedule Maintenance Task</h1>");
            
            // Display success message
            if (successMessage != null && !successMessage.trim().isEmpty()) {
                out.println("<div class='success'>Scheduled Successfully Made</div>");
            }
            
            // Display error message
            if (errorMessage != null && !errorMessage.trim().isEmpty()) {
                out.println("<div class='error'>Something Went Wrong</div>");
            }
            
            out.println("<form method='POST' action='ShowMaintenance?action=schedule'>");
            // Vehicle selection dropdown
            out.println("<div class='form-group'>");
            out.println("<label for='vehicleId'>Select Vehicle <span class='required'>*</span></label>");
            out.println("<select id='vehicleId' name='vehicleId' required>");
            out.println("<option value=''>-- Select a Vehicle --</option>");
            
            for (Vehicle vehicle : allVehicles) {
                String selected = "";
                String selectedVehicleId = request.getParameter("vehicleId");
                if (selectedVehicleId != null && selectedVehicleId.equals(String.valueOf(vehicle.getVehicleId()))) {
                    selected = " selected";
                }
                out.println("<option value='" + vehicle.getVehicleId() + "'" + selected + ">");
                out.println(" " + vehicle.getNumber() + " - " + vehicle.getVehicleType());
                out.println("</option>");
            }
            out.println("</select>");
            out.println("</div>");
            
            // Task description
            out.println("<div class='form-group'>");
            out.println("<label for='taskDescription'>Task Description <span class='required'>*</span></label>");
            String taskDescription = request.getParameter("taskDescription");
            String taskDescriptionValue = taskDescription != null ? taskDescription : "";
            out.println("<textarea id='taskDescription' name='taskDescription' placeholder='Enter detailed description of the maintenance task...' required>" + taskDescriptionValue + "</textarea>");
            out.println("</div>");
            
            // Scheduled date
            out.println("<div class='form-group'>");
            out.println("<label for='scheduledDate'>Scheduled Date</label>");
            String scheduledDate = request.getParameter("scheduledDate");
            String scheduledDateValue = scheduledDate != null ? scheduledDate : "";
            out.println("<input type='date' id='scheduledDate' name='scheduledDate' value='" + scheduledDateValue + "' min='" + LocalDate.now().toString() + "'>");
            out.println("<small style='color: #666; font-size: 12px;'>Leave empty if no specific date is required</small>");
            out.println("</div>");
            
            // Status selection
            out.println("<div class='form-group'>");
            out.println("<label for='status'>Status</label>");
            out.println("<select id='status' name='status'>");
            String[] statuses = {"Scheduled", "In-Progress", "Alert"};
            String selectedStatus = request.getParameter("status");
            if (selectedStatus == null) selectedStatus = "Scheduled";
            
            for (String status : statuses) {
                String selected = status.equals(selectedStatus) ? " selected" : "";
                out.println("<option value='" + status + "'" + selected + ">" + status + "</option>");
            }
            out.println("</select>");
            out.println("</div>");
            
            // Form buttons
            out.println("<div class='form-actions'>");
            out.println("<button type='submit' class='btn'>Schedule Task</button>");
            out.println("<button type='button' class='btn btn-secondary' onclick=\"window.location.href='ShowMaintenance'\">Cancel</button>");
            out.println("</div>");
            
            out.println("</form>");
            out.println("</div>");
            out.println("</body>");
            out.println("</html>");
            
        } catch (Exception e) {
            e.printStackTrace();
            out.println("<!DOCTYPE html>");
            out.println("<html><head><title>Error</title></head><body>");
            out.println("<h1>Error</h1>");
            out.println("<p>Failed to load scheduling form. Please try again.</p>");
            out.println("<a href='ShowMaintenance'>Back to Dashboard</a>");
            out.println("</body></html>");
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
                status = "Scheduled"; // Default status
            }

            if (!isValidStatus(status)) {
                request.setAttribute("error", "Invalid status selected");
                showSchedulingForm(request, response);
                return;
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
                response.sendRedirect("ShowMaintenance?successMessage=success");
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
