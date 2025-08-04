/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package datatransferobject;

import java.sql.Timestamp;

/**
 * Model class for Operator Status Logs
 * @author Ali
 */
public class OperatorStatusLog {
    private int logId;
    private int userId;
    private String status;
    private Timestamp timestamp;
    
    // Default constructor
    public OperatorStatusLog() {
    }
    
    // Full constructor
    public OperatorStatusLog(int userId, String status, Timestamp timestamp) {
        this.userId = userId;
        this.status = status;
        this.timestamp = timestamp;
    }
    
    // Getters and Setters
    public int getLogId() {
        return logId;
    }
    
    public void setLogId(int logId) {
        this.logId = logId;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public Timestamp getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
    
    @Override
    public String toString() {
        return "OperatorStatusLog{" +
                "logId=" + logId +
                ", userId=" + userId +
                ", status='" + status + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}