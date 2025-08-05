package dataaccesslayer.maintenancelog;

import java.util.List;

/**
 * Interface for maintenance log data access operations.
 */
public interface MaintenanceLogDAO {
    /**
     * Schedules a new maintenance task
     * @param task The maintenance task to be scheduled
     * @return true if the task was successfully scheduled, false otherwise
     */
    boolean scheduleNewTask(MaintenanceLog task);
    
    /**
     * Retrieves all alerts and maintenance tasks
     * @return List of all maintenance logs including alerts
     */
    List<MaintenanceLog> getAlarmsAndTasks();
    
    /**
     * Retrieves maintenance logs by vehicle ID
     * @param vehicleId The ID of the vehicle
     * @return List of maintenance logs for the specified vehicle
     */
    List<MaintenanceLog> getMaintenanceLogsByVehicleId(int vehicleId);
    
    /**
     * Retrieves maintenance logs by status
     * @param status The status to filter by
     * @return List of maintenance logs with the specified status
     */
    List<MaintenanceLog> getMaintenanceLogsByStatus(String status);
    
    /**
     * Updates an existing maintenance log
     * @param maintenanceLog The maintenance log to be updated
     * @return true if the log was successfully updated, false otherwise
     */
    boolean updateMaintenanceLog(MaintenanceLog maintenanceLog);
    
    /**
     * Retrieves a maintenance log by its ID
     * @param taskId The ID of the maintenance task
     * @return The maintenance log object or null if not found
     */
    MaintenanceLog getMaintenanceLogById(int taskId);
    
    /**
     * Deletes a maintenance log by its ID
     * @param taskId The ID of the maintenance task to be deleted
     * @return true if the log was successfully deleted, false otherwise
     */
    boolean deleteMaintenanceLog(int taskId);
}
