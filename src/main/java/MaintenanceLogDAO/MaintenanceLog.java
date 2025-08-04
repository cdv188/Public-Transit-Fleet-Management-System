package MaintenanceLogDAO;

import java.time.LocalDate;

/**
 *
 * @author Chester
 */
public class MaintenanceLog {
     private int taskId;
    private int vehicleId;
    private String taskDescription;
    private LocalDate scheduledDate;
    private LocalDate completionDate;
    private String status;
    
    // Additional field for display purposes (not in database)
    private String vehicleNumber;
    
    /**
     * Default constructor
     */
    public MaintenanceLog() {}
    
    /**
     * Constructor with required fields
     * 
     * @param vehicleId The ID of the vehicle
     * @param taskDescription Description of the maintenance task
     * @param status The status of the maintenance task
     */
    public MaintenanceLog(int vehicleId, String taskDescription, String status) {
        this.vehicleId = vehicleId;
        this.taskDescription = taskDescription;
        this.status = status;
    }
    
    /**
     * Full constructor
     * 
     * @param taskId The unique task ID
     * @param vehicleId The ID of the vehicle
     * @param taskDescription Description of the maintenance task
     * @param scheduledDate When the task is scheduled
     * @param completionDate When the task was completed
     * @param status The current status of the task
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
    
    // Getters and Setters
    
    public int getTaskId() {
        return taskId;
    }
    
    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }
    
    public int getVehicleId() {
        return vehicleId;
    }
    
    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }
    
    public String getTaskDescription() {
        return taskDescription;
    }
    
    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }
    
    public LocalDate getScheduledDate() {
        return scheduledDate;
    }
    
    public void setScheduledDate(LocalDate scheduledDate) {
        this.scheduledDate = scheduledDate;
    }
    
    public LocalDate getCompletionDate() {
        return completionDate;
    }
    
    public void setCompletionDate(LocalDate completionDate) {
        this.completionDate = completionDate;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getVehicleNumber() {
        return vehicleNumber;
    }
    
    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }
    
    /**
     * Checks if the maintenance task is overdue
     * @return true if the task is scheduled but past its scheduled date
     */
    public boolean isOverdue() {
        return scheduledDate != null && 
               scheduledDate.isBefore(LocalDate.now()) && 
               !"Completed".equals(status);
    }
    
    /**
     * Checks if the maintenance task is an alert
     * @return true if the status is "Alert"
     */
    public boolean isAlert() {
        return "Alert".equals(status);
    }
    
    /**
     * Checks if the maintenance task is completed
     * @return true if the status is "Completed"
     */
    public boolean isCompleted() {
        return "Completed".equals(status);
    }
    
    /**
     * Checks if the maintenance task is in progress
     * @return true if the status is "In-Progress"
     */
    public boolean isInProgress() {
        return "In-Progress".equals(status);
    }
    
    /**
     * Checks if the maintenance task is scheduled
     * @return true if the status is "Scheduled"
     */
    public boolean isScheduled() {
        return "Scheduled".equals(status);
    }
    
    
}
