package businesslayers.command.maintenanceCommand;

import dataaccesslayer.maintenance.MaintenanceLog;
import dataaccesslayer.maintenance.MaintenanceLogDAO;
import dataaccesslayer.maintenance.MaintenanceLogImpl;
import dataaccesslayer.maintenance.MaintenanceLogLogic;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import dataaccesslayer.vehicles.VehicleDAO;
import dataaccesslayer.vehicles.VehicleDAOImpl;
import businesslayers.builder.Vehicle;
import businesslayers.command.Command;
import java.io.IOException;
import java.util.List;

/**
 * Command that handles scheduling and updating maintenance tasks.
 */
public class MaintenanceDashboardCommand implements Command {

    private MaintenanceLogDAO maintenanceLogDAO;
    private VehicleDAO vehicleDAO;
    private MaintenanceLogLogic logic = new MaintenanceLogLogic();

    /** Creates a new command with default DAO implementations. */
    public MaintenanceDashboardCommand() {
        this.maintenanceLogDAO = new MaintenanceLogImpl();
        this.vehicleDAO = new VehicleDAOImpl();
    }

    /**
     * Executes the dashboard command for GET (form display) or POST (scheduling).
     */
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

    /**
     * Displays the scheduling form with a list of all vehicles.
     */
    private void showSchedulingForm(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        try {
            List<Vehicle> allVehicles = vehicleDAO.getAllVehicles();
            request.setAttribute("vehicles", allVehicles);
            request.getRequestDispatcher("/views/schedule-maintenance.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Failed to load scheduling form");
            request.getRequestDispatcher("/views/error.jsp").forward(request, response);
        }
    }

    /**
     * Processes a maintenance scheduling request.
     */
    private void processScheduling(HttpServletRequest request, HttpServletResponse response) {
        try {
            String vehicleIdStr = request.getParameter("vehicleId");
            String taskDescription = request.getParameter("taskDescription");
            String scheduledDateStr = request.getParameter("scheduledDate");
            String status = request.getParameter("status");

            if (vehicleIdStr == null || vehicleIdStr.trim().isEmpty()) {
                response.sendRedirect("ShowMaintenance?error=selectionrequired");
                return;
            }
            if (taskDescription == null || taskDescription.trim().isEmpty()) {
                response.sendRedirect("ShowMaintenance?error=descriptionrequired");
                return;
            }

            int vehicleId;
            try {
                vehicleId = Integer.parseInt(vehicleIdStr);
            } catch (NumberFormatException e) {
                response.sendRedirect("ShowMaintenance?error=invalidvehicleselections");
                return;
            }

            Vehicle vehicle = vehicleDAO.getVehicleById(vehicleId);
            if (vehicle == null) {
                response.sendRedirect("ShowMaintenance?error=notexist");
                return;
            }

            LocalDate scheduledDate = null;
            if (scheduledDateStr != null && !scheduledDateStr.trim().isEmpty()) {
                try {
                    scheduledDate = LocalDate.parse(scheduledDateStr);
                    if (scheduledDate.isBefore(LocalDate.now())) {
                        response.sendRedirect("ShowMaintenance?error=olddate");
                        return;
                    }
                } catch (DateTimeParseException e) {
                    response.sendRedirect("ShowMaintenance?error=invalidformat");
                    return;
                }
            }

            if (status == null || status.trim().isEmpty()) {
                status = "Scheduled";
            }

            MaintenanceLog maintenanceLog = new MaintenanceLog();
            maintenanceLog.setVehicleId(vehicleId);
            maintenanceLog.setTaskDescription(taskDescription.trim());
            maintenanceLog.setScheduledDate(scheduledDate);
            maintenanceLog.setStatus(status);

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
     * Updates an existing maintenance task.
     */
    public void updateMaintenanceTask(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String taskIdStr = request.getParameter("taskId");
            String newStatus = request.getParameter("status");
            String completionDateStr = request.getParameter("completionDate");

            int taskId = Integer.parseInt(taskIdStr);
            MaintenanceLog existingTask = logic.getMaintenanceLogById(taskId);

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

            if ("Completed".equals(existingTask.getStatus()) && existingTask.getCompletionDate() == null) {
                existingTask.setCompletionDate(LocalDate.now());
            }

            boolean success = logic.updateMaintenanceLog(existingTask);

            if (success) {
                if ("In-Progress".equals(newStatus)) {
                    response.sendRedirect("ShowMaintenance?successMessage=taskstarted");
                } else {
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
     * Validates the status against allowed values.
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
