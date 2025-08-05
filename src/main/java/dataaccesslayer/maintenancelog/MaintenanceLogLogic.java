package dataaccesslayer.maintenancelog;

import java.util.List;

/**
 * Business logic layer for maintenance log operations.
 */
public class MaintenanceLogLogic {
    private MaintenanceLogImpl maintenanceDAO = new MaintenanceLogImpl();
     /**
     * Schedules a new maintenance task
     * @param task The maintenance task to be scheduled
     * @return true if the task was successfully scheduled, false otherwise
     */
    public boolean scheduleNewTask(MaintenanceLog task){
        return maintenanceDAO.scheduleNewTask(task);
    }
    
    /**
     * Retrieves all alerts and maintenance tasks
     * @return List of all maintenance logs including alerts
     */
    public List<MaintenanceLog> getAlarmsAndTasks(){
        return maintenanceDAO.getAlarmsAndTasks();
    }
    
    /**
     * Retrieves maintenance logs by vehicle ID
     * @param vehicleId The ID of the vehicle
     * @return List of maintenance logs for the specified vehicle
     */
    public List<MaintenanceLog> getMaintenanceLogsByVehicleId(int vehicleId){
        return maintenanceDAO.getMaintenanceLogsByVehicleId(vehicleId);
    }
    
    /**
     * Retrieves maintenance logs by status
     * @param status The status to filter by
     * @return List of maintenance logs with the specified status
     */
    public List<MaintenanceLog> getMaintenanceLogsByStatus(String status){
        return maintenanceDAO.getMaintenanceLogsByStatus(status);
    }
    
    /**
     * Updates an existing maintenance log
     * @param maintenanceLog The maintenance log to be updated
     * @return true if the log was successfully updated, false otherwise
     */
    public boolean updateMaintenanceLog(MaintenanceLog maintenanceLog){
        return maintenanceDAO.updateMaintenanceLog(maintenanceLog);
    }
    
    /**
     * Retrieves a maintenance log by its ID
     * @param taskId The ID of the maintenance task
     * @return The maintenance log object or null if not found
     */
    public MaintenanceLog getMaintenanceLogById(int taskId){
        return maintenanceDAO.getMaintenanceLogById(taskId);
    }
    
    /**
     * Deletes a maintenance log by its ID
     * @param taskId The ID of the maintenance task to be deleted
     * @return true if the log was successfully deleted, false otherwise
     */
    public boolean deleteMaintenanceLog(int taskId){
        return maintenanceDAO.deleteMaintenanceLog(taskId);
    }
}
