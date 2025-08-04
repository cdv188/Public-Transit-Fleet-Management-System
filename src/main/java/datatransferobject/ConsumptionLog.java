/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package datatransferobject;

import java.sql.Date;

/**
 * Model class for tracking fuel/energy consumption
 * @author Ali
 */
public class ConsumptionLog {
    private int logId;
    private int vehicleId;
    private Date logDate;
    private double usageAmount;
    
    // Default constructor
    public ConsumptionLog() {
    }
    
    // Full constructor
    public ConsumptionLog(int vehicleId, Date logDate, double usageAmount) {
        this.vehicleId = vehicleId;
        this.logDate = logDate;
        this.usageAmount = usageAmount;
    }
    
    // Getters and Setters
    public int getLogId() {
        return logId;
    }
    
    public void setLogId(int logId) {
        this.logId = logId;
    }
    
    public int getVehicleId() {
        return vehicleId;
    }
    
    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }
    
    public Date getLogDate() {
        return logDate;
    }
    
    public void setLogDate(Date logDate) {
        this.logDate = logDate;
    }
    
    public double getUsageAmount() {
        return usageAmount;
    }
    
    public void setUsageAmount(double usageAmount) {
        this.usageAmount = usageAmount;
    }
    
    @Override
    public String toString() {
        return "ConsumptionLog{" +
                "logId=" + logId +
                ", vehicleId=" + vehicleId +
                ", logDate=" + logDate +
                ", usageAmount=" + usageAmount +
                '}';
    }
}