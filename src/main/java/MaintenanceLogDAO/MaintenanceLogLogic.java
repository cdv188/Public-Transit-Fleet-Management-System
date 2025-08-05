package MaintenanceLogDAO;

import java.util.List;

/**
 * Provides a higher-level interface for managing maintenance logs by delegating
 * operations to the {@link MaintenanceLogImpl} data access object.
 * This class acts as the business logic layer for scheduling, retrieving,
 * updating, and deleting maintenance log records.
 */
public class MaintenanceLogLogic {

    /** Data access object used to interact with the maintenance log database. */
    private MaintenanceLogImpl maintenanceDAO = new MaintenanceLogImpl();

    /**
     * Schedules a new maintenance task.
     *
     * @param task the {@link MaintenanceLog} object to be scheduled
     * @return {@code true} if the task was successfully scheduled, {@code false} otherwise
     */
    public boolean scheduleNewTask(MaintenanceLog task) {
        return maintenanceDAO.scheduleNewTask(task);
    }

    /**
     * Retrieves all alerts and maintenance tasks.
     *
     * @return a list of all {@link MaintenanceLog} records including alerts
     */
    public List<MaintenanceLog> getAlarmsAndTasks() {
        return maintenanceDAO.getAlarmsAndTasks();
    }

    /**
     * Retrieves maintenance logs for a specific vehicle.
     *
     * @param vehicleId the ID of the vehicle
     * @return a list of {@link MaintenanceLog} records associated with the given vehicle ID
     */
    public List<MaintenanceLog> getMaintenanceLogsByVehicleId(int vehicleId) {
        return maintenanceDAO.getMaintenanceLogsByVehicleId(vehicleId);
    }

    /**
     * Retrieves maintenance logs filtered by a specific status.
     *
     * @param status the status to filter by (e.g., "Scheduled", "Completed")
     * @return a list of {@link MaintenanceLog} records matching the given status
     */
    public List<MaintenanceLog> getMaintenanceLogsByStatus(String status) {
        return maintenanceDAO.getMaintenanceLogsByStatus(status);
    }

    /**
     * Updates an existing maintenance log record.
     *
     * @param maintenanceLog the {@link MaintenanceLog} object containing updated details
     * @return {@code true} if the log was successfully updated, {@code false} otherwise
     */
    public boolean updateMaintenanceLog(MaintenanceLog maintenanceLog) {
        return maintenanceDAO.updateMaintenanceLog(maintenanceLog);
    }

    /**
     * Retrieves a maintenance log by its task ID.
     *
     * @param taskId the ID of the maintenance task
     * @return the {@link MaintenanceLog} object if found, or {@code null} if not found
     */
    public MaintenanceLog getMaintenanceLogById(int taskId) {
        return maintenanceDAO.getMaintenanceLogById(taskId);
    }

    /**
     * Deletes a maintenance log by its task ID.
     *
     * @param taskId the ID of the maintenance task to delete
     * @return {@code true} if the log was successfully deleted, {@code false} otherwise
     */
    public boolean deleteMaintenanceLog(int taskId) {
        return maintenanceDAO.deleteMaintenanceLog(taskId);
    }
}
