/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package dao;

import dto.ConsumptionLog;
import java.sql.Date;
import java.util.List;

/**
 * DAO interface for ConsumptionLogs operations
 * @author Ali
 */
public interface ConsumptionLogsDAO {
    /**
     * Add a new consumption log
     * @param log The consumption log to add
     * @return true if successful
     */
    boolean addConsumptionLog(ConsumptionLog log);
    
    /**
     * Get all consumption logs for a vehicle
     * @param vehicleId The vehicle ID
     * @return List of consumption logs
     */
    List<ConsumptionLog> getConsumptionByVehicle(int vehicleId);
    
    /**
     * Get consumption logs for a vehicle within a date range
     * @param vehicleId The vehicle ID
     * @param startDate Start date
     * @param endDate End date
     * @return List of consumption logs
     */
    List<ConsumptionLog> getConsumptionByDateRange(int vehicleId, Date startDate, Date endDate);
    
    /**
     * Get average consumption for a vehicle
     * @param vehicleId The vehicle ID
     * @return Average consumption amount
     */
    double getAverageConsumption(int vehicleId);
}