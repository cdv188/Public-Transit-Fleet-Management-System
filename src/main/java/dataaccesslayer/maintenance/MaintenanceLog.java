package dataaccesslayer.maintenance;

import java.time.LocalDate;

/**
 * Represents a single maintenance task for a vehicle.
 * This class serves as a data transfer object (DTO) for maintenance logs,
 * including details like the task description, dates, and status.
 */
public class MaintenanceLog {
    private int taskId;
    private int vehicleId;
    private String taskDescription;
    private LocalDate scheduledDate;
    private LocalDate completionDate;
    private String status;
    
    /**
     * An additional field for displaying the vehicle's number, not stored in the database.
     */
    private String vehicleNumber;
    
    /**
     * Constructs a new, empty `MaintenanceLog` object.
     */
    public MaintenanceLog() {}
    
    /**
     * Constructs a new `MaintenanceLog` with the specified vehicle ID, task description, and status.
     *
     * @param vehicleId The unique identifier of the vehicle.
     * @param taskDescription A description of the maintenance task.
     * @param status The current status of the task (e.g., "Scheduled", "In-Progress", "Completed").
     */
    public MaintenanceLog(int vehicleId, String taskDescription, String status) {
        this.vehicleId = vehicleId;
        this.taskDescription = taskDescription;
        this.status = status;
    }
    
    /**
     * Constructs a new `MaintenanceLog` with all available details.
     *
     * @param taskId The unique identifier for the maintenance task.
     * @param vehicleId The unique identifier of the vehicle.
     * @param taskDescription A description of the maintenance task.
     * @param scheduledDate The date the task is scheduled to be performed.
     * @param completionDate The date the task was completed.
     * @param status The current status of the task.
     */
    public MaintenanceLog(int taskId, int vehicleId, String taskDescription,
                          LocalDate scheduledDate, LocalDate completionDate, String status) {
        this.taskId = taskId;
        this.vehicleId = vehicleId;
        this.taskDescription = taskDescription;
        this.scheduledDate = scheduledDate;
        this.completionDate = completionDate;
        this.status = status;
    }
    
    /**
     * Retrieves the unique identifier for the maintenance task.
     *
     * @return The task ID.
     */
    public int getTaskId() {
        return taskId;
    }
    
    /**
     * Sets the unique identifier for the maintenance task.
     *
     * @param taskId The task ID to set.
     */
    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }
    
    /**
     * Retrieves the unique identifier of the vehicle.
     *
     * @return The vehicle ID.
     */
    public int getVehicleId() {
        return vehicleId;
    }
    
    /**
     * Sets the unique identifier of the vehicle.
     *
     * @param vehicleId The vehicle ID to set.
     */
    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }
    
    /**
     * Retrieves the description of the maintenance task.
     *
     * @return The task description.
     */
    public String getTaskDescription() {
        return taskDescription;
    }
    
    /**
     * Sets the description of the maintenance task.
     *
     * @param taskDescription The task description to set.
     */
    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }
    
    /**
     * Retrieves the date the task is scheduled.
     *
     * @return The scheduled date.
     */
    public LocalDate getScheduledDate() {
        return scheduledDate;
    }
    
    /**
     * Sets the date the task is scheduled.
     *
     * @param scheduledDate The scheduled date to set.
     */
    public void setScheduledDate(LocalDate scheduledDate) {
        this.scheduledDate = scheduledDate;
    }
    
    /**
     * Retrieves the date the task was completed.
     *
     * @return The completion date.
     */
    public LocalDate getCompletionDate() {
        return completionDate;
    }
    
    /**
     * Sets the date the task was completed.
     *
     * @param completionDate The completion date to set.
     */
    public void setCompletionDate(LocalDate completionDate) {
        this.completionDate = completionDate;
    }
    
    /**
     * Retrieves the current status of the maintenance task.
     *
     * @return The task status.
     */
    public String getStatus() {
        return status;
    }
    
    /**
     * Sets the current status of the maintenance task.
     *
     * @param status The status to set.
     */
    public void setStatus(String status) {
        this.status = status;
    }
    
    /**
     * Retrieves the vehicle's number for display purposes.
     *
     * @return The vehicle number.
     */
    public String getVehicleNumber() {
        return vehicleNumber;
    }
    
    /**
     * Sets the vehicle's number for display purposes.
     *
     * @param vehicleNumber The vehicle number to set.
     */
    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }
    
    /**
     * Checks if the maintenance task is overdue.
     * A task is considered overdue if it has a scheduled date in the past and its status is not "Completed".
     *
     * @return {@code true} if the task is overdue, {@code false} otherwise.
     */
    public boolean isOverdue() {
        return scheduledDate != null &&
               scheduledDate.isBefore(LocalDate.now()) &&
               !"Completed".equals(status);
    }
    
    /**
     * Checks if the maintenance task is currently an "Alert".
     *
     * @return {@code true} if the task status is "Alert", {@code false} otherwise.
     */
    public boolean isAlert() {
        return "Alert".equals(status);
    }
    
    /**
     * Checks if the maintenance task is "Completed".
     *
     * @return {@code true} if the task status is "Completed", {@code false} otherwise.
     */
    public boolean isCompleted() {
        return "Completed".equals(status);
    }
    
    /**
     * Checks if the maintenance task is "In-Progress".
     *
     * @return {@code true} if the task status is "In-Progress", {@code false} otherwise.
     */
    public boolean isInProgress() {
        return "In-Progress".equals(status);
    }
    
    /**
     * Checks if the maintenance task is "Scheduled".
     *
     * @return {@code true} if the task status is "Scheduled", {@code false} otherwise.
     */
    public boolean isScheduled() {
        return "Scheduled".equals(status);
    }
}