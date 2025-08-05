package dataaccesslayer.maintenance;

import java.util.List;

/**
 * The Data Access Object (DAO) interface for performing CRUD operations
 * on { MaintenanceLog} objects.
 * This interface defines the contract for interacting with the underlying data source
 * to manage maintenance tasks and alerts.
 */
public interface MaintenanceLogDAO {
    
    /**
     * Schedules a new maintenance task.
     *
     * @param task The {@link MaintenanceLog} object representing the new task to be scheduled.
     * @return {@code true} if the task was successfully scheduled, {@code false} otherwise.
     */
    boolean scheduleNewTask(MaintenanceLog task);
    
    /**
     * Retrieves a list of all maintenance tasks and alerts from the data source.
     *
     * @return A {@link List} of all {@link MaintenanceLog} objects.
     */
    List<MaintenanceLog> getAlarmsAndTasks();
    
    /**
     * Retrieves a list of maintenance logs for a specific vehicle.
     *
     * @param vehicleId The unique identifier of the vehicle.
     * @return A {@link List} of {@link MaintenanceLog} objects associated with the specified vehicle ID.
     */
    List<MaintenanceLog> getMaintenanceLogsByVehicleId(int vehicleId);
    
    /**
     * Retrieves a list of maintenance logs that match a specific status.
     *
     * @param status The status to filter by (e.g., "Scheduled", "In-Progress", "Completed").
     * @return A {@link List} of {@link MaintenanceLog} objects with the specified status.
     */
    List<MaintenanceLog> getMaintenanceLogsByStatus(String status);
    
    /**
     * Updates an existing maintenance log in the data source.
     *
     * @param maintenanceLog The {@link MaintenanceLog} object containing the updated information.
     * @return {@code true} if the maintenance log was successfully updated, {@code false} otherwise.
     */
    boolean updateMaintenanceLog(MaintenanceLog maintenanceLog);
    
    /**
     * Retrieves a single maintenance log by its unique task ID.
     *
     * @param taskId The unique identifier of the maintenance task.
     * @return The {@link MaintenanceLog} object with the given ID, or {@code null} if not found.
     */
    MaintenanceLog getMaintenanceLogById(int taskId);
    
    /**
     * Deletes a maintenance log from the data source by its unique task ID.
     *
     * @param taskId The unique identifier of the maintenance task to be deleted.
     * @return {@code true} if the maintenance log was successfully deleted, {@code false} otherwise.
     */
    boolean deleteMaintenanceLog(int taskId);
}